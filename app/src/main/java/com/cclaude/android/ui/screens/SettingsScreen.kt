package com.cclaude.android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
nimport androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cclaude.android.data.ApprovalMode
import com.cclaude.android.data.ThemeMode
import com.cclaude.android.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = viewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    
    val apiKey by viewModel.apiKey.collectAsStateWithLifecycle()
    val baseUrl by viewModel.baseUrl.collectAsStateWithLifecycle()
    val model by viewModel.model.collectAsStateWithLifecycle()
    val approvalMode by viewModel.approvalMode.collectAsStateWithLifecycle()
    val maxTokens by viewModel.maxTokens.collectAsStateWithLifecycle()
    val temperature by viewModel.temperature.collectAsStateWithLifecycle()
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    
    var apiKeyInput by remember { mutableStateOf(apiKey) }
    var baseUrlInput by remember { mutableStateOf(baseUrl) }
    var modelInput by remember { mutableStateOf(model) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("设置") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // API Section
            SectionTitle("API 设置")
            
            OutlinedTextField(
                value = apiKeyInput,
                onValueChange = { 
                    apiKeyInput = it
                    viewModel.updateApiKey(it)
                },
                label = { Text("API Key") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            
            OutlinedTextField(
                value = baseUrlInput,
                onValueChange = { 
                    baseUrlInput = it
                    viewModel.updateBaseUrl(it)
                },
                label = { Text("Base URL") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            
            OutlinedTextField(
                value = modelInput,
                onValueChange = { 
                    modelInput = it
                    viewModel.updateModel(it)
                },
                label = { Text("模型") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            
            // Approval Mode
            SectionTitle("安全设置")
            
            Text("审批模式", style = MaterialTheme.typography.bodyMedium)
            ApprovalMode.entries.forEach { mode ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(mode.displayName)
                    RadioButton(
                        selected = approvalMode == mode,
                        onClick = { viewModel.updateApprovalMode(mode) }
                    )
                }
            }
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            
            // Model Parameters
            SectionTitle("模型参数")
            
            Text("Max Tokens: $maxTokens")
            Slider(
                value = maxTokens.toFloat(),
                onValueChange = { viewModel.updateMaxTokens(it.toInt()) },
                valueRange = 1024f..8192f,
                steps = 7
            )
            
            Text("Temperature: %.1f".format(temperature))
            Slider(
                value = temperature,
                onValueChange = { viewModel.updateTemperature(it) },
                valueRange = 0f..1f,
                steps = 9
            )
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            
            // Appearance
            SectionTitle("外观")
            
            Text("主题")
            ThemeMode.entries.forEach { mode ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(mode.displayName)
                    RadioButton(
                        selected = themeMode == mode,
                        onClick = { viewModel.updateThemeMode(mode) }
                    )
                }
            }
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            
            // About
            SectionTitle("关于")
            Text("CClaude v${viewModel.getVersion()}")
            Text("基于 libcclaude.so 构建", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary
    )
}
