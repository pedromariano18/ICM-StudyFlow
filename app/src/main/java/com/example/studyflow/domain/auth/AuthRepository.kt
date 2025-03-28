package com.example.studyflow.domain.auth

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Unit>
    fun isUserLoggedIn(): Boolean
    fun logout()
}