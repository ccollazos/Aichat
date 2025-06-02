package com.example.aichat

import android.annotation.SuppressLint
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Suppress("PLUGIN_IS_NOT_ENABLED")
@Serializable
data class ChatMessage(
    val text: String,
    val isUser: Boolean
)

@SuppressLint("ParcelCreator")
class ChatViewModel(@SuppressLint("StaticFieldLeak") private val context: Context) : ViewModel(),
    Parcelable {
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    constructor(parcel: Parcel) : this(TODO("context")) {
    }

    init {
        viewModelScope.launch {
            _messages.value = loadMessages(context)
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
            saveMessages(context, _messages.value)
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

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChatViewModel> {
        override fun createFromParcel(parcel: Parcel): ChatViewModel {
            return ChatViewModel(parcel)
        }

        override fun newArray(size: Int): Array<ChatViewModel?> {
            return arrayOfNulls(size)
        }
    }
} 