package com.example.workout_log.data.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.workout_log.domain.model.Workout
import java.time.LocalDate

@Dao
interface WorkoutDao {
    @Query("SELECT EXISTS(SELECT * FROM Workouts WHERE Date = :date)")
    suspend fun doesWorkoutExistForDate(date: LocalDate): Boolean

    @Query("SELECT EXISTS(SELECT * FROM Workouts WHERE WorkoutID = :workoutId)")
    suspend fun doesWorkoutExist(workoutId: Long): Boolean

    @Query("SELECT * FROM Workouts WHERE Date = :date LIMIT 1")
    suspend fun getWorkoutForDate(date: LocalDate): Workout

    @Query("SELECT * FROM Workouts WHERE WorkoutID = :workoutId")
    suspend fun getWorkoutForId(workoutId: Long): Workout

    @Insert
    suspend fun insertWorkout(workout: Workout)
}