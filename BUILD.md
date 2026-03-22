# 构建指南

## 环境要求

- JDK 17
- Android SDK 34
- NDK r25c
- CMake 3.22.1

## 快速构建

### F-Droid 版本（推荐）

```bash
# 构建 F-Droid Debug
./gradlew assembleFdroidDebug

# 构建 F-Droid Release
./gradlew assembleFdroidRelease

# 输出位置
# app/build/outputs/apk/fdroid/debug/app-fdroid-debug.apk
# app/build/outputs/apk/fdroid/release/app-fdroid-release.apk
```

### Play Store 版本

```bash
./gradlew assemblePlayRelease
```

## 本地验证

```bash
# 验证 F-Droid 合规性
./verify-fdroid.sh app/build/outputs/apk/fdroid/release/app-fdroid-release.apk
```

## GitHub Actions 自动构建

项目配置了自动构建：

- **Push 到 main**: 构建 Debug 版本
- **Pull Request**: 运行 Lint 和测试
- **Tag (v*)**: 创建 Release

## F-Droid 发布流程

1. 更新版本号 (`versionCode` 和 `versionName`)
2. 创建 Git Tag: `git tag v1.0.0`
3. Push Tag: `git push origin v1.0.0`
4. GitHub Actions 自动构建 Release
5. F-Droid 自动检测并构建

## 签名配置

### 本地构建

创建 `local.properties`:

```properties
store.file=keystore.jks
store.password=YOUR_PASSWORD
key.alias=cclaude
key.password=YOUR_PASSWORD
```

### CI 构建

在 GitHub Repository Settings 中添加 Secrets:
- `KEYSTORE_BASE64`: Base64 编码的 keystore
- `KEYSTORE_PASSWORD`
- `KEY_ALIAS`
- `KEY_PASSWORD`

## 常见问题

### Gradle 同步失败

```bash
# 清除缓存
./gradlew clean
rm -rf ~/.gradle/caches

# 重新同步
./gradlew --refresh-dependencies
```

### NDK 版本不匹配

```bash
# 在 local.properties 中指定 NDK 路径
sdk.dir=/path/to/android-sdk
ndk.dir=/path/to/android-sdk/ndk/25.2.9519653
```

### CMake 找不到

```bash
# 安装 CMake
sdkmanager "cmake;3.22.1"
```
