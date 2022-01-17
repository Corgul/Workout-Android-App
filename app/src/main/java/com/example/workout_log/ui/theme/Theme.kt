package com.example.workout_log.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = Grey500,
    primaryVariant = Grey800,
    secondary = Red500,
    secondaryVariant = Red700,
    onPrimary = White
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