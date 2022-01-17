package com.example.workout_log.domain.util

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.workout_log.data.data_source.ExerciseTypesDatabase
import com.example.workout_log.domain.common.Constants.EXERCISE_NAME_FILE_KEY
import com.example.workout_log.domain.model.ExerciseName
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class ExerciseNameDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val fileName = inputData.getString(EXERCISE_NAME_FILE_KEY)
            if (fileName != null) {
                applicationContext.assets.open(fileName).use { inputStream ->
                    JsonReader(inputStream.reader()).use { jsonReader ->
                        val exerciseName = object : TypeToken<List<ExerciseName>>() {}.type
                        val exerciseNameList: List<ExerciseName> = Gson().fromJson(jsonReader, exerciseName)

                        val database = ExerciseTypesDatabase.getInstance(applicationContext)
                        database.exerciseNameDao().insertAll(exerciseNameList)

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