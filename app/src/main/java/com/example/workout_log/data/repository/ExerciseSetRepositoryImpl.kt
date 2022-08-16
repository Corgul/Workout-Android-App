package com.example.workout_log.data.repository

import com.example.workout_log.data.data_source.ExerciseSetDao
import com.example.workout_log.domain.model.ExerciseSet
import com.example.workout_log.domain.repository.ExerciseSetRepository
import javax.inject.Inject

class ExerciseSetRepositoryImpl @Inject constructor(
    private val exerciseSetDao: ExerciseSetDao
) : ExerciseSetRepository {
    override suspend fun insertSets(sets: List<ExerciseSet>) = exerciseSetDao.insertSets(sets)

    override suspend fun updateExerciseSet(exerciseSet: ExerciseSet) =
        exerciseSetDao.updateExerciseSet(exerciseSet)

    override suspend fun updateExerciseSets(exerciseSets: List<ExerciseSet>) =
        exerciseSetDao.updateExerciseSets(exerciseSets)

    override suspend fun deleteSets(exerciseSets: List<ExerciseSet>) =
        exerciseSetDao.deleteSets(exerciseSets)

    override suspend fun insertSet(set: ExerciseSet) =
        exerciseSetDao.insertSet(set)
}