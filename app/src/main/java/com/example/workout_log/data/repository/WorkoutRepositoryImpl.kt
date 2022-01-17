package com.example.workout_log.data.repository

import com.example.workout_log.data.data_source.WorkoutDao
import com.example.workout_log.domain.model.Workout
import com.example.workout_log.domain.repository.WorkoutRepository
import java.time.LocalDate
import javax.inject.Inject

class WorkoutRepositoryImpl @Inject constructor(
    private val workoutDao: WorkoutDao
) : WorkoutRepository {
    override suspend fun doesWorkoutExist(date: LocalDate): Boolean = workoutDao.doesWorkoutExistForDate(date)

    override suspend fun getWorkoutForDate(date: LocalDate): Workout = workoutDao.getWorkoutForDate(date)

    override suspend fun addWorkout(workout: Workout) = workoutDao.insertWorkout(workout)
}