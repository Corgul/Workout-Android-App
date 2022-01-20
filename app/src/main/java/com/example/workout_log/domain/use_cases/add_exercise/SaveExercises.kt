package com.example.workout_log.domain.use_cases.add_exercise

import com.example.workout_log.domain.model.Exercise
import com.example.workout_log.domain.model.ExerciseName
import com.example.workout_log.domain.model.Workout
import com.example.workout_log.domain.repository.ExerciseRepository
import com.example.workout_log.domain.util.WorkoutAppLogger
import javax.inject.Inject

class SaveExercises @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) {
    suspend operator fun invoke(workout: Workout?, exerciseNameList: List<ExerciseName>) {
        if (workout == null) {
            WorkoutAppLogger.d("Workout is null and should not be null at this point")
            return
        }
        // Save the list of exercises
        val exerciseList = Exercise.from(workout, exerciseNameList)
        exerciseRepository.addExercisesForWorkout(exerciseList)
    }
}