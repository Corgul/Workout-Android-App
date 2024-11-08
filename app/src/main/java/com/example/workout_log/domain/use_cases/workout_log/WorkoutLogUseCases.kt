package com.example.workout_log.domain.use_cases.workout_log

import javax.inject.Inject

data class WorkoutLogUseCases @Inject constructor(
    val getWorkoutById: GetWorkoutById,
    val getWorkoutWithExercisesAndSets: GetWorkoutWithExercisesAndSets,
    val getExercisesForWorkout: GetExercisesForWorkout,
    val updateExercises: UpdateExercises,
    val updateSetWeight: UpdateSetWeight,
    val updateSetReps: UpdateSetReps,
    val updateWorkoutName: UpdateWorkoutName,
    val addSet: AddSet
)