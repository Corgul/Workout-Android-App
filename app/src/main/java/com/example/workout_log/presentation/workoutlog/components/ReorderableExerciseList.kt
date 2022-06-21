package com.example.workout_log.presentation.workoutlog.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.workout_log.domain.model.Exercise
import com.example.workout_log.presentation.util.ReorderableList
import org.burnoutcrew.reorderable.ItemPosition

@Composable
fun ReorderableExerciseList(
    exercises: List<Exercise>,
    onMove: (ItemPosition, ItemPosition) -> Unit
) {
    ReorderableList(data = exercises, onMove = onMove) { exercise ->
        Text("Exercise: ${exercise.exerciseName}", modifier = Modifier.padding(16.dp))
    }
}