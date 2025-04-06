package com.example.studyflow.presentation.qr

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

@Composable
fun QrScannerScreen(
    onResult: (String) -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            onResult(result.contents)
        } else {
            Toast.makeText(context, "Scan cancelado", Toast.LENGTH_SHORT).show()
            onCancel()
        }
    }

    LaunchedEffect(Unit) {
        launcher.launch(
            ScanOptions().apply {
                setBeepEnabled(true)
                setOrientationLocked(false)
                setPrompt("Scan QR code")
                setCameraId(1) // Use a specific camera of the device
            }
        )
    }
}
