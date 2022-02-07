package com.example.workout_log.domain.use_cases.workout_log

import com.example.workout_log.domain.model.Workout
import com.example.workout_log.domain.repository.WorkoutRepository
import java.time.LocalDate
import javax.inject.Inject

class GetWorkoutByDate @Inject constructor(
    private val workoutRepository: WorkoutRepository
) {
    suspend operator fun invoke(workoutDate: LocalDate): Workout? = workoutRepository.getWorkoutForDate(workoutDate)
}