package com.example.studyflow.domain.model

<<<<<<< HEAD
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.studyflow.presentation.theme.gradient1
import com.example.studyflow.presentation.theme.gradient2
import com.example.studyflow.presentation.theme.gradient3
import com.example.studyflow.presentation.theme.gradient4
import com.example.studyflow.presentation.theme.gradient5
=======

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.studyflow.ui.theme.gradient1
import com.example.studyflow.ui.theme.gradient2
import com.example.studyflow.ui.theme.gradient3
import com.example.studyflow.ui.theme.gradient4
import com.example.studyflow.ui.theme.gradient5

>>>>>>> a70146e4786adf9fb18010758f902a7f2d25629c

@Entity
data class Subject(
    val name: String,
    val goalHours: Float,
    val colors: List<Int>,
    @PrimaryKey(autoGenerate = true)
    val subjectId: Int? = null
) {
    companion object {
        val subjectCardColors = listOf(gradient1, gradient2, gradient3, gradient4, gradient5)
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> a70146e4786adf9fb18010758f902a7f2d25629c
