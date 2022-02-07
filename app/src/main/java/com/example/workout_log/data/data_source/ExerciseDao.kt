package com.example.workout_log.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.workout_log.domain.model.Exercise
import com.example.workout_log.domain.model.ExerciseAndExerciseSets
import com.example.workout_log.domain.model.ExerciseSet
import com.example.workout_log.domain.model.WorkoutWithExercisesAndSets
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface ExerciseDao {
    @Insert
    suspend fun insertExercises(exercises: List<Exercise>): List<Long>

    @Delete
    suspend fun deleteExercise(exercise: Exercise)

    @Query("DELETE FROM Exercises")
    suspend fun deleteExercises()
}