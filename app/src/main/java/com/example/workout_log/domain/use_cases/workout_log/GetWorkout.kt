package com.example.workout_log.domain.use_cases.workout_log

import com.example.workout_log.domain.model.Workout
import com.example.workout_log.domain.repository.WorkoutRepository
import com.example.workout_log.domain.util.WorkoutAppLogger
import com.example.workout_log.domain.util.formatDate
import java.time.LocalDate
import javax.inject.Inject

class GetWorkout @Inject constructor(
    private val workoutRepository: WorkoutRepository
) {
    suspend operator fun invoke(date: LocalDate? = null, workoutId: Long? = null): Workout? {
        if (workoutId != null) {
            return workoutRepository.getWorkoutForId(workoutId)
        } else if (date != null) {
            return workoutRepository.getWorkoutForDate(date)
        }
        return null
    }
}