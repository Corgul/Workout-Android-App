package com.example.workout_log.presentation.calendar

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.workout_log.R
import com.example.workout_log.domain.model.ExerciseAndExerciseSets
import com.example.workout_log.domain.model.Workout
import com.example.workout_log.presentation.calendar.components.CalendarBottomSheet
import com.example.workout_log.presentation.calendar.components.DayContent
import com.example.workout_log.presentation.calendar.state.rememberCalendarScreenState
import com.example.workout_log.ui.theme.Shapes
import io.github.boguszpawlowski.composecalendar.CalendarState
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState
import java.time.LocalDate

@ExperimentalMaterialApi
@Composable
fun CalendarScreen(
    navController: NavController,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val workoutDays by viewModel.workoutDaysFlow.collectAsState(initial = emptyList())
    val workoutWithExercisesAndSets by viewModel.workoutDetailFlow.collectAsState(initial = null)
    val workout = workoutWithExercisesAndSets?.workout
    val exercisesAndSets = workoutWithExercisesAndSets?.getExercisesAndSets()

    CalendarScreenContent(
        navController = navController,
        workoutDays = workoutDays,
        workout = workout,
        exercisesAndSets = exercisesAndSets,
        onDateSelectionChange = viewModel::onDateSelected
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CalendarScreenContent(
    navController: NavController,
    workoutDays: List<LocalDate>,
    workout: Workout?,
    exercisesAndSets: List<ExerciseAndExerciseSets>?,
    onDateSelectionChange: (List<LocalDate>) -> Unit
) {
    val calendarScreenState = rememberCalendarScreenState(
        calendarState = rememberSelectableCalendarState(onSelectionChanged = onDateSelectionChange),
        bottomSheetScaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)),
        navController = navController,
        coroutineScope = rememberCoroutineScope()
    )
    calendarScreenState.changeBottomSheetVisibility(workout != null)
    val isBottomSheetVisible = calendarScreenState.isBottomSheetVisible

    CalendarBottomSheet(
        scaffoldState = calendarScreenState.bottomSheetScaffoldState,
        isBottomSheetVisible = isBottomSheetVisible,
        workout = workout,
        exercisesAndSets = exercisesAndSets,
        onGoToWorkoutClicked = calendarScreenState::navigateToWorkoutScreen
    ) {
        CalendarContent(
            calendarState = calendarScreenState.calendarState,
            workoutDays = workoutDays,
            shouldShowAddNewWorkoutButton = !isBottomSheetVisible,
            onAddNewWorkoutClicked = calendarScreenState::addNewWorkoutButtonClicked
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CalendarContent(
    calendarState: CalendarState<DynamicSelectionState>,
    workoutDays: List<LocalDate>,
    shouldShowAddNewWorkoutButton: Boolean,
    onAddNewWorkoutClicked: (Context) -> Unit
) {
    Column() {
        SelectableCalendar(calendarState = calendarState, dayContent = { DayContent(state = it, workoutDays = workoutDays) })
        if (shouldShowAddNewWorkoutButton) {
            AddNewWorkoutButton(onAddNewWorkoutClicked = onAddNewWorkoutClicked)
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun AddNewWorkoutButton(onAddNewWorkoutClicked: (Context) -> Unit) {
    val context = LocalContext.current
    Button(
        onClick = { onAddNewWorkoutClicked(context) },
        shape = Shapes.small,
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, top = 32.dp)
    ) {
        Text(text = stringResource(id = R.string.add_workout_for_date_button))
    }
}