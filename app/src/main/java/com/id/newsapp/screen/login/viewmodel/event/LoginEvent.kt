package com.id.newsapp.screen.login.viewmodel.event

sealed class LoginEvent {
    data class EmailChanged(val value: String) : LoginEvent()
    data class PasswordChanged(val value: String) : LoginEvent()
    data object Submit : LoginEvent()
    data object ResetLoginStatus : LoginEvent()
}