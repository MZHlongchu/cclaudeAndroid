package com.cclaude.android.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "cclaude_settings")

class SettingsRepository private constructor(context: Context) {
    
    private val dataStore = context.dataStore
    
    companion object {
        @Volatile
        private var INSTANCE: SettingsRepository? = null
        
        fun initialize(context: Context) {
            INSTANCE = SettingsRepository(context)
        }
        
        fun getInstance(): SettingsRepository {
            return INSTANCE ?: throw IllegalStateException("SettingsRepository not initialized")
        }
    }
    
    private object Keys {
        val API_KEY = stringPreferencesKey("api_key")
        val BASE_URL = stringPreferencesKey("base_url")
        val MODEL = stringPreferencesKey("model")
        val APPROVAL_MODE = intPreferencesKey("approval_mode")
        val MAX_TOKENS = intPreferencesKey("max_tokens")
        val TEMPERATURE = floatPreferencesKey("temperature")
        val THEME_MODE = intPreferencesKey("theme_mode")
    }
    
    object Defaults {
        const val BASE_URL = "https://api.anthropic.com"
        const val MODEL = "claude-sonnet-4-20250514"
        const val APPROVAL_MODE = 1
        const val MAX_TOKENS = 4096
        const val TEMPERATURE = 0.7f
        const val THEME_MODE = 0
    }
    
    val apiKey: Flow<String> = dataStore.data.map { it[Keys.API_KEY] ?: "" }
    suspend fun setApiKey(key: String) = dataStore.edit { it[Keys.API_KEY] = key }
    fun getApiKeyBlocking(): String = runBlocking { apiKey.first() }
    
    val baseUrl: Flow<String> = dataStore.data.map { it[Keys.BASE_URL] ?: Defaults.BASE_URL }
    suspend fun setBaseUrl(url: String) = dataStore.edit { it[Keys.BASE_URL] = url }
    
    val model: Flow<String> = dataStore.data.map { it[Keys.MODEL] ?: Defaults.MODEL }
    suspend fun setModel(model: String) = dataStore.edit { it[Keys.MODEL] = model }
    
    val approvalMode: Flow<Int> = dataStore.data.map { it[Keys.APPROVAL_MODE] ?: Defaults.APPROVAL_MODE }
    suspend fun setApprovalMode(mode: Int) = dataStore.edit { it[Keys.APPROVAL_MODE] = mode }
    
    val maxTokens: Flow<Int> = dataStore.data.map { it[Keys.MAX_TOKENS] ?: Defaults.MAX_TOKENS }
    suspend fun setMaxTokens(tokens: Int) = dataStore.edit { it[Keys.MAX_TOKENS] = tokens }
    
    val temperature: Flow<Float> = dataStore.data.map { it[Keys.TEMPERATURE] ?: Defaults.TEMPERATURE }
    suspend fun setTemperature(temp: Float) = dataStore.edit { it[Keys.TEMPERATURE] = temp }
    
    val themeMode: Flow<Int> = dataStore.data.map { it[Keys.THEME_MODE] ?: Defaults.THEME_MODE }
    suspend fun setThemeMode(mode: Int) = dataStore.edit { it[Keys.THEME_MODE] = mode }
}

enum class ApprovalMode(val value: Int, val displayName: String) {
    AUTO(0, "自动"),
    CAUTIOUS(1, "谨慎"),
    STRICT(2, "严格"),
    YOLO(3, "YOLO");
    
    companion object {
        fun fromValue(value: Int): ApprovalMode = values().find { it.value == value } ?: CAUTIOUS
    }
}

enum class ThemeMode(val value: Int, val displayName: String) {
    SYSTEM(0, "跟随系统"),
    LIGHT(1, "浅色"),
    DARK(2, "深色");
    
    companion object {
        fun fromValue(value: Int): ThemeMode = values().find { it.value == value } ?: SYSTEM
    }
}
