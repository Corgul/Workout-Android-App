package com.example.workout_log.presentation.add_exercise

import com.example.workout_log.domain.model.ExerciseName
import com.example.workout_log.domain.model.ExerciseType

sealed class AddExerciseEvent {
    data class ExerciseTypeClicked(val exerciseType: ExerciseType): AddExerciseEvent()
    data class ExerciseNameClicked(val exerciseName: ExerciseName): AddExerciseEvent()
    data class OnSaveClicked(val onSave: (workoutId: Long?) -> Unit): AddExerciseEvent()
    object OnBackPressed: AddExerciseEvent()
}