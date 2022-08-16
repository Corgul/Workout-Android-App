package com.example.workout_log.presentation.workoutlog.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.workout_log.domain.model.ExerciseSet
import com.example.workout_log.presentation.util.ReorderableList
import org.burnoutcrew.reorderable.ItemPosition

@Composable
fun ReorderableSetList(
    sets: List<ExerciseSet>,
    onChecked: (exerciseSet: ExerciseSet, isChecked: Boolean) -> Unit,
    onMove: (ItemPosition, ItemPosition) -> Unit
) {
    ReorderableList(data = sets, onMove = onMove) { exerciseSet ->
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .width(1.dp)
        )

        ReorderableSetListRow(exerciseSet = exerciseSet, onChecked = onChecked)

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .width(1.dp)
        )
    }
}

@Composable
fun ReorderableSetListRow(
    exerciseSet: ExerciseSet,
    onChecked: (exerciseSet: ExerciseSet, isChecked: Boolean) -> Unit
) {
    var checkedState by remember { mutableStateOf(false) }
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Checkbox(checked = checkedState, onCheckedChange = {
            checkedState = it
            onChecked(exerciseSet, it)
        })
        Text(text = "${exerciseSet.setNumber}")
        Text(text = "${exerciseSet.weight}")
        Text(text = "${exerciseSet.reps}")

        Icon(imageVector = Icons.Filled.DragHandle, contentDescription = "Drag Indicator", modifier = Modifier.padding(16.dp))
    }
}