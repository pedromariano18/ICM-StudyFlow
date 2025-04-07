package com.example.studyflow.location

import androidx.lifecycle.ViewModel

class StudyModeViewModel : ViewModel() {
    val isStudyModeActive = StudyModeRepository.isStudyModeActive
}
