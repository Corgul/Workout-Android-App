package com.example.workout_log.presentation.add_exercise

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workout_log.domain.model.ExerciseName
import com.example.workout_log.domain.use_cases.add_exercise.GetExerciseNames
import com.example.workout_log.domain.use_cases.add_exercise.GetExerciseTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AddExerciseViewModel @Inject constructor(
    private val getExerciseNames: GetExerciseNames,
    getExerciseTypes: GetExerciseTypes
) : ViewModel() {
    private val _state = mutableStateOf(AddExerciseState())
    val state: State<AddExerciseState> = _state

    private val selectedExerciseNames = linkedSetOf<ExerciseName>()

    init {
        // Get the list of exercise types
        getExerciseTypes().onEach { exerciseTypesList ->
            _state.value = state.value.copy(
                exerciseTypes = exerciseTypesList
            )
        }.launchIn(viewModelScope)
    }

    @ExperimentalCoroutinesApi
    fun onEvent(event: AddExerciseEvent) {
        when (event) {
            is AddExerciseEvent.ExerciseTypeClicked -> {
                getExerciseNames(event.exerciseType.exerciseTypeId).onEach { exerciseNamesList ->
                    _state.value = state.value.copy(
                        exerciseNames = exerciseNamesList,
                        exerciseNamesVisibility = true
                    )
                }.launchIn(viewModelScope)
            }
            is AddExerciseEvent.ExerciseNameClicked -> {
                onExerciseNameClicked(event.exerciseName)
            }
            is AddExerciseEvent.OnBackPressed -> {
                if (state.value.exerciseNamesVisibility) {
                    _state.value = state.value.copy(
                        exerciseNamesVisibility = false
                    )
                }
            }
        }
    }

    private fun onExerciseNameClicked(exerciseName: ExerciseName) {
        // If the exercise name is selected already and this method is called, then it was de selected
        if (selectedExerciseNames.contains(exerciseName)) {
            selectedExerciseNames.remove(exerciseName)
            _state.value = state.value.copy(
                selectedExercises = selectedExerciseNames.toList()
            )
        } else {
            selectedExerciseNames.add(exerciseName)
            _state.value = state.value.copy(
                selectedExercises = selectedExerciseNames.toList()
            )
        }
    }
}