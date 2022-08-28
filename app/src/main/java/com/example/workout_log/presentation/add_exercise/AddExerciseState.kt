package com.example.workout_log.presentation.add_exercise

import com.example.workout_log.domain.model.ExerciseName
import com.example.workout_log.domain.model.ExerciseType

data class AddExerciseState(
    val exerciseTypes: List<ExerciseType> = emptyList(),
    val exerciseNames: List<ExerciseName> = emptyList(),
)