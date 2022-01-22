package com.example.workout_log.data.repository

import com.example.workout_log.data.data_source.ExerciseTypeDao
import com.example.workout_log.domain.model.ExerciseTypeWithNames
import com.example.workout_log.domain.repository.ExerciseTypeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseTypeRepositoryImpl @Inject constructor(
    private val exerciseTypeDao: ExerciseTypeDao
) : ExerciseTypeRepository {
    override fun getExerciseTypesWithNames(): Flow<List<ExerciseTypeWithNames>> =
        exerciseTypeDao.getExerciseTypeWithNames()
}