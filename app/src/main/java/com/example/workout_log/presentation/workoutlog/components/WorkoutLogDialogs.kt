package com.example.workout_log.presentation.workoutlog.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.workout_log.domain.model.Exercise
import com.example.workout_log.domain.model.ExerciseAndExerciseSets
import com.example.workout_log.domain.model.ExerciseSet
import com.example.workout_log.domain.model.Workout
import com.example.workout_log.domain.util.WorkoutAppLogger
import com.example.workout_log.presentation.workoutlog.WorkoutLogDialogsState
import com.example.workout_log.presentation.workoutlog.WorkoutLogTextField
import com.example.workout_log.presentation.workoutlog.state.rememberEditExerciseDialogState
import com.example.workout_log.presentation.workoutlog.workoutLogTextFieldColors
import com.example.workout_log.ui.theme.Grey475
import com.example.workout_log.ui.theme.Grey500
import org.burnoutcrew.reorderable.ItemPosition

@Composable
fun EditWorkoutNameDialog(
    dialogState: WorkoutLogDialogsState,
    workout: Workout?,
    onDismiss: () -> Unit,
    onConfirm: (newName: String, workout: Workout) -> Unit
) {
    if (dialogState is WorkoutLogDialogsState.EditWorkoutNameDialogState && dialogState.show && workout != null) {
        val workoutNameTextValue = remember { mutableStateOf(TextFieldValue(workout.workoutName)) }
        val maxWorkoutNameCharacters = 32

        Dialog(onDismissRequest = onDismiss) {
            Card(elevation = 8.dp, shape = RoundedCornerShape(12.dp)) {
                Column(modifier = Modifier.background(Grey500)) {
                    Text(
                        text = "Edit Workout Name",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(top = 16.dp, start = 16.dp, bottom = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    WorkoutLogTextField(
                        textFieldValue = workoutNameTextValue,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        isNumberTextField = false,
                        colors = workoutLogTextFieldColors(
                            backgroundColor = Grey475,
                            unfocusedIndicatorColor = MaterialTheme.colors.onSurface.copy(alpha = TextFieldDefaults.UnfocusedIndicatorLineOpacity)
                        ),
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Left)
                    ) {
                        if (it.text.length > maxWorkoutNameCharacters) {
                            return@WorkoutLogTextField
                        }
                        workoutNameTextValue.value = it
                    }

                    // Buttons
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        TextButton(onClick = onDismiss) {
                            Text(text = "CANCEL", style = LocalTextStyle.current.copy(color = Color.White))
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        TextButton(onClick = {
                            onConfirm(workoutNameTextValue.value.text, workout)
                        }) {
                            Text(text = "OK", style = LocalTextStyle.current.copy(color = Color.White))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReorderExercisesDialog(
    dialogState: WorkoutLogDialogsState,
    exercises: List<Exercise>,
    onDismiss: () -> Unit,
    onConfirm: (exercises: List<Exercise>) -> Unit,
) {
    if (dialogState is WorkoutLogDialogsState.ReorderExerciseDialogState && dialogState.show) {
        var exerciseList by remember {
            mutableStateOf(exercises)
        }
        val onMove: (ItemPosition, ItemPosition) -> Unit = { from, to ->
            exerciseList = exerciseList.toMutableList().apply {
                add(to.index, removeAt(from.index))
            }
        }
        Dialog(onDismissRequest = onDismiss) {

            Card(
                elevation = 8.dp,
                shape = RoundedCornerShape(12.dp)
            ) {

                Column(modifier = Modifier.background(Grey500)) {

                    Text(
                        text = "Reorder Exercises",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(top = 16.dp, start = 16.dp, bottom = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    ReorderableExerciseList(exercises = exerciseList, onMove)

                    // Buttons
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        TextButton(onClick = onDismiss) {
                            Text(text = "CANCEL", style = LocalTextStyle.current.copy(color = Color.White))
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        TextButton(onClick = {
                            WorkoutAppLogger.d("new exercise list: $exerciseList")
                            onConfirm(exerciseList)
                        }) {
                            Text(text = "OK", style = LocalTextStyle.current.copy(color = Color.White))
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun EditExerciseDialog(
    dialogState: WorkoutLogDialogsState,
    onDismiss: () -> Unit,
    onConfirm: (exerciseSets: List<ExerciseSet>) -> Unit,
    onDelete: (exerciseAndExerciseSets: ExerciseAndExerciseSets, exerciseSets: List<ExerciseSet>) -> Unit
) {
    if (dialogState is WorkoutLogDialogsState.EditExerciseDialogState && dialogState.show) {
        val editExerciseDialogState = rememberEditExerciseDialogState(sets = dialogState.exerciseAndExerciseSets.sets)

        Dialog(onDismissRequest = onDismiss) {
            Card(elevation = 8.dp, shape = RoundedCornerShape(12.dp)) {
                Column(modifier = Modifier.background(Grey500)) {
                    Text(
                        text = "Edit ${dialogState.exerciseAndExerciseSets.exercise.exerciseName}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(top = 16.dp, start = 16.dp, bottom = 8.dp, end = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 86.dp), horizontalArrangement = Arrangement.spacedBy(32.dp)
                    ) {
                        Text("Set")
                        Text("lbs")
                        Text("Reps")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    ReorderableSetList(
                        sets = editExerciseDialogState.reorderableSetList,
                        onChecked = editExerciseDialogState::onSetChecked,
                        onMove = editExerciseDialogState::onSetsMoved
                    )

                    // Buttons
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        if (editExerciseDialogState.showDeleteButton) {
                            IconButton(onClick = {
                                onDelete(dialogState.exerciseAndExerciseSets, editExerciseDialogState.checkedSets)
                            }) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Sets")
                            }
                            Text(
                                text = "(${editExerciseDialogState.checkedSets.size})",
                                style = LocalTextStyle.current.copy(color = Color.White)
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        TextButton(onClick = onDismiss) {
                            Text(text = "CANCEL", style = LocalTextStyle.current.copy(color = Color.White))
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        TextButton(onClick = {
                            onConfirm(editExerciseDialogState.reorderableSetList)
                        }) {
                            Text(text = "OK", style = LocalTextStyle.current.copy(color = Color.White))
                        }
                    }
                }
            }
        }
    }
}

