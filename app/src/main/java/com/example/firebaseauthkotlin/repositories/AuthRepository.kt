package com.example.firebaseauthkotlin.repositories

import com.example.firebaseauthkotlin.utilities.Resource
import com.google.firebase.auth.AuthResult

interface AuthRepository {
    suspend fun login(email: String, password: String): Resource<AuthResult>
}