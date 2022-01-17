package com.example.workout_log.data.data_source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.workout_log.domain.common.Constants
import com.example.workout_log.domain.model.ExerciseName
import com.example.workout_log.domain.model.ExerciseType
import com.example.workout_log.domain.util.ExerciseNameDatabaseWorker
import com.example.workout_log.domain.util.ExerciseTypeDatabaseWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [ExerciseType::class, ExerciseName::class], version = 1, exportSchema = true)
abstract class ExerciseTypesDatabase : RoomDatabase() {
    abstract fun exerciseTypeDao(): ExerciseTypeDao
    abstract fun exerciseNameDao(): ExerciseNameDao

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: ExerciseTypesDatabase? = null

        fun getInstance(context: Context): ExerciseTypesDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): ExerciseTypesDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ExerciseTypesDatabase::class.java,
                "ExerciseTypesDatabase"
            )
                .fallbackToDestructiveMigration()
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Prepopulate the database
                        val populateExerciseNameRequest = OneTimeWorkRequestBuilder<ExerciseNameDatabaseWorker>()
                            .setInputData(workDataOf(Constants.EXERCISE_NAME_FILE_KEY to Constants.EXERCISE_NAME_FILE))
                            .build()
                        val populateExerciseTypeRequest = OneTimeWorkRequestBuilder<ExerciseTypeDatabaseWorker>()
                            .setInputData(workDataOf(Constants.EXERCISE_TYPE_FILE_KEY to Constants.EXERCISE_TYPE_FILE))
                            .build()
                        // Queue both requests concurrently
                        WorkManager.getInstance(context).enqueue(listOf(populateExerciseNameRequest, populateExerciseTypeRequest))
                    }
                })
                .build()
        }
    }
}