#!/bin/bash

echo "🍎 开始构建iOS应用..."

echo ""
echo "📦 步骤1: 编译ComposeApp Framework..."
./gradlew :composeApp:linkDebugFrameworkIosArm64
if [ $? -ne 0 ]; then
    echo "❌ Framework编译失败"
    exit 1
fi
echo "✅ Framework编译成功"

echo ""
echo "🔧 步骤2: 生成Dummy Framework..."
./gradlew :composeApp:generateDummyFramework
if [ $? -ne 0 ]; then
    echo "❌ Dummy Framework生成失败"
    exit 1
fi
echo "✅ Dummy Framework生成成功"

echo ""
echo "📱 步骤3: 安装iOS依赖..."
cd iosApp
pod install
if [ $? -ne 0 ]; then
    echo "❌ Pod安装失败"
    exit 1
fi
echo "✅ Pod安装成功"

echo ""
echo "🎯 步骤4: 打开Xcode项目..."
open iosApp.xcworkspace

echo ""
echo "🎉 iOS项目准备完成！"
echo ""
echo "📋 下一步操作："
echo "   1. 在Xcode中选择目标设备（模拟器或真机）"
echo "   2. 点击运行按钮 (⌘+R)"
echo "   3. 应用将显示两个标签页：Compose和Skia"
echo ""
echo "📁 项目文件位置："
echo "   - Xcode项目: iosApp/iosApp.xcworkspace"
echo "   - Framework: composeApp/build/bin/iosArm64/debugFramework/ComposeApp.framework" 