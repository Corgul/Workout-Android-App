package com.example.workout_log.domain.repository

import com.example.workout_log.domain.model.ExerciseSet

interface ExerciseSetRepository {
    suspend fun insertSets(sets: List<ExerciseSet>)

    suspend fun updateExerciseSet(exerciseSet: ExerciseSet)

    suspend fun updateExerciseSets(exerciseSets: List<ExerciseSet>)

    suspend fun deleteSets(exerciseSets: List<ExerciseSet>)

    suspend fun insertSet(set: ExerciseSet)
}