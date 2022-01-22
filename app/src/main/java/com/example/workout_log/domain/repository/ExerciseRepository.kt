package com.example.workout_log.domain.repository

import com.example.workout_log.domain.model.Exercise
import com.example.workout_log.domain.model.ExerciseAndExerciseSets
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {
    suspend fun insertExercises(exercises: List<Exercise>): List<Long>

    fun getExercisesAndSetsForWorkout(workoutId: Long): Flow<List<ExerciseAndExerciseSets>>

    suspend fun deleteExercises()
}