package com.example.workout_log.presentation.workoutlog.state

import com.example.workout_log.domain.model.ExerciseAndExerciseSets
import com.example.workout_log.domain.model.ExerciseSet

interface WorkoutLogCardListener {
    fun onWeightChanged(set: ExerciseSet, newWeight: Int?)
    fun onRepsChanged(set: ExerciseSet, newReps: Int?)
    fun onAddSetButtonClicked(exerciseAndExerciseSets: ExerciseAndExerciseSets)
}