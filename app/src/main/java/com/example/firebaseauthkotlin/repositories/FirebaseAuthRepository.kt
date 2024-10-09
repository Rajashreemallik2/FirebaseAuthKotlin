package com.example.firebaseauthkotlin.repositories

import android.content.ContentValues.TAG
import android.util.Log
import com.example.firebaseauthkotlin.entities.User
import com.example.firebaseauthkotlin.utilities.Resource
import com.example.firebaseauthkotlin.utilities.executeWithTryCatch
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseAuthRepository : AuthRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val existingUsers = FirebaseFirestore.getInstance().collection("users")

    override suspend fun login(email: String, password: String): Resource<AuthResult> {
        return withContext(Dispatchers.IO) {
            executeWithTryCatch {
                val result = firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Log.i(TAG, "Login: User logged in successfully!")
                        }
                    }
                Resource.Success(result.await())
            }
        }
    }

    override suspend fun loginWithGoogle(account: GoogleSignInAccount?): Resource<AuthResult> {
        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)

        return withContext(Dispatchers.IO) {
            executeWithTryCatch {
                val resultTask = firebaseAuth.signInWithCredential(credential)
                resultTask
                    .addOnSuccessListener { authRes ->
                        if (authRes.additionalUserInfo!!.isNewUser) {
                            Log.i(TAG, "New user logged in, user: ${authRes.user?.displayName}")
                        } else {
                            Log.i(
                                TAG, "Existing user logged in, user: ${authRes.user?.displayName}"
                            )
                        }
                    }
                    .addOnFailureListener { err ->
                        Log.e(TAG, "Login Failed with error : ${err.message}")
                    }
                Resource.Success(resultTask.await())
            }
        }
    }

    override suspend fun register(
        email: String, name: String, password: String
    ): Resource<AuthResult> {
        return withContext(Dispatchers.IO) {
            executeWithTryCatch {
                val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                val userId = result.user?.uid!!
                val user = User(userId = userId, name = name)
                existingUsers.document(userId).set(user)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Log.i(TAG, "Register: User registered successfully!")
                        }
                    }
                Resource.Success(result)
            }
        }
    }
}