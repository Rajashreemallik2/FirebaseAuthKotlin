package com.example.firebaseauthkotlin.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.firebaseauthkotlin.R
import com.example.firebaseauthkotlin.utilities.EventObserver
import com.example.firebaseauthkotlin.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_sign_up.cirSignUpButton
import kotlinx.android.synthetic.main.activity_sign_up.editTextEmail
import kotlinx.android.synthetic.main.activity_sign_up.editTextName
import kotlinx.android.synthetic.main.activity_sign_up.editTextPassword

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        subscribeToObservers()

        cirSignUpButton.setOnClickListener {
            viewModel.register(
                editTextEmail.text.toString().trim(),
                editTextName.text.toString().trim(),
                editTextPassword.text.toString().trim()
            )
        }
    }

    private fun subscribeToObservers() {
        viewModel.registerStatus.observe(this, EventObserver(
            onError = {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            },
            onLoading = {
                Toast.makeText(this, "Loading", Toast.LENGTH_LONG).show()
            }
        ) {
            Toast.makeText(this, "User registered successfully! " + it.user, Toast.LENGTH_LONG)
                .show()
        })
    }
}