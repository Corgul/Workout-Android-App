package com.example.workout_log.domain.repository

import com.example.workout_log.domain.model.Exercise
import com.example.workout_log.domain.model.Workout
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {
    suspend fun addExercisesForWorkout(exercises: List<Exercise>)

    fun getExercisesForWorkout(workout: Workout?): Flow<List<Exercise>>

    suspend fun deleteExercises()
}