package com.example.workout_log.domain.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun LocalDate.formatDate(): String {
    val formatter = DateTimeFormatter.ofPattern("dd LLLL")
    return formatter.format(this)
}

val Context.workoutDataStore: DataStore<Preferences> by preferencesDataStore(name = "workoutPreferences")