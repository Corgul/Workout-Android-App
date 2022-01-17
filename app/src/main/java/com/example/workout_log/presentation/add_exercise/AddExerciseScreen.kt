package com.example.workout_log.presentation.add_exercise

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.workout_log.domain.model.ExerciseName
import com.example.workout_log.domain.model.ExerciseType
import kotlinx.coroutines.ExperimentalCoroutinesApi

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
                .clickable { onExerciseNameClicked(exerciseName) }) {
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