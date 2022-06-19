package com.example.workout_log.domain.repository

import com.example.workout_log.domain.model.Exercise

interface ExerciseRepository {
    suspend fun getNumberOfExercisesForWorkout(workoutId: Long): Int

    suspend fun getExercisesForWorkout(workoutId: Long): List<Exercise>

    suspend fun updateExercises(exercises: List<Exercise>)

    suspend fun insertExercises(exercises: List<Exercise>): List<Long>

    suspend fun deleteExercise(exercise: Exercise)

    suspend fun deleteExercises()
}