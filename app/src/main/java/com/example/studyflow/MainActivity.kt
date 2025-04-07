package com.example.studyflow

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import com.example.studyflow.location.GeofencingManager
import com.example.studyflow.presentation.NavGraphs
import com.example.studyflow.presentation.destinations.SessionScreenRouteDestination
import com.example.studyflow.presentation.session.StudySessionTimerService
import com.example.studyflow.presentation.theme.StudyflowTheme
import com.example.studyflow.util.StudyModeManager
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var isBound by mutableStateOf(false)
    private lateinit var timerService: StudySessionTimerService

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
            val binder = service as StudySessionTimerService.StudySessionTimerBinder
            timerService = binder.getService()
            isBound = true
        }
        override fun onServiceDisconnected(p0: ComponentName?) {
            isBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, StudySessionTimerService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onResume() {
        super.onResume()
        // Verifica se a permissão de localização foi concedida
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("Geofence", "🟢 Permissão confirmada em onResume")
            // Inicializa o StudyModeManager e registra o geofence
            StudyModeManager.init(this)
            registerGeofence()
        } else {
            Log.e("Geofence", "🔴 Permissão de localização NÃO concedida em onResume")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            if (isBound) {
                StudyflowTheme {
                    Column {
                        DestinationsNavHost(
                            navGraph = NavGraphs.root,
                            dependenciesContainerBuilder = {
                                dependency(SessionScreenRouteDestination) { timerService }
                            }
                        )
                    }
                }
            }
        }
        requestPermission() // Solicita as permissões necessárias
    }

    private fun requestPermission() {
        val permissions = mutableListOf<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(android.Manifest.permission.POST_NOTIFICATIONS)
        }
        permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
        permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        permissions.add(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        ActivityCompat.requestPermissions(this, permissions.toTypedArray(), 1001)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            if (grantResults.isNotEmpty() && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                Log.d("Geofence", "Permissão concedida no onRequestPermissionsResult")
                StudyModeManager.init(this)
                registerGeofence()
            } else {
                Log.e("Geofence", "Permissão de localização NÃO concedida no onRequestPermissionsResult")
            }
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        isBound = false
    }

    // Método privado para registrar o geofence com as coordenadas desejadas
    private fun registerGeofence() {
        Log.d("TestGeofence", "Chamando registerGeofence()")
        // Coordenadas da Biblioteca Universitária de Aveiro (convertidas)
        val studyZoneLatitude = 40.631078
        val studyZoneLongitude =  -8.659624
        val radiusInMeters = 100f

        val geofencingManager = GeofencingManager(this)
        geofencingManager.addGeofence("studyZone", studyZoneLatitude, studyZoneLongitude, radiusInMeters)
    }
}
