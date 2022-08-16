package com.example.workout_log.presentation.workoutlog.state

import androidx.compose.runtime.*
import com.example.workout_log.domain.model.ExerciseSet
import org.burnoutcrew.reorderable.ItemPosition

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