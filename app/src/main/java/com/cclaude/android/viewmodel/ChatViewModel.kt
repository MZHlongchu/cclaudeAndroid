package com.cclaude.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cclaude.android.data.ChatMessage
import com.cclaude.android.data.ChatRepository
import com.cclaude.android.data.MessageRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    
    private val repository = ChatRepository.getInstance()
    
    val messages: StateFlow<List<ChatMessage>> = repository.messages
    val isLoading: StateFlow<Boolean> = repository.isLoading
    
    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized.asStateFlow()
    
    init {
        viewModelScope.launch {
            _isInitialized.value = repository.initialize()
        }
    }
    
    fun updateInput(text: String) {
        _inputText.value = text
    }
    
    fun sendMessage() {
        val text = _inputText.value.trim()
        if (text.isBlank() || isLoading.value) return
        
        viewModelScope.launch {
            _inputText.value = ""
            repository.sendMessage(text)
                .onFailure { error ->
                    _errorMessage.value = error.message
                }
        }
    }
    
    fun clearChat() {
        repository.clearMessages()
    }
    
    fun dismissError() {
        _errorMessage.value = null
    }
    
    fun retryInitialization() {
        viewModelScope.launch {
            _isInitialized.value = repository.initialize()
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        repository.destroy()
    }
}
