package com.id.newsapp.screen.register.viewmodel.event

sealed class RegisterEvent {
    data class EmailChanged(val email: String) : RegisterEvent()
    data class PasswordChanged(val password: String) : RegisterEvent()
    data object Submit : RegisterEvent()
    data object Reset : RegisterEvent()
}