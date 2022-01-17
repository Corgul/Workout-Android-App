package com.example.workout_log.data.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.workout_log.domain.model.ExerciseType
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseTypeDao {
    @Query("SELECT * FROM ExerciseTypes")
    fun getExerciseTypes(): Flow<List<ExerciseType>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(exerciseTypes: List<ExerciseType>)
}