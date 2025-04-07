package com.example.studyflow.location

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

class GeofencingManager(private val context: Context) {

    private val geofencingClient: GeofencingClient by lazy {
        LocationServices.getGeofencingClient(context)
    }

    /**
     * Adiciona um geofence para uma área específica.
     * @param requestId Identificador único para o geofence.
     * @param latitude Latitude do centro da área.
     * @param longitude Longitude do centro da área.
     * @param radius Raio em metros para a área.
     */
    fun addGeofence(
        requestId: String,
        latitude: Double,
        longitude: Double,
        radius: Float
    ) {
        val geofence = Geofence.Builder()
            .setRequestId(requestId)
            .setCircularRegion(latitude, longitude, radius)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .build()

        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()

        val pendingIntent = getGeofencePendingIntent()

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("GeofencingManager", "Permissão de localização não concedida")
            return
        }

        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
            .addOnSuccessListener {
                Log.d("GeofencingManager", "Geofence '$requestId' adicionada com sucesso")
            }
            .addOnFailureListener { e ->
                Log.e("GeofencingManager", "Falha ao adicionar geofence '$requestId': ${e.message}")
            }
    }

    /**
     * Remove o geofence com base no requestId.
     */
    fun removeGeofence(requestId: String) {
        geofencingClient.removeGeofences(listOf(requestId))
            .addOnSuccessListener {
                Log.d("GeofencingManager", "Geofence '$requestId' removida com sucesso")
            }
            .addOnFailureListener { e ->
                Log.e("GeofencingManager", "Falha ao remover geofence '$requestId': ${e.message}")
            }
    }

    // Cria o PendingIntent que será usado pelo sistema para chamar o BroadcastReceiver
    private fun getGeofencePendingIntent(): PendingIntent {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        return PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
