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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.workout_log.domain.model.ExerciseAndExerciseSets
import com.example.workout_log.domain.model.Workout
import com.example.workout_log.domain.model.WorkoutWithExercisesAndSets
import com.example.workout_log.presentation.util.extensions.collapsedVisibilityFraction
import com.example.workout_log.presentation.util.extensions.expandedVisibilityFraction
import com.example.workout_log.ui.theme.Grey100
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun BottomSheet(
    workoutWithExercisesAndSets: WorkoutWithExercisesAndSets?,
    scaffoldState: BottomSheetScaffoldState,
    isBottomSheetVisible: Boolean = false,
    onGoToWorkoutClicked: () -> Unit,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    // Round the corner of the bottom sheet based on swipe progress
    val radius = (30 * scaffoldState.collapsedVisibilityFraction).dp
    val height = if (isBottomSheetVisible) 160.dp else 0.dp
    val sheetToggle: () -> Unit = {
        scope.launch {
            if (scaffoldState.bottomSheetState.isCollapsed) {
                scaffoldState.bottomSheetState.expand()
            } else {
                scaffoldState.bottomSheetState.collapse()
            }
        }
    }

    BottomSheetScaffold(
        modifier = Modifier.fillMaxWidth(),
        scaffoldState = scaffoldState,
        sheetShape = RoundedCornerShape(topStart = radius, topEnd = radius),
        sheetPeekHeight = height,
        sheetContent = {
            BottomSheetContent {
                BottomSheetExpanded {
                    BottomSheetExpandedContent(
                        workoutWithExercisesAndSets,
                        collapsedVisibilityFraction = scaffoldState.expandedVisibilityFraction,
                        onGoToWorkoutClicked
                    )
                }
                BottomSheetCollapsed(
                    isCollapsed = scaffoldState.bottomSheetState.isCollapsed,
                    currentFraction = scaffoldState.collapsedVisibilityFraction,
                    onSheetClick = sheetToggle
                ) {
                    BottomSheetCollapsedContent(workout = workoutWithExercisesAndSets?.workout, onGoToWorkoutClicked)
                }
            }
        },
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
    onGoToWorkoutClicked: () -> Unit
) {
    Text(workout?.workoutName ?: "Workout Name")

    GoToWorkoutButton(onGoToWorkoutClicked)
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
fun ColumnScope.BottomSheetExpandedContent(
    workoutWithExercisesAndSets: WorkoutWithExercisesAndSets?,
    collapsedVisibilityFraction: Float,
    onGoToWorkoutClicked: () -> Unit
) {
    if (workoutWithExercisesAndSets == null) {
        return
    }

    WorkoutHeader(workoutWithExercisesAndSets.workout, collapsedVisibilityFraction)

    ExerciseCards(workoutWithExercisesAndSets.exercisesAndSets)

    GoToWorkoutButton(onGoToWorkoutClicked, modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 16.dp))
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
fun ExerciseCards(exercisesAndSets: List<ExerciseAndExerciseSets>) {
    LazyColumn(modifier = Modifier.padding(horizontal = 4.dp)) {
        items(exercisesAndSets) { exerciseAndSets ->
            ExerciseCard(exerciseAndSets = exerciseAndSets)
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        exerciseAndSets.exercise.exerciseName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.h6
                    )
                    IconButton(onClick = { expanded = !expanded }) {
                        if (expanded) {
                            Icon(imageVector = Icons.Filled.ExpandLess, contentDescription = "Collapse")
                        } else {
                            Icon(imageVector = Icons.Filled.ExpandMore, contentDescription = "Expand")
                        }
                    }
                }
                AnimatedVisibility(visible = expanded) {
                    Column {
                        exerciseAndSets.sets.forEach { exerciseSet ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(exerciseSet.setNumber.toString(), style = MaterialTheme.typography.h6)
                                Text("${exerciseSet.weight} lbs", style = MaterialTheme.typography.h6)
                                Text("${exerciseSet.reps} reps", style = MaterialTheme.typography.h6)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GoToWorkoutButton(onClicked: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClicked,
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
        modifier = modifier
    ) {
        Text("Go to Workout")
    }
}