package com.example.workout_log.presentation.calendar.state

import android.content.Context
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.workout_log.R
import com.example.workout_log.domain.model.Workout
import com.example.workout_log.domain.util.extensions.formatDate
import com.example.workout_log.presentation.util.Screen
import io.github.boguszpawlowski.composecalendar.CalendarState
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterialApi::class)
data class CalendarScreenState(
    val calendarState: CalendarState<DynamicSelectionState>,
    val bottomSheetScaffoldState: BottomSheetScaffoldState,
    val navController: NavController,
    val coroutineScope: CoroutineScope
) {
    var isBottomSheetVisible by mutableStateOf(false)

    fun changeBottomSheetVisibility(showBottomSheet: Boolean) {
        if (showBottomSheet) {
            showBottomSheet()
            return
        }
        hideBottomSheet()
    }

    private fun showBottomSheet() {
        isBottomSheetVisible = true
    }

    private fun hideBottomSheet() {
        isBottomSheetVisible = false
    }

    fun navigateToWorkoutScreen(workout: Workout?) {
        workout?.let {
            navigateToWorkoutScreen(it.date)
        }
    }

    private fun navigateToWorkoutScreen(date: LocalDate) {
        navController.navigate(Screen.WorkoutLogScreen.route + "?workoutDate=${date.toEpochDay()}") {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
        }
    }

    fun addNewWorkoutButtonClicked(context: Context) {
        val selectedDate = calendarState.selectionState.selection.firstOrNull()
        if (selectedDate == null) {
            showSnackbar(context.resources.getString(R.string.add_new_workout_button_snackbar))
        } else {
            showSnackbar(context.resources.getString(R.string.add_exercises_for_workout_snackbar, selectedDate.formatDate()))
            navigateToWorkoutScreen(selectedDate)
        }
    }

    private fun showSnackbar(message: String) {
        coroutineScope.launch {
            bottomSheetScaffoldState.snackbarHostState.showSnackbar(message = message)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun rememberCalendarScreenState(
    calendarState: CalendarState<DynamicSelectionState>,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    navController: NavController,
    coroutineScope: CoroutineScope
) = remember {
    CalendarScreenState(
        calendarState = calendarState,
        bottomSheetScaffoldState = bottomSheetScaffoldState,
        navController = navController,
        coroutineScope = coroutineScope
    )
}

@OptIn(ExperimentalMaterialApi::class)
data class CalendarBottomSheetState(
    val bottomSheetScaffoldState: BottomSheetScaffoldState,
    val coroutineScope: CoroutineScope,
) {
    // Round the corner of the bottom sheet based on swipe progress
    fun getHeight(isBottomSheetVisible: Boolean): Dp {
        return if (isBottomSheetVisible) 160.dp else 0.dp
    }

    fun toggleSheet() {
        coroutineScope.launch {
            if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                bottomSheetScaffoldState.bottomSheetState.expand()
            } else {
                bottomSheetScaffoldState.bottomSheetState.collapse()
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun rememberCalendarBottomSheetState(
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    coroutineScope: CoroutineScope
) = remember {
    CalendarBottomSheetState(
        bottomSheetScaffoldState = bottomSheetScaffoldState,
        coroutineScope = coroutineScope
    )
}