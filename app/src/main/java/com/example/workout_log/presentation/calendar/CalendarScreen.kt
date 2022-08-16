package com.example.workout_log.presentation.calendar

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
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.workout_log.R
import com.example.workout_log.domain.model.Workout
import com.example.workout_log.domain.util.formatDate
import com.example.workout_log.presentation.calendar.components.BottomSheet
import com.example.workout_log.presentation.calendar.components.DayContent
import com.example.workout_log.presentation.util.Screen
import com.example.workout_log.ui.theme.Shapes
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import kotlinx.coroutines.launch
import java.time.LocalDate

@ExperimentalMaterialApi
@Composable
fun CalendarScreen(
    navController: NavController,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val calendarState = rememberSelectableCalendarState(onSelectionChanged = viewModel::onDateSelected)
    val workoutDays by viewModel.workoutDaysFlow.collectAsState(initial = emptyList())
    val workoutWithExercisesAndSets by viewModel.workoutDetailFlow.collectAsState(initial = null)
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    )
    val isBottomSheetVisible = workoutWithExercisesAndSets != null

    BottomSheet(
        workoutWithExercisesAndSets,
        scaffoldState,
        isBottomSheetVisible,
        onGoToWorkoutClicked = { onGoToWorkoutClicked(navController, workoutWithExercisesAndSets?.workout) }) {
        Column() {
            SelectableCalendar(calendarState = calendarState, dayContent = { DayContent(state = it, workoutDays = workoutDays) })
            if (!isBottomSheetVisible) {
                AddNewWorkoutButton(navController = navController, scaffoldState, calendarState.selectionState.selection.firstOrNull())
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun AddNewWorkoutButton(navController: NavController, scaffoldState: BottomSheetScaffoldState, selectedDate: LocalDate?) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Button(
        onClick = {
            if (selectedDate == null) {
                scope.launch { scaffoldState.snackbarHostState.showSnackbar(message = context.resources.getString(R.string.add_new_workout_button_snackbar)) }
            } else {
                scope.launch { scaffoldState.snackbarHostState.showSnackbar(message = context.resources.getString(R.string.add_exercises_for_workout_snackbar, selectedDate.formatDate())) }
                navController.navigate(Screen.WorkoutLogScreen.route + "?workoutDate=${selectedDate.toEpochDay()}") {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                }
            }
        },
        shape = Shapes.small,
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, top = 32.dp)
    ) {
        Text(text = stringResource(id = R.string.add_workout_for_date_button))
    }
}

private fun onGoToWorkoutClicked(navController: NavController, workout: Workout?) {
    workout?.let {
        navController.navigate(Screen.WorkoutLogScreen.route + "?workoutDate=${it.date.toEpochDay()}") {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
        }
    }
}