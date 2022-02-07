package com.example.workout_log.domain.use_cases.workout_log

import javax.inject.Inject

data class WorkoutBottomSheetUseCases @Inject constructor(
    val deleteWorkout: DeleteWorkout
)