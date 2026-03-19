#ifndef CCLAUDE_JNI_H
#define CCLAUDE_JNI_H

#include <jni.h>
#include <string>
#include <memory>
#include <vector>
#include <functional>

// Forward declaration for CClaude context
typedef struct cclaude_ctx cclaude_ctx_t;

namespace cclaude {

// Message structure
struct ChatMessage {
    std::string id;
    std::string role;  // "user" or "assistant"
    std::string content;
    long long timestamp;
    bool isStreaming;
};

// Callback types
using StreamCallback = std::function<void(const std::string& chunk)>;
using LogCallback = std::function<void(const std::string& level, const std::string& message)>;

// CClaude bridge class
class CClaudeBridge {
public:
    CClaudeBridge();
    ~CClaudeBridge();

    // Initialization
    bool initialize(const std::string& apiKey, 
                    const std::string& baseUrl,
                    const std::string& model,
                    int approvalMode);
    void shutdown();
    bool isInitialized() const { return initialized_; }

    // Chat operations
    std::string sendMessage(const std::string& message);
    void sendMessageStreaming(const std::string& message, StreamCallback callback);
    void cancelStream();

    // Memory operations
    bool saveToMemory(const std::string& key, const std::string& value);
    std::string retrieveFromMemory(const std::string& key);
    void clearMemory();

    // Tools
    std::string executeTool(const std::string& toolName, const std::string& params);
    std::vector<std::string> getAvailableTools();

    // Settings
    void setApprovalMode(int mode);
    void setMaxTokens(int maxTokens);
    void setTemperature(float temperature);

    // Status
    std::string getStatus();
    std::string getVersion();

private:
    cclaude_ctx_t* ctx_;
    bool initialized_;
    bool streaming_;
    std::string apiKey_;
    std::string baseUrl_;
    std::string model_;

    // Native callbacks
    static int approvalCallback(cclaude_ctx_t* ctx, const char* action, const char* params);
};

} // namespace cclaude

// JNI Functions
extern "C" {

JNIEXPORT jlong JNICALL
Java_com_cclaude_android_native_CClaudeNative_createContext(JNIEnv* env, jobject thiz);

JNIEXPORT void JNICALL
Java_com_cclaude_android_native_CClaudeNative_destroyContext(JNIEnv* env, jobject thiz, jlong handle);

JNIEXPORT jboolean JNICALL
Java_com_cclaude_android_native_CClaudeNative_initialize(JNIEnv* env, jobject thiz, jlong handle,
                                                         jstring apiKey, jstring baseUrl, 
                                                         jstring model, jint approvalMode);

JNIEXPORT jstring JNICALL
Java_com_cclaude_android_native_CClaudeNative_sendMessage(JNIEnv* env, jobject thiz, jlong handle,
                                                          jstring message);

JNIEXPORT void JNICALL
Java_com_cclaude_android_native_CClaudeNative_sendMessageStreaming(JNIEnv* env, jobject thiz, 
                                                                   jlong handle, jstring message,
                                                                   jobject callback);

JNIEXPORT void JNICALL
Java_com_cclaude_android_native_CClaudeNative_cancelStream(JNIEnv* env, jobject thiz, jlong handle);

JNIEXPORT jboolean JNICALL
Java_com_cclaude_android_native_CClaudeNative_saveToMemory(JNIEnv* env, jobject thiz, jlong handle,
                                                           jstring key, jstring value);

JNIEXPORT jstring JNICALL
Java_com_cclaude_android_native_CClaudeNative_retrieveFromMemory(JNIEnv* env, jobject thiz, 
                                                                 jlong handle, jstring key);

JNIEXPORT void JNICALL
Java_com_cclaude_android_native_CClaudeNative_clearMemory(JNIEnv* env, jobject thiz, jlong handle);

JNIEXPORT jstring JNICALL
Java_com_cclaude_android_native_CClaudeNative_getStatus(JNIEnv* env, jobject thiz, jlong handle);

JNIEXPORT jstring JNICALL
Java_com_cclaude_android_native_CClaudeNative_getVersion(JNIEnv* env, jobject thiz);

} // extern "C"

#endif // CCLAUDE_JNI_H
