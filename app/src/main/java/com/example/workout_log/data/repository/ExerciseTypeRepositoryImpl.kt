package com.example.workout_log.data.repository

import com.example.workout_log.data.data_source.ExerciseTypeDao
import com.example.workout_log.domain.repository.ExerciseTypeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseTypeRepositoryImpl @Inject constructor(
    private val exerciseTypeDao: ExerciseTypeDao
) : ExerciseTypeRepository {
    override fun getExerciseTypes() = exerciseTypeDao.getExerciseTypes()
}