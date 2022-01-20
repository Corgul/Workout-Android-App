package com.example.workout_log.domain.repository

import com.example.workout_log.domain.model.Workout
import java.time.LocalDate

interface WorkoutRepository {
    suspend fun doesWorkoutExist(date: LocalDate): Boolean

    suspend fun doesWorkoutExist(workoutId: Long): Boolean

    suspend fun getWorkoutForDate(date: LocalDate): Workout?

    suspend fun getWorkoutForId(workoutId: Long): Workout?

    suspend fun addWorkout(workout: Workout)
}