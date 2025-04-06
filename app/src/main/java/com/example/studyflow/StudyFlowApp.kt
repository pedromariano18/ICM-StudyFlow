package com.example.studyflow

import android.app.Application
import com.example.studyflow.util.NotificationSender
import com.example.studyflow.util.ReminderScheduler
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class StudyFlowApp : Application() {
    override fun onCreate() {
        super.onCreate()
        NotificationSender.createChannelIfNeeded(this)
        ReminderScheduler.scheduleDailyReminder(this)
        // ReminderScheduler.scheduleDailyReminder(this, testMode = true) // modo de teste
    }
}

