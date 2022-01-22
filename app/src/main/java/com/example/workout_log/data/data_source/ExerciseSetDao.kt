package com.example.workout_log.data.data_source

import androidx.room.Dao
import androidx.room.Insert
import com.example.workout_log.domain.model.ExerciseSet

@Dao
interface ExerciseSetDao {
    @Insert
    suspend fun insertSets(sets: List<ExerciseSet>)
}