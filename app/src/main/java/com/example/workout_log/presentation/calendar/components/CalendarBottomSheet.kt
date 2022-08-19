package com.example.workout_log.presentation.calendar.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.workout_log.R
import com.example.workout_log.domain.model.ExerciseAndExerciseSets
import com.example.workout_log.domain.model.ExerciseSet
import com.example.workout_log.domain.model.Workout
import com.example.workout_log.presentation.calendar.state.rememberCalendarBottomSheetState
import com.example.workout_log.presentation.util.extensions.collapsedVisibilityFraction
import com.example.workout_log.presentation.util.extensions.expandedVisibilityFraction
import com.example.workout_log.ui.theme.Grey100
import kotlinx.coroutines.CoroutineScope

@ExperimentalMaterialApi
@Composable
fun CalendarBottomSheet(
    scope: CoroutineScope = rememberCoroutineScope(),
    scaffoldState: BottomSheetScaffoldState,
    isBottomSheetVisible: Boolean = false,
    workout: Workout?,
    exercisesAndSets: List<ExerciseAndExerciseSets>?,
    onGoToWorkoutClicked: (Workout?) -> Unit,
    content: @Composable () -> Unit
) {
    // Round the corner of the bottom sheet based on swipe progress
    val calendarBottomSheetState = rememberCalendarBottomSheetState(bottomSheetScaffoldState = scaffoldState, coroutineScope = scope)
    val radius = (30 * calendarBottomSheetState.bottomSheetScaffoldState.collapsedVisibilityFraction).dp

    CalendarBottomSheetScaffold(
        sheetShape = RoundedCornerShape(topStart = radius, topEnd = radius),
        sheetPeekHeight = calendarBottomSheetState.getHeight(isBottomSheetVisible),
        scaffoldState = calendarBottomSheetState.bottomSheetScaffoldState,
        sheetContent = {
            BottomSheetContent {
                if (workout == null || exercisesAndSets == null) {
                    return@BottomSheetContent
                }
                BottomSheetExpanded {
                    BottomSheetExpandedContent(
                        header = {
                            WorkoutHeader(
                                workout = workout,
                                collapsedVisibilityFraction = calendarBottomSheetState.bottomSheetScaffoldState.expandedVisibilityFraction
                            )
                        },
                        cards = {
                            ExerciseCards(exercisesAndSets = exercisesAndSets) {
                                GoToWorkoutButton(onClicked = { onGoToWorkoutClicked(workout) }, modifier = Modifier.padding(top = 16.dp))
                            }
                        }
                    )
                }
                BottomSheetCollapsed(
                    isCollapsed = calendarBottomSheetState.bottomSheetScaffoldState.bottomSheetState.isCollapsed,
                    currentFraction = calendarBottomSheetState.bottomSheetScaffoldState.collapsedVisibilityFraction,
                    onSheetClick = calendarBottomSheetState::toggleSheet
                ) {
                    BottomSheetCollapsedContent(workout = workout) {
                        GoToWorkoutButton(onClicked = { onGoToWorkoutClicked(workout) })
                    }
                }
            }
        }
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CalendarBottomSheetScaffold(
    sheetShape: Shape,
    sheetPeekHeight: Dp,
    scaffoldState: BottomSheetScaffoldState,
    sheetContent: @Composable ColumnScope.() -> Unit,
    content: @Composable () -> Unit
) {
    BottomSheetScaffold(
        modifier = Modifier.fillMaxWidth(),
        scaffoldState = scaffoldState,
        sheetShape = sheetShape,
        sheetPeekHeight = sheetPeekHeight,
        sheetContent = sheetContent,
    ) {
        content()
    }
}

@ExperimentalMaterialApi
@Composable
fun BottomSheetContent(
    heightFraction: Float = 0.8f,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(fraction = heightFraction)
    ) {
        content()
    }
}

@Composable
fun ColumnScope.BottomSheetLine() {
    Box(
        modifier = Modifier
            .padding(top = 12.dp)
            .height(6.dp)
            .width(70.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Grey100)
            .align(Alignment.CenterHorizontally)
    )
}

@Composable
fun BottomSheetCollapsed(
    isCollapsed: Boolean,
    currentFraction: Float,
    onSheetClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    Column(modifier = Modifier.clickable(onClick = onSheetClick, enabled = isCollapsed)) {
        BottomSheetLine()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                .graphicsLayer(alpha = 1f - currentFraction),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            content()
        }
    }
}

