package com.example.workout_log.data.repository

import com.example.workout_log.data.data_source.ExerciseDao
import com.example.workout_log.domain.model.Exercise
import com.example.workout_log.domain.model.Workout
import com.example.workout_log.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseRepositoryImpl @Inject constructor(
    private val exerciseDao: ExerciseDao
) : ExerciseRepository {
    override suspend fun addExercisesForWorkout(exercises: List<Exercise>) {
        exerciseDao.insertExercises(exercises)
    }

    override fun getExercisesForWorkout(workout: Workout?): Flow<List<Exercise>> =
        exerciseDao.getExercisesForWorkout(workout?.workoutId)

    override suspend fun deleteExercises() = exerciseDao.deleteExercises()
}