package com.example.workout_log.presentation.workoutlog.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import com.example.workout_log.domain.model.Exercise

@Composable
fun ReorderExercisesDialog(
    show: Boolean,
    exercises: List<Exercise>,
    onDismiss: () -> Unit,
    onConfirm: (exercises: List<Exercise>) -> Unit,
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = { onConfirm(exercises) }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Dismiss")
                }
            },
            title = { Text(text = "Reorder Exercises") },
            text = { Text("Exercises to reorder  here") }
        )
    }
}