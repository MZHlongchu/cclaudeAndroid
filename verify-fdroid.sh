#!/bin/bash
# F-Droid 版本验证脚本
# 确保 APK 符合 F-Droid 要求

set -e

echo "=== CClaude F-Droid Verification ==="

APK_FILE="${1:-app/build/outputs/apk/fdroid/release/app-fdroid-release.apk}"

if [ ! -f "$APK_FILE" ]; then
    echo "❌ APK not found: $APK_FILE"
    exit 1
fi

echo "Verifying: $APK_FILE"
echo ""

# 检查文件大小
echo "📦 File Size:"
ls -lh "$APK_FILE"
echo ""

# 检查 APK 内容
echo "📋 APK Contents (libs):"
unzip -l "$APK_FILE" | grep -E "\.so$|lib/" || true
echo ""

# 检查禁止的库
echo "🔍 Checking for forbidden libraries..."
FORBIDDEN=(
    "firebase"
    "google.*play.*services"
    "crashlytics"
    "analytics"
    "bugsnag"
    "sentry"
    "flurry"
    "mixpanel"
    "adjust"
    "appsflyer"
)

FOUND_FORBIDDEN=0
for lib in "${FORBIDDEN[@]}"; do
    if unzip -l "$APK_FILE" | grep -iE "$lib" > /dev/null 2>&1; then
        echo "  ❌ Found forbidden library: $lib"
        FOUND_FORBIDDEN=1
    fi
done

if [ $FOUND_FORBIDDEN -eq 0 ]; then
    echo "  ✅ No forbidden libraries found"
fi
echo ""

# 检查权限
echo "🔐 Permissions:"
aapt dump permissions "$APK_FILE" 2>/dev/null || echo "  (aapt not available)"
echo ""

# 检查签名
echo "✍️  Signature:"
apksigner verify --print-certs "$APK_FILE" 2>/dev/null | head -5 || echo "  (apksigner not available)"
echo ""

# 计算校验和
echo "🔢 Checksums:"
echo "  MD5:    $(md5sum "$APK_FILE" | cut -d' ' -f1)"
echo "  SHA1:   $(sha1sum "$APK_FILE" | cut -d' ' -f1)"
echo "  SHA256: $(sha256sum "$APK_FILE" | cut -d' ' -f1)"
echo ""

# 最终验证
echo "=== Verification Summary ==="
if [ $FOUND_FORBIDDEN -eq 0 ]; then
    echo "✅ APK passes F-Droid requirements"
    exit 0
else
    echo "❌ APK contains forbidden libraries"
    exit 1
fi
