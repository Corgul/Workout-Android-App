package com.example.workout_log.data.repository

import com.example.workout_log.data.data_source.WorkoutDao
import com.example.workout_log.domain.model.Workout
import com.example.workout_log.domain.model.WorkoutWithExercisesAndSets
import com.example.workout_log.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class WorkoutRepositoryImpl @Inject constructor(
    private val workoutDao: WorkoutDao
) : WorkoutRepository {
    override suspend fun doesWorkoutExist(date: LocalDate): Boolean = workoutDao.doesWorkoutExistForDate(date)

    override suspend fun doesWorkoutExist(workoutId: Long): Boolean = workoutDao.doesWorkoutExist(workoutId)

    override suspend fun getWorkoutForDate(date: LocalDate): Workout? = workoutDao.getWorkoutForDate(date)

    override suspend fun addWorkout(workout: Workout): Long = workoutDao.insertWorkout(workout)

    override suspend fun getWorkoutForId(workoutId: Long) = workoutDao.getWorkoutForId(workoutId)

    override fun getExercisesAndSetsForWorkout(workoutDate: LocalDate): Flow<WorkoutWithExercisesAndSets?> =
        workoutDao.getExercisesAndSetsForWorkout(workoutDate)

    override fun getWorkoutDays(): Flow<List<LocalDate>> = workoutDao.getWorkoutDays()

    override suspend fun deleteWorkout(workout: Workout) = workoutDao.deleteWorkout(workout)

    override suspend fun updateWorkout(workout: Workout) = workoutDao.updateWorkout(workout)
}