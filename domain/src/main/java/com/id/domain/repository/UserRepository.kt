package com.id.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun register(email: String, password: String): Boolean
    suspend fun isUserRegistered(email: String): Boolean
    suspend fun getPassword(): String?
    suspend fun clearUser()
    suspend fun setLoggedIn(value: Boolean)
    fun isLoggedIn(): Flow<Boolean>
    fun getEmail(): Flow<String>
}