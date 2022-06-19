package com.example.workout_log.presentation.add_exercise

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workout_log.domain.model.ExerciseName
import com.example.workout_log.domain.model.ExerciseType
import com.example.workout_log.domain.model.ExerciseTypeWithNames
import com.example.workout_log.domain.use_cases.add_exercise.AddExerciseUseCases
import com.example.workout_log.domain.util.WorkoutAppLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddExerciseViewModel @Inject constructor(
    private val useCases: AddExerciseUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = mutableStateOf(AddExerciseState())
    val state: State<AddExerciseState> = _state

    private val workoutDate: LocalDate
    private val selectedExerciseNames = linkedSetOf<ExerciseName>()

    private lateinit var exerciseTypesWithExerciseNames: List<ExerciseTypeWithNames>

    init {
        val workoutDateLong = savedStateHandle.get<Long>("workoutDate") ?: LocalDate.now().toEpochDay()
        workoutDate = LocalDate.ofEpochDay(workoutDateLong)
        viewModelScope.launch {
            WorkoutAppLogger.d("Workout date nav arg is: $workoutDate")
            getExerciseTypesWithExerciseNames()
        }
    }

    private suspend fun getExerciseTypesWithExerciseNames() {
        useCases.getExerciseTypesWithExerciseNames().collect { exerciseTypesWithExerciseNamesList ->
            WorkoutAppLogger.d("Exercise list : ${exerciseTypesWithExerciseNamesList.size}")
            exerciseTypesWithExerciseNames = exerciseTypesWithExerciseNamesList
            val exerciseTypes = exerciseTypesWithExerciseNames.map { it.exerciseType }
            _state.value = state.value.copy(
                exerciseTypes = exerciseTypes
            )
        }
    }

    @ExperimentalCoroutinesApi
    fun onEvent(event: AddExerciseEvent) {
        when (event) {
            is AddExerciseEvent.ExerciseTypeClicked -> {
                onExerciseTypeClicked(event.exerciseType)
            }
            is AddExerciseEvent.ExerciseNameClicked -> {
                onExerciseNameClicked(event.exerciseName)
            }
            is AddExerciseEvent.OnBackPressed -> {
                onBackClicked()
            }
            is AddExerciseEvent.OnSaveClicked -> {
                viewModelScope.launch {
                    saveExercises(event.onExercisesSaved)
                }
            }
        }
    }

    private fun onExerciseTypeClicked(exerciseType: ExerciseType) {
        // Get the matching exercise type in the list and return it's list of exercise names
        val exerciseNameList = exerciseTypesWithExerciseNames
            .first {
                it.exerciseType.exerciseTypeId == exerciseType.exerciseTypeId
            }.exerciseNames

        _state.value = state.value.copy(
            exerciseNames = exerciseNameList,
            exerciseNamesVisibility = true
        )
    }

    private fun onExerciseNameClicked(exerciseName: ExerciseName) {
        // If the exercise name is selected already and this method is called, then it was de selected
        if (selectedExerciseNames.contains(exerciseName)) {
            selectedExerciseNames.remove(exerciseName)
        } else {
            selectedExerciseNames.add(exerciseName)
        }

        _state.value = state.value.copy(
            selectedExercises = selectedExerciseNames.toList()
        )
    }

    private fun onBackClicked() {
        if (state.value.exerciseNamesVisibility) {
            _state.value = state.value.copy(
                exerciseNamesVisibility = false
            )
        }
    }

    private suspend fun saveExercises(onExercisesSaved: (workoutDateLong: Long) -> Unit) {
        val workout = useCases.getWorkoutByDate(workoutDate)
        val workoutId = workout?.workoutId ?: useCases.createWorkout(workoutDate)
        val numberOfExercisesForWorkout = useCases.getNumberOfExercisesForWorkout(workoutId)

        useCases.saveExercises(workoutId, numberOfExercisesForWorkout, selectedExerciseNames.toList())

        onExercisesSaved(workoutDate.toEpochDay())
    }
}