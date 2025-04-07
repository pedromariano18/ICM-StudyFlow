package com.example.studyflow.location

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object StudyModeRepository {
    private val _isStudyModeActive = MutableStateFlow(false)
    val isStudyModeActive = _isStudyModeActive.asStateFlow()

    fun activateStudyMode() {
        _isStudyModeActive.value = true
    }

    fun deactivateStudyMode() {
        _isStudyModeActive.value = false
    }
}
