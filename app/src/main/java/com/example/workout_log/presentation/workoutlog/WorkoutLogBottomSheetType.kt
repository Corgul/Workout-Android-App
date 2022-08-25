package com.example.workout_log.presentation.workoutlog

import com.example.workout_log.domain.model.ExerciseAndExerciseSets

sealed class WorkoutLogBottomSheet {
    object WorkoutBottomSheet: WorkoutLogBottomSheet()
    class ExerciseBottomSheet(val exerciseAndExerciseSets: ExerciseAndExerciseSets): WorkoutLogBottomSheet()
    object NoBottomSheet: WorkoutLogBottomSheet()
}