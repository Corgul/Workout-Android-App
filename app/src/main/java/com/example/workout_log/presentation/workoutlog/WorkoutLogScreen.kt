package com.example.workout_log.presentation.workoutlog

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.workout_log.R
import com.example.workout_log.domain.model.ExerciseAndExerciseSets
import com.example.workout_log.domain.model.ExerciseSet
import com.example.workout_log.domain.model.Workout
import com.example.workout_log.presentation.util.Screen
import com.example.workout_log.presentation.util.WorkoutNameHelper
import com.example.workout_log.presentation.util.extensions.onFocusSelectAll
import com.example.workout_log.presentation.workoutlog.components.*
import com.example.workout_log.presentation.workoutlog.state.WorkoutLogDialog
import com.example.workout_log.presentation.workoutlog.state.WorkoutLogDialogState
import com.example.workout_log.presentation.workoutlog.state.rememberWorkoutLogDialogState
import com.example.workout_log.ui.theme.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
@Composable
fun WorkoutLogScreen(
    navController: NavController,
    workoutDate: Long,
    viewModel: WorkoutLogViewModel = hiltViewModel()
) {
    ModalBottomSheet(navController, workoutDate, viewModel)
}

@OptIn(ExperimentalCoroutinesApi::class)
@ExperimentalMaterialApi
@Composable
fun ModalBottomSheet(navController: NavController, workoutDate: Long, viewModel: WorkoutLogViewModel) {
    val modalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val workoutLogDialogState = rememberWorkoutLogDialogState()
    var currentBottomSheet by remember { mutableStateOf<WorkoutLogBottomSheet>(WorkoutLogBottomSheet.WorkoutBottomSheet) }
    val workout = viewModel.state.value.workout
    val exercisesAndSets = viewModel.state.value.exercisesAndSets

    val openBottomSheet: (bottomSheet: WorkoutLogBottomSheet) -> Unit = {
        currentBottomSheet = it
        coroutineScope.launch { modalBottomSheetState.show() }
    }

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetContent = {
            if (currentBottomSheet is WorkoutLogBottomSheet.ExerciseBottomSheet) {
                ExerciseBottomSheet(
                    viewModel = viewModel,
                    exerciseAndExerciseSets = (currentBottomSheet as WorkoutLogBottomSheet.ExerciseBottomSheet).exerciseAndExerciseSets,
                    coroutineScope = coroutineScope,
                    bottomSheetState = modalBottomSheetState,
                    scaffoldState = scaffoldState,
                    workoutLogDialogState = workoutLogDialogState
                )
            } else {
                WorkoutBottomSheet(
                    viewModel = viewModel,
                    workout = workout,
                    exercisesAndSets.map { it.exercise },
                    coroutineScope = coroutineScope,
                    bottomSheetState = modalBottomSheetState,
                    scaffoldState = scaffoldState,
                    workoutLogDialogState = workoutLogDialogState
                )
            }
        }
    ) {
        WorkoutLogScaffold(
            navController = navController,
            viewModel = viewModel,
            workoutDate = workoutDate,
            openBottomSheet = openBottomSheet,
            scaffoldState = scaffoldState,
            workoutLogDialogsState = workoutLogDialogState,
            workout = workout,
            exercisesAndSets = exercisesAndSets
        )
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalMaterialApi
@Composable
fun WorkoutLogScaffold(
    navController: NavController,
    viewModel: WorkoutLogViewModel,
    workoutDate: Long,
    openBottomSheet: (bottomSheetType: WorkoutLogBottomSheet) -> Unit,
    scaffoldState: ScaffoldState,
    workoutLogDialogsState: WorkoutLogDialogState,
    workout: Workout?,
    exercisesAndSets: List<ExerciseAndExerciseSets>
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            WorkoutLogTopBar(workout, openBottomSheet, workoutDate)
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
                onClick = {
                    navController.navigate(Screen.AddExerciseScreen.route + "/${workoutDate}")
                },
                modifier = Modifier.padding(end = 16.dp, bottom = 16.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(id = R.string.workout_log_fab_content_description))
            }
        }
    ) {
        when (workoutLogDialogsState.currentDialog.value) {
            is WorkoutLogDialog.ReorderExerciseDialog -> {
                ReorderExercisesDialog(
                    exercisesAndSets.map { it.exercise },
                    onDismiss = workoutLogDialogsState::dismissDialog,
                    onConfirm = viewModel::onReorderExerciseDialogConfirmed
                )
            }
            is WorkoutLogDialog.EditWorkoutNameDialog -> {
                EditWorkoutNameDialog(
                    workout = workout,
                    onDismiss = workoutLogDialogsState::dismissDialog,
                    onConfirm = viewModel::onEditWorkoutNameDialogConfirmed
                )
            }
            is WorkoutLogDialog.EditExerciseDialog -> {
                EditExerciseDialog(
                    exerciseAndSets = (workoutLogDialogsState.currentDialog.value as WorkoutLogDialog.EditExerciseDialog).exerciseAndExerciseSets,
                    onDismiss = workoutLogDialogsState::dismissDialog,
                    onConfirm = viewModel::onEditExerciseDialogConfirmed,
                    onDelete = viewModel::onEditExerciseDialogDelete
                )
            }
        }

        WorkoutLog(
            exercisesAndSets,
            viewModel::onAddSetButtonClicked,
            viewModel::onWeightChanged,
            viewModel::onRepsChanged,
            openBottomSheet
        )
    }
}

@Composable
fun WorkoutLogTopBar(workout: Workout?, openBottomSheet: (bottomSheetType: WorkoutLogBottomSheet) -> Unit, workoutDate: Long) {
    TopAppBar(
        title = { Text(text = WorkoutNameHelper.getWorkoutName(workout?.workoutName, workoutDate)) },
        actions = {
            if (workout != null) {
                IconButton(onClick = { openBottomSheet(WorkoutLogBottomSheet.WorkoutBottomSheet) }) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = stringResource(id = R.string.workout_bottom_sheet_menu_content_description))
                }
            }
        }
    )
}

@ExperimentalMaterialApi
@Composable
fun WorkoutLog(
    exercisesAndSets: List<ExerciseAndExerciseSets>,
    onAddSetButtonClicked: (exerciseAndSets: ExerciseAndExerciseSets) -> Unit,
    onWeightChanged: (exerciseSet: ExerciseSet, newWeight: Int?) -> Unit,
    onRepsChanged: (exerciseSet: ExerciseSet, newReps: Int?) -> Unit,
    openBottomSheet: (bottomSheetType: WorkoutLogBottomSheet) -> Unit
) {
    WorkoutLogCards(exercisesAndSets, onAddSetButtonClicked, onWeightChanged, onRepsChanged, openBottomSheet)
}

@ExperimentalMaterialApi
@Composable
fun WorkoutLogCards(
    exercisesAndSets: List<ExerciseAndExerciseSets>,
    onAddSetButtonClicked: (exerciseAndSets: ExerciseAndExerciseSets) -> Unit,
    onWeightChanged: (exerciseSet: ExerciseSet, newWeight: Int?) -> Unit,
    onRepsChanged: (exerciseSet: ExerciseSet, newReps: Int?) -> Unit,
    openBottomSheet: (bottomSheetType: WorkoutLogBottomSheet) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(exercisesAndSets) { exerciseAndSets ->
            var expanded by remember { mutableStateOf(true) }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp), shape = RoundedCornerShape(12.dp),
                elevation = 6.dp
            ) {
                Column(Modifier.clickable { expanded = !expanded }) {
                    ExerciseNameRow(exerciseAndSets, openBottomSheet)
                    AnimatedVisibility(visible = expanded) {
                        Column {
                            ExerciseHeaderRow()

                            exerciseAndSets.sets.forEach {
                                ExerciseSetRow(it, onWeightChanged, onRepsChanged)
                            }

                            AddSetButton(exerciseAndSets, onAddSetButtonClicked)
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun ExerciseNameRow(
    exerciseAndSets: ExerciseAndExerciseSets,
    openBottomSheet: (bottomSheetType: WorkoutLogBottomSheet) -> Unit
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
            openBottomSheet(WorkoutLogBottomSheet.ExerciseBottomSheet(exerciseAndSets))
        }) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = stringResource(id = R.string.exercise_bottom_sheet_menu_content_description))
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
fun WorkoutLogTextField(
    textFieldValue: MutableState<TextFieldValue>,
    modifier: Modifier,
    isNumberTextField: Boolean,
    colors: TextFieldColors = workoutLogTextFieldColors(),
    textStyle: TextStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
    onValueChanged: (TextFieldValue) -> Unit
) {
    val customTextSelectionColors = TextSelectionColors(
        handleColor = Indigo400,
        backgroundColor = Indigo200.copy(alpha = 0.4f)
    )
    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
        val keyboardOptions =
            if (isNumberTextField) KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number) else KeyboardOptions.Default
        OutlinedTextField(
            value = textFieldValue.value,
            modifier = Modifier
                .then(modifier)
                .onFocusSelectAll(textFieldValue),
            maxLines = 1,
            keyboardOptions = keyboardOptions,
            shape = RoundedCornerShape(8.dp),
            colors = colors,
            textStyle = textStyle,
            onValueChange = onValueChanged
        )
    }
}

@Composable
fun workoutLogTextFieldColors(
    backgroundColor: Color = Grey200,
    focusedIndicatorColor: Color = Indigo200,
    unfocusedIndicatorColor: Color = Color.Transparent,
    cursorColor: Color = Color.White
): TextFieldColors =
    TextFieldDefaults.textFieldColors(
        backgroundColor = backgroundColor,
        focusedIndicatorColor = focusedIndicatorColor,
        unfocusedIndicatorColor = unfocusedIndicatorColor,
        cursorColor = cursorColor
    )


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