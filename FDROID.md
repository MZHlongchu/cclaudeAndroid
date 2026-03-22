# CClaude F-Droid 版本

这是一个专门为 F-Droid 构建的隐私优先版本。

## 隐私特性

### 零跟踪承诺
- ❌ 无 Google Play Services
- ❌ 无 Firebase Analytics
- ❌ 无 Crashlytics
- ❌ 无 Google Mobile Services
- ❌ 无专有跟踪库
- ❌ 无遥测或数据收集

### 本地优先设计
- ✅ 所有数据本地存储
- ✅ 加密保存 API Key
- ✅ 无需账户注册
- ✅ 可选离线功能
- ✅ 开源代码可审计

## F-Droid 兼容性

### 构建要求
```
sdk: 34
ndk: r25c
gradle: 8.2
```

### 自动更新
F-Droid 会自动检测新版本并构建。

## 权限说明

```xml
<!-- 必需权限 -->
<uses-permission android:name="android.permission.INTERNET" />
<!-- 仅用于连接 Claude API -->

<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<!-- 检测网络连接状态 -->

<!-- 可选权限 -->
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" android:maxSdkVersion="28" />
<!-- 仅用于 Android 9 及以下导出对话 -->
```

## 自托管 F-Droid 仓库

```bash
# 添加 CClaude F-Droid 仓库
fdroid repo add https://fdroid.cclaude.org/fdroid/repo

# 或者扫描 QR 码
# [QR Code Image]
```

## 从源码构建

```bash
# 克隆仓库
git clone https://github.com/cclaude/android.git
cd android

# 构建 F-Droid 版本
./gradlew assembleFdroidRelease

# 输出 APK
# app/build/outputs/apk/fdroid/release/app-fdroid-release.apk
```

## 验证构建

```bash
# 下载 F-Droid 签名工具
wget https://f-droid.org/F-Droid.apk.asc

# 验证 APK 签名
apksigner verify --print-certs app-fdroid-release.apk
```

## 安全审计

### 依赖扫描
```bash
./gradlew dependencyInsight --dependency androidx
```

### 权限扫描
```bash
aapt dump permissions app/build/outputs/apk/fdroid/release/app-fdroid-release.apk
```

## 报告问题

请在 GitHub Issues 报告隐私或安全问题：
https://github.com/cclaude/android/issues

## 许可证

MIT License - 详见 LICENSE 文件
