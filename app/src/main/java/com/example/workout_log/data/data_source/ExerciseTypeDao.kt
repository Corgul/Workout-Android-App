package com.example.workout_log.data.data_source

import androidx.room.*
import com.example.workout_log.domain.model.ExerciseType
import com.example.workout_log.domain.model.ExerciseTypeWithNames
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseTypeDao {
    @Transaction
    @Query("SELECT * FROM ExerciseTypes")
    fun getExerciseTypeWithNames(): Flow<List<ExerciseTypeWithNames>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(exerciseTypes: List<ExerciseType>)
}