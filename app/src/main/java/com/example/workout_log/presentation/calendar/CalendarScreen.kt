package com.example.workout_log.presentation.calendar

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
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
                scope.launch { scaffoldState.snackbarHostState.showSnackbar(message = "Please select a date") }
            } else {
                Toast.makeText(context, "Add Exercises for ${selectedDate.formatDate()} workout", Toast.LENGTH_LONG).show()
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
        Text("Add Exercise for Date")
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