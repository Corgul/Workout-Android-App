package com.example.workout_log.presentation.workoutlog.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
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
        Divider(modifier = Modifier.fillMaxWidth().width(1.dp))
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(exercise.exerciseName, modifier = Modifier.padding(16.dp).weight(0.75f), maxLines = 1, overflow = TextOverflow.Ellipsis)
            Icon(imageVector = Icons.Filled.DragIndicator, contentDescription = "Drag Indicator", modifier = Modifier.padding(16.dp))
        }
        Divider(modifier = Modifier.fillMaxWidth().width(1.dp))
    }
}