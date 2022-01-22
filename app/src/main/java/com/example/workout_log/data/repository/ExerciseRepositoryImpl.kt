package com.example.workout_log.data.repository

import com.example.workout_log.data.data_source.ExerciseDao
import com.example.workout_log.domain.model.Exercise
import com.example.workout_log.domain.model.ExerciseAndExerciseSets
import com.example.workout_log.domain.model.ExerciseSet
import com.example.workout_log.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseRepositoryImpl @Inject constructor(
    private val exerciseDao: ExerciseDao
) : ExerciseRepository {
    override suspend fun insertExercises(exercises: List<Exercise>): List<Long> =
        exerciseDao.insertExercises(exercises)

    override fun getExercisesAndSetsForWorkout(workoutId: Long): Flow<List<ExerciseAndExerciseSets>> =
        exerciseDao.getExercisesAndSetsForWorkout(workoutId)

    override suspend fun deleteExercises() = exerciseDao.deleteExercises()
}