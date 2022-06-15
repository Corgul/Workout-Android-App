package com.example.workout_log.presentation.util

import com.example.workout_log.domain.util.formatDate
import java.time.LocalDate

object WorkoutNameHelper {
    fun getWorkoutName(workoutName: String?, workoutDate: Long): String {
        val today = LocalDate.now()
        if (today.formatDate() == workoutName || workoutDate == today.toEpochDay()) {
            return "Today's Workout"
        }
        return workoutName ?: "New Workout"
    }
}