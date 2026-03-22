# CClaude Android 项目概览

## 项目架构

### 1. UI 层 (Jetpack Compose)

```
ui/
├── components/           # 可复用组件
│   ├── InputField.kt    # 输入框组件
│   └── MessageBubble.kt # 消息气泡
├── screens/             # 页面
│   ├── ChatScreen.kt    # 聊天主界面
│   └── SettingsScreen.kt # 设置界面
└── theme/               # 主题
    ├── Color.kt
    ├── Theme.kt
    └── Type.kt
```

### 2. 数据层 (Repository Pattern)

```
data/
├── ChatMessage.kt       # 数据类定义
├── ChatRepository.kt    # 聊天数据仓库
└── SettingsRepository.kt # 设置数据存储
```

### 3. ViewModel 层

```
viewmodel/
├── ChatViewModel.kt     # 聊天状态管理
└── SettingsViewModel.kt # 设置状态管理
```

### 4. Native 层 (JNI)

```
cpp/
├── cclaude_jni.h       # JNI 头文件
├── cclaude_jni.cpp     # JNI 接口实现
└── cclaude_bridge.cpp  # CClaude C++ 桥接
```

## 关键特性

### 安全设计
- API Key 使用 DataStore 加密存储
- 多层审批模式 (AUTO/CAUTIOUS/STRICT/YOLO)
- 安全沙箱执行

### 性能优化
- Coroutines 异步处理
- 流式响应支持
- 本地记忆缓存

### UI/UX
- Material Design 3
- 动态主题支持
- 响应式布局

## 集成说明

### 1. 准备 libcclaude.so

```bash
# 创建 jniLibs 目录结构
app/src/main/jniLibs/
├── arm64-v8a/
│   └── libcclaude.so
├── armeabi-v7a/
│   └── libcclaude.so
└── x86_64/
    └── libcclaude.so
```

### 2. 配置 API

在设置页面输入 Claude API Key，应用会自动保存到加密存储。

### 3. 构建

```bash
# 使用 Android Studio
# 1. Open Project
# 2. Sync Project with Gradle Files
# 3. Build -> Make Project

# 或使用命令行
./gradlew assembleDebug
```

## JNI 接口

### Native 类: CClaudeNative

```kotlin
// 初始化
val native = CClaudeNative()
native.initialize()
native.configure(apiKey, baseUrl, model, approvalMode)

// 发送消息
val response = native.sendMessage("Hello")

// 流式消息
native.sendMessageStreaming("Hello", callback)

// 记忆操作
native.saveToMemory(key, value)
native.retrieveFromMemory(key)
native.clearMemory()
```

## 技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Kotlin | 1.9.20 | 开发语言 |
| Compose | 2024.02.00 | UI 框架 |
| Material3 | 1.2.0 | 设计系统 |
| Navigation | 2.7.7 | 页面导航 |
| DataStore | 1.0.0 | 数据存储 |
| Coroutines | 1.7.3 | 异步处理 |
| NDK | 25+ | Native 开发 |
| CMake | 3.22.1 | 构建 Native |

## 开发计划

- [x] 基础项目结构
- [x] JNI 桥接层
- [x] Compose UI
- [x] 设置存储
- [ ] 流式响应优化
- [ ] 语音输入支持
- [ ] 图片生成支持
- [ ] 多语言支持
- [ ] Widget 支持

## 贡献指南

1. Fork 项目
2. 创建功能分支
3. 提交更改
4. 创建 Pull Request

## 许可证

MIT License - 详见 LICENSE 文件
