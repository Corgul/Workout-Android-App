package com.example.workout_log.domain.use_cases.workout_log

import com.example.workout_log.domain.model.ExerciseSet
import com.example.workout_log.domain.repository.ExerciseSetRepository
import javax.inject.Inject

class UpdateSetReps @Inject constructor(
    private val exerciseSetRepository: ExerciseSetRepository
) {
    suspend operator fun invoke(exerciseSet: ExerciseSet, newReps: Int) {
        exerciseSet.reps = newReps
        exerciseSetRepository.updateExerciseSet(exerciseSet)
    }
}