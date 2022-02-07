package com.example.workout_log.domain.use_cases.workout_log

import javax.inject.Inject

data class WorkoutLogUseCases @Inject constructor(
    val getWorkoutById: GetWorkoutById,
    val getWorkoutWithExercisesAndSets: GetWorkoutWithExercisesAndSets,
    val updateSetWeight: UpdateSetWeight,
    val updateSetReps: UpdateSetReps,
    val addSet: AddSet
)