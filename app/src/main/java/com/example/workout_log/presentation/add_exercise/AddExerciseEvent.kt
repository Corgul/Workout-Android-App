package com.example.workout_log.presentation.add_exercise

import com.example.workout_log.domain.model.ExerciseName
import com.example.workout_log.domain.model.ExerciseType

sealed class AddExerciseEvent {
    data class ExerciseTypeClicked(val exerciseType: ExerciseType) : AddExerciseEvent()
    data class OnSaveClicked(val selectedExerciseNames: List<ExerciseName>, val onExercisesSaved: (workoutDateLong: Long) -> Unit) :
        AddExerciseEvent()
}