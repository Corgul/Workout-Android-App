package com.example.workout_log.data.repository

import com.example.workout_log.data.data_source.ExerciseNameDao
import com.example.workout_log.domain.model.ExerciseTypeWithNames
import com.example.workout_log.domain.repository.ExerciseNameRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseNameRepositoryImpl @Inject constructor(
    private val exerciseName: ExerciseNameDao
) : ExerciseNameRepository {
    override fun getExerciseTypeWithNames(exerciseTypeId: Int): Flow<List<ExerciseTypeWithNames>> =
        exerciseName.getExerciseTypeWithNames(exerciseTypeId)
}