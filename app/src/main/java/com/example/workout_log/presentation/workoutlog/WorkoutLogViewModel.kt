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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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
    private val workoutDate: LocalDate
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
                exercisesAndSets = workoutWithExercisesAndSets.exercisesAndSets,
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

    fun deleteExercise(exercise: Exercise) {
        viewModelScope.launch {
            exerciseBottomSheetUseCases.deleteExercise(exercise)
        }
    }

    fun deleteWorkout(workout: Workout?) {
        if (workout == null) {
            return
        }

        viewModelScope.launch {
            workoutBottomSheetUseCases.deleteWorkout(workout)
        }
    }
}