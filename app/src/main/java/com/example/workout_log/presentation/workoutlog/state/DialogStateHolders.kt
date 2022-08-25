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

class WorkoutLogDialogState(listener: WorkoutLogDialogListener) : WorkoutDialogVisibilityModifier, WorkoutLogDialogListener by listener {
    var currentDialog by mutableStateOf<WorkoutLogDialog>(WorkoutLogDialog.NoDialog)
        private set

    override fun showEditExerciseDialog(exerciseAndSets: ExerciseAndExerciseSets) {
        currentDialog = WorkoutLogDialog.EditExerciseDialog(exerciseAndSets)
    }

    override fun showReorderExerciseDialog() {
        currentDialog = WorkoutLogDialog.ReorderExerciseDialog
    }

    override fun showEditWorkoutNameDialog() {
        currentDialog = WorkoutLogDialog.EditWorkoutNameDialog
    }

    fun dismissDialog() {
        currentDialog = WorkoutLogDialog.NoDialog
    }
}

@Composable
fun rememberWorkoutLogDialogState(listener: WorkoutLogDialogListener) = remember { WorkoutLogDialogState(listener) }

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