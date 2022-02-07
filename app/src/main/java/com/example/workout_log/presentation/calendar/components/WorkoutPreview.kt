package com.example.workout_log.presentation.calendar.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.workout_log.domain.model.ExerciseAndExerciseSets
import com.example.workout_log.domain.model.WorkoutWithExercisesAndSets
import com.example.workout_log.presentation.util.Screen
import io.github.boguszpawlowski.composecalendar.CalendarState
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState

@Composable
fun WorkoutPreview(
    navController: NavController,
    workoutWithExercisesAndSets: WorkoutWithExercisesAndSets
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = workoutWithExercisesAndSets.workout.workoutName, style = MaterialTheme.typography.h6)
            IconButton(onClick = { navController.navigate(Screen.WorkoutLogScreen.route + "?workoutDate=${workoutWithExercisesAndSets.workout.date.toEpochDay()}") }) {
                Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit Workout")
            }
        }

        ExerciseAndSetsPreview(exercisesAndSets = workoutWithExercisesAndSets.exercisesAndSets)
    }
}

@Composable
fun ExerciseAndSetsPreview(exercisesAndSets: List<ExerciseAndExerciseSets>) {
    val listState = rememberLazyListState()
    LazyColumn(modifier = Modifier.simpleVerticalScrollbar(listState), state = listState) {
        exercisesAndSets.forEach { exerciseAndSets ->
            item {
                Text(
                    text = exerciseAndSets.exercise.exerciseName
                )
            }

            items(exerciseAndSets.sets) { exerciseSet ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Text(text = exerciseSet.setNumber.toString())
                    Text(text = "${exerciseSet.weight} lbs")
                    Text(text = "${exerciseSet.reps} reps")
                }
            }

            item {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .width(1.dp)
                )
            }
        }
    }
}