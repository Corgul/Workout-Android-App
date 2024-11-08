package com.example.workout_log.domain.use_cases.workout_log

import com.example.workout_log.domain.model.Workout
import com.example.workout_log.domain.repository.WorkoutRepository
import com.example.workout_log.domain.util.extensions.formatDate
import java.time.LocalDate
import javax.inject.Inject

class CreateWorkout @Inject constructor(
    private val workoutRepository: WorkoutRepository
) {
    suspend operator fun invoke(date: LocalDate): Long =
        workoutRepository.addWorkout(Workout("${date.formatDate()} Workout", date))
}