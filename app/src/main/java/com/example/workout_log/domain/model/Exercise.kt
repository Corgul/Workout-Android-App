package com.example.workout_log.domain.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import java.time.LocalDate

@kotlinx.parcelize.Parcelize
@Entity(
    tableName = "Exercises",
    foreignKeys = [
        ForeignKey(
            entity = Workout::class,
            parentColumns = ["WorkoutID"],
            childColumns = ["WorkoutID"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        )
    ],
    indices = [Index("WorkoutID")]
)
data class Exercise(
    @ColumnInfo(name = "WorkoutID")
    val workoutId: Long,

    @ColumnInfo(name = "ExerciseType")
    val exerciseTypeName: String,

    @ColumnInfo(name = "ExerciseName")
    val exerciseName: String
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ExerciseID")
    @NonNull
    var exerciseId: Long = 0L

    companion object {
        fun from(workoutId: Long, exerciseNameList: List<ExerciseName>): List<Exercise> {
            val list = mutableListOf<Exercise>()
            exerciseNameList.forEach { exerciseName ->
                list.add(exerciseName.toExercise(workoutId))
            }
            return list
        }
    }
}