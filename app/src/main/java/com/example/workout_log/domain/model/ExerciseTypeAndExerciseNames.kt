package com.example.workout_log.domain.model

import androidx.room.Embedded
import androidx.room.Relation

data class ExerciseTypeWithNames(
    @Embedded val exerciseType: ExerciseType,
    @Relation(
        parentColumn = "ID",
        entityColumn = "ExerciseTypeID"
    )
    val exerciseNames: List<ExerciseName>
)