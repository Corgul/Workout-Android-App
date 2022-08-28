package com.example.workout_log.presentation.add_exercise.state

import androidx.compose.runtime.*
import androidx.navigation.NavController
import com.example.workout_log.domain.model.ExerciseName
import com.example.workout_log.presentation.util.Screen

data class AddExerciseViewState(
    val navController: NavController,

) {
    var exerciseNamesVisibility by mutableStateOf(false)
        private set
    val selectedExercises = mutableStateListOf<ExerciseName>()

    fun navigationBackButtonClicked() {
        if (!exerciseNamesVisibility) {
            navController.navigateUp()
        } else {
            onBackPressed()
        }
    }

    fun onBackPressed() {
        if (exerciseNamesVisibility) {
            exerciseNamesVisibility = false
        }
    }

    fun navigateToWorkoutLog(workoutDateLong: Long) {
        navController.navigate(Screen.WorkoutLogScreen.route + "?workoutDate=$workoutDateLong")
    }

    fun onExerciseNameClicked(exerciseName: ExerciseName) {
        // If the exercise name is selected already and this method is called, then it was de selected
        if (selectedExercises.contains(exerciseName)) {
            selectedExercises.remove(exerciseName)
        } else {
            selectedExercises.add(exerciseName)
        }
    }

    fun onExerciseTypeClicked() {
        exerciseNamesVisibility = true
    }

    fun isExerciseNameSelected(exerciseName: ExerciseName) = selectedExercises.contains(exerciseName)

    fun getSelectedExercisesList() = selectedExercises.toList()
}

@Composable
fun rememberAddExerciseViewState(
    navController: NavController
) = remember { AddExerciseViewState(navController) }