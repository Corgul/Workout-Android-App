package com.example.workout_log.data.repository

import com.example.workout_log.data.data_source.ExerciseDao
import com.example.workout_log.domain.model.Exercise
import com.example.workout_log.domain.model.ExerciseAndExerciseSets
import com.example.workout_log.domain.model.WorkoutWithExercisesAndSets
import com.example.workout_log.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseRepositoryImpl @Inject constructor(
    private val exerciseDao: ExerciseDao
) : ExerciseRepository {
    override suspend fun getNumberOfExercisesForWorkout(workoutId: Long): Int =
        exerciseDao.getNumberOfExercisesForWorkout(workoutId)

    override suspend fun getExercisesForWorkout(workoutId: Long): List<Exercise> =
        exerciseDao.getExercisesForWorkout(workoutId)

    override suspend fun updateExercises(exercises: List<Exercise>) =
        exerciseDao.updateExercises(exercises)

    override suspend fun insertExercises(exercises: List<Exercise>): List<Long> =
        exerciseDao.insertExercises(exercises)

    override suspend fun deleteExercise(exercise: Exercise) = exerciseDao.deleteExercise(exercise)

    override suspend fun deleteExercises() = exerciseDao.deleteExercises()
}