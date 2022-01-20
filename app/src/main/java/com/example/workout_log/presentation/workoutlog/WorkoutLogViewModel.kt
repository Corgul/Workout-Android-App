package com.example.workout_log.presentation.workoutlog

import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workout_log.domain.common.WorkoutDataStore
import com.example.workout_log.domain.model.Exercise
import com.example.workout_log.domain.model.Workout
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
    private val workoutDataStore: WorkoutDataStore,
    private val useCases: WorkoutLogUseCases
) : ViewModel() {
    private val _state = mutableStateOf(WorkoutLogState())
    val state: State<WorkoutLogState> = _state

    // Might be overkill to have this as a flow
    private val workout = MutableStateFlow<Workout?>(null)

    private val exercises: Flow<List<Exercise>> = workout
        .filter { it != null }
        .flatMapLatest { useCases.getExercises(it) }

    init {
        viewModelScope.launch {
            workoutDataStore.preferencesFlow.onEach { workoutId: Long ->
                if (workoutId != -1L) {
                    workout.value = useCases.getWorkout(workoutId = workoutId)
                    _state.value = state.value.copy(
                        workoutId = workoutId
                    )
                }
            }.launchIn(viewModelScope)

            exercises.onEach { exercises ->
                _state.value = state.value.copy(
                    exercises = exercises
                )
            }.launchIn(viewModelScope)
        }
    }
}