package com.example.firebaseauthkotlin.repositories

import android.content.ContentValues.TAG
import android.util.Log
import com.example.firebaseauthkotlin.entities.User
import com.example.firebaseauthkotlin.utilities.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseAuthRepository : AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val users = FirebaseFirestore.getInstance().collection("users")

    override suspend fun login(email: String, password: String): Resource<AuthResult> {
        return withContext(Dispatchers.IO) {
            try {
                val result = auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Log.e(TAG, "Login: User logged in successfully!")
                        }
                    }
                Resource.Success(result.await())
            } catch (e: Exception) {
                Resource.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    override suspend fun register(
        email: String, name: String, password: String
    ): Resource<AuthResult> {
        return withContext(Dispatchers.IO) {
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val userId = result.user?.uid!!
                val user = User(userId = userId, name = name)
                users.document(userId).set(user)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Log.e(TAG, "Register: User registered successfully!")
                        }
                    }
                Resource.Success(result)
            } catch (e: Exception) {
                Resource.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}