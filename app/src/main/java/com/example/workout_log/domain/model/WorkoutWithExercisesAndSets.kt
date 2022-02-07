package com.example.workout_log.domain.model

import androidx.room.Embedded
import androidx.room.Relation

data class WorkoutWithExercisesAndSets(
    @Embedded val workout: Workout,
    @Relation(
        entity = Exercise::class,
        parentColumn = "WorkoutID",
        entityColumn = "WorkoutID"
    )
    val exercisesAndSets: List<ExerciseAndExerciseSets>
)