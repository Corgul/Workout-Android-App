package com.example.workout_log.presentation.workoutlog.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DynamicFeed
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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
        BottomSheetRow(rowText = "Delete Exercise", Icons.Default.Delete) {
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
    exercises: List<Exercise>,
    coroutineScope: CoroutineScope,
    bottomSheetState: ModalBottomSheetState,
    scaffoldState: ScaffoldState
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        BottomSheetRow(rowText = "Edit Workout Name", rowIcon = Icons.Default.Edit) {
            coroutineScope.launch { bottomSheetState.hide() }

            viewModel.showEditWorkoutNameDialog()
        }
        
        BottomSheetRow(rowText = "Reorder Exercises", rowIcon = Icons.Default.DynamicFeed) {
            coroutineScope.launch { bottomSheetState.hide() }
            if (exercises.size <= 1) {
                coroutineScope.launch { scaffoldState.snackbarHostState.showSnackbar("You need more than 2 exercises to reorder") }
                return@BottomSheetRow
            }
            viewModel.showReorderExerciseDialog()
        }
        
        BottomSheetRow(rowText = "Delete Workout", Icons.Default.Delete) {
            coroutineScope.launch {
                bottomSheetState.hide()
                val snackbarResult = scaffoldState.snackbarHostState.showSnackbar("Deleted Workout", actionLabel = "Undo")
                when (snackbarResult) {
                    SnackbarResult.Dismissed -> viewModel.deleteWorkoutSnackbarDismissed()
                    SnackbarResult.ActionPerformed -> viewModel.deleteWorkoutSnackbarUndoClicked()
                }
            }
            viewModel.deleteWorkoutClicked(workout)
        }
    }
}

@Composable
fun BottomSheetRow(rowText: String, rowIcon: ImageVector, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(vertical = 16.dp, horizontal = 16.dp)
    ) {
        Icon(imageVector = rowIcon, contentDescription = rowText, modifier = Modifier.padding(end = 8.dp))
        Text(text = rowText, modifier = Modifier.padding(start = 8.dp))
    }
}