package com.example.aichat

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChatMessage(
    val text: String,
    val isUser: Boolean
)

class ChatViewModel(private val context: Context) : ViewModel() {
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                _messages.value = loadMessages(context)
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error loading messages", e)
                _messages.value = emptyList()
            }
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
        kotlinx.coroutines.delay(1000)
        val currentMessages = _messages.value.toMutableList()
        currentMessages.add(ChatMessage("Respuesta IA: $userMessage", false))
        _messages.value = currentMessages
        save()
    }

    private fun save() {
        viewModelScope.launch {
            try {
                saveMessages(context, _messages.value)
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error saving messages", e)
            }
        }
    }

    companion object {
        fun provideFactory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return ChatViewModel(context.applicationContext) as T
                }
            }
    }
} 