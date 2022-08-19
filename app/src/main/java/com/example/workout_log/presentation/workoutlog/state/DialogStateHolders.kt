package com.example.workout_log.presentation.workoutlog.state

import androidx.compose.runtime.*
import com.example.workout_log.domain.model.ExerciseAndExerciseSets
import com.example.workout_log.domain.model.ExerciseSet
import org.burnoutcrew.reorderable.ItemPosition

sealed class WorkoutLogDialog {
    class EditExerciseDialog(val exerciseAndExerciseSets: ExerciseAndExerciseSets) : WorkoutLogDialog()
    object ReorderExerciseDialog : WorkoutLogDialog()
    object EditWorkoutNameDialog : WorkoutLogDialog()
    object NoDialog : WorkoutLogDialog()
}

class WorkoutLogDialogState {
    val currentDialog: State<WorkoutLogDialog>
        get() = _currentDialog
    var _currentDialog = mutableStateOf<WorkoutLogDialog>(WorkoutLogDialog.NoDialog)

    fun showEditExerciseDialog(exerciseAndSets: ExerciseAndExerciseSets) {
        _currentDialog.value = WorkoutLogDialog.EditExerciseDialog(exerciseAndSets)
    }

    fun showReorderExerciseDialog() {
        _currentDialog.value = WorkoutLogDialog.ReorderExerciseDialog
    }

    fun showEditWorkoutNameDialog() {
        _currentDialog.value = WorkoutLogDialog.EditWorkoutNameDialog
    }

    fun dismissDialog() {
        _currentDialog.value = WorkoutLogDialog.NoDialog
    }
}

@Composable
fun rememberWorkoutLogDialogState() = remember { WorkoutLogDialogState() }

data class EditExerciseDialogState(
    private val sets: List<ExerciseSet>,
) {
    val checkedSets = mutableStateListOf<ExerciseSet>()
    var reorderableSetList by mutableStateOf(sets)
    var showDeleteButton by mutableStateOf(false)

    fun onSetChecked(exerciseSet: ExerciseSet, isChecked: Boolean) {
        if (isChecked) {
            checkedSets.add(exerciseSet)
        } else {
            checkedSets.remove(exerciseSet)
        }
        showOrHideDeleteButton()
    }

    private fun showOrHideDeleteButton() {
        // If there are no checked sets do not show the delete button
        showDeleteButton = checkedSets.size != 0
    }

    fun onSetsMoved(fromPosition: ItemPosition, toPosition: ItemPosition) {
        reorderableSetList = reorderableSetList.toMutableList().apply {
            add(toPosition.index, removeAt(fromPosition.index))
        }
    }
}

@Composable
fun rememberEditExerciseDialogState(
    sets: List<ExerciseSet>
) = remember {
    EditExerciseDialogState(sets)
}