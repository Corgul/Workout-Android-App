package com.example.workout_log.domain.model

import androidx.room.Embedded
import androidx.room.Relation

data class ExerciseAndExerciseSets(
    @Embedded
    val exercise: Exercise,

    @Relation(
        parentColumn = "ExerciseID",
        entityColumn = "ExerciseID"
    )
    var sets: List<ExerciseSet>
)