package com.example.workout_log.domain.repository

import com.example.workout_log.domain.model.Workout
import com.example.workout_log.domain.model.WorkoutWithExercisesAndSets
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface WorkoutRepository {
    suspend fun doesWorkoutExist(date: LocalDate): Boolean

    suspend fun doesWorkoutExist(workoutId: Long): Boolean

    suspend fun getWorkoutForDate(date: LocalDate): Workout?

    suspend fun getWorkoutForId(workoutId: Long): Workout

    suspend fun addWorkout(workout: Workout): Long

    fun getExercisesAndSetsForWorkout(workoutDate: LocalDate): Flow<WorkoutWithExercisesAndSets?>

    fun getWorkoutDays(): Flow<List<LocalDate>>

    suspend fun deleteWorkout(workout: Workout)

    suspend fun updateWorkout(workout: Workout)
}