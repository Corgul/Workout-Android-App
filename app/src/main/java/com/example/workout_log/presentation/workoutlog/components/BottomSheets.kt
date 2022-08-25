package com.example.workout_log.presentation.workoutlog.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DynamicFeed
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.workout_log.R
import com.example.workout_log.domain.model.Exercise
import com.example.workout_log.domain.model.ExerciseAndExerciseSets
import com.example.workout_log.domain.model.Workout

@ExperimentalMaterialApi
@Composable
fun ExerciseBottomSheet(
    exerciseAndExerciseSets: ExerciseAndExerciseSets,
    hideBottomSheet: () -> Unit,
    showEditExerciseDialog: (ExerciseAndExerciseSets) -> Unit,
    deleteExerciseClicked: (Exercise) -> Unit
) {
    Column() {
        BottomSheetRow(rowText = stringResource(id = R.string.edit_exercise), rowIcon = Icons.Default.Edit) {
            hideBottomSheet()
            showEditExerciseDialog(exerciseAndExerciseSets)
        }
        BottomSheetRow(rowText = stringResource(id = R.string.delete_exercise), Icons.Default.Delete) {
            hideBottomSheet()
            deleteExerciseClicked(exerciseAndExerciseSets.exercise)
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun WorkoutBottomSheet(
    workout: Workout?,
    exercises: List<Exercise>,
    hideBottomSheet: () -> Unit,
    showEditWorkoutNameDialog: () -> Unit,
    showReorderExerciseDialog: () -> Unit,
    showReorderExerciseSnackbar: () -> Unit,
    deleteWorkoutClicked: (Workout?) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        BottomSheetRow(rowText = stringResource(id = R.string.edit_workout_name), rowIcon = Icons.Default.Edit) {
            hideBottomSheet()
            showEditWorkoutNameDialog()
        }
        
        BottomSheetRow(rowText = stringResource(id = R.string.reorder_exercises), rowIcon = Icons.Default.DynamicFeed) {
            hideBottomSheet()
            if (exercises.size <= 1) {
                showReorderExerciseSnackbar()
            }
            showReorderExerciseDialog()
        }
        
        BottomSheetRow(rowText = stringResource(id = R.string.delete_workout), Icons.Default.Delete) {
            hideBottomSheet()
            deleteWorkoutClicked(workout)
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