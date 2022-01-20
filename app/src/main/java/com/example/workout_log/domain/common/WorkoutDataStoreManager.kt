package com.example.workout_log.domain.common

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.example.workout_log.domain.util.workoutDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutDataStore @Inject constructor(
    @ApplicationContext context: Context
) {
    private val dataStore = context.workoutDataStore

    val preferencesFlow = dataStore.data
        .map { preferences ->
            preferences[WORKOUT_ID] ?: -1L
        }

    suspend fun storeWorkoutId(workoutId: Long?) {
        if (workoutId == null || workoutId == -1L) {
            return
        }

        dataStore.edit { preferences ->
            preferences[WORKOUT_ID] = workoutId
        }
    }

    companion object {
        private const val WORKOUT_ID_KEY_NAME = "WORKOUT_ID"
        private val WORKOUT_ID = longPreferencesKey(WORKOUT_ID_KEY_NAME)
    }
}