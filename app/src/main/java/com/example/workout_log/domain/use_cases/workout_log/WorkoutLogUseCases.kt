package com.example.workout_log.domain.use_cases.workout_log

import javax.inject.Inject

data class WorkoutLogUseCases @Inject constructor(
    val getWorkoutById: GetWorkoutById,
    val getExercisesAndSets: GetExercisesAndSets,
    val updateSetWeight: UpdateSetWeight,
    val updateSetReps: UpdateSetReps
)