package com.example.workout_log.domain.use_cases.workout_log

import com.example.workout_log.domain.model.Exercise
import com.example.workout_log.domain.repository.ExerciseRepository
import javax.inject.Inject

class DeleteExercise @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) {
    suspend operator fun invoke(exercise: Exercise) = exerciseRepository.deleteExercise(exercise)
}