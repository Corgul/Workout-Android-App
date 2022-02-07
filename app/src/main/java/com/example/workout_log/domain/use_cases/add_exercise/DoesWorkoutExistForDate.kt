package com.example.workout_log.domain.use_cases.add_exercise

import com.example.workout_log.domain.repository.WorkoutRepository
import java.time.LocalDate
import javax.inject.Inject

class DoesWorkoutExistForDate @Inject constructor(
    private val workoutRepository: WorkoutRepository
) {
    suspend operator fun invoke(workoutDate: LocalDate): Boolean = workoutRepository.doesWorkoutExist(workoutDate)
}