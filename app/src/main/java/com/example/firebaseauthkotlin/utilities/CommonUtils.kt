package com.example.firebaseauthkotlin.utilities

import android.app.Activity
import android.widget.Toast

inline fun <T> executeWithTryCatch(action: () -> Resource<T>): Resource<T> {
    return try {
        action()
    } catch (e: Exception) {
        Resource.Error(e.message ?: "An unknown error occurred")
    }
}

inline fun <T> loginEventObserver(activity: Activity): EventObserver<T> {
    return EventObserver(
        onError = {
            Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
        },
        onLoading = {
            Toast.makeText(activity, "Loading", Toast.LENGTH_LONG).show()
        }
    ) {
        Toast.makeText(activity, "Login Successful", Toast.LENGTH_LONG).show()
    }
}