package com.example.workout_log.domain.use_cases.workout_log

import com.example.workout_log.domain.repository.ExerciseRepository
import javax.inject.Inject

class GetExercisesForWorkout @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) {
    suspend operator fun invoke(workoutId: Long) = exerciseRepository.getExercisesForWorkout(workoutId)
}