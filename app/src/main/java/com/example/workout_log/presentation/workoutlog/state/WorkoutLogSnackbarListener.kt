package com.example.workout_log.presentation.workoutlog.state

import com.example.workout_log.domain.model.Exercise

interface WorkoutLogSnackbarListener {
    fun deleteExerciseSnackbarDismissed(exercise: Exercise)
    fun deleteExerciseSnackbarUndoClicked()

    fun deleteWorkoutSnackbarDismissed()
    fun deleteWorkoutSnackbarUndoClicked()
}