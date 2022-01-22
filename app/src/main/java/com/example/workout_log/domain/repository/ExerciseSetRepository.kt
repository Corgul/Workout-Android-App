package com.example.workout_log.domain.repository

import com.example.workout_log.domain.model.ExerciseSet

interface ExerciseSetRepository {
    suspend fun insertSets(sets: List<ExerciseSet>)
}