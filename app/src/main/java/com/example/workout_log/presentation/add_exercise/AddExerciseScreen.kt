package com.example.workout_log.presentation.add_exercise

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.workout_log.domain.model.ExerciseName
import com.example.workout_log.domain.model.ExerciseType
import com.example.workout_log.domain.util.WorkoutAppLogger
import com.example.workout_log.presentation.util.Screen
import com.example.workout_log.ui.theme.Grey200
import com.example.workout_log.ui.theme.White
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@Composable
fun AddExerciseScreen(
    navController: NavController,
    viewModel: AddExerciseViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    BackHandler(enabled = state.exerciseNamesVisibility) {
        viewModel.onEvent(AddExerciseEvent.OnBackPressed)
    }
    Scaffold(
        topBar = {
            AddExerciseTopBar(navController = navController, state = state, viewModel = viewModel)
        }
    ) {
        if (state.exerciseNamesVisibility) {
            ExerciseNames(state = state) { exerciseName ->
                viewModel.onEvent(AddExerciseEvent.ExerciseNameClicked(exerciseName))
            }
        } else {
            ExerciseTypes(state = state) { exerciseType ->
                viewModel.onEvent(AddExerciseEvent.ExerciseTypeClicked(exerciseType))
            }
        }
    }
}

// TODO See what code can be shared between this and the one below
@Composable
fun ExerciseTypes(state: AddExerciseState, onExerciseTypeClicked: (exerciseType: ExerciseType) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(state.exerciseTypes) { exerciseType ->
            Box(modifier = Modifier
                .fillMaxSize()
                .clickable { onExerciseTypeClicked(exerciseType) }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = exerciseType.exerciseType,
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier.padding(all = 8.dp)
                    )
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .width(1.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ExerciseNames(state: AddExerciseState, onExerciseNameClicked: (exerciseName: ExerciseName) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(state.exerciseNames) { exerciseName ->
            Box(modifier = Modifier
                .fillMaxSize()
                .clickable { onExerciseNameClicked(exerciseName) }
                .background(exerciseNameBackgroundColor(state, exerciseName))) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = exerciseName.exerciseName,
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier.padding(all = 8.dp)
                    )
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .width(1.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun exerciseNameBackgroundColor(state: AddExerciseState, exerciseName: ExerciseName): Color {
    return if (exerciseName in state.selectedExercises) {
        Grey200
    } else {
        MaterialTheme.colors.background
    }
}

@ExperimentalCoroutinesApi
@Composable
fun AddExerciseTopBar(navController: NavController, state: AddExerciseState, viewModel: AddExerciseViewModel) {
    val coroutineScope = rememberCoroutineScope()
    TopAppBar(
        title = { Text("Add Exercise") },
        navigationIcon = {
            IconButton(
                onClick = {
                    if (!state.exerciseNamesVisibility) {
                        navController.navigateUp()
                    } else {
                        viewModel.onEvent(AddExerciseEvent.OnBackPressed)
                    }
                }
            ) {
                Icon(Icons.Filled.ArrowBack, "Back")
            }
        },
        actions = {
            if (state.selectedExercises.isNotEmpty()) {
                Box(
                    Modifier
                        .clickable {
                            // Use callbacks because this view needs the workout ID to pass to the home screen
                            viewModel.onEvent(AddExerciseEvent.OnSaveClicked {
                                var workoutId: Long? = it
                                if (workoutId == null) {
                                    workoutId = -1L
                                }
                                navController.navigate(Screen.WorkoutLogScreen.route + "?newlyAddedWorkoutId=$workoutId")
                            })
                        }
                        .fillMaxHeight()) {
                    val numberOfSelectedExercises = state.selectedExercises.size
                    Text(
                        "Add ($numberOfSelectedExercises)",
                        modifier = Modifier
                            .padding(end = 8.dp, start = 8.dp)
                            .align(Alignment.Center),
                        color = White
                    )
                }
            }
        },
    )
}