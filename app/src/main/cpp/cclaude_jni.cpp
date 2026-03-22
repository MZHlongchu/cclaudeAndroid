/*
 * CClaude JNI Implementation
 * Java Native Interface for Android
 * 更新时间: 2026-03-17
 */

#include "cclaude_jni.h"
#include <jni.h>
#include <android/log.h>

#define LOG_TAG "CClaudeJNI"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

using namespace cclaude;

// Helper class to manage JNI environment
class JniHelper {
public:
    static std::string jstringToString(JNIEnv* env, jstring str) {
        if (!str) return "";
        const char* cstr = env->GetStringUTFChars(str, nullptr);
        std::string result(cstr);
        env->ReleaseStringUTFChars(str, cstr);
        return result;
    }

    static jstring stringToJstring(JNIEnv* env, const std::string& str) {
        return env->NewStringUTF(str.c_str());
    }
};

// Global reference for stream callback
static jobject g_streamCallback = nullptr;
static JNIEnv* g_callbackEnv = nullptr;

extern "C" {

JNIEXPORT jlong JNICALL
Java_com_cclaude_android_native_CClaudeNative_createContext(JNIEnv* env, jobject /*thiz*/) {
    CClaudeBridge* bridge = new CClaudeBridge();
    return reinterpret_cast<jlong>(bridge);
}

JNIEXPORT void JNICALL
Java_com_cclaude_android_native_CClaudeNative_destroyContext(JNIEnv* /*env*/, jobject /*thiz", 
                                                             jlong handle) {
    auto* bridge = reinterpret_cast<CClaudeBridge*>(handle);
    delete bridge;
    LOGD("Context destroyed");
}

JNIEXPORT jboolean JNICALL
Java_com_cclaude_android_native_CClaudeNative_initialize(JNIEnv* env, jobject /*thiz*/, jlong handle,
                                                         jstring apiKey, jstring baseUrl,
                                                         jstring model, jint approvalMode) {
    auto* bridge = reinterpret_cast<CClaudeBridge*>(handle);
    if (!bridge) return JNI_FALSE;

    std::string key = JniHelper::jstringToString(env, apiKey);
    std::string url = JniHelper::jstringToString(env, baseUrl);
    std::string mdl = JniHelper::jstringToString(env, model);

    bool result = bridge->initialize(key, url, mdl, approvalMode);
    return result ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jstring JNICALL
Java_com_cclaude_android_native_CClaudeNative_sendMessage(JNIEnv* env, jobject /*thiz*/, jlong handle,
                                                          jstring message) {
    auto* bridge = reinterpret_cast<CClaudeBridge*>(handle);
    if (!bridge) {
        return JniHelper::stringToJstring(env, "Error: Invalid context");
    }

    std::string msg = JniHelper::jstringToString(env, message);
    std::string response = bridge->sendMessage(msg);
    
    return JniHelper::stringToJstring(env, response);
}

JNIEXPORT void JNICALL
Java_com_cclaude_android_native_CClaudeNative_sendMessageStreaming(JNIEnv* env, jobject /*thiz",
                                                                   jlong handle, jstring message,
                                                                   jobject callback) {
    auto* bridge = reinterpret_cast<CClaudeBridge*>(handle);
    if (!bridge) return;

    // Store global reference for callback
    if (g_streamCallback) {
        env->DeleteGlobalRef(g_streamCallback);
    }
    g_streamCallback = env->NewGlobalRef(callback);
    g_callbackEnv = env;

    // Get callback method ID
    jclass callbackClass = env->GetObjectClass(callback);
    jmethodID onChunkMethod = env->GetMethodID(callbackClass, "onChunk", "(Ljava/lang/String;)V");

    std::string msg = JniHelper::jstringToString(env, message);
    
    bridge->sendMessageStreaming(msg, [env, callback, onChunkMethod](const std::string& chunk) {
        if (env && callback && onChunkMethod) {
            jstring jchunk = JniHelper::stringToJstring(env, chunk);
            env->CallVoidMethod(callback, onChunkMethod, jchunk);
            env->DeleteLocalRef(jchunk);
        }
    });

    // Cleanup
    if (g_streamCallback) {
        env->DeleteGlobalRef(g_streamCallback);
        g_streamCallback = nullptr;
    }
}

JNIEXPORT void JNICALL
Java_com_cclaude_android_native_CClaudeNative_cancelStream(JNIEnv* /*env*/, jobject /*thiz",
                                                           jlong handle) {
    auto* bridge = reinterpret_cast<CClaudeBridge*>(handle);
    if (bridge) {
        bridge->cancelStream();
    }
}

JNIEXPORT jboolean JNICALL
Java_com_cclaude_android_native_CClaudeNative_saveToMemory(JNIEnv* env, jobject /*thiz", jlong handle,
                                                           jstring key, jstring value) {
    auto* bridge = reinterpret_cast<CClaudeBridge*>(handle);
    if (!bridge) return JNI_FALSE;

    std::string k = JniHelper::jstringToString(env, key);
    std::string v = JniHelper::jstringToString(env, value);
    
    return bridge->saveToMemory(k, v) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jstring JNICALL
Java_com_cclaude_android_native_CClaudeNative_retrieveFromMemory(JNIEnv* env, jobject /*thiz",
                                                                 jlong handle, jstring key) {
    auto* bridge = reinterpret_cast<CClaudeBridge*>(handle);
    if (!bridge) {
        return JniHelper::stringToJstring(env, "");
    }

    std::string k = JniHelper::jstringToString(env, key);
    std::string value = bridge->retrieveFromMemory(k);
    
    return JniHelper::stringToJstring(env, value);
}

JNIEXPORT void JNICALL
Java_com_cclaude_android_native_CClaudeNative_clearMemory(JNIEnv* /*env*/, jobject /*thiz",
                                                          jlong handle) {
    auto* bridge = reinterpret_cast<CClaudeBridge*>(handle);
    if (bridge) {
        bridge->clearMemory();
    }
}

JNIEXPORT jstring JNICALL
Java_com_cclaude_android_native_CClaudeNative_getStatus(JNIEnv* env, jobject /*thiz", jlong handle) {
    auto* bridge = reinterpret_cast<CClaudeBridge*>(handle);
    if (!bridge) {
        return JniHelper::stringToJstring(env, "Error: Invalid context");
    }

    std::string status = bridge->getStatus();
    return JniHelper::stringToJstring(env, status);
}

JNIEXPORT jstring JNICALL
Java_com_cclaude_android_native_CClaudeNative_getVersion(JNIEnv* env, jobject /*thiz*/) {
    CClaudeBridge bridge;
    std::string version = bridge.getVersion();
    return JniHelper::stringToJstring(env, version);
}

} // extern "C"
