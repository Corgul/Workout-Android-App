package com.example.workout_log.domain.use_cases.workout_log

import javax.inject.Inject

data class ExerciseBottomSheetUseCases @Inject constructor(
    val deleteExercise: DeleteExercise,
    val updateSets: UpdateSets,
    val deleteSets: DeleteSets
)