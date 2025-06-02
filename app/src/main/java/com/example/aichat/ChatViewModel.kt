package com.example.aichat

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlin.text.Typography.dagger

@Suppress("PLUGIN_IS_NOT_ENABLED")
@Serializable
data class ChatMessage(
    val text: String,
    val isUser: Boolean
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val app: Application
) : ViewModel() {
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    init {
        viewModelScope.launch {
            _messages.value = loadMessages(app)
        }
    }

    fun sendMessage(message: String) {
        if (message.isBlank()) return
        val currentMessages = _messages.value.toMutableList()
        currentMessages.add(ChatMessage(message, true))
        _messages.value = currentMessages
        save()
        // Simular respuesta de la IA
        viewModelScope.launch {
            simulateAIResponse(message)
        }
    }

    private suspend fun simulateAIResponse(userMessage: String) {
        delay(1000)
        val currentMessages = _messages.value.toMutableList()
        currentMessages.add(ChatMessage("Respuesta IA: $userMessage", false))
        _messages.value = currentMessages
        save()
    }

    private fun save() {
        viewModelScope.launch {
            saveMessages(app, _messages.value)
        }
    }
} 