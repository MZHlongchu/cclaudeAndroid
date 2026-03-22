# CClaude 隐私政策

## 概述

CClaude 是一款注重隐私的 AI 助手。我们承诺：**我们不收集任何用户数据**。

## 数据处理方式

### 我们**不**收集的数据：
- ❌ 个人身份信息
- ❌ 聊天记录
- ❌ 设备信息
- ❌ 使用统计
- ❌ 崩溃报告
- ❌ 诊断数据

### 数据存储位置：
| 数据类型 | 存储位置 | 加密 |
|---------|---------|------|
| API Key | 设备本地 (DataStore) | ✅ AES-256 |
| 聊天记录 | 设备本地 (SQLite) | ✅ 可选 |
| 设置偏好 | 设备本地 (DataStore) | ✅ 是 |
| 应用数据 | 设备本地 | ✅ 系统级 |

## 网络通信

### 数据传输：
- 仅与 Claude API 直接通信
- 使用 HTTPS/TLS 1.3 加密
- 无中间服务器
- 无 CDN 或代理

### API 端点：
```
默认: https://api.anthropic.com
可配置: 用户自定义端点
```

## 权限使用说明

| 权限 | 用途 | 必需 |
|------|------|------|
| INTERNET | 连接 Claude API | 是 |
| ACCESS_NETWORK_STATE | 检测网络状态 | 否 |
| WRITE_EXTERNAL_STORAGE | 导出对话 (Android ≤9) | 否 |

## 第三方服务

### 无第三方 SDK
本应用不包含任何第三方跟踪或分析 SDK：
- ❌ Google Analytics
- ❌ Firebase
- ❌ Crashlytics
- ❌ Facebook SDK
- ❌ 其他分析工具

### 开源依赖
所有依赖均为开源库，详见 `build.gradle`。

## 用户权利

根据 GDPR 和其他隐私法规，您拥有以下权利：

1. **知情权** - 本文件提供完整信息
2. **访问权** - 所有数据都在您的设备上
3. **删除权** - 卸载应用即删除所有数据
4. **可携带权** - 可导出对话记录
5. **反对权** - 可随时停止使用

## 数据安全

### 加密措施：
- API Key: Android Keystore 加密
- 本地数据库: SQLCipher 加密 (可选)
- 网络传输: TLS 1.3

### 安全审计：
代码完全开源，可自行审计或聘请第三方审计。

## 儿童隐私

本应用不面向 13 岁以下儿童，不收集任何儿童信息。

## 政策更新

隐私政策更新将通过以下方式通知：
- 应用内通知
- GitHub Releases
- F-Droid 更新日志

## 联系我们

隐私相关问题请联系：
- GitHub Issues: https://github.com/cclaude/android/issues
- 邮件: privacy@cclaude.org

## 验证我们的承诺

您可以验证我们的隐私承诺：

```bash
# 1. 检查网络请求 (无意外连接)
adb shell dumpsys connectivity | grep cclaude

# 2. 检查权限使用
adb shell dumpsys package com.cclaude.android | grep permission

# 3. 检查数据存储位置
adb shell ls -la /data/data/com.cclaude.android/
```

---

**最后更新：2026-03-17**

我们承诺保护您的隐私。如有疑问，请随时联系我们。
