/*
 * CClaude Bridge Implementation
 * Connects to libcclaude.so via JNI
 * 更新时间: 2026-03-17
 */

#include "cclaude_jni.h"
#include <dlfcn.h>
#include <android/log.h>

#define LOG_TAG "CClaudeBridge"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

namespace cclaude {

// Function pointers for libcclaude
typedef cclaude_ctx_t* (*cclaude_init_fn)(const char* api_key, const char* base_url);
typedef void (*cclaude_deinit_fn)(cclaude_ctx_t* ctx);
typedef int (*cclaude_chat_fn)(cclaude_ctx_t* ctx, const char* message, char* response, size_t resp_size);
typedef const char* (*cclaude_get_version_fn)(void);

static void* g_libcclaude = nullptr;
static cclaude_init_fn g_cclaude_init = nullptr;
static cclaude_deinit_fn g_cclaude_deinit = nullptr;
static cclaude_chat_fn g_cclaude_chat = nullptr;
static cclaude_get_version_fn g_cclaude_version = nullptr;

// Load libcclaude.so dynamically
static bool loadCClaudeLibrary() {
    if (g_libcclaude) return true;
    
    g_libcclaude = dlopen("libcclaude.so", RTLD_LAZY);
    if (!g_libcclaude) {
        LOGE("Failed to load libcclaude.so: %s", dlerror());
        return false;
    }
    
    g_cclaude_init = (cclaude_init_fn)dlsym(g_libcclaude, "cclaude_init_simple");
    g_cclaude_deinit = (cclaude_deinit_fn)dlsym(g_libcclaude, "cclaude_deinit");
    g_cclaude_chat = (cclaude_chat_fn)dlsym(g_libcclaude, "cclaude_chat");
    g_cclaude_version = (cclaude_get_version_fn)dlsym(g_libcclaude, "cclaude_get_version");
    
    if (!g_cclaude_init || !g_cclaude_deinit) {
        LOGE("Failed to find required symbols");
        dlclose(g_libcclaude);
        g_libcclaude = nullptr;
        return false;
    }
    
    LOGD("libcclaude.so loaded successfully");
    return true;
}

CClaudeBridge::CClaudeBridge() : ctx_(nullptr), initialized_(false), streaming_(false) {
    loadCClaudeLibrary();
}

CClaudeBridge::~CClaudeBridge() {
    shutdown();
}

bool CClaudeBridge::initialize(const std::string& apiKey,
                                const std::string& baseUrl,
                                const std::string& model,
                                int approvalMode) {
    if (initialized_) {
        shutdown();
    }
    
    if (!g_cclaude_init) {
        LOGE("Library not loaded");
        return false;
    }
    
    apiKey_ = apiKey;
    baseUrl_ = baseUrl;
    model_ = model;
    
    // Initialize context with API key
    ctx_ = g_cclaude_init(apiKey.c_str(), baseUrl.c_str());
    if (!ctx_) {
        LOGE("Failed to initialize CClaude context");
        return false;
    }
    
    initialized_ = true;
    LOGD("CClaude initialized successfully");
    return true;
}

void CClaudeBridge::shutdown() {
    if (ctx_ && g_cclaude_deinit) {
        g_cclaude_deinit(ctx_);
        ctx_ = nullptr;
    }
    initialized_ = false;
    streaming_ = false;
}

std::string CClaudeBridge::sendMessage(const std::string& message) {
    if (!initialized_ || !ctx_ || !g_cclaude_chat) {
        return "Error: Not initialized";
    }
    
    char response[65536] = {0};
    int result = g_cclaude_chat(ctx_, message.c_str(), response, sizeof(response));
    
    if (result < 0) {
        return "Error: Failed to get response";
    }
    
    return std::string(response);
}

void CClaudeBridge::sendMessageStreaming(const std::string& message, StreamCallback callback) {
    if (!initialized_ || !ctx_) {
        callback("Error: Not initialized");
        return;
    }
    
    streaming_ = true;
    
    // For streaming, we'll accumulate in chunks
    // This is a simplified implementation
    std::string response = sendMessage(message);
    
    // Simulate streaming by breaking into chunks
    const size_t chunkSize = 20;
    for (size_t i = 0; i < response.length() && streaming_; i += chunkSize) {
        size_t len = std::min(chunkSize, response.length() - i);
        callback(response.substr(i, len));
    }
    
    streaming_ = false;
}

void CClaudeBridge::cancelStream() {
    streaming_ = false;
}

bool CClaudeBridge::saveToMemory(const std::string& key, const std::string& value) {
    // Placeholder - would call actual memory functions
    LOGD("Save to memory: %s", key.c_str());
    return true;
}

std::string CClaudeBridge::retrieveFromMemory(const std::string& key) {
    // Placeholder
    return "";
}

void CClaudeBridge::clearMemory() {
    LOGD("Clear memory");
}

std::string CClaudeBridge::executeTool(const std::string& toolName, const std::string& params) {
    LOGD("Execute tool: %s", toolName.c_str());
    return "Tool execution placeholder";
}

std::vector<std::string> CClaudeBridge::getAvailableTools() {
    return {"web_search", "file_read", "file_write", "code_execute"};
}

void CClaudeBridge::setApprovalMode(int mode) {
    LOGD("Set approval mode: %d", mode);
}

void CClaudeBridge::setMaxTokens(int maxTokens) {
    LOGD("Set max tokens: %d", maxTokens);
}

void CClaudeBridge::setTemperature(float temperature) {
    LOGD("Set temperature: %.2f", temperature);
}

std::string CClaudeBridge::getStatus() {
    if (!initialized_) {
        return "Not initialized";
    }
    return "Ready";
}

std::string CClaudeBridge::getVersion() {
    if (g_cclaude_version) {
        return g_cclaude_version();
    }
    return "0.5.0-android";
}

int CClaudeBridge::approvalCallback(cclaude_ctx_t* ctx, const char* action, const char* params) {
    // Return 1 to allow, 0 to deny
    LOGD("Approval request: %s", action);
    return 1;
}

} // namespace cclaude
