package com.example.workout_log.presentation.workoutlog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
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
    onAddSetButtonClicked: (exerciseAndSets: ExerciseAndExerciseSets) -> Unit,
    onWeightChanged: (exerciseSet: ExerciseSet, newWeight: Int?) -> Unit,
    onRepsChanged: (exerciseSet: ExerciseSet, newReps: Int?) -> Unit
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
                AddSetButton(exerciseAndSets, onAddSetButtonClicked)
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
                modifier = Modifier.padding(end = 88.dp)
            )

            Text(
                text = "Reps",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(end = 16.dp)
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
    onWeightChanged: (exerciseSet: ExerciseSet, newWeight: Int?) -> Unit,
    onRepsChanged: (exerciseSet: ExerciseSet, newReps: Int?) -> Unit
) {
    Row(modifier = Modifier.padding(rowPadding()), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = exerciseSet.setNumber.toString(),
            style = MaterialTheme.typography.h6,
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            var setWeight by rememberSaveable { mutableStateOf(exerciseSet.weight.toString()) }
            var setReps by rememberSaveable { mutableStateOf(exerciseSet.reps.toString()) }
            val maxWeightCharacters = 3
            val maxRepCharacters = 2

            TextField(
                value = setWeight,
                modifier = Modifier
                    .width(116.dp)
                    .padding(end = 48.dp),
                maxLines = 1,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                onValueChange = {
                    if (it.length > maxWeightCharacters) {
                        return@TextField
                    }
                    setWeight = it
                    val setWeightNum = setWeight.toIntOrNull()
                    onWeightChanged(exerciseSet, setWeightNum)
                })

            TextField(
                value = setReps,
                modifier = Modifier.width(64.dp),
                maxLines = 1,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                onValueChange = {
                    if (it.length > maxRepCharacters) {
                        return@TextField
                    }
                    setReps = it
                    val setRepsNum = setReps.toIntOrNull()
                    onRepsChanged(exerciseSet, setRepsNum)
                })
        }
    }
}

@Composable
fun AddSetButton(exerciseAndSets: ExerciseAndExerciseSets, onAddSetButtonClicked: (exerciseAndSets: ExerciseAndExerciseSets) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(rowPadding()), horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { onAddSetButtonClicked(exerciseAndSets) },
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

@Preview
@Composable
fun prev() {

}