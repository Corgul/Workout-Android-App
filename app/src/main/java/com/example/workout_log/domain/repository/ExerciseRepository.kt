package com.example.workout_log.domain.repository

import com.example.workout_log.domain.model.Exercise
import com.example.workout_log.domain.model.ExerciseAndExerciseSets
import com.example.workout_log.domain.model.WorkoutWithExercisesAndSets
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ExerciseRepository {
    suspend fun insertExercises(exercises: List<Exercise>): List<Long>

    suspend fun deleteExercise(exercise: Exercise)

    suspend fun deleteExercises()
}