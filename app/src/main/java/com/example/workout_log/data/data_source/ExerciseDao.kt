package com.example.workout_log.data.data_source

import androidx.room.*
import com.example.workout_log.domain.model.Exercise
import com.example.workout_log.domain.model.ExerciseAndExerciseSets
import com.example.workout_log.domain.model.ExerciseSet
import com.example.workout_log.domain.model.WorkoutWithExercisesAndSets
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface ExerciseDao {
    @Query("SELECT COUNT(*) FROM Exercises WHERE WorkoutID = :workoutId")
    suspend fun getNumberOfExercisesForWorkout(workoutId: Long): Int

    @Query("SELECT * FROM Exercises WHERE WorkoutID = :workoutId ORDER BY ExercisePositionNumber")
    suspend fun getExercisesForWorkout(workoutId: Long): List<Exercise>

    @Update
    suspend fun updateExercises(exercise: List<Exercise>)

    @Insert
    suspend fun insertExercises(exercises: List<Exercise>): List<Long>

    @Delete
    suspend fun deleteExercise(exercise: Exercise)

    @Query("DELETE FROM Exercises")
    suspend fun deleteExercises()
}