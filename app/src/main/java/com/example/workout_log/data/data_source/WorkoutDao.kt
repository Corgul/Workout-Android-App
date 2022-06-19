package com.example.workout_log.data.data_source

import androidx.room.*
import com.example.workout_log.domain.model.Workout
import com.example.workout_log.domain.model.WorkoutWithExercisesAndSets
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface WorkoutDao {
    @Query("SELECT EXISTS(SELECT * FROM Workouts WHERE Date = :date)")
    suspend fun doesWorkoutExistForDate(date: LocalDate): Boolean

    @Query("SELECT EXISTS(SELECT * FROM Workouts WHERE WorkoutID = :workoutId)")
    suspend fun doesWorkoutExist(workoutId: Long): Boolean

    @Query("SELECT * FROM Workouts WHERE Date = :date")
    suspend fun getWorkoutForDate(date: LocalDate): Workout?

    @Query("SELECT * FROM Workouts WHERE WorkoutID = :workoutId")
    suspend fun getWorkoutForId(workoutId: Long): Workout

    @Transaction
    @Query("SELECT * FROM Workouts WHERE Date = :workoutDate")
    fun getExercisesAndSetsForWorkout(workoutDate: LocalDate): Flow<WorkoutWithExercisesAndSets?>

    @Query("SELECT Date FROM WORKOUTS")
    fun getWorkoutDays(): Flow<List<LocalDate>>

    @Insert
    suspend fun insertWorkout(workout: Workout): Long

    @Delete
    suspend fun deleteWorkout(workout: Workout)
}