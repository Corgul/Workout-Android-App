package com.example.workout_log.presentation.workoutlog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.workout_log.domain.model.Exercise
import com.example.workout_log.domain.model.ExerciseAndExerciseSets
import com.example.workout_log.domain.model.ExerciseSet
import com.example.workout_log.presentation.util.Screen
import com.example.workout_log.ui.theme.Shapes
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@Composable
fun WorkoutLogScreen(
    navController: NavController,
    viewModel: WorkoutLogViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val state = viewModel.state.value
    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddExerciseScreen.route + "?workoutId=${state.workoutId}")
                },
                modifier = Modifier.padding(end = 16.dp, bottom = 16.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Exercises")
            }
        }
    ) {
        WorkoutLog(
            state.exercisesAndSets,
            viewModel::onAddSetButtonClicked,
            viewModel::onWeightChanged,
            viewModel::onRepsChanged
        )
    }
}

@Composable
fun WorkoutLog(
    exercisesAndSets: List<ExerciseAndExerciseSets>,
    onAddSetButtonClicked: (exercise: Exercise) -> Unit,
    onWeightChanged: (exerciseSet: ExerciseSet, newWeight: Int) -> Unit,
    onRepsChanged: (exerciseSet: ExerciseSet, newReps: Int) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        exercisesAndSets.forEach { exerciseAndSets ->
            item {
                ExerciseNameRow(exerciseAndSets.exercise)
                ExerciseHeaderRow()
            }
            items(exerciseAndSets.sets) { set ->
                ExerciseSetRow(set, onWeightChanged, onRepsChanged)
            }
            item {
                AddSetButton(exerciseAndSets.exercise, onAddSetButtonClicked)
            }
        }
    }
}

@Composable
fun ExerciseNameRow(exercise: Exercise) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(rowPadding())
    ) {
        Text(
            text = exercise.exerciseName,
            style = MaterialTheme.typography.h6,
        )
    }
}

@Composable
fun ExerciseHeaderRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(rowPadding())
    ) {
        Text(
            text = "Set",
            style = MaterialTheme.typography.h6,
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Text(
                text = "lbs",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(end = 64.dp)
            )

            Text(
                text = "Reps",
                style = MaterialTheme.typography.h6,
            )
        }
    }
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .width(1.dp)
    )
}

@Composable
fun ExerciseSetRow(
    exerciseSet: ExerciseSet,
    onWeightChanged: (exerciseSet: ExerciseSet, newWeight: Int) -> Unit,
    onRepsChanged: (exerciseSet: ExerciseSet, newReps: Int) -> Unit
) {
    Row(modifier = Modifier.padding(rowPadding())) {
        Text(
            text = exerciseSet.setNumber.toString(),
            style = MaterialTheme.typography.h6,
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
//            TextField(
//                value = exerciseSet.weight.toString(),
//                modifier = Modifier.width(64.dp).padding(end = 64.dp),
//                onValueChange = {
//                    if (it.isNotEmpty()) {
//                        onWeightChanged(exerciseSet, it.toInt())
//                    }
//                })

            TextField(
                value = exerciseSet.reps.toString(),
                modifier = Modifier.width(64.dp).padding(end = 16.dp),
                onValueChange = {
                    if (it.isNotEmpty()) {
                        onRepsChanged(exerciseSet, it.toInt())
                    }
                })
        }
    }
}

@Composable
fun AddSetButton(exercise: Exercise, onAddSetButtonClicked: (exercise: Exercise) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(rowPadding()), horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { onAddSetButtonClicked(exercise) },
            shape = Shapes.small,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp)
        ) {
            Text("ADD SET")
        }
    }
}

private fun rowPadding() = PaddingValues(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 16.dp)