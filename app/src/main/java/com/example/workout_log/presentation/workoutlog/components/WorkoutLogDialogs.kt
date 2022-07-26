package com.example.workout_log.presentation.workoutlog.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.workout_log.domain.model.Exercise
import com.example.workout_log.domain.util.WorkoutAppLogger
import com.example.workout_log.ui.theme.Grey500
import org.burnoutcrew.reorderable.ItemPosition

@Composable
fun ReorderExercisesDialog(
    show: Boolean,
    exercises: List<Exercise>,
    onDismiss: () -> Unit,
    onConfirm: (exercises: List<Exercise>) -> Unit,
) {
    if (show) {
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