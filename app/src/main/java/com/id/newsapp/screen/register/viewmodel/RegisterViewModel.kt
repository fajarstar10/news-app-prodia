package com.id.newsapp.screen.register.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.id.domain.repository.UserRepository
import com.id.newsapp.screen.register.viewmodel.event.RegisterEvent
import com.id.newsapp.screen.register.viewmodel.state.RegisterState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.EmailChanged -> {
                _state.update { it.copy(email = event.email, error = null) }
            }

            is RegisterEvent.PasswordChanged -> {
                _state.update { it.copy(password = event.password, error = null) }
            }

            is RegisterEvent.Submit -> {
                register()
            }

            is RegisterEvent.Reset -> {
                _state.update { RegisterState() }
            }
        }
    }

    private fun register() {
        val email = _state.value.email.trim()
        val password = _state.value.password

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            if (repository.isUserRegistered(email)) {
                _state.update { it.copy(isLoading = false, error = "Email sudah terdaftar.") }
                return@launch
            }

            if (password.length < 6) {
                _state.update { it.copy(isLoading = false, error = "Password minimal 6 karakter") }
                return@launch
            }

            val success = repository.register(email, password)
            _state.update {
                it.copy(
                    isLoading = false,
                    isRegistered = success,
                    error = if (success) null else "Gagal mendaftar"
                )
            }
        }
    }
}