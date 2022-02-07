package com.example.workout_log.domain.use_cases.calendar

import com.example.workout_log.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetWorkoutDays @Inject constructor(
    private val workoutRepository: WorkoutRepository
) {
    operator fun invoke(): Flow<List<LocalDate>> = workoutRepository.getWorkoutDays()
}