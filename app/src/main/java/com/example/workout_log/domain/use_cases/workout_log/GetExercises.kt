package com.example.workout_log.domain.use_cases.workout_log

import com.example.workout_log.domain.model.Exercise
import com.example.workout_log.domain.model.Workout
import com.example.workout_log.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExercises @Inject constructor(private val exerciseRepository: ExerciseRepository) {
    operator fun invoke(workout: Workout?): Flow<List<Exercise>> {
        return exerciseRepository.getExercisesForWorkout(workout)
    }
}