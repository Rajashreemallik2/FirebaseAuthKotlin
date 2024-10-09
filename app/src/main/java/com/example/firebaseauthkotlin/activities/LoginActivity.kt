package com.example.firebaseauthkotlin.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.firebaseauthkotlin.R
import com.example.firebaseauthkotlin.utilities.EventObserver
import com.example.firebaseauthkotlin.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.editTextEmail
import kotlinx.android.synthetic.main.activity_login.editTextPassword

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        subscribeToObservers()

        val button = findViewById<Button>(R.id.cirLoginButton)
        button.setOnClickListener {
            viewModel.login(
                editTextEmail.text.toString().trim(), editTextPassword.text.toString().trim()
            )
        }

        val signUpBtn = findViewById<TextView>(R.id.signupTextLink)
        signUpBtn.setOnClickListener {
            intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
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