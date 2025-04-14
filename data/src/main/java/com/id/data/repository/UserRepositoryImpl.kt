package com.id.data.repository

import com.id.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(
    private val preferences: AppPreferences
) : UserRepository {

    override suspend fun register(email: String, password: String): Boolean {
        return try {
            preferences.save(AppPreferences.EMAIL_KEY, email)
            preferences.save(AppPreferences.PASSWORD_KEY, password)
            preferences.setLoggedIn(true)
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun isUserRegistered(email: String): Boolean {
        return preferences.get(AppPreferences.EMAIL_KEY, "") == email
    }

    override suspend fun getPassword(): String {
        return preferences.get(AppPreferences.PASSWORD_KEY, "")
    }

    override suspend fun clearUser() {
        preferences.clear()
    }

    override suspend fun setLoggedIn(value: Boolean) {
        preferences.setLoggedIn(value)
    }

    override fun isLoggedIn(): Flow<Boolean> {
        return preferences.isLoggedIn()
    }

    override fun getEmail(): Flow<String> {
        return preferences.observe(AppPreferences.EMAIL_KEY, "")
    }
}