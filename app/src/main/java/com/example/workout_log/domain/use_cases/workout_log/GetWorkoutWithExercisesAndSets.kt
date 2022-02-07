package com.example.workout_log.domain.use_cases.workout_log

import com.example.workout_log.domain.model.WorkoutWithExercisesAndSets
import com.example.workout_log.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetWorkoutWithExercisesAndSets @Inject constructor(private val workoutRepository: WorkoutRepository) {
    operator fun invoke(workoutDate: LocalDate): Flow<WorkoutWithExercisesAndSets?> =
        workoutRepository.getExercisesAndSetsForWorkout(workoutDate)
}