package com.example.workout_log.domain.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "ExerciseSets",
    foreignKeys = [
        ForeignKey(
            entity = Exercise::class,
            parentColumns = ["ExerciseID"],
            childColumns = ["ExerciseID"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        )
    ],
    indices = [Index("ExerciseID")]
)
data class ExerciseSet(
    @ColumnInfo(name = "ExerciseID")
    val exerciseId: Long = 0,

    @ColumnInfo(name = "SetNumber")
    var setNumber: Int = 1,

    @ColumnInfo(name = "Reps")
    var reps: Int = 0,

    @ColumnInfo(name = "Weight")
    var weight: Int = 0
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ExerciseSetID")
    @NonNull
    var setId: Long = 0L

    companion object {
        fun from(exerciseIds: List<Long>): List<ExerciseSet> = exerciseIds.map { ExerciseSet(it) }
    }
}