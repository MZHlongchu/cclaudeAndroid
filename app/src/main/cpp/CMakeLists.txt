cmake_minimum_required(VERSION 3.22.1)
project("cclaude-jni")

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

find_library(log-lib log)

include_directories(${CMAKE_CURRENT_SOURCE_DIR})

add_library(
    cclaude-jni
    SHARED
    cclaude_jni.cpp
    cclaude_bridge.cpp
)

target_link_libraries(
    cclaude-jni
    ${log-lib}
    android
)

# Note: libcclaude.so should be placed in jniLibs directory
# and will be loaded dynamically at runtime
