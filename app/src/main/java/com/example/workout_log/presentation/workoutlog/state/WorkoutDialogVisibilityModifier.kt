package com.example.workout_log.presentation.workoutlog.state

import com.example.workout_log.domain.model.ExerciseAndExerciseSets

interface WorkoutDialogVisibilityModifier {
    fun showEditExerciseDialog(exerciseAndSets: ExerciseAndExerciseSets)
    fun showEditWorkoutNameDialog()
    fun showReorderExerciseDialog()
}