package com.example.studyflow.presentation.qr

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.studyflow.presentation.destinations.SubjectScreenRouteDestination
import com.example.studyflow.presentation.subject.SubjectScreenNavArgs
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun QrScannerScreenRoute(navigator: DestinationsNavigator) {
    val viewModel: QrScannerViewModel = hiltViewModel()
    val context = LocalContext.current

    QrScannerScreen(
        onResult = { content ->
            val subjectId = content.removePrefix("subject:").toIntOrNull()
            if (subjectId == null) {
                Toast.makeText(context, "QR inválido!", Toast.LENGTH_SHORT).show()
                return@QrScannerScreen
            }

            // Aqui podes usar o ViewModel real para verificar a base de dados (só exemplo):
            val idsValidos = listOf(1, 2, 3, 4)
            if (subjectId !in idsValidos) {
                Toast.makeText(context, "Disciplina não encontrada!", Toast.LENGTH_SHORT).show()
                return@QrScannerScreen
            }

            navigator.navigate(SubjectScreenRouteDestination(SubjectScreenNavArgs(subjectId)))
        },
        onCancel = { navigator.popBackStack() }
    )
}
