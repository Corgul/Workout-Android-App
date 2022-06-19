package com.example.workout_log.domain.use_cases.add_exercise

import com.example.workout_log.domain.repository.ExerciseRepository
import javax.inject.Inject

class GetNumberOfExercisesForWorkout @Inject constructor(
    private val repository: ExerciseRepository
) {
    suspend operator fun invoke(workoutId: Long) = repository.getNumberOfExercisesForWorkout(workoutId)
}