package com.example.workout_log.domain.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@kotlinx.parcelize.Parcelize
@Entity(tableName = "ExerciseTypes")
data class ExerciseType(
    @PrimaryKey
    @ColumnInfo(name = "ID")
    @NonNull
    val exerciseTypeId: Int,

    @ColumnInfo(name = "ExerciseType")
    @NonNull
    val exerciseType: String
) : Parcelable {

}