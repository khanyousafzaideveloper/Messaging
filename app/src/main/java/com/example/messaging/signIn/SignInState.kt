package com.example.civicengagementplatform.signIn

data class SignInState(
    val isSignInSuccessful:Boolean = false,
    val signInError: String? = null
)
