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
            is AddExerciseEvent.OnSaveClicked -> {
                viewModelScope.launch {
                    saveExercises(event.selectedExerciseNames, event.onExercisesSaved)
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
        )
    }

    private suspend fun saveExercises(selectedExerciseNames: List<ExerciseName>, onExercisesSaved: (workoutDateLong: Long) -> Unit) {
        val workout = useCases.getWorkoutByDate(workoutDate)
        val workoutId = workout?.workoutId ?: useCases.createWorkout(workoutDate)
        val numberOfExercisesForWorkout = useCases.getNumberOfExercisesForWorkout(workoutId)

        useCases.saveExercises(workoutId, numberOfExercisesForWorkout, selectedExerciseNames)

        onExercisesSaved(workoutDate.toEpochDay())
    }
}