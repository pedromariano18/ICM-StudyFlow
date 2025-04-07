package com.example.studyflow.location

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log

object AutoStudyModeActions {
    fun activateStudyMode(context: Context) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0, 0)
        setScreenBrightness(context, 50)
        Log.d("AutoStudyModeActions", "Study mode activated")
    }

    fun deactivateStudyMode(context: Context) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 5, 0)
        setScreenBrightness(context, 200)
        Log.d("AutoStudyModeActions", "Study mode deactivated")
    }

    private fun setScreenBrightness(context: Context, brightnessValue: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(context)) {
                try {
                    Settings.System.putInt(
                        context.contentResolver,
                        Settings.System.SCREEN_BRIGHTNESS,
                        brightnessValue
                    )
                } catch (e: Exception) {
                    Log.e("AutoStudyModeActions", "Failed to set screen brightness: ${e.message}")
                }
            } else {
                Log.w("AutoStudyModeActions", "WRITE_SETTINGS permission not granted. Redirecting to settings.")
                val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
                    data = Uri.parse("package:" + context.packageName)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            }
        } else {
            try {
                Settings.System.putInt(
                    context.contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS,
                    brightnessValue
                )
            } catch (e: Exception) {
                Log.e("AutoStudyModeActions", "Failed to set screen brightness: ${e.message}")
            }
        }
    }
}
