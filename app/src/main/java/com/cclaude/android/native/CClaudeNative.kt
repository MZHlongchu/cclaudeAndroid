package com.cclaude.android.native

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CClaudeNative {
    
    companion object {
        private const val TAG = "CClaudeNative"
        
        init {
            try {
                System.loadLibrary("cclaude-jni")
                Log.d(TAG, "Native library loaded")
            } catch (e: UnsatisfiedLinkError) {
                Log.e(TAG, "Failed to load native library", e)
                throw e
            }
        }
    }
    
    private var nativeHandle: Long = 0
    
    interface StreamCallback {
        fun onChunk(chunk: String)
    }
    
    fun initialize(): Boolean {
        nativeHandle = createContext()
        return nativeHandle != 0L
    }
    
    fun destroy() {
        if (nativeHandle != 0L) {
            destroyContext(nativeHandle)
            nativeHandle = 0
        }
    }
    
    fun configure(apiKey: String, baseUrl: String, model: String, approvalMode: Int): Boolean {
        return initialize(nativeHandle, apiKey, baseUrl, model, approvalMode)
    }
    
    fun sendMessage(message: String): String {
        return sendMessage(nativeHandle, message)
    }
    
    fun sendMessageStreaming(message: String): Flow<String> = flow {
        val callback = object : StreamCallback {
            override fun onChunk(chunk: String) {
                // Note: This is simplified - real implementation needs thread safety
            }
        }
        sendMessageStreaming(nativeHandle, message, callback)
    }.flowOn(Dispatchers.IO)
    
    fun cancelStream() {
        cancelStream(nativeHandle)
    }
    
    fun saveToMemory(key: String, value: String): Boolean {
        return saveToMemory(nativeHandle, key, value)
    }
    
    fun retrieveFromMemory(key: String): String {
        return retrieveFromMemory(nativeHandle, key)
    }
    
    fun clearMemory() {
        clearMemory(nativeHandle)
    }
    
    fun getStatus(): String {
        return getStatus(nativeHandle)
    }
    
    fun getVersion(): String {
        return getVersion()
    }
    
    // Native methods
    private external fun createContext(): Long
    private external fun destroyContext(handle: Long)
    private external fun initialize(handle: Long, apiKey: String, baseUrl: String, model: String, approvalMode: Int): Boolean
    private external fun sendMessage(handle: Long, message: String): String
    private external fun sendMessageStreaming(handle: Long, message: String, callback: StreamCallback)
    private external fun cancelStream(handle: Long)
    private external fun saveToMemory(handle: Long, key: String, value: String): Boolean
    private external fun retrieveFromMemory(handle: Long, key: String): String
    private external fun clearMemory(handle: Long)
    private external fun getStatus(handle: Long): String
    private external fun getVersion(): String
}
