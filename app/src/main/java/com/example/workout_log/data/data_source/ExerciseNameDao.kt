package com.example.workout_log.data.data_source

import androidx.room.*
import com.example.workout_log.domain.model.ExerciseName
import com.example.workout_log.domain.model.ExerciseTypeWithNames
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseNameDao {
    @Query("SELECT * FROM ExerciseNames")
    fun getExerciseNames(): Flow<List<ExerciseName>>

    @Transaction
    @Query("SELECT * FROM ExerciseTypes WHERE ID = :exerciseTypeId")
    fun getExerciseTypeWithNames(exerciseTypeId: Int): Flow<List<ExerciseTypeWithNames>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(exerciseNames: List<ExerciseName>)
}