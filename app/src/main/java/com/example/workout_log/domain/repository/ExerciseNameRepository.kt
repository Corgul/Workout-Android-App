package com.example.workout_log.domain.repository

import com.example.workout_log.domain.model.ExerciseName
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

interface ExerciseNameRepository {
    @ExperimentalCoroutinesApi
    fun getExerciseNames(exerciseTypeId: Int): Flow<List<ExerciseName>>
}