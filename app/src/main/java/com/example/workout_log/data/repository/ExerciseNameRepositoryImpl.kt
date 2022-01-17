package com.example.workout_log.data.repository

import com.example.workout_log.data.data_source.ExerciseNameDao
import com.example.workout_log.domain.model.ExerciseName
import com.example.workout_log.domain.model.ExerciseTypeWithNames
import com.example.workout_log.domain.repository.ExerciseNameRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseNameRepositoryImpl @Inject constructor(
    private val exerciseName: ExerciseNameDao
) : ExerciseNameRepository {
    @ExperimentalCoroutinesApi
    override fun getExerciseNames(exerciseTypeId: Int): Flow<List<ExerciseName>> {
        val flow = exerciseName.getExerciseTypesWithNames(exerciseTypeId)
        return flow.flatMapLatest { exerciseTypeWithNamesList: List<ExerciseTypeWithNames> ->
            flow {
                emit(exerciseTypeWithNamesList.first().exerciseNames)
            }
        }
    }
}