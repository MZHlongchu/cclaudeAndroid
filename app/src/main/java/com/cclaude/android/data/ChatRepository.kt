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
        if (apiKey.isBlank()) return@withContext false

        if (!cclaudeNative.initialize()) return@withContext false

        val baseUrl = settings.getBaseUrlBlocking()
        val model = settings.getModelBlocking()
        val approvalMode = settings.getApprovalModeBlocking()

        return@withContext cclaudeNative.configure(apiKey, baseUrl, model, approvalMode)
    }

    suspend fun sendMessage(content: String): Result<String> = withContext(Dispatchers.IO) {
        _isLoading.value = true
        try {
            addMessage(
                ChatMessage(
                    id = UUID.randomUUID().toString(),
                    role = MessageRole.USER,
                    content = content
                )
            )

            val response = cclaudeNative.sendMessage(content)

            addMessage(
                ChatMessage(
                    id = UUID.randomUUID().toString(),
                    role = MessageRole.ASSISTANT,
                    content = response
                )
            )
            Result.success(response)
        } catch (e: Exception) {
            addMessage(
                ChatMessage(
                    id = UUID.randomUUID().toString(),
                    role = MessageRole.SYSTEM,
                    content = "Error: ${e.message}",
                    isError = true
                )
            )
            Result.failure(e)
        } finally {
            _isLoading.value = false
        }
    }

    fun sendMessageStreaming(content: String): Flow<String> = cclaudeNative.sendMessageStreaming(content)

    private fun addMessage(message: ChatMessage) {
        _messages.value = _messages.value.toMutableList().apply { add(message) }
    }

    fun clearMessages() {
        _messages.value = emptyList()
        cclaudeNative.clearMemory()
    }

    fun getVersion(): String = cclaudeNative.getVersion()
    fun getStatus(): String = cclaudeNative.getStatus()
    fun destroy() = cclaudeNative.destroy()
}
