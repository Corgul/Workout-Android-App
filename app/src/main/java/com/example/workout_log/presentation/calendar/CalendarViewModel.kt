package com.example.workout_log.presentation.calendar

import androidx.lifecycle.ViewModel
import com.example.workout_log.domain.model.Workout
import com.example.workout_log.domain.model.WorkoutWithExercisesAndSets
import com.example.workout_log.domain.use_cases.calendar.CalendarUseCases
import com.example.workout_log.domain.util.WorkoutAppLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val useCases: CalendarUseCases
) : ViewModel() {
    private val selectedDateFlow = MutableStateFlow<LocalDate?>(null)
    val workoutDaysFlow: Flow<List<LocalDate>> = useCases.getWorkoutDays()

    val workoutDetailFlow: Flow<WorkoutWithExercisesAndSets?> = selectedDateFlow
        .filterNotNull()
        .flatMapLatest {
            useCases.getWorkoutWithExercisesAndSets(it)
        }

    /**
     * @param selectedDates should only ever be of size 1 because of the selection type we allow this calendar
     */
    fun onDateSelected(selectedDates: List<LocalDate>) {
        selectedDateFlow.value = selectedDates.firstOrNull()
        WorkoutAppLogger.d("Selected date ${selectedDates.joinToString { it.toString() }}")
    }
}