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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.workout_log.R
import com.example.workout_log.domain.model.ExerciseName
import com.example.workout_log.domain.model.ExerciseType
import com.example.workout_log.presentation.add_exercise.state.AddExerciseViewState
import com.example.workout_log.presentation.add_exercise.state.rememberAddExerciseViewState
import com.example.workout_log.ui.theme.Grey200
import com.example.workout_log.ui.theme.White
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Composable
fun AddExerciseScreen(
    navController: NavController,
    viewModel: AddExerciseViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val viewState = rememberAddExerciseViewState(navController = navController)
    BackHandler(enabled = viewState.exerciseNamesVisibility) {
        viewState.onBackPressed()
    }
    AddExerciseScreenContent(
        state = state,
        viewState = viewState,
        onExerciseTypeClicked = { exerciseType: ExerciseType ->
            viewModel.onEvent(AddExerciseEvent.ExerciseTypeClicked(exerciseType))
            viewState.onExerciseTypeClicked()
        },
        onSaveClicked = {
            viewModel.onEvent(AddExerciseEvent.OnSaveClicked(viewState.getSelectedExercisesList()) { workoutDateLong ->
                viewState.navigateToWorkoutLog(workoutDateLong)
            })
        }
    )
}

@Composable
fun AddExerciseScreenContent(
    state: AddExerciseState,
    viewState: AddExerciseViewState,
    onExerciseTypeClicked: (ExerciseType) -> Unit,
    onSaveClicked: () -> Unit
) {
    AddExerciseScaffold(
        selectedExercisesCount = viewState.selectedExercises.size,
        navigationBackButtonClicked = viewState::navigationBackButtonClicked,
        onSaveClicked = onSaveClicked
    ) {
        if (viewState.exerciseNamesVisibility) {
            ExerciseNames(
                exerciseNames = state.exerciseNames,
                selectedExercises = viewState.selectedExercises,
                onExerciseNameClicked = viewState::onExerciseNameClicked
            )
        } else {
            ExerciseTypes(
                exerciseTypes = state.exerciseTypes, onExerciseTypeClicked = onExerciseTypeClicked
            )
        }
    }
}

@Composable
fun AddExerciseScaffold(
    selectedExercisesCount: Int,
    navigationBackButtonClicked: () -> Unit,
    onSaveClicked: () -> Unit,
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            AddExerciseTopBar(selectedExercisesCount, navigationBackButtonClicked, onSaveClicked)
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            content()
        }
    }
}

@Composable
fun AddExerciseTopBar(
    selectedExercisesCount: Int,
    navigationBackButtonClicked: () -> Unit,
    onSaveClicked: () -> Unit
) {
    TopAppBar(
        title = { Text(stringResource(id = R.string.add_exercise)) },
        navigationIcon = {
            IconButton(
                onClick = navigationBackButtonClicked
            ) {
                Icon(Icons.Filled.ArrowBack, stringResource(id = R.string.back_content_description))
            }
        },
        actions = {
            if (selectedExercisesCount != 0) {
                Box(
                    modifier = Modifier
                        .clickable { onSaveClicked() }
                        .fillMaxHeight()
                ) {
                    Text(
                        stringResource(id = R.string.add_exercise_top_bar_button, selectedExercisesCount),
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

@Composable
fun ExerciseNames(
    exerciseNames: List<ExerciseName>,
    selectedExercises: List<ExerciseName>,
    onExerciseNameClicked: (exerciseName: ExerciseName) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(exerciseNames) { exerciseName ->
            AddExerciseRow(
                modifier = Modifier.background(exerciseNameBackgroundColor(selectedExercises, exerciseName)),
                onClick = { onExerciseNameClicked(exerciseName) }
            ) {
                AddExerciseRowContent(label = exerciseName.exerciseName)
            }
        }
    }
}

@Composable
fun AddExerciseRow(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .clickable { onClick() }
        .then(modifier)) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            content()
        }
    }
}

@Composable
fun AddExerciseRowContent(
    label: String
) {
    Text(
        text = label,
        style = MaterialTheme.typography.body1,
        modifier = Modifier.padding(all = 8.dp)
    )
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .width(1.dp)
    )
}

@Composable
fun ExerciseTypes(
    exerciseTypes: List<ExerciseType>,
    onExerciseTypeClicked: (ExerciseType) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(exerciseTypes) { exerciseType ->
            AddExerciseRow(
                onClick = { onExerciseTypeClicked(exerciseType) }
            ) {
                AddExerciseRowContent(label = exerciseType.exerciseType)
            }
        }
    }
}

@Composable
private fun exerciseNameBackgroundColor(
    selectedExercises: List<ExerciseName>,
    exerciseName: ExerciseName
): Color {
    return if (selectedExercises.contains(exerciseName)) {
        Grey200
    } else {
        MaterialTheme.colors.background
    }
}