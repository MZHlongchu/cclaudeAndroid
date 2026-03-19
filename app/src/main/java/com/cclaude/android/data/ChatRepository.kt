package com.cclaude.android.data

import com.cclaude.android.native.CClaudeNative
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.util.UUID

class ChatRepository private constructor() {
    
    private val cclaudeNative = CClaudeNative()
    private val conversations = mutableListOf<Conversation>()
    private val currentConversationId = MutableStateFlow<String?>(null)
    
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages = _messages.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    
    companion object {
        @Volatile
        private var INSTANCE: ChatRepository? = null
        
        fun getInstance(): ChatRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ChatRepository().also { INSTANCE = it }
            }
        }
    }
    
    suspend fun initialize(): Boolean = withContext(Dispatchers.IO) {
        val settings = SettingsRepository.getInstance()
        val apiKey = settings.getApiKeyBlocking()
        
        if (apiKey.isBlank()) {
            return@withContext false
        }
        
        if (!cclaudeNative.initialize()) {
            return@withContext false
        }
        
        val baseUrl = settings.baseUrl.toString()
        val model = settings.model.toString()
        val approvalMode = settings.approvalMode.toString().toIntOrNull() ?: 1
        
        return@withContext cclaudeNative.configure(apiKey, baseUrl, model, approvalMode)
    }
    
    suspend fun sendMessage(content: String): Result<String> = withContext(Dispatchers.IO) {
        _isLoading.value = true
        
        try {
            // Add user message
            val userMessage = ChatMessage(
                id = UUID.randomUUID().toString(),
                role = MessageRole.USER,
                content = content
            )
            addMessage(userMessage)
            
            // Send to CClaude
            val response = cclaudeNative.sendMessage(content)
            
            // Add assistant message
            val assistantMessage = ChatMessage(
                id = UUID.randomUUID().toString(),
                role = MessageRole.ASSISTANT,
                content = response
            )
            addMessage(assistantMessage)
            
            Result.success(response)
        } catch (e: Exception) {
            val errorMessage = ChatMessage(
                id = UUID.randomUUID().toString(),
                role = MessageRole.SYSTEM,
                content = "Error: ${e.message}",
                isError = true
            )
            addMessage(errorMessage)
            Result.failure(e)
        } finally {
            _isLoading.value = false
        }
    }
    
    fun sendMessageStreaming(content: String): Flow<String> {
        // Implementation for streaming
        return cclaudeNative.sendMessageStreaming(content)
    }
    
    private fun addMessage(message: ChatMessage) {
        val current = _messages.value.toMutableList()
        current.add(message)
        _messages.value = current
    }
    
    fun clearMessages() {
        _messages.value = emptyList()
        cclaudeNative.clearMemory()
    }
    
    fun getVersion(): String {
        return cclaudeNative.getVersion()
    }
    
    fun getStatus(): String {
        return cclaudeNative.getStatus()
    }
    
    fun destroy() {
        cclaudeNative.destroy()
    }
}
