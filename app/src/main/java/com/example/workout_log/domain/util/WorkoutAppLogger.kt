package com.example.workout_log.domain.util

import android.util.Log

object WorkoutAppLogger {
    fun d(message: String) {
        val stElements = Thread.currentThread().stackTrace
        Log.d(getTag(), message)
    }

    private fun getTag(): String {
        var tag = ""
        val ste = Thread.currentThread().stackTrace
        for (i in ste.indices) {
            if (ste[i].methodName == "d") {
                tag = "(" + ste[i + 1].fileName + ":" + ste[i + 1].lineNumber + ")"
            }
        }
        return tag
    }
}