package com.example.workout_log.presentation.workoutlog

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workout_log.domain.common.WorkoutDataStore
import com.example.workout_log.domain.model.Exercise
import com.example.workout_log.domain.model.ExerciseAndExerciseSets
import com.example.workout_log.domain.model.ExerciseSet
import com.example.workout_log.domain.model.Workout
import com.example.workout_log.domain.use_cases.workout_log.WorkoutLogUseCases
import com.example.workout_log.domain.util.WorkoutAppLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class WorkoutLogViewModel @Inject constructor(
    private val useCases: WorkoutLogUseCases,
    workoutDataStore: WorkoutDataStore
) : ViewModel() {
    private val _state = mutableStateOf(WorkoutLogState())
    val state: State<WorkoutLogState> = _state

    private val workoutId: Flow<Long> = workoutDataStore.preferencesFlow
    // Update the exercises and sets based on the currently selected workout
    private val exercisesAndSets: Flow<List<ExerciseAndExerciseSets>> = workoutDataStore.preferencesFlow
        .flatMapLatest { useCases.getExercisesAndSets(it) }

    init {
        viewModelScope.launch {
            getWorkoutId()
            getExercisesAndSets()
        }
    }

    private fun getWorkoutId() {
        workoutId.onEach { workoutId: Long ->
            if (workoutId != Workout.invalidWorkoutId) {
                _state.value = state.value.copy(
                    workoutId = workoutId
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun getExercisesAndSets() {
        exercisesAndSets.onEach { exercisesAndSets ->
            WorkoutAppLogger.d("Got new exercises and sets")
            _state.value = state.value.copy(
                exercisesAndSets = exercisesAndSets
            )
        }.launchIn(viewModelScope)
    }

    fun onAddSetButtonClicked(exerciseAndExerciseSets: ExerciseAndExerciseSets) {
        viewModelScope.launch {
            // In case the set list is out of order, get the max set number
            val setNumber = exerciseAndExerciseSets.sets.map { it.setNumber }.maxOf { it } + 1
            useCases.addSet(exerciseAndExerciseSets.exercise, setNumber)
        }
    }

    fun onWeightChanged(exerciseSet: ExerciseSet, newWeight: Int?) {
        if (newWeight == null) {
            return
        }
        viewModelScope.launch {
            useCases.updateSetWeight(exerciseSet, newWeight)
        }
    }

    fun onRepsChanged(exerciseSet: ExerciseSet, newReps: Int?) {
        if (newReps == null) {
            return
        }
        viewModelScope.launch {
            useCases.updateSetReps(exerciseSet, newReps)
        }
    }
}