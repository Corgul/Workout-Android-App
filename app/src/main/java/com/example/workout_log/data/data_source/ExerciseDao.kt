package com.example.workout_log.data.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.workout_log.domain.model.Exercise
import com.example.workout_log.domain.model.ExerciseAndExerciseSets
import com.example.workout_log.domain.model.ExerciseSet
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM Exercises WHERE WorkoutID = :workoutId")
    fun getExercisesAndSetsForWorkout(workoutId: Long): Flow<List<ExerciseAndExerciseSets>>

    @Insert
    suspend fun insertExercises(exercises: List<Exercise>): List<Long>

    @Query("DELETE FROM Exercises")
    suspend fun deleteExercises()
}