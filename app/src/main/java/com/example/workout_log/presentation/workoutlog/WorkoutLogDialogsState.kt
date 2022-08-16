package com.example.workout_log.presentation.workoutlog

import com.example.workout_log.domain.model.ExerciseAndExerciseSets

sealed class WorkoutLogDialogsState(val show: Boolean = false) {
    class ReorderExerciseDialogState(show: Boolean) : WorkoutLogDialogsState(show)
    class EditWorkoutNameDialogState(show: Boolean) : WorkoutLogDialogsState(show)
    class EditExerciseDialogState(show: Boolean, val exerciseAndExerciseSets: ExerciseAndExerciseSets) : WorkoutLogDialogsState(show)
    object UnknownDialogState : WorkoutLogDialogsState()
}