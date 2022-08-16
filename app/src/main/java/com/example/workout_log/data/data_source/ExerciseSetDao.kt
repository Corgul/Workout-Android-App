package com.example.workout_log.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.example.workout_log.domain.model.ExerciseSet

@Dao
interface ExerciseSetDao {
    @Insert
    suspend fun insertSets(sets: List<ExerciseSet>)

    @Insert
    suspend fun insertSet(set: ExerciseSet)

    @Update
    suspend fun updateExerciseSet(exerciseSet: ExerciseSet)

    @Delete
    suspend fun deleteSets(exerciseSets: List<ExerciseSet>)

    @Update
    suspend fun updateExerciseSets(exerciseSets: List<ExerciseSet>)
}