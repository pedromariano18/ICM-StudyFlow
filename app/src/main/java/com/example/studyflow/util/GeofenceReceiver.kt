package com.example.studyflow.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("GeofenceReceiver", "🔔 Broadcast recebido: ${intent.action}")

        val event = GeofencingEvent.fromIntent(intent) ?: return
        if (event.hasError()) {
            Log.e("GeofenceReceiver", "Erro no evento: ${event.errorCode}")
            return
        }

        when (event.geofenceTransition) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                Log.d("GeofenceReceiver", "✅ Entrou na geofence")
                Toast.makeText(context, "🟢 Entrou na zona de estudo", Toast.LENGTH_SHORT).show()
                StudyUtils.enableStudyMode(context)
            }
            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                Log.d("GeofenceReceiver", "🔴 Saiu da geofence")
                Toast.makeText(context, "🔴 Saiu da zona de estudo", Toast.LENGTH_SHORT).show()
                StudyUtils.disableStudyMode(context)
            }
            else -> {
                Log.d("GeofenceReceiver", "⚠️ Outro tipo de transição: ${event.geofenceTransition}")
            }
        }
    }
}
