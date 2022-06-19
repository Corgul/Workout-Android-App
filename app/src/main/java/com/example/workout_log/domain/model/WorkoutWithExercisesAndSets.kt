package com.example.workout_log.domain.model

import androidx.room.Embedded
import androidx.room.Relation

data class WorkoutWithExercisesAndSets(
    @Embedded val workout: Workout,
    @Relation(
        entity = Exercise::class,
        parentColumn = "WorkoutID",
        entityColumn = "WorkoutID"
    )
    private val exercisesAndSets: List<ExerciseAndExerciseSets>
) {
    /**
     * Returns a list of exercises ordered by their position
     */
    fun getExercisesAndSets(): List<ExerciseAndExerciseSets> = exercisesAndSets.sortedBy { it.exercise.exercisePosition }
}