package com.cclaude.android.native

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CClaudeNative {

    companion object {
        private const val TAG = "CClaudeNative"
    }

    private var configured = false
    private var apiKey: String = ""
    private var baseUrl: String = "https://api.anthropic.com"
    private var model: String = "claude-sonnet-4-20250514"
    private var approvalMode: Int = 1
    private val memory = linkedMapOf<String, String>()

    fun initialize(): Boolean {
        runCatching { System.loadLibrary("cclaude") }
            .onSuccess { Log.d(TAG, "libcclaude loaded") }
            .onFailure { Log.w(TAG, "libcclaude not loaded, using Kotlin fallback") }
        return true
    }

    fun destroy() {
        configured = false
        memory.clear()
    }

    fun configure(apiKey: String, baseUrl: String, model: String, approvalMode: Int): Boolean {
        this.apiKey = apiKey
        this.baseUrl = baseUrl
        this.model = model
        this.approvalMode = approvalMode
        configured = apiKey.isNotBlank()
        return configured
    }

    fun sendMessage(message: String): String {
        return if (!configured) {
            "CClaude 尚未完成配置，请先设置 API Key。"
        } else {
            "[CClaude Android]
模型: $model
审批模式: $approvalMode

你说: $message

当前为可构建演示版本，已完成 UI / 数据层 / 打包链路，可继续接入完整 JNI 调用。"
        }
    }

    fun sendMessageStreaming(message: String): Flow<String> = flow {
        val text = sendMessage(message)
        text.chunked(24).forEach {
            emit(it)
            delay(20)
        }
    }.flowOn(Dispatchers.IO)

    fun cancelStream() = Unit

    fun saveToMemory(key: String, value: String): Boolean {
        memory[key] = value
        return true
    }

    fun retrieveFromMemory(key: String): String = memory[key].orEmpty()

    fun clearMemory() {
        memory.clear()
    }

    fun getStatus(): String = if (configured) "Ready" else "Not configured"

    fun getVersion(): String = "0.5.0-android-buildable"
}
