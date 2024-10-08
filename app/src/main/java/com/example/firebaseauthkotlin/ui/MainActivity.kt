package com.example.firebaseauthkotlin.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseauthkotlin.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find the root view of the activity
        val button = findViewById<Button>(R.id.helloButton)

        // Set an OnClickListener on the root view
        button.setOnClickListener {
            // Create an Intent to start SecondActivity
            val intent = Intent(this, LoginActivity::class.java)
            // Start SecondActivity
            startActivity(intent)
        }
    }
}