package com.example.studyflow.util

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.util.Log
import android.widget.Toast
import com.example.studyflow.presentation.session.ServiceHelper

object StudyUtils {

    fun enableStudyMode(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (!notificationManager.isNotificationPolicyAccessGranted) {
            val intent = Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
            context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        } else {
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT
        }

        ServiceHelper.triggerForegroundService(
            context = context,
            action = Constants.ACTION_SERVICE_START
        )

        Toast.makeText(context, " Modo Estudo Ativado", Toast.LENGTH_SHORT).show()
        Log.d("StudyMode", " Modo Estudo ATIVADO: entrou na geofence")
    }

    fun disableStudyMode(context: Context) {
        // Reativar som
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL

        // Parar timer/música
        ServiceHelper.triggerForegroundService(
            context = context,
            action = Constants.ACTION_SERVICE_CANCEL
        )
        Toast.makeText(context, " Modo Estudo Desativado", Toast.LENGTH_SHORT).show()
        Log.d("StudyMode", "Modo Estudo DESATIVADO: saiu da geofence")
    }
}
