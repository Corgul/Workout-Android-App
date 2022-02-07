package com.example.workout_log.domain.common

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.example.workout_log.domain.model.Workout
import com.example.workout_log.domain.util.workoutDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutDataStore @Inject constructor(
    @ApplicationContext context: Context
) {
    private val dataStore = context.workoutDataStore

    val workoutIdPreferencesFlow = dataStore.data
        .map { preferences ->
            preferences[WORKOUT_ID] ?: Workout.invalidWorkoutId
        }

    val workoutDatePreferencesFlow = dataStore.data
        .map { preferences ->
            val epochDay = preferences[WORKOUT_DATE]
            if (epochDay == null) {
                LocalDate.now()
            } else {
                LocalDate.ofEpochDay(epochDay)
            }
        }

    suspend fun storeWorkoutId(workoutId: Long?) {
        if (workoutId == null || workoutId == Workout.invalidWorkoutId) {
            return
        }

        dataStore.edit { preferences ->
            preferences[WORKOUT_ID] = workoutId
        }
    }

    suspend fun storeWorkoutDate(date: LocalDate?) {
        if (date == null) {
            return
        }

        dataStore.edit { preferences ->
            preferences[WORKOUT_DATE] = date.toEpochDay()
        }
    }

    companion object {
        private const val WORKOUT_ID_KEY_NAME = "WORKOUT_ID"
        private const val WORKOUT_DATE_KEY_NAME = "WORKOUT_DATE"
        private val WORKOUT_ID = longPreferencesKey(WORKOUT_ID_KEY_NAME)
        private val WORKOUT_DATE = longPreferencesKey(WORKOUT_DATE_KEY_NAME)
    }
}