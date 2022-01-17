package com.example.workout_log.domain.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@kotlinx.parcelize.Parcelize
@Entity(
    tableName = "Workouts",
    indices = [Index(value = ["Date"], unique = true)]
)
data class Workout(
    @ColumnInfo(name = "WorkoutName")
    val workoutName: String = "",

    @ColumnInfo(name = "Date")
    val date: LocalDate

) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "WorkoutID")
    var workoutId: Long = 0
}