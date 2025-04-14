package com.id.data.repository

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore by preferencesDataStore(name = "app_preferences")

class AppPreferences(private val context: Context) {

    companion object {
        val EMAIL_KEY = stringPreferencesKey("email")
        val PASSWORD_KEY = stringPreferencesKey("password")
        val TOKEN_KEY = stringPreferencesKey("auth_token")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    }

    suspend fun <T> save(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { it[key] = value }
    }

    suspend fun <T> get(key: Preferences.Key<T>, default: T): T {
        return context.dataStore.data.map { it[key] ?: default }.first()
    }

    fun <T> observe(key: Preferences.Key<T>, default: T): Flow<T> {
        return context.dataStore.data.catch { if (it is IOException) emit(emptyPreferences()) else throw it }
            .map { it[key] ?: default }
    }

    suspend fun delete(key: Preferences.Key<*>) {
        context.dataStore.edit { it.remove(key) }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }

    suspend fun setLoggedIn(value: Boolean) {
        save(IS_LOGGED_IN, value)
    }

    fun isLoggedIn(): Flow<Boolean> {
        return observe(IS_LOGGED_IN, false)
    }
}