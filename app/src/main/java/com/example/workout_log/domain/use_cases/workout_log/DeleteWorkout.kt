package com.example.workout_log.domain.use_cases.workout_log

import com.example.workout_log.domain.model.Workout
import com.example.workout_log.domain.repository.WorkoutRepository
import javax.inject.Inject

class DeleteWorkout @Inject constructor(
    private val workoutRepository: WorkoutRepository
) {
    suspend operator fun invoke(workout: Workout) = workoutRepository.deleteWorkout(workout)
}