package com.example.workout_log.domain.model

import androidx.annotation.NonNull
import androidx.room.*

@Entity(
    tableName = "ExerciseSets",
    foreignKeys = [
        ForeignKey(
            entity = Exercise::class,
            parentColumns = ["ExerciseID"],
            childColumns = ["ExerciseID"]
        )
    ],
    indices = [Index("ExerciseID")]
)
data class ExerciseSet(
    @ColumnInfo(name = "ExerciseID")
    val exerciseId: Long = 0,

    @ColumnInfo(name = "SetNumber")
    val setNumber: Int = 1,

    @ColumnInfo(name = "Reps")
    val reps: Int = 0,

    @ColumnInfo(name = "Weight")
    val weight: Int = 0
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ExerciseSetID")
    @NonNull
    var setId: Long = 0
}