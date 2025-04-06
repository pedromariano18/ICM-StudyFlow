package com.example.studyflow.util

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class DailyReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        NotificationSender.sendBasicNotification(
            context = applicationContext,
            title = "Hora de Estudar 📚",
            message = "Já estudaste hoje? Vamos bater a meta!"
        )
        return Result.success()
    }
}
