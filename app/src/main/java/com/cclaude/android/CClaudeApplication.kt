package com.cclaude.android

import android.app.Application
import android.content.Context
import com.cclaude.android.data.SettingsRepository

class CClaudeApplication : Application() {
    
    companion object {
        lateinit var context: Context
            private set
    }
    
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        SettingsRepository.initialize(context)
    }
}
