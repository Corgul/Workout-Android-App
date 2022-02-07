package com.example.workout_log.presentation.workoutlog.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.workout_log.domain.model.Exercise
import com.example.workout_log.domain.model.Workout
import com.example.workout_log.presentation.workoutlog.WorkoutLogViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun ExerciseBottomSheet(
    viewModel: WorkoutLogViewModel,
    exercise: Exercise,
    coroutineScope: CoroutineScope,
    bottomSheetState: ModalBottomSheetState
) {
    Column() {
        DeleteRow(rowText = "Delete Exercise") {
            coroutineScope.launch { bottomSheetState.hide() }
            viewModel.deleteExercise(exercise)
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun WorkoutBottomSheet(
    viewModel: WorkoutLogViewModel,
    workout: Workout?,
    coroutineScope: CoroutineScope,
    bottomSheetState: ModalBottomSheetState
) {
    Column() {
        DeleteRow(rowText = "Delete Workout") {
            coroutineScope.launch { bottomSheetState.hide() }
            viewModel.deleteWorkout(workout)
        }
    }
}

@Composable
fun DeleteRow(rowText: String, onDelete: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onDelete()
            }
            .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 24.dp)
    ) {
        Icon(imageVector = Icons.Default.Delete, contentDescription = rowText, modifier = Modifier.padding(end = 8.dp))
        Text(text = rowText, modifier = Modifier.padding(start = 8.dp))
    }
}