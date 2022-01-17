package com.example.workout_log.domain.repository

import com.example.workout_log.domain.model.ExerciseTypeWithNames
import kotlinx.coroutines.flow.Flow

interface ExerciseNameRepository {
    fun getExerciseTypeWithNames(exerciseTypeId: Int): Flow<List<ExerciseTypeWithNames>>
}