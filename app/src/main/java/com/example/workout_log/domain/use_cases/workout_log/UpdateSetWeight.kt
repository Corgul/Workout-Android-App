package com.example.workout_log.domain.use_cases.workout_log

import com.example.workout_log.domain.model.ExerciseSet
import com.example.workout_log.domain.repository.ExerciseSetRepository
import javax.inject.Inject

class UpdateSetWeight @Inject constructor(
    private val exerciseSetRepository: ExerciseSetRepository
) {
    suspend operator fun invoke(exerciseSet: ExerciseSet, newWeight: Int) {
        exerciseSet.weight = newWeight
        exerciseSetRepository.updateExerciseSet(exerciseSet)
    }
}