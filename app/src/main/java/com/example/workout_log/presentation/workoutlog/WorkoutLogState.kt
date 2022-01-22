package com.example.workout_log.presentation.workoutlog

import com.example.workout_log.domain.model.ExerciseAndExerciseSets
import com.example.workout_log.domain.model.Workout

data class WorkoutLogState(
    val exercisesAndSets: List<ExerciseAndExerciseSets> = emptyList(),
    val workoutId: Long = Workout.invalidWorkoutId
)