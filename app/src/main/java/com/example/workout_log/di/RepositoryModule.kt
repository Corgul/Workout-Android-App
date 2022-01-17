package com.example.workout_log.di

import com.example.workout_log.data.repository.ExerciseNameRepositoryImpl
import com.example.workout_log.data.repository.ExerciseRepositoryImpl
import com.example.workout_log.data.repository.ExerciseTypeRepositoryImpl
import com.example.workout_log.data.repository.WorkoutRepositoryImpl
import com.example.workout_log.domain.repository.ExerciseNameRepository
import com.example.workout_log.domain.repository.ExerciseRepository
import com.example.workout_log.domain.repository.ExerciseTypeRepository
import com.example.workout_log.domain.repository.WorkoutRepository
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
    abstract fun provideExerciseTypeRepository(exerciseTypeRepository: ExerciseTypeRepositoryImpl): ExerciseTypeRepository

    @Singleton
    @Binds
    abstract fun provideExerciseNameRepository(exerciseNameRepository: ExerciseNameRepositoryImpl): ExerciseNameRepository
}