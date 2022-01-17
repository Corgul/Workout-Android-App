package com.example.workout_log.data.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.workout_log.domain.model.Exercise
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM Exercises WHERE WorkoutID = :workoutId")
    fun getExercisesForWorkout(workoutId: Long?): Flow<List<Exercise>>

    @Insert
    suspend fun insertExercises(exercises: List<Exercise>)

    @Query("DELETE FROM Exercises")
    suspend fun deleteExercises()
}