package com.example.workout_log.domain.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@kotlinx.parcelize.Parcelize
@Entity(
    tableName = "Exercises",
    foreignKeys = [
        ForeignKey(
            entity = Workout::class,
            parentColumns = ["WorkoutID"],
            childColumns = ["WorkoutID"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
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
    val exerciseName: String,

    @ColumnInfo(name = "ExercisePositionNumber")
    var exercisePosition: Int
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ExerciseID")
    @NonNull
    var exerciseId: Long = 0L

    companion object {
        fun from(workoutId: Long, numberOfWorkoutsForExercise: Int, exerciseNameList: List<ExerciseName>): List<Exercise> {
            var exercisePosition = numberOfWorkoutsForExercise + 1
            val list = mutableListOf<Exercise>()
            exerciseNameList.forEach { exerciseName ->
                list.add(exerciseName.toExercise(workoutId, exercisePosition))
                exercisePosition += 1
            }
            return list
        }
    }
}