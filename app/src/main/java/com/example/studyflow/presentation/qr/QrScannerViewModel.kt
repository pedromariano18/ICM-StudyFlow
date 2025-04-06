package com.example.studyflow.presentation.qr

import androidx.lifecycle.ViewModel
import com.example.studyflow.presentation.subject.SubjectScreenNavArgs
import com.example.studyflow.presentation.destinations.SubjectScreenRouteDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QrScannerViewModel @Inject constructor() : ViewModel() {

    fun processQrContent(content: String, navigator: DestinationsNavigator) {
        if (content.startsWith("subject:")) {
            val subjectId = content.removePrefix("subject:").toIntOrNull()
            if (subjectId != null) {
                val navArgs = SubjectScreenNavArgs(subjectId = subjectId)
                navigator.navigate(SubjectScreenRouteDestination(navArgs))
            }
        }
    }
    fun subjectExists(id: Int): Boolean {
        // Substitui por tua fonte real
        val existingSubjects = listOf(1, 2, 3, 4) // Exemplo: IDs válidos
        return id in existingSubjects
    }
}
