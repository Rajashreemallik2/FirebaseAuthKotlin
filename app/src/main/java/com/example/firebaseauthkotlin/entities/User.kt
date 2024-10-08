package com.example.firebaseauthkotlin.entities

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val userId: String = "",
    val name: String = ""
)
