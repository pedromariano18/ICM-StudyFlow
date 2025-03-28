package com.example.studyflow.data.auth

import com.example.studyflow.domain.auth.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseAuthRepositoryImpl : AuthRepository {
    private val auth = FirebaseAuth.getInstance()

    override suspend fun login(email: String, password: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun isUserLoggedIn(): Boolean = auth.currentUser != null

    override fun logout() = auth.signOut()
}