@Composable
fun BottomSheetCollapsedContent(
    workout: Workout?,
    button: @Composable () -> Unit
) {
    Text(workout?.workoutName ?: stringResource(id = R.string.default_workout_name))

    button()
}

@Composable
fun BottomSheetExpanded(
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .padding(top = 32.dp)
            .fillMaxSize()
    ) {
        content()
    }
}

@Composable
fun BottomSheetExpandedContent(
    header: @Composable () -> Unit,
    cards: @Composable () -> Unit
) {
    header()
    cards()
}

@Composable
fun WorkoutHeader(workout: Workout, collapsedVisibilityFraction: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer(alpha = 1f - collapsedVisibilityFraction)
    ) {
        Text(workout.workoutName, style = MaterialTheme.typography.h5, modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun ExerciseCards(exercisesAndSets: List<ExerciseAndExerciseSets>, button: @Composable () -> Unit) {
    LazyColumn(modifier = Modifier.padding(horizontal = 4.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        items(exercisesAndSets, key = { exerciseAndSets -> exerciseAndSets.exercise.exerciseId }) { exerciseAndSets ->
            ExerciseCard(exerciseAndSets = exerciseAndSets)
        }
        item {
            button()
        }
    }
}

@Composable
fun ExerciseCard(exerciseAndSets: ExerciseAndExerciseSets) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = 10.dp
    ) {
        Box(modifier = Modifier
            .clickable { expanded = !expanded }
            .padding(16.dp)) {
            Column {
                ExerciseCardContent(exerciseName = exerciseAndSets.exercise.exerciseName) {
                    ExerciseCardExpandCollapseIconButton(isExpanded = expanded, onExpandCollapseButtonClicked = { expanded = !expanded })
                }
                AnimatedVisibility(visible = expanded) {
                    ExerciseCardExpandedContent(sets = exerciseAndSets.sets) { exerciseSet ->
                        ExerciseCardExpandedContentRow(exerciseSet = exerciseSet)
                    }
                }
            }
        }
    }
}

@Composable
fun ExerciseCardContent(exerciseName: String, iconButton: @Composable () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = exerciseName,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.h6
        )
        iconButton()
    }
}

@Composable
fun ExerciseCardExpandCollapseIconButton(isExpanded: Boolean, onExpandCollapseButtonClicked: () -> Unit) {
    IconButton(onClick = onExpandCollapseButtonClicked) {
        if (isExpanded) {
            Icon(
                imageVector = Icons.Filled.ExpandLess,
                contentDescription = stringResource(id = R.string.calendar_bottom_sheet_collapse_content_description)
            )
        } else {
            Icon(
                imageVector = Icons.Filled.ExpandMore,
                contentDescription = stringResource(id = R.string.calendar_bottom_sheet_expand_content_description)
            )
        }
    }
}

@Composable
fun ExerciseCardExpandedContent(sets: List<ExerciseSet>, content: @Composable (ExerciseSet) -> Unit) {
    Column {
        sets.forEach { exerciseSet ->
            key(exerciseSet.setId) {
                content(exerciseSet)
            }
        }
    }
}

@Composable
fun ExerciseCardExpandedContentRow(exerciseSet: ExerciseSet) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(exerciseSet.setNumber.toString(), style = MaterialTheme.typography.h6)
        Text(
            text = stringResource(id = R.string.calendar_exercise_card_lbs, exerciseSet.weight),
            style = MaterialTheme.typography.h6
        )
        Text(
            text = stringResource(id = R.string.calendar_exercise_card_reps, exerciseSet.reps),
            style = MaterialTheme.typography.h6
        )
    }
}

@Composable
fun GoToWorkoutButton(onClicked: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClicked,
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
        modifier = modifier
    ) {
        Text(text = stringResource(id = R.string.go_to_workout))
    }
}