cmake_minimum_required(VERSION 3.18.1)
project(cclaude-jni)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# 使用预构建的 libcclaude.so
add_library(cclaude SHARED IMPORTED)
set_target_properties(cclaude PROPERTIES
    IMPORTED_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/../jniLibs/${ANDROID_ABI}/libcclaude.so
    INTERFACE_INCLUDE_DIRECTORIES ${CMAKE_CURRENT_SOURCE_DIR})

find_library(log-lib log)
find_library(android-lib android)

add_library(
    cclaude-jni
    SHARED
    cclaude_jni.cpp
)

target_link_libraries(
    cclaude-jni
    cclaude
    ${log-lib}
    ${android-lib}
)
