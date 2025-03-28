package com.example.studyflow.presentation.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyflow.domain.auth.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoggedIn by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun login() {
        val trimmedEmail = email.trim()
        val trimmedPassword = password.trim()

        if (trimmedEmail.isBlank() || trimmedPassword.isBlank()) {
            errorMessage = "Email and password cannot be empty"
            return
        }

        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(trimmedEmail, trimmedPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    isLoggedIn = true
                    errorMessage = null
                } else {
                    val exception = task.exception
                    val errorCode = (exception as? FirebaseAuthException)?.errorCode

                    errorMessage = when (errorCode) {
                        "ERROR_USER_NOT_FOUND" -> "No account found for this email"
                        "ERROR_INVALID_EMAIL" -> "Invalid email format"
                        "ERROR_WRONG_PASSWORD" -> "Incorrect password"
                        "ERROR_USER_DISABLED" -> "This user account has been disabled"
                        else -> exception?.localizedMessage ?: "Login failed"
                    }
                }

            }
    }

    fun firebaseAuthWithGoogle(idToken: String, onResult: (Boolean, String?) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    isLoggedIn = true
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    fun register(onResult: (Boolean, String?) -> Unit) {
        val trimmedEmail = email.trim()
        val trimmedPassword = password.trim()

        if (trimmedEmail.isBlank() || trimmedPassword.isBlank()) {
            onResult(false, "Email and password cannot be empty")
            return
        }

        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(trimmedEmail, trimmedPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    isLoggedIn = true
                    onResult(true, null)
                } else {
                    val exception = task.exception
                    val errorCode = (exception as? FirebaseAuthException)?.errorCode

                    val message = when (errorCode) {
                        "ERROR_EMAIL_ALREADY_IN_USE" -> "This email is already in use"
                        "ERROR_INVALID_EMAIL" -> "Invalid email format"
                        "ERROR_WEAK_PASSWORD" -> "Password should be at least 6 characters"
                        else -> exception?.localizedMessage ?: "Registration failed"
                    }

                    onResult(false, message)
                }
            }
    }
}
