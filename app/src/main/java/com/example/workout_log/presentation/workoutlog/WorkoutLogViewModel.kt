package com.example.workout_log.presentation.workoutlog

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workout_log.domain.model.*
import com.example.workout_log.domain.use_cases.workout_log.ExerciseBottomSheetUseCases
import com.example.workout_log.domain.use_cases.workout_log.WorkoutBottomSheetUseCases
import com.example.workout_log.domain.use_cases.workout_log.WorkoutLogUseCases
import com.example.workout_log.domain.util.WorkoutAppLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class WorkoutLogViewModel @Inject constructor(
    private val logUseCases: WorkoutLogUseCases,
    private val exerciseBottomSheetUseCases: ExerciseBottomSheetUseCases,
    private val workoutBottomSheetUseCases: WorkoutBottomSheetUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = mutableStateOf(WorkoutLogState())
    val state: State<WorkoutLogState> = _state
    private val _dialogState = mutableStateOf(WorkoutLogDialogsState())
    val dialogState: State<WorkoutLogDialogsState> = _dialogState

    private val workoutDate: LocalDate
    private var cachedWorkoutLogState: WorkoutLogState? = null
    private var cachedExercisesAndSets: List<ExerciseAndExerciseSets>? = null

    // Update the exercises and sets based on the currently selected workout
    private val workoutWithExercisesAndSets: Flow<WorkoutWithExercisesAndSets?>

    init {
        val workoutDateLong = savedStateHandle.get<Long>("workoutDate") ?: LocalDate.now().toEpochDay()
        workoutDate = LocalDate.ofEpochDay(workoutDateLong)
        workoutWithExercisesAndSets = logUseCases.getWorkoutWithExercisesAndSets(workoutDate)
        viewModelScope.launch {
            getWorkoutWithExercisesAndSets()
        }
    }

    private fun getWorkoutWithExercisesAndSets() {
        workoutWithExercisesAndSets.onEach { workoutWithExercisesAndSets ->
            if (workoutWithExercisesAndSets == null) {
                _state.value = WorkoutLogState()
                return@onEach
            }
            WorkoutAppLogger.d("Got new exercises and sets")
            _state.value = state.value.copy(
                exercisesAndSets = workoutWithExercisesAndSets.getExercisesAndSets(),
                workout = workoutWithExercisesAndSets.workout
            )
        }.launchIn(viewModelScope)
    }

    fun onAddSetButtonClicked(exerciseAndExerciseSets: ExerciseAndExerciseSets) {
        viewModelScope.launch {
            // In case the set list is out of order, get the max set number
            val setNumber = exerciseAndExerciseSets.sets.map { it.setNumber }.maxOf { it } + 1
            logUseCases.addSet(exerciseAndExerciseSets.exercise, setNumber)
        }
    }

    fun onWeightChanged(exerciseSet: ExerciseSet, newWeight: Int?) {
        if (newWeight == null) {
            return
        }
        viewModelScope.launch {
            logUseCases.updateSetWeight(exerciseSet, newWeight)
        }
    }

    fun onRepsChanged(exerciseSet: ExerciseSet, newReps: Int?) {
        if (newReps == null) {
            return
        }
        viewModelScope.launch {
            logUseCases.updateSetReps(exerciseSet, newReps)
        }
    }

    fun deleteWorkoutClicked(workout: Workout?) {
        if (workout == null) {
            return
        }

        viewModelScope.launch {
            cachedWorkoutLogState = state.value
            // Hide and cache the workout by setting it to nothing to give the user an opportunity to undo before deleting
            _state.value = WorkoutLogState()
        }
    }

    fun deleteExerciseClicked(exercise: Exercise) {
        viewModelScope.launch {
            val exerciseListWithRemovedExercise = state.value.exercisesAndSets.filter { it.exercise.exerciseId != exercise.exerciseId }
            cachedExercisesAndSets = state.value.exercisesAndSets
            _state.value = state.value.copy(exercisesAndSets = exerciseListWithRemovedExercise)
        }
    }

    fun deleteWorkoutSnackbarDismissed() {
        viewModelScope.launch {
            deleteWorkout(cachedWorkoutLogState?.workout)
            cachedWorkoutLogState = null
        }
    }

    fun deleteWorkoutSnackbarUndoClicked() {
        _state.value = cachedWorkoutLogState?.copy() ?: WorkoutLogState()
        cachedWorkoutLogState = null
    }

    fun deleteExerciseSnackbarDismissed(exercise: Exercise) {
        viewModelScope.launch {
            deleteExercise(exercise)
            cachedExercisesAndSets = null
        }
    }

    fun deleteExerciseSnackbarUndoClicked() {
        cachedExercisesAndSets?.let { cachedExercisesAndSets -> _state.value = state.value.copy(exercisesAndSets = cachedExercisesAndSets) }
        cachedExercisesAndSets = null
    }

    private suspend fun deleteExercise(exercise: Exercise) {
        val deletedPosition = exercise.exercisePosition
        val workoutId = exercise.workoutId
        exerciseBottomSheetUseCases.deleteExercise(exercise)
        updateExercisePositionsForDeletedExercise(deletedPosition, workoutId)
    }

    private suspend fun deleteWorkout(workout: Workout?) {
        if (workout == null) {
            return
        }
        workoutBottomSheetUseCases.deleteWorkout(workout)
    }

    /**
     * Function that shifts all exercise positions for a given workout down by 1.
     * This function is meant to be called after an exercise is deleted so that it can rearrange the exercise positions to be valid
     */
    private suspend fun updateExercisePositionsForDeletedExercise(deletedPosition: Int, workoutId: Long) {
        var exercises = logUseCases.getExercisesForWorkout(workoutId)
        if (exercises.isEmpty()) {
            // Delete workout if there are no more exercises
            deleteWorkout(state.value.workout)
            return
        }

        exercises = exercises.slice(deletedPosition - 1 until exercises.size)

        var newPosition = deletedPosition
        exercises.forEach { exercise ->
            exercise.exercisePosition = newPosition
            newPosition += 1
        }

        logUseCases.updateExercises(exercises)
    }

    //region Dialogs
    fun showReorderExerciseDialog() {
        _dialogState.value = WorkoutLogDialogsState(true)
    }

    fun onReorderExerciseDialogDismissed() {
        dismissReorderExerciseDialog()
    }

    /**
     * The list of exercises will need their positions rearranged
     */
    fun onReorderExerciseDialogConfirmed(exercises: List<Exercise>) {
        viewModelScope.launch {
            exercises.toMutableList().forEachIndexed { index, exercise -> exercise.exercisePosition = index + 1 }
            logUseCases.updateExercises(exercises)
            dismissReorderExerciseDialog()
        }
    }

    private fun dismissReorderExerciseDialog() {
        _dialogState.value = WorkoutLogDialogsState(false)
    }

    fun showEditWorkoutNameDialog() {
        _dialogState.value = WorkoutLogDialogsState(showEditWorkoutNameDialog = true)
    }

    fun onEditWorkoutNameDialogDismissed() {
        dismissEditWorkoutNameDialog()
    }

    fun onEditWorkoutNameDialogConfirmed(newWorkoutName: String, workout: Workout) {
        viewModelScope.launch {
            logUseCases.updateWorkoutName(workout, newWorkoutName)
            dismissEditWorkoutNameDialog()
        }
    }

    private fun dismissEditWorkoutNameDialog() {
        _dialogState.value = WorkoutLogDialogsState(showEditWorkoutNameDialog = false)
    }
    //endregion
}