package com.example.workout_log.di

import android.content.Context
import com.example.workout_log.data.data_source.ExerciseNameDao
import com.example.workout_log.data.data_source.ExerciseTypeDao
import com.example.workout_log.data.data_source.ExerciseTypesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ExerciseTypeDatabaseModule {

    @Singleton
    @Provides
    fun provideExerciseTypeDatabase(@ApplicationContext context: Context): ExerciseTypesDatabase {
        return ExerciseTypesDatabase.getInstance(context)
    }

    @Provides
    fun provideExerciseTypeDao(exerciseTypesDatabase: ExerciseTypesDatabase): ExerciseTypeDao {
        return exerciseTypesDatabase.exerciseTypeDao()
    }

    @Provides
    fun providesExerciseNameDao(exerciseTypesDatabase: ExerciseTypesDatabase): ExerciseNameDao {
        return exerciseTypesDatabase.exerciseNameDao()
    }
}