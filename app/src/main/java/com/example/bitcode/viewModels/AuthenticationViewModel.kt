package com.example.bitcode.viewModels

import androidx.lifecycle.ViewModel
import com.example.bitcode.googleAuth.SignInResult
import com.example.bitcode.googleAuth.SignInState
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AuthenticationViewModel: ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()
    private val _isVerified = MutableStateFlow(false)
    val isVerified = _isVerified.asStateFlow()

    fun onSignInResultOneTap(result: SignInResult) {
        _state.update { it.copy(
            isSignInSuccessful = result.data != null,
            signInError = result.errorMessage
        ) }
    }

    fun onSignInResult(result: GoogleSignInAccount) {
        _state.update { it.copy(
            isSignInSuccessful = result.account != null,
            signInError = ""
        ) }
    }


    fun onSignInResultFacebook(result: AuthResult) {
        _state.update { it.copy(
            isSignInSuccessful = result.user != null,
            signInError = "Error"
        ) }
    }
    fun onSignInResultGithub(result: AuthResult) {
        _state.update { it.copy(
            isSignInSuccessful = result.user != null,
            signInError = "Error"
        ) }
    }
    fun onSignInResultNative(result: AuthResult) {
        _state.update { it.copy(
            isSignInSuccessful = result.user != null,
            signInError = "Error"
        ) }
    }

    fun resetState() {
        _state.update { SignInState() }
    }

    fun updateVerificationState(value: Boolean) {
        _isVerified.update { value }
    }
}