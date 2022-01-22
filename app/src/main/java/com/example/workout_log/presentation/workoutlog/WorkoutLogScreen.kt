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
        WorkoutLog(state.exercisesAndSets) { exercise ->
            viewModel.onAddSetButtonClicked(exercise)
        }
    }
}

@Composable
fun WorkoutLog(exercisesAndSets: List<ExerciseAndExerciseSets>, onAddSetButtonClicked: (exercise: Exercise) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        exercisesAndSets.forEach { exerciseAndSets ->
            item {
                ExerciseNameRow(exerciseAndSets)
            }
            items(exerciseAndSets.sets) { set ->
                ExerciseSetRow(set)
            }
            item {
                AddSetButton(onAddSetButtonClicked)
            }
        }
    }
}

@Composable
fun ExerciseNameRow(exerciseAndSets: ExerciseAndExerciseSets) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = exerciseAndSets.exercise.exerciseName,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(all = 8.dp)
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .width(1.dp)
        )
    }
}

@Composable
fun ExerciseSetRow(exerciseSet: ExerciseSet) {
    Row() {
        Text(
            text = exerciseSet.setNumber.toString(),
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(all = 8.dp)
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .width(1.dp)
        )
    }
}

@Composable
fun AddSetButton(onAddSetButtonClicked: (exercise: Exercise) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Button(
            onClick = { /*TODO*/ },
            shape = Shapes.small,
            modifier = Modifier.fillMaxWidth().padding(start = 32.dp, end = 32.dp)
        ) {
            Text("ADD SET")
        }
    }
}