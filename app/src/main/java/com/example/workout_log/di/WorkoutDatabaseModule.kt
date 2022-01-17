package com.example.workout_log.di

import android.content.Context
import com.example.workout_log.data.data_source.ExerciseDao
import com.example.workout_log.data.data_source.ExerciseSetDao
import com.example.workout_log.data.data_source.WorkoutDao
import com.example.workout_log.data.data_source.WorkoutDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object WorkoutDatabaseModule {
    @Singleton
    @Provides
    fun provideWorkoutDatabase(@ApplicationContext context: Context): WorkoutDatabase {
        return WorkoutDatabase.getInstance(context)
    }

    @Provides
    fun provideWorkoutDao(workoutDatabase: WorkoutDatabase): WorkoutDao {
        return workoutDatabase.workoutDao()
    }

    @Provides
    fun provideExerciseDao(workoutDatabase: WorkoutDatabase): ExerciseDao {
        return workoutDatabase.exerciseDao()
    }

    @Provides
    fun provideExerciseSetDao(workoutDatabase: WorkoutDatabase): ExerciseSetDao {
        return workoutDatabase.exerciseSetDao()
    }
}