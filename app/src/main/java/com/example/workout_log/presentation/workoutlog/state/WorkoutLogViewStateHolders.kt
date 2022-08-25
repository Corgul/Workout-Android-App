package com.example.workout_log.presentation.workoutlog.state

import android.content.Context
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.workout_log.R
import com.example.workout_log.domain.model.Exercise
import com.example.workout_log.domain.model.ExerciseAndExerciseSets
import com.example.workout_log.presentation.util.Screen
import com.example.workout_log.presentation.workoutlog.WorkoutLogBottomSheet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class WorkoutLogViewState(
    private val coroutineScope: CoroutineScope,
    val scaffoldState: ScaffoldState,
    private val navController: NavController,
    private val context: Context,
    val workoutDateLong: Long,
    private val snackbarListener: WorkoutLogSnackbarListener,
    private val workoutLogCardListener: WorkoutLogCardListener
) : WorkoutLogCardListener by workoutLogCardListener {
    @OptIn(ExperimentalMaterialApi::class)
    val modalBottomSheetState: ModalBottomSheetState = ModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    var currentBottomSheet by mutableStateOf<WorkoutLogBottomSheet>(WorkoutLogBottomSheet.NoBottomSheet)
        private set

    @OptIn(ExperimentalMaterialApi::class)
    fun openWorkoutBottomSheet() {
        currentBottomSheet = WorkoutLogBottomSheet.WorkoutBottomSheet
        coroutineScope.launch { modalBottomSheetState.show() }
    }

    @OptIn(ExperimentalMaterialApi::class)
    fun openExerciseBottomSheet(exerciseAndSets: ExerciseAndExerciseSets) {
        currentBottomSheet = WorkoutLogBottomSheet.ExerciseBottomSheet(exerciseAndSets)
        coroutineScope.launch { modalBottomSheetState.show() }
    }

    @OptIn(ExperimentalMaterialApi::class)
    fun hideBottomSheet() {
        coroutineScope.launch { modalBottomSheetState.hide() }
    }

    fun showReorderExerciseSnackbar() {
        coroutineScope.launch {
            scaffoldState.snackbarHostState.showSnackbar(message = context.resources.getString(R.string.reorder_exercise_snackbar))
        }
    }

    fun showDeleteExerciseSnackbar(exercise: Exercise) {
        coroutineScope.launch {
            val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(message = context.resources.getString(R.string.deleted_exercise), actionLabel = context.resources.getString(
                R.string.undo))
            when (snackbarResult) {
                SnackbarResult.Dismissed -> snackbarListener.deleteExerciseSnackbarDismissed(exercise)
                SnackbarResult.ActionPerformed -> snackbarListener.deleteExerciseSnackbarUndoClicked()
            }
        }
    }

    fun showDeleteWorkoutSnackbar() {
        coroutineScope.launch {
            val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(message = context.resources.getString(R.string.deleted_workout), actionLabel = context.resources.getString(R.string.undo))
            when (snackbarResult) {
                SnackbarResult.Dismissed -> snackbarListener.deleteWorkoutSnackbarDismissed()
                SnackbarResult.ActionPerformed -> snackbarListener.deleteWorkoutSnackbarUndoClicked()
            }
        }
    }

    fun navigateToAddExercisesScreen() {
        navController.navigate(Screen.AddExerciseScreen.route + "/${workoutDateLong}")
    }
}

@Composable
fun rememberWorkoutLogViewState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavController = rememberNavController(),
    context: Context,
    workoutDateLong: Long,
    snackbarListener: WorkoutLogSnackbarListener,
    cardListener: WorkoutLogCardListener,
    key: Any
) = remember(key) {
    WorkoutLogViewState(coroutineScope, scaffoldState, navController, context, workoutDateLong, snackbarListener, cardListener)
}