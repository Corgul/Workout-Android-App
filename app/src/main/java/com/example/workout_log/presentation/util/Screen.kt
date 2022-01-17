package com.example.workout_log.presentation.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val screenName: String, val icon: ImageVector) {
    object WorkoutLogScreen: Screen("workout_log_screen", "Home", Icons.Filled.Home)
    object CalendarScreen: Screen("calendar_screen", "Calendar", Icons.Filled.CalendarToday)
    object AddExerciseScreen: Screen("add_exercise_screen", "Add Exercise", Icons.Filled.Add)
}