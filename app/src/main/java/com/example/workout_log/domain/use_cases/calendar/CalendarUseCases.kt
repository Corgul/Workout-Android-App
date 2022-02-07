package com.example.workout_log.domain.use_cases.calendar

import com.example.workout_log.domain.use_cases.workout_log.GetWorkoutWithExercisesAndSets
import javax.inject.Inject

data class CalendarUseCases @Inject constructor(
    val getWorkoutDays: GetWorkoutDays,
    val getWorkoutWithExercisesAndSets: GetWorkoutWithExercisesAndSets
)