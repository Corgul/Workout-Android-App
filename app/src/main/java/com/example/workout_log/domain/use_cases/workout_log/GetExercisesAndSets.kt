package com.example.workout_log.domain.use_cases.workout_log

import com.example.workout_log.domain.model.ExerciseAndExerciseSets
import com.example.workout_log.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExercisesAndSets @Inject constructor(private val exerciseRepository: ExerciseRepository) {
    operator fun invoke(workoutId: Long): Flow<List<ExerciseAndExerciseSets>> =
        exerciseRepository.getExercisesAndSetsForWorkout(workoutId)

}