package com.example.studyflow.util

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class InactivityWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        Log.d("INACTIVITY", "🟢 Worker executado!")

        NotificationSender.sendBasicNotification(
            context = applicationContext,
            title = "Saudades de ti! 👀",
            message = "Já passaram 3 dias sem estudar. Bora retomar o foco?"
        )
        return Result.success()
    }
}
