package com.example.workout_log.domain.use_cases.workout_log

import com.example.workout_log.domain.model.Exercise
import com.example.workout_log.domain.model.ExerciseSet
import com.example.workout_log.domain.repository.ExerciseSetRepository
import javax.inject.Inject

class AddSet @Inject constructor(
    private val exerciseSetRepository: ExerciseSetRepository
) {
    suspend operator fun invoke(exercise: Exercise, setNumber: Int) {
        val exerciseSet = ExerciseSet(exercise.exerciseId, setNumber, 0, 0)
        exerciseSetRepository.insertSet(exerciseSet)
    }
}