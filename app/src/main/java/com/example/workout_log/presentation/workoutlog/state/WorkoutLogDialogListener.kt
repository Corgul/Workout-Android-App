package com.example.workout_log.presentation.workoutlog.state

import com.example.workout_log.domain.model.Exercise
import com.example.workout_log.domain.model.ExerciseAndExerciseSets
import com.example.workout_log.domain.model.ExerciseSet
import com.example.workout_log.domain.model.Workout

interface WorkoutLogDialogListener {
    fun onReorderExerciseDialogConfirmed(exercises: List<Exercise>)
    fun onEditWorkoutNameDialogConfirmed(newName: String, workout: Workout)
    fun onEditExerciseDialogConfirmed(exerciseSets: List<ExerciseSet>)
    fun onEditExerciseDialogDelete(exerciseAndExerciseSets: ExerciseAndExerciseSets, exerciseSets: List<ExerciseSet>)
}