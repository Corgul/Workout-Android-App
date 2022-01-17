package com.example.workout_log.domain.util

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.workout_log.data.data_source.ExerciseTypesDatabase
import com.example.workout_log.domain.common.Constants.EXERCISE_TYPE_FILE_KEY
import com.example.workout_log.domain.model.ExerciseType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class ExerciseTypeDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val fileName = inputData.getString(EXERCISE_TYPE_FILE_KEY)
            if (fileName != null) {
                applicationContext.assets.open(fileName).use { inputStream ->
                    JsonReader(inputStream.reader()).use { jsonReader ->
                        val exerciseName = object : TypeToken<List<ExerciseType>>() {}.type
                        val exerciseNameList: List<ExerciseType> = Gson().fromJson(jsonReader, exerciseName)

                        val database = ExerciseTypesDatabase.getInstance(applicationContext)
                        database.exerciseTypeDao().insertAll(exerciseNameList)

                        Result.success()
                    }
                }
            } else {
                WorkoutAppLogger.d("Failed populating the database")
                Result.failure()
            }
        } catch (e: Exception) {
            WorkoutAppLogger.d("Exception thrown $e")
            Result.failure()
        }
    }
}