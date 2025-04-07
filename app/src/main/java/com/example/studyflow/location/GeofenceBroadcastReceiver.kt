package com.example.studyflow.location

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent) ?: run {
            Log.e("GeofenceBR", "Geofencing event is null")
            return
        }

        if (geofencingEvent.hasError()) {
            Log.e("GeofenceBR", "Erro no geofencing: ${geofencingEvent.errorCode}")
            return
        }

        when (geofencingEvent.geofenceTransition) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                AutoStudyModeActions.activateStudyMode(context)
                StudyModeRepository.activateStudyMode()
            }
            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                AutoStudyModeActions.deactivateStudyMode(context)
                StudyModeRepository.deactivateStudyMode()
            }
            else -> {
                Log.w("GeofenceBR", "Transição desconhecida: ${geofencingEvent.geofenceTransition}")
            }
        }
    }
}
