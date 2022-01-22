package com.example.workout_log.domain.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.*

@kotlinx.parcelize.Parcelize
@Entity(
    tableName = "ExerciseNames",
    foreignKeys = [
        ForeignKey(
            entity = ExerciseType::class,
            parentColumns = ["ID"],
            childColumns = ["ExerciseTypeID"]
        )
    ],
    indices = [Index("ExerciseTypeID")]
)
data class ExerciseName(
    @ColumnInfo(name = "ExerciseTypeID")
    @NonNull
    val exerciseTypeId: Int,

    @ColumnInfo(name = "ExerciseName")
    @NonNull
    val exerciseName: String,

    @ColumnInfo(name = "ExerciseTypeName")
    val exerciseTypeName: String
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "name_id")
    @NonNull
    var nameId: Long = 0

    fun toExercise(workoutId: Long): Exercise =
        Exercise(workoutId, this.exerciseTypeName, this.exerciseName)
}