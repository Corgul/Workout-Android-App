package com.example.workout_log.presentation.add_exercise

import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workout_log.domain.common.WorkoutDataStore
import com.example.workout_log.domain.model.ExerciseName
import com.example.workout_log.domain.model.Workout
import com.example.workout_log.domain.use_cases.add_exercise.GetExerciseNames
import com.example.workout_log.domain.use_cases.add_exercise.GetExerciseTypes
import com.example.workout_log.domain.use_cases.add_exercise.SaveExercises
import com.example.workout_log.domain.use_cases.workout_log.CreateWorkout
import com.example.workout_log.domain.use_cases.workout_log.GetWorkout
import com.example.workout_log.domain.util.WorkoutAppLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddExerciseViewModel @Inject constructor(
    private val getExerciseNames: GetExerciseNames,
    private val saveExercises: SaveExercises,
    private val getWorkout: GetWorkout,
    private val createWorkout: CreateWorkout,
    private val workoutDataStore: WorkoutDataStore,
    getExerciseTypes: GetExerciseTypes,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = mutableStateOf(AddExerciseState())
    val state: State<AddExerciseState> = _state

    private var workoutIdArg = savedStateHandle.get<Long>("workoutId") ?: -1L
    private val selectedExerciseNames = linkedSetOf<ExerciseName>()

    init {
        WorkoutAppLogger.d("Workout id nav arg is: $workoutIdArg")
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
            is AddExerciseEvent.OnSaveClicked -> {
                viewModelScope.launch {
                    saveExercises(event.onSave)
                }
            }
        }
    }

    private suspend fun saveExercises(onSave: (workoutId: Long?) -> Unit) {
        val workout: Workout?

        if (workoutIdArg == -1L) {
            val date = LocalDate.now()
            // TODO - Pass in date instead of passing in LocalDate.now()
            createWorkout(date)
            workout = getWorkout(date = date)
            workoutDataStore.storeWorkoutId(workout?.workoutId)
        } else {
            workout = getWorkout(workoutId = workoutIdArg)
        }

        saveExercises(workout, selectedExerciseNames.toList())

        onSave(workout?.workoutId)
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
}