# ProGuard rules for CClaude
-keep class com.cclaude.android.native.** { *; }
-keepclasseswithmembernames class * {
    native <methods>;
}
