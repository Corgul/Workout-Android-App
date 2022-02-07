package com.example.workout_log.presentation.workoutlog

import com.example.workout_log.domain.model.Exercise

sealed class WorkoutLogBottomSheet {
    object WorkoutBottomSheet: WorkoutLogBottomSheet()
    class ExerciseBottomSheet(val exercise: Exercise): WorkoutLogBottomSheet()
}