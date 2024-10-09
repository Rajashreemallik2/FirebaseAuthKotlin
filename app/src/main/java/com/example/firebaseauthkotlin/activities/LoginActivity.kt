package com.example.firebaseauthkotlin.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.firebaseauthkotlin.R
import com.example.firebaseauthkotlin.utilities.loginEventObserver
import com.example.firebaseauthkotlin.viewmodels.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
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

        val googleSignIn = findViewById<ImageView>(R.id.googleSignIn)
        googleSignIn.setOnClickListener {
            val intent = googleSignInClientIntent()
            googleSingInActivityLauncher.launch(intent)
        }
    }

    private val googleSingInActivityLauncher = initiateGoogleSignInActivity()

    private fun initiateGoogleSignInActivity(): ActivityResultLauncher<Intent> {
        return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                Log.d(TAG, "onActivityResult : ${result.data!!.extras}")
                val accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = accountTask.getResult(ApiException::class.java)
                    viewModel.loginWithGoogle(account)
                } catch (e: ApiException) {
                    Log.w(TAG, "onActivityResult : ${e.message}")
                }
            } else {
                Log.w(TAG, "onActivityResult : ${result.data}")
            }
        }
    }

    private fun googleSignInClientIntent(): Intent {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(this, googleSignInOptions).signInIntent
    }

    private fun subscribeToObservers() {
        viewModel.loginStatus.observe(this, loginEventObserver(this))
        viewModel.loginWithGoogleStatus.observe(this, loginEventObserver(this))
    }
}