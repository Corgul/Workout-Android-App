package com.example.workout_log.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = Grey500,
    primaryVariant = Grey800,
    secondary = Indigo200,
    secondaryVariant = Indigo700,
    onPrimary = White,
    onSecondary = Grey800
)

@Composable
fun WorkoutlogTheme(content: @Composable() () -> Unit) {
    val colors = DarkColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}