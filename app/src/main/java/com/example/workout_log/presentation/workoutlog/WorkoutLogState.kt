package com.example.workout_log.presentation.workoutlog

import com.example.workout_log.domain.model.Exercise

data class WorkoutLogState(
    val exercises: List<Exercise> = emptyList(),
    val workoutId: Long = -1L
)