package com.example.studyflow.util

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

object ReminderScheduler {

    fun scheduleDailyReminder(context: Context, testMode: Boolean = false) {
        val workRequest = if (testMode) {
            // Notificação em 1 minuto
            PeriodicWorkRequestBuilder<DailyReminderWorker>(
                15, TimeUnit.MINUTES // mínimo permitido pelo Android
            ).setInitialDelay(1, TimeUnit.MINUTES)
                .addTag("test_reminder")
                .build()
        } else {
            // Notificação diária às 9h
            val now = Calendar.getInstance()
            val target = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 9)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                if (before(now)) add(Calendar.DAY_OF_YEAR, 1)
            }
            val delay = target.timeInMillis - now.timeInMillis

            PeriodicWorkRequestBuilder<DailyReminderWorker>(
                24, TimeUnit.HOURS
            ).setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .build()
        }

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "daily_study_reminder",
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }

    fun scheduleInactivityReminder(context: Context, testMode: Boolean = false) {
        val delay = if (testMode) 1L else 3L

        val workRequest = OneTimeWorkRequestBuilder<InactivityWorker>()
            .setInitialDelay(delay, if (testMode) TimeUnit.MINUTES else TimeUnit.DAYS)
            .addTag("inactivity_reminder")
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "inactivity_reminder",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }
}
