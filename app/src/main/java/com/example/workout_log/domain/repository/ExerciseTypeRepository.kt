package com.example.workout_log.domain.repository

import com.example.workout_log.domain.model.ExerciseType
import kotlinx.coroutines.flow.Flow

interface ExerciseTypeRepository {
    fun getExerciseTypes(): Flow<List<ExerciseType>>
}