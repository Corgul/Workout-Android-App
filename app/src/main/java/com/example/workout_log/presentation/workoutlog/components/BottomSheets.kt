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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.workout_log.R
import com.example.workout_log.domain.model.Exercise
import com.example.workout_log.domain.model.ExerciseAndExerciseSets
import com.example.workout_log.domain.model.Workout
import com.example.workout_log.presentation.workoutlog.WorkoutLogViewModel
import com.example.workout_log.presentation.workoutlog.state.WorkoutLogDialogState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun ExerciseBottomSheet(
    viewModel: WorkoutLogViewModel,
    exerciseAndExerciseSets: ExerciseAndExerciseSets,
    coroutineScope: CoroutineScope,
    bottomSheetState: ModalBottomSheetState,
    scaffoldState: ScaffoldState,
    workoutLogDialogState: WorkoutLogDialogState
) {
    Column() {
        val context = LocalContext.current
        BottomSheetRow(rowText = stringResource(id = R.string.edit_exercise), rowIcon = Icons.Default.Edit) {
            coroutineScope.launch { bottomSheetState.hide() }
            workoutLogDialogState.showEditExerciseDialog(exerciseAndExerciseSets)
        }
        BottomSheetRow(rowText = stringResource(id = R.string.delete_exercise), Icons.Default.Delete) {
            coroutineScope.launch {
                bottomSheetState.hide()
                val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(message = context.resources.getString(R.string.deleted_exercise), actionLabel = context.resources.getString(R.string.undo))
                when (snackbarResult) {
                    SnackbarResult.Dismissed -> viewModel.deleteExerciseSnackbarDismissed(exerciseAndExerciseSets.exercise)
                    SnackbarResult.ActionPerformed -> viewModel.deleteExerciseSnackbarUndoClicked()
                }
            }
            viewModel.deleteExerciseClicked(exerciseAndExerciseSets.exercise)
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
    scaffoldState: ScaffoldState,
    workoutLogDialogState: WorkoutLogDialogState
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        val context = LocalContext.current
        BottomSheetRow(rowText = stringResource(id = R.string.edit_workout_name), rowIcon = Icons.Default.Edit) {
            coroutineScope.launch { bottomSheetState.hide() }

            workoutLogDialogState.showEditWorkoutNameDialog()
        }
        
        BottomSheetRow(rowText = stringResource(id = R.string.reorder_exercises), rowIcon = Icons.Default.DynamicFeed) {
            coroutineScope.launch { bottomSheetState.hide() }
            if (exercises.size <= 1) {
                coroutineScope.launch { scaffoldState.snackbarHostState.showSnackbar(message = context.resources.getString(R.string.reorder_exercise_snackbar)) }
                return@BottomSheetRow
            }
            workoutLogDialogState.showReorderExerciseDialog()
        }
        
        BottomSheetRow(rowText = stringResource(id = R.string.delete_workout), Icons.Default.Delete) {
            coroutineScope.launch {
                bottomSheetState.hide()
                val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(message = context.resources.getString(R.string.deleted_workout), actionLabel = context.resources.getString(R.string.undo))
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