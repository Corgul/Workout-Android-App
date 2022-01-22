package com.example.workout_log.domain.use_cases.add_exercise

import com.example.workout_log.domain.model.Exercise
import com.example.workout_log.domain.model.ExerciseName
import com.example.workout_log.domain.model.ExerciseSet
import com.example.workout_log.domain.repository.ExerciseRepository
import com.example.workout_log.domain.repository.ExerciseSetRepository
import javax.inject.Inject

class SaveExercises @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    private val exerciseSetRepository: ExerciseSetRepository
) {
    suspend operator fun invoke(workoutId: Long, exerciseNameList: List<ExerciseName>) {
        // Save the list of exercises
        val exerciseList = Exercise.from(workoutId, exerciseNameList)
        val insertedExerciseIds = exerciseRepository.insertExercises(exerciseList)
        // Save the list of exercise sets corresponding to the new exercises
        val setsList = ExerciseSet.from(insertedExerciseIds)
        exerciseSetRepository.insertSets(setsList)
    }
}