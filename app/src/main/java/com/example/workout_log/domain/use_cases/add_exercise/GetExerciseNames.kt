package com.example.workout_log.domain.use_cases.add_exercise

import com.example.workout_log.domain.model.ExerciseName
import com.example.workout_log.domain.model.ExerciseTypeWithNames
import com.example.workout_log.domain.repository.ExerciseNameRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetExerciseNames @Inject constructor(
    private val repository: ExerciseNameRepository
){
    @ExperimentalCoroutinesApi
    operator fun invoke(exerciseTypeId: Int): Flow<List<ExerciseName>> {
        val flow = repository.getExerciseTypeWithNames(exerciseTypeId)
        return flow.flatMapLatest { exerciseTypeWithNamesList: List<ExerciseTypeWithNames> ->
            flow {
                emit(exerciseTypeWithNamesList.first().exerciseNames)
            }
        }
    }
}