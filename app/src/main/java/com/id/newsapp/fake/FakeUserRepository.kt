package com.id.newsapp.fake

import com.id.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeUserRepository : UserRepository {

    private var storedEmail: String? = null
    private var storedPassword: String? = null
    private val isLoggedInFlow = MutableStateFlow(false)
    private val emailFlow = MutableStateFlow("")

    override suspend fun register(email: String, password: String): Boolean {
        storedEmail = email
        storedPassword = password
        emailFlow.value = email
        return true
    }

    override suspend fun isUserRegistered(email: String): Boolean {
        return storedEmail == email
    }

    override suspend fun getPassword(): String? {
        return storedPassword
    }

    override suspend fun clearUser() {
        storedEmail = null
        storedPassword = null
        isLoggedInFlow.value = false
        emailFlow.value = ""
    }

    override suspend fun setLoggedIn(value: Boolean) {
        isLoggedInFlow.value = value
    }

    override fun isLoggedIn(): Flow<Boolean> = isLoggedInFlow

    override fun getEmail(): Flow<String> = emailFlow

    fun setEmail(email: String) {
        storedEmail = email
        emailFlow.value = email
    }
}