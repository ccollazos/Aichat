package com.example.aichat

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable
import com.example.aichat.ChatMessage as ChatMessage1

val Context.dataStore by preferencesDataStore(name = "chat_prefs")

object ChatPrefsKeys {
    val MESSAGES = stringPreferencesKey("messages")
}

@Serializable
data class SerializableChatMessage(
    val text: String,
    val isUser: Boolean
)

suspend fun saveMessages(context: Context, messages: List<ChatMessage1>) {
    val serializableList = messages.map { SerializableChatMessage(it.text, it.isUser) }
    val json = Json.encodeToString(serializableList)
    context.dataStore.edit { prefs ->
        prefs[ChatPrefsKeys.MESSAGES] = json
    }
}

suspend fun loadMessages(context: Context): List<ChatMessage1> {
    val prefs = context.dataStore.data.first()
    val json = prefs[ChatPrefsKeys.MESSAGES] ?: return emptyList()
    return try {
        val serializableList = Json.decodeFromString<List<SerializableChatMessage>>(json)
        serializableList.map { ChatMessage1(it.text, it.isUser) }
    } catch (e: Exception) {
        emptyList()
    }
} 