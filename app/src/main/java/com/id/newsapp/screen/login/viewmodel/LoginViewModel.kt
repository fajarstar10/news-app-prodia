package com.id.newsapp.screen.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.id.domain.repository.UserRepository
import com.id.newsapp.screen.login.viewmodel.event.LoginEvent
import com.id.newsapp.screen.login.viewmodel.state.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                _state.update { it.copy(email = event.value, error = null) }
            }

            is LoginEvent.PasswordChanged -> {
                _state.update { it.copy(password = event.value, error = null) }
            }

            is LoginEvent.Submit -> {
                login()
            }

            is LoginEvent.ResetLoginStatus -> {
                _state.update { it.copy(isLoggedIn = false) }
            }
        }
    }

    private fun login() {
        val email = _state.value.email.trim()
        val password = _state.value.password.trim()

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val registeredEmail = repository.isUserRegistered(email)
            val storedPassword = repository.getPassword()

            if (!registeredEmail) {
                _state.update { it.copy(isLoading = false, error = "Akun tidak ditemukan") }
                return@launch
            }

            if (password != storedPassword) {
                _state.update { it.copy(isLoading = false, error = "Password salah") }
                return@launch
            }

            _state.update { it.copy(isLoading = false, isLoggedIn = true) }
        }
    }
}