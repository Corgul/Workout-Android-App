package com.example.workout_log.presentation.add_exercise

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun AddExerciseScreen(
    navController: NavController,
    viewModel: AddExerciseViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    ExerciseTypes(state = state)
}

@Composable
fun ExerciseTypes(state: AddExerciseState) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(state.exerciseTypes) { exerciseType ->
            Text(
                text = exerciseType.exerciseType,
                style = MaterialTheme.typography.h5
            )
            Spacer(modifier = Modifier.height(8.dp))
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .width(1.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}