cmake_minimum_required(VERSION 3.18.1)
project(cclaude-jni)

# Import pre-built Zig library (to be compiled separately)
add_library(cclaude SHARED IMPORTED)
set_target_properties(cclaude PROPERTIES
    IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/../jniLibs/${ANDROID_ABI}/libcclaude.so
    INTERFACE_INCLUDE_DIRECTORIES ${CMAKE_SOURCE_DIR}
)

# JNI wrapper (currently using Kotlin fallback, Zig library will be added)
# Note: Zig code needs to be compiled separately with:
#   cd zig-core && zig build -Dtarget=aarch64-linux-android
#   cp zig-out/lib/libcclaude.so ../app/src/main/jniLibs/arm64-v8a/

# For now, we'll use the Kotlin-based CClaudeNative implementation
# which provides a working demo without native dependencies
