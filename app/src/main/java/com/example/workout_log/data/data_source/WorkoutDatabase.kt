package com.example.workout_log.data.data_source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.workout_log.data.data_source.converter.Converters
import com.example.workout_log.domain.model.Exercise
import com.example.workout_log.domain.model.ExerciseSet
import com.example.workout_log.domain.model.Workout

@Database(entities = [Workout::class, Exercise::class, ExerciseSet::class], version = 3)
@TypeConverters(Converters::class)
abstract class WorkoutDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun exerciseSetDao(): ExerciseSetDao

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: WorkoutDatabase? = null

        fun getInstance(context: Context): WorkoutDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): WorkoutDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                WorkoutDatabase::class.java,
                "WorkoutDatabase"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}