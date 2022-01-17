package com.example.workout_log.domain.model

import androidx.room.Embedded
import androidx.room.Relation

data class WorkoutAndExercises(
    @Embedded
    val workout: Workout,

    @Relation(
        parentColumn = "WorkoutID",
        entityColumn = "WorkoutID"
    )
    val exercises: List<Exercise>
)