#!/bin/bash

echo "🚀 开始测试所有平台的编译..."

echo ""
echo "📱 测试 Android 平台..."
./gradlew :composeApp:assembleDebug
if [ $? -eq 0 ]; then
    echo "✅ Android 编译成功"
    echo "   APK 位置: composeApp/build/outputs/apk/debug/composeApp-debug.apk"
else
    echo "❌ Android 编译失败"
    exit 1
fi

echo ""
echo "🍎 测试 iOS 平台..."
./gradlew :composeApp:linkDebugFrameworkIosArm64
if [ $? -eq 0 ]; then
    echo "✅ iOS 编译成功"
    echo "   Framework 位置: composeApp/build/bin/iosArm64/debugFramework/ComposeApp.framework"
else
    echo "❌ iOS 编译失败"
    exit 1
fi

echo ""
echo "🌸 测试 HarmonyOS 平台..."
./gradlew :composeApp:linkDebugSharedOhosArm64
if [ $? -eq 0 ]; then
    echo "✅ HarmonyOS 编译成功"
    echo "   库文件位置: composeApp/build/bin/ohosArm64/debugShared/libkn.so"
else
    echo "❌ HarmonyOS 编译失败"
    exit 1
fi

echo ""
echo "🎉 所有平台编译成功！"
echo ""
echo "📋 生成的文件列表："
echo "   Android APK: composeApp/build/outputs/apk/debug/composeApp-debug.apk"
echo "   iOS Framework: composeApp/build/bin/iosArm64/debugFramework/ComposeApp.framework"
echo "   HarmonyOS Library: composeApp/build/bin/ohosArm64/debugShared/libkn.so" 