package com.example.firebaseauthkotlin.ui

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.firebaseauthkotlin.R
import com.example.firebaseauthkotlin.utility.EventObserver
import com.example.firebaseauthkotlin.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.editTextEmail
import kotlinx.android.synthetic.main.activity_login.editTextPassword

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Find the root view of the activity
        val button = findViewById<Button>(R.id.cirLoginButton)

        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        subscribeToObservers()

        // Set an OnClickListener on the root view
        button.setOnClickListener {
            // Create an Intent to start SecondActivity
            Toast.makeText(this, "hello", Toast.LENGTH_LONG).show()
            viewModel.login(
                editTextEmail.text.toString().trim(),
                editTextPassword.text.toString().trim()
            )
        }
    }

    private fun subscribeToObservers() {
        viewModel.loginStatus.observe(this, EventObserver(
            onError = {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            },
            onLoading = {
                Toast.makeText(this, "Locating", Toast.LENGTH_LONG).show()
            }
        ) {
            Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show()
        })
    }
}