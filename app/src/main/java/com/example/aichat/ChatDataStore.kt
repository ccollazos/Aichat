package com.example.aichat

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val Context.dataStore by preferencesDataStore(name = "chat_prefs")

object ChatPrefsKeys {
    val MESSAGES = stringPreferencesKey("messages")
}

suspend fun saveMessages(context: Context, messages: List<ChatMessage>) {
    val json = try {
        Json.encodeToString(messages)
    } catch (e: Exception) {
        "[]"
    }
    context.dataStore.edit { prefs ->
        prefs[ChatPrefsKeys.MESSAGES] = json
    }
}

suspend fun loadMessages(context: Context): List<ChatMessage> {
    val prefs = context.dataStore.data.first()
    val json = prefs[ChatPrefsKeys.MESSAGES] ?: return emptyList()
    return try {
        Json.decodeFromString(json)
    } catch (e: Exception) {
        emptyList()
    }
} 