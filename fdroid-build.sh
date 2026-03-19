#!/bin/bash
# F-Droid 构建脚本
# 用于在 F-Droid 构建服务器上编译

set -e

echo "=== CClaude F-Droid Build ==="
echo "Build started at $(date)"

# 检查环境
echo "Checking environment..."
java -version
cmake --version

# 构建
echo "Building..."
./gradlew clean assembleFdroidRelease

# 验证 APK
echo "Verifying APK..."
APK_FILE="app/build/outputs/apk/fdroid/release/app-fdroid-release.apk"

if [ -f "$APK_FILE" ]; then
    echo "APK built successfully: $APK_FILE"
    ls -lh "$APK_FILE"
    
    # 检查权限
    echo "Checking permissions..."
    aapt dump permissions "$APK_FILE" || true
    
    # 计算校验和
    echo "SHA256:"
    sha256sum "$APK_FILE"
else
    echo "ERROR: APK not found!"
    exit 1
fi

echo "Build completed at $(date)"
