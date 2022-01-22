package com.example.workout_log.domain.use_cases.add_exercise

import com.example.workout_log.domain.repository.ExerciseTypeRepository
import javax.inject.Inject

class GetExerciseTypesWithExerciseNames @Inject constructor(
    private val repository: ExerciseTypeRepository
) {
    operator fun invoke() = repository.getExerciseTypesWithNames()
}