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
import com.example.workout_log.presentation.workoutlog.state.WorkoutLogCardListener
import com.example.workout_log.presentation.workoutlog.state.WorkoutLogDialogListener
import com.example.workout_log.presentation.workoutlog.state.WorkoutLogSnackbarListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDate
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class WorkoutLogViewModel @Inject constructor(
    private val logUseCases: WorkoutLogUseCases,
    private val exerciseBottomSheetUseCases: ExerciseBottomSheetUseCases,
    private val workoutBottomSheetUseCases: WorkoutBottomSheetUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel(), WorkoutLogSnackbarListener, WorkoutLogDialogListener, WorkoutLogCardListener {
    private val _state = mutableStateOf(WorkoutLogState())
    val state: State<WorkoutLogState> = _state

    private val workoutDate: LocalDate
    private var cachedWorkoutLogState: WorkoutLogState? = null
    private var cachedExercisesAndSets: List<ExerciseAndExerciseSets>? = null

    // Update the exercises and sets based on the currently selected workout
    private val workoutWithExercisesAndSets: Flow<WorkoutWithExercisesAndSets?>

    private var deleteWorkoutJob: Job? = null
    private var deleteExerciseJob: Job? = null

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
            WorkoutAppLogger.d("${_state.value.workout?.workoutName}")
            if (deleteWorkoutJob != null) {
                deleteExerciseJob?.cancel()
                deleteWorkoutJob?.join()
                deleteWorkoutJob = null
                deleteExerciseJob = null
                return@onEach
            }
            if (deleteExerciseJob != null) {
                deleteExerciseJob?.join()
                deleteExerciseJob = null
                return@onEach
            }
            _state.value = WorkoutLogState(workoutWithExercisesAndSets.getExercisesAndSets(), workoutWithExercisesAndSets.workout)
            WorkoutAppLogger.d("${_state.value.workout?.workoutName}")
        }.launchIn(viewModelScope)
    }

    fun onStop() {
        viewModelScope.launch {
            deleteWorkoutJob?.join()
            deleteExerciseJob?.join()
            deleteWorkoutJob = null
            deleteExerciseJob = null
        }
    }

    override fun onAddSetButtonClicked(exerciseAndExerciseSets: ExerciseAndExerciseSets) {
        viewModelScope.launch {
            // In case the set list is out of order, get the max set number
            val setNumber = exerciseAndExerciseSets.sets.map { it.setNumber }.maxOf { it } + 1
            logUseCases.addSet(exerciseAndExerciseSets.exercise, setNumber)
        }
    }

    override fun onWeightChanged(exerciseSet: ExerciseSet, newWeight: Int?) {
        if (newWeight == null) {
            return
        }
        viewModelScope.launch {
            logUseCases.updateSetWeight(exerciseSet, newWeight)
        }
    }

    override fun onRepsChanged(exerciseSet: ExerciseSet, newReps: Int?) {
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

        deleteWorkoutJob = viewModelScope.launch {
            cachedWorkoutLogState = state.value
            // Hide and cache the workout by setting it to nothing to give the user an opportunity to undo before deleting
            _state.value = WorkoutLogState()
            deleteExerciseJob?.join()
            deleteExerciseJob = null
            deleteWorkoutDelayed()
        }
    }

    private suspend fun deleteWorkoutDelayed() = withContext(Dispatchers.IO) {
        delay(SNACKBAR_DURATION)
        deleteWorkout(cachedWorkoutLogState?.workout)
        cachedWorkoutLogState = null
    }

    fun deleteExerciseClicked(exercise: Exercise) {
        deleteExerciseJob = viewModelScope.launch {
            deleteExerciseJob?.join()
            val exerciseListWithRemovedExercise = state.value.exercisesAndSets.filter { it.exercise.exerciseId != exercise.exerciseId }
            cachedExercisesAndSets = state.value.exercisesAndSets
            _state.value = state.value.copy(exercisesAndSets = exerciseListWithRemovedExercise)
            deleteExerciseDelayed(exercise)
        }
    }

    private suspend fun deleteExerciseDelayed(exercise: Exercise) = withContext(Dispatchers.IO) {
        delay(SNACKBAR_DURATION)
        deleteExercise(exercise)
        cachedExercisesAndSets = null
    }

    override fun deleteWorkoutSnackbarDismissed() {
        viewModelScope.launch {
            deleteWorkoutJob = null
            cachedWorkoutLogState = null
        }
    }

    override fun deleteWorkoutSnackbarUndoClicked() {
        _state.value = cachedWorkoutLogState?.copy() ?: WorkoutLogState()
        cachedWorkoutLogState = null
        deleteWorkoutJob?.cancel()
    }

    override fun deleteExerciseSnackbarDismissed(exercise: Exercise) {
        viewModelScope.launch {
            deleteExerciseJob = null
            cachedExercisesAndSets = null
        }
    }

    override fun deleteExerciseSnackbarUndoClicked() {
        deleteExerciseJob?.cancel()
        if (deleteWorkoutJob != null) {
            cachedExercisesAndSets?.let { cachedWorkoutLogState = cachedWorkoutLogState?.copy(exercisesAndSets = it) }
            cachedExercisesAndSets = null
            return
        }
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

    /**
     * The list of exercises will need their positions rearranged
     */
    override fun onReorderExerciseDialogConfirmed(exercises: List<Exercise>) {
        viewModelScope.launch {
            exercises.toMutableList().forEachIndexed { index, exercise -> exercise.exercisePosition = index + 1 }
            logUseCases.updateExercises(exercises)
        }
    }

    override fun onEditWorkoutNameDialogConfirmed(newWorkoutName: String, workout: Workout) {
        viewModelScope.launch {
            logUseCases.updateWorkoutName(workout, newWorkoutName)
        }
    }

    override fun onEditExerciseDialogConfirmed(exerciseSets: List<ExerciseSet>) {
        viewModelScope.launch {
            exerciseSets.toMutableList().forEachIndexed { index, exerciseSet -> exerciseSet.setNumber = index + 1 }
            exerciseBottomSheetUseCases.updateSets(exerciseSets)
        }
    }

    override fun onEditExerciseDialogDelete(exerciseAndExerciseSets: ExerciseAndExerciseSets, setsToDelete: List<ExerciseSet>) {
        viewModelScope.launch {
            if (setsToDelete.size == exerciseAndExerciseSets.sets.size) {
                deleteExercise(exerciseAndExerciseSets.exercise)
            } else {
                updateSetsPositionsForDeletedSets(exerciseAndExerciseSets.sets, setsToDelete)
                exerciseBottomSheetUseCases.deleteSets(setsToDelete)
            }
        }
    }

    /**
     * Updates the sets positions in [sets] based on the sets to delete in [setsToDelete]
     */
    private suspend fun updateSetsPositionsForDeletedSets(sets: List<ExerciseSet>, setsToDelete: List<ExerciseSet>) {
        val newSetList = sets.toMutableList()
        setsToDelete.forEach { exerciseSet ->
            if (sets.contains(exerciseSet)) {
                newSetList.remove(exerciseSet)
            }
        }
        newSetList.forEachIndexed { index, exerciseSet -> exerciseSet.setNumber = index + 1 }
        exerciseBottomSheetUseCases.updateSets(newSetList)
    }
    //endregion

    companion object {
        private const val SNACKBAR_DURATION = 4000L
    }
}