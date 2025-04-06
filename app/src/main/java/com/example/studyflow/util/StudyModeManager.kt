package com.example.studyflow.util

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import kotlin.jvm.java
import com.example.studyflow.util.GeofenceReceiver

object StudyModeManager {

    private lateinit var geofencingClient: GeofencingClient

    fun init(context: Context) {
        geofencingClient = LocationServices.getGeofencingClient(context)

        val geofence = Geofence.Builder()
            .setRequestId("study_location")
            .setCircularRegion(40.631096758851825, -8.659562736853278, 500f)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(
                Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT
            )
            .build()

        val request = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()

        val intent = Intent(context, GeofenceReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            geofencingClient.addGeofences(request, pendingIntent)
                .addOnSuccessListener {
                    Log.d("Geofence", "✅ Geofence adicionada com sucesso")
                }
                .addOnFailureListener { e ->
                    Log.e("Geofence", "❌ Falha ao adicionar geofence: ${e.message}")
                }
        } else {
            Log.e("Geofence", "❌ Permissão de localização não concedida!")
        }
    }
}
