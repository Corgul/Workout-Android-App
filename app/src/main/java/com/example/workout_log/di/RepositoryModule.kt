package com.example.workout_log.di

import com.example.workout_log.data.repository.*
import com.example.workout_log.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun provideWorkoutRepository(workoutRepository: WorkoutRepositoryImpl): WorkoutRepository

    @Singleton
    @Binds
    abstract fun provideExerciseRepository(exerciseRepository: ExerciseRepositoryImpl): ExerciseRepository

    @Singleton
    @Binds
    abstract fun provideExerciseSetRepository(exerciseSetRepositoryImpl: ExerciseSetRepositoryImpl): ExerciseSetRepository

    @Singleton
    @Binds
    abstract fun provideExerciseTypeRepository(exerciseTypeRepository: ExerciseTypeRepositoryImpl): ExerciseTypeRepository

    @Singleton
    @Binds
    abstract fun provideExerciseNameRepository(exerciseNameRepository: ExerciseNameRepositoryImpl): ExerciseNameRepository
}