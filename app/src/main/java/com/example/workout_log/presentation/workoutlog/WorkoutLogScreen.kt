package com.example.workout_log.presentation.workoutlog

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.workout_log.R
import com.example.workout_log.domain.model.Exercise
import com.example.workout_log.domain.model.ExerciseAndExerciseSets
import com.example.workout_log.domain.model.ExerciseSet
import com.example.workout_log.domain.model.Workout
import com.example.workout_log.presentation.util.WorkoutNameHelper
import com.example.workout_log.presentation.workoutlog.components.EditExerciseDialog
import com.example.workout_log.presentation.workoutlog.components.EditWorkoutNameDialog
import com.example.workout_log.presentation.workoutlog.components.ExerciseBottomSheet
import com.example.workout_log.presentation.workoutlog.components.ReorderExercisesDialog
import com.example.workout_log.presentation.workoutlog.components.WorkoutBottomSheet
import com.example.workout_log.presentation.workoutlog.components.WorkoutLogTextField
import com.example.workout_log.presentation.workoutlog.state.WorkoutDialogVisibilityModifier
import com.example.workout_log.presentation.workoutlog.state.WorkoutLogDialog
import com.example.workout_log.presentation.workoutlog.state.WorkoutLogDialogState
import com.example.workout_log.presentation.workoutlog.state.WorkoutLogViewState
import com.example.workout_log.presentation.workoutlog.state.rememberWorkoutLogDialogState
import com.example.workout_log.presentation.workoutlog.state.rememberWorkoutLogViewState
import com.example.workout_log.ui.theme.Green200
import com.example.workout_log.ui.theme.Grey800
import com.example.workout_log.ui.theme.Indigo700
import com.example.workout_log.ui.theme.Shapes
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
@Composable
fun WorkoutLogScreen(
    navController: NavController,
    workoutDate: Long,
    viewModel: WorkoutLogViewModel = hiltViewModel()
) {
    val workoutLogState = viewModel.state.value
    val workoutLogViewState = rememberWorkoutLogViewState(
        navController = navController,
        context = LocalContext.current,
        workoutDateLong = workoutDate,
        snackbarListener = viewModel,
        cardListener = viewModel,
        key = workoutDate
    )
    val workoutLogDialogState = rememberWorkoutLogDialogState(viewModel)
    
    DisposableEffect(viewModel) {
        onDispose { viewModel.onStop() }
    }
    
    WorkoutLogScreenContent(
        workoutLogState = workoutLogState,
        workoutLogViewState = workoutLogViewState,
        workoutLogDialogState = workoutLogDialogState,
        deleteExerciseClicked = viewModel::deleteExerciseClicked,
        deleteWorkoutClicked = viewModel::deleteWorkoutClicked
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WorkoutLogScreenContent(
    workoutLogState: WorkoutLogState,
    workoutLogViewState: WorkoutLogViewState,
    workoutLogDialogState: WorkoutLogDialogState,
    deleteExerciseClicked: (Exercise) -> Unit,
    deleteWorkoutClicked: (Workout?) -> Unit
) {
    WorkoutLogModalBottomSheets(
        workout = workoutLogState.workout,
        exercisesAndSets = workoutLogState.exercisesAndSets,
        workoutLogViewState = workoutLogViewState,
        workoutDialogVisibilityModifier = workoutLogDialogState,
        deleteExerciseClicked = { exercise: Exercise ->
            workoutLogViewState.showDeleteExerciseSnackbar(exercise)
            deleteExerciseClicked(exercise)
        },
        deleteWorkoutClicked = { workout: Workout? ->
            workoutLogViewState.showDeleteWorkoutSnackbar()
            deleteWorkoutClicked(workout)
        }
    ) {
        WorkoutLogScaffold(
            workoutLogViewState = workoutLogViewState,
            title = WorkoutNameHelper.getWorkoutName(workoutLogState.workout?.workoutName, workoutLogViewState.workoutDateLong),
            showMenuButton = workoutLogState.workout != null
        ) {
            WorkoutLogDialogs(
                workoutLogDialogState = workoutLogDialogState,
                workout = workoutLogState.workout,
                exercisesAndSets = workoutLogState.exercisesAndSets
            )
            WorkoutLog(
                exercisesAndSets = workoutLogState.exercisesAndSets,
                workoutLogViewState = workoutLogViewState
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WorkoutLogModalBottomSheets(
    workout: Workout?,
    exercisesAndSets: List<ExerciseAndExerciseSets>,
    workoutLogViewState: WorkoutLogViewState,
    workoutDialogVisibilityModifier: WorkoutDialogVisibilityModifier,
    deleteExerciseClicked: (Exercise) -> Unit,
    deleteWorkoutClicked: (Workout?) -> Unit,
    content: @Composable () -> Unit
) {
    ModalBottomSheetLayout(
        sheetState = workoutLogViewState.modalBottomSheetState,
        sheetContent = {
            if (workoutLogViewState.currentBottomSheet is WorkoutLogBottomSheet.ExerciseBottomSheet) {
                ExerciseBottomSheet(
                    exerciseAndExerciseSets = (workoutLogViewState.currentBottomSheet as WorkoutLogBottomSheet.ExerciseBottomSheet).exerciseAndExerciseSets,
                    hideBottomSheet = workoutLogViewState::hideBottomSheet,
                    showEditExerciseDialog = workoutDialogVisibilityModifier::showEditExerciseDialog,
                    deleteExerciseClicked = deleteExerciseClicked
                )
            } else if (workoutLogViewState.currentBottomSheet is WorkoutLogBottomSheet.WorkoutBottomSheet) {
                WorkoutBottomSheet(
                    workout = workout,
                    exercisesAndSets.map { it.exercise },
                    hideBottomSheet = workoutLogViewState::hideBottomSheet,
                    showEditWorkoutNameDialog = workoutDialogVisibilityModifier::showEditWorkoutNameDialog,
                    showReorderExerciseDialog = workoutDialogVisibilityModifier::showReorderExerciseDialog,
                    showReorderExerciseSnackbar = workoutLogViewState::showReorderExerciseSnackbar,
                    deleteWorkoutClicked = deleteWorkoutClicked
                )
            } else {
                Spacer(modifier = Modifier.height(1.dp))
            }
        }
    ) {
        content()
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalMaterialApi
@Composable
fun WorkoutLogScaffold(
    workoutLogViewState: WorkoutLogViewState,
    title: String,
    showMenuButton: Boolean,
    content: @Composable () -> Unit
) {
    Scaffold(
        scaffoldState = workoutLogViewState.scaffoldState,
        topBar = {
            WorkoutLogTopBar(
                title,
                workoutLogViewState::openWorkoutBottomSheet,
                showMenuButton
            )
        },
        snackbarHost = {
            SnackbarHost(it) { data ->
                Snackbar(
                    actionColor = Indigo700,
                    snackbarData = data,
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = workoutLogViewState::navigateToAddExercisesScreen,
                modifier = Modifier.padding(end = 16.dp, bottom = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.workout_log_fab_content_description)
                )
            }
        }
    ) {
        content()
    }
}

@Composable
fun WorkoutLogTopBar(workoutName: String, openBottomSheet: () -> Unit, showMenuButton: Boolean) {
    TopAppBar(
        title = { Text(text = workoutName) },
        actions = {
            if (showMenuButton) {
                IconButton(onClick = openBottomSheet) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = stringResource(id = R.string.workout_bottom_sheet_menu_content_description)
                    )
                }
            }
        }
    )
}

@Composable
fun WorkoutLogDialogs(
    workoutLogDialogState: WorkoutLogDialogState,
    workout: Workout?,
    exercisesAndSets: List<ExerciseAndExerciseSets>
) {
    when (workoutLogDialogState.currentDialog) {
        is WorkoutLogDialog.ReorderExerciseDialog -> {
            ReorderExercisesDialog(
                exercisesAndSets.map { it.exercise },
                onDismiss = workoutLogDialogState::dismissDialog,
                onConfirm = workoutLogDialogState::onReorderExerciseDialogConfirmed
            )
        }
        is WorkoutLogDialog.EditWorkoutNameDialog -> {
            EditWorkoutNameDialog(
                workout = workout,
                onDismiss = workoutLogDialogState::dismissDialog,
                onConfirm = workoutLogDialogState::onEditWorkoutNameDialogConfirmed
            )
        }
        is WorkoutLogDialog.EditExerciseDialog -> {
            EditExerciseDialog(
                exerciseAndSets = (workoutLogDialogState.currentDialog as WorkoutLogDialog.EditExerciseDialog).exerciseAndExerciseSets,
                onDismiss = workoutLogDialogState::dismissDialog,
                onConfirm = workoutLogDialogState::onEditExerciseDialogConfirmed,
                onDelete = workoutLogDialogState::onEditExerciseDialogDelete
            )
        }
        else -> Unit
    }
}

@Composable
fun WorkoutLog(
    exercisesAndSets: List<ExerciseAndExerciseSets>,
    workoutLogViewState: WorkoutLogViewState
) {
    WorkoutLogCards(exercisesAndSets, workoutLogViewState)
}

@Composable
fun WorkoutLogCards(
    exercisesAndSets: List<ExerciseAndExerciseSets>,
    workoutLogViewState: WorkoutLogViewState
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(exercisesAndSets) { exerciseAndSets ->
            WorkoutLogCard(exerciseAndSets = exerciseAndSets, workoutLogViewState = workoutLogViewState)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WorkoutLogCard(
    exerciseAndSets: ExerciseAndExerciseSets,
    workoutLogViewState: WorkoutLogViewState
) {
    var expanded by remember { mutableStateOf(true) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp), shape = RoundedCornerShape(12.dp),
        elevation = 6.dp
    ) {
        Column(Modifier.clickable { expanded = !expanded }) {
            ExerciseNameRow(exerciseAndSets, workoutLogViewState::openExerciseBottomSheet)
            AnimatedVisibility(visible = expanded) {
                Column {
                    ExerciseHeaderRow()

                    exerciseAndSets.sets.forEach {
                        ExerciseSetRow(it, workoutLogViewState::onWeightChanged, workoutLogViewState::onRepsChanged)
                    }

                    AddSetButton(exerciseAndSets, workoutLogViewState::onAddSetButtonClicked)
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun ExerciseNameRow(
    exerciseAndSets: ExerciseAndExerciseSets,
    openBottomSheet: (ExerciseAndExerciseSets) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(rowPadding()),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = exerciseAndSets.exercise.exerciseName,
            style = MaterialTheme.typography.h6,
        )
        IconButton(onClick = {
            openBottomSheet(exerciseAndSets)
        }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = stringResource(id = R.string.exercise_bottom_sheet_menu_content_description)
            )
        }
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
            text = stringResource(id = R.string.sets),
            style = MaterialTheme.typography.subtitle1,
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = stringResource(id = R.string.lbs),
            modifier = Modifier.padding(end = 116.dp)
        )

        Text(
            text = stringResource(id = R.string.reps),
            modifier = Modifier.padding(end = 24.dp)
        )
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
        Text(text = exerciseSet.setNumber.toString())

        Spacer(modifier = Modifier.weight(1f))

        val setWeightTextValue = remember(exerciseSet.setId) { mutableStateOf(TextFieldValue(exerciseSet.weight.toString())) }
        val setRepsTextValue = remember(exerciseSet.setId) { mutableStateOf(TextFieldValue(exerciseSet.reps.toString())) }
        val maxWeightCharacters = 3
        val maxRepCharacters = 2

        WorkoutLogTextField(
            textFieldValue = setWeightTextValue,
            Modifier
                .padding(end = 56.dp)
                .width(80.dp),
            true
        ) {
            if (it.text.length > maxWeightCharacters) {
                return@WorkoutLogTextField
            }
            setWeightTextValue.value = it
            onWeightChanged(exerciseSet, it.text.toIntOrNull())
        }

        WorkoutLogTextField(textFieldValue = setRepsTextValue, Modifier.width(64.dp), true) {
            if (it.text.length > maxRepCharacters) {
                return@WorkoutLogTextField
            }
            setRepsTextValue.value = it
            onRepsChanged(exerciseSet, it.text.toInt())
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
            colors = ButtonDefaults.buttonColors(backgroundColor = Green200),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp)
        ) {
            Text(text = stringResource(id = R.string.add_set_button), color = Grey800)
        }
    }
}

private fun rowPadding() = PaddingValues(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 16.dp)

@Preview
@Composable
fun prev() {

}