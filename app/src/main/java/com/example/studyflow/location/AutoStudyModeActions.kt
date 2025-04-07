package com.example.studyflow.location

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log

object AutoStudyModeActions {

    /**
     * Ativa o modo de estudo:
     * - Muta notificações.
     * - Ajusta o brilho da tela.
     */
    fun activateStudyMode(context: Context) {
        // Mutar notificações definindo o volume para 0
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0, 0)
        // Ajusta o brilho para um valor baixo (ex: 50)
        setScreenBrightness(context, 50)
        Log.d("AutoStudyModeActions", "Study mode activated")
    }

    /**
     * Desativa o modo de estudo:
     * - Restaura o volume das notificações.
     * - Restaura o brilho da tela para um valor padrão.
     */
    fun deactivateStudyMode(context: Context) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        // Restaura o volume das notificações para um valor padrão (ex: 5)
        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 5, 0)
        // Restaura o brilho da tela para um valor mais alto (ex: 200)
        setScreenBrightness(context, 200)
        Log.d("AutoStudyModeActions", "Study mode deactivated")
    }

    /**
     * Ajusta o brilho da tela usando a API do sistema.
     * Em dispositivos com API >= 23, verifica se a permissão WRITE_SETTINGS foi concedida.
     */
    private fun setScreenBrightness(context: Context, brightnessValue: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Verifica se o app tem permissão para alterar as configurações do sistema
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
            // Em API's inferiores a 23, a verificação não é necessária
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
