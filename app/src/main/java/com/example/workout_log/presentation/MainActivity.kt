package com.example.workout_log.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.workout_log.domain.util.WorkoutAppLogger
import com.example.workout_log.presentation.add_exercise.AddExerciseScreen
import com.example.workout_log.presentation.util.Screen
import com.example.workout_log.presentation.calendar.CalendarScreen
import com.example.workout_log.presentation.workoutlog.WorkoutLogScreen
import com.example.workout_log.ui.theme.WorkoutlogTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkoutlogTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    Scaffold(
                        bottomBar = {
                            BottomNav(navController = navController)
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = Screen.WorkoutLogScreen.route + "?newlyAddedWorkoutId={newlyAddedWorkoutId}",
                            Modifier.padding(innerPadding)
                        ) {
                            composable(
                                route = Screen.WorkoutLogScreen.route + "?newlyAddedWorkoutId={newlyAddedWorkoutId}",
                                arguments = listOf(
                                    navArgument("newlyAddedWorkoutId") {
                                        type = NavType.LongType
                                        defaultValue = -1
                                    }
                                )
                            ) {
                                WorkoutLogScreen(navController = navController)
                            }
                            composable(route = Screen.CalendarScreen.route) {
                                CalendarScreen(navController = navController)
                            }
                            composable(
                                route = Screen.AddExerciseScreen.route + "?workoutId={workoutId}",
                                arguments = listOf(
                                    navArgument("workoutId") {
                                        type = NavType.LongType
                                        defaultValue = -1
                                    }
                                )
                            ) {
                                AddExerciseScreen(navController = navController)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNav(navController: NavController) {
    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        val items = listOf(
            Screen.WorkoutLogScreen,
            Screen.CalendarScreen
        )
        items.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(screen.icon, contentDescription = null) },
                label = { Text(screen.screenName) },
                // Trim off the beginning of the string so even with arguments it will match and show as selected
                selected = currentDestination?.hierarchy?.any { it.route?.substringBefore("?") == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}