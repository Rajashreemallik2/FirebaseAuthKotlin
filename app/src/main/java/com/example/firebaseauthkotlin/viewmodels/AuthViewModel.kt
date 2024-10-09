package com.example.firebaseauthkotlin.viewmodels

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseauthkotlin.R
import com.example.firebaseauthkotlin.repositories.AuthRepository
import com.example.firebaseauthkotlin.utilities.Event
import com.example.firebaseauthkotlin.utilities.Resource
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val applicationContext: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    private val _loginStatus = MutableLiveData<Event<Resource<AuthResult>>>()
    val loginStatus: LiveData<Event<Resource<AuthResult>>> = _loginStatus

    private val _loginWithGoogleStatus = MutableLiveData<Event<Resource<AuthResult>>>()
    val loginWithGoogleStatus: LiveData<Event<Resource<AuthResult>>> = _loginWithGoogleStatus

    private val _registerStatus = MutableLiveData<Event<Resource<AuthResult>>>()
    val registerStatus: LiveData<Event<Resource<AuthResult>>> = _registerStatus

    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            val error = applicationContext.getString(R.string.error_input_empty)
            _loginStatus.postValue(Event(Resource.Error(error)))
        } else {
            _loginStatus.postValue(Event(Resource.Loading()))
            viewModelScope.launch(dispatcher) {
                val result = repository.login(email, password)
                _loginStatus.postValue(Event(result))
            }
        }
    }

    fun loginWithGoogle(account: GoogleSignInAccount?) {
        _loginWithGoogleStatus.postValue(Event(Resource.Loading()))
        viewModelScope.launch(dispatcher) {
            val result = repository.loginWithGoogle(account)
            _loginWithGoogleStatus.postValue(Event(result))
        }
    }

    fun register(email: String, name: String, password: String) {
        val error =
            if (email.isEmpty() || name.isEmpty() || password.isEmpty()) {
                applicationContext.getString(R.string.error_input_empty)
            } else if (password.length < 8) {
                applicationContext.getString(R.string.error_password_short)
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                applicationContext.getString(R.string.error_invalid_email)
            } else null

        error?.let {
            _registerStatus.postValue(Event(Resource.Error(it)))
            return
        }

        _registerStatus.postValue(Event(Resource.Loading()))
        viewModelScope.launch(dispatcher) {
            val result = repository.register(email, name, password)
            _registerStatus.postValue(Event(result))
        }
    }
}