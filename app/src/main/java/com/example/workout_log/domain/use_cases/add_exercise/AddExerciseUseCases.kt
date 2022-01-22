package com.example.workout_log.domain.use_cases.add_exercise

import com.example.workout_log.domain.use_cases.workout_log.CreateWorkout
import javax.inject.Inject

class AddExerciseUseCases @Inject constructor(
    val getExerciseTypesWithExerciseNames: GetExerciseTypesWithExerciseNames,
    val saveExercises: SaveExercises,
    val createWorkout: CreateWorkout
)