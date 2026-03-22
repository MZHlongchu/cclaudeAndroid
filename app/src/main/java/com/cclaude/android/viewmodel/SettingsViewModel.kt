package com.cclaude.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cclaude.android.data.ApprovalMode
import com.cclaude.android.data.SettingsRepository
import com.cclaude.android.data.ThemeMode
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {
    
    private val settings = SettingsRepository.getInstance()
    
    val apiKey: StateFlow<String> = settings.apiKey.stateIn(
        viewModelScope, SharingStarted.Lazily, ""
    )
    
    val baseUrl: StateFlow<String> = settings.baseUrl.stateIn(
        viewModelScope, SharingStarted.Lazily, SettingsRepository.Defaults.BASE_URL
    )
    
    val model: StateFlow<String> = settings.model.stateIn(
        viewModelScope, SharingStarted.Lazily, SettingsRepository.Defaults.MODEL
    )
    
    val approvalMode: StateFlow<ApprovalMode> = settings.approvalMode.map { 
        ApprovalMode.fromValue(it) 
    }.stateIn(viewModelScope, SharingStarted.Lazily, ApprovalMode.CAUTIOUS)
    
    val maxTokens: StateFlow<Int> = settings.maxTokens.stateIn(
        viewModelScope, SharingStarted.Lazily, SettingsRepository.Defaults.MAX_TOKENS
    )
    
    val temperature: StateFlow<Float> = settings.temperature.stateIn(
        viewModelScope, SharingStarted.Lazily, SettingsRepository.Defaults.TEMPERATURE
    )
    
    val themeMode: StateFlow<ThemeMode> = settings.themeMode.map { 
        ThemeMode.fromValue(it) 
    }.stateIn(viewModelScope, SharingStarted.Lazily, ThemeMode.SYSTEM)
    
    fun updateApiKey(key: String) {
        viewModelScope.launch {
            settings.setApiKey(key)
        }
    }
    
    fun updateBaseUrl(url: String) {
        viewModelScope.launch {
            settings.setBaseUrl(url)
        }
    }
    
    fun updateModel(model: String) {
        viewModelScope.launch {
            settings.setModel(model)
        }
    }
    
    fun updateApprovalMode(mode: ApprovalMode) {
        viewModelScope.launch {
            settings.setApprovalMode(mode.value)
        }
    }
    
    fun updateMaxTokens(tokens: Int) {
        viewModelScope.launch {
            settings.setMaxTokens(tokens)
        }
    }
    
    fun updateTemperature(temp: Float) {
        viewModelScope.launch {
            settings.setTemperature(temp)
        }
    }
    
    fun updateThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            settings.setThemeMode(mode.value)
        }
    }
    
    fun getVersion(): String {
        return com.cclaude.android.data.ChatRepository.getInstance().getVersion()
    }
}
