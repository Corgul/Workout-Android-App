package com.example.workout_log.domain.use_cases.workout_log

import com.example.workout_log.domain.model.Workout
import com.example.workout_log.domain.repository.WorkoutRepository
import javax.inject.Inject

class UpdateWorkoutName @Inject constructor(
    private val repository: WorkoutRepository
) {
    suspend operator fun invoke(workout: Workout, newWorkoutName: String) {
        workout.workoutName = newWorkoutName
        repository.updateWorkout(workout)
    }
}