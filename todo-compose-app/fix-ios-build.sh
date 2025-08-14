#!/bin/bash

echo "🔧 修复iOS构建问题..."

echo ""
echo "🧹 步骤1: 清理所有构建文件..."
./gradlew clean
cd iosApp
pod deintegrate
rm -rf Pods Podfile.lock
cd ..

echo ""
echo "📦 步骤2: 重新生成Framework..."
./gradlew :composeApp:generateDummyFramework

echo ""
echo "📱 步骤3: 重新安装Pods..."
cd iosApp
pod install
cd ..

echo ""
echo "🎯 步骤4: 打开Xcode项目..."
open iosApp/iosApp.xcworkspace

echo ""
echo "✅ iOS构建问题修复完成！"
echo ""
echo "📋 如果还有问题，请尝试："
echo "   1. 在Xcode中清理构建文件夹 (Product -> Clean Build Folder)"
echo "   2. 删除DerivedData: rm -rf ~/Library/Developer/Xcode/DerivedData"
echo "   3. 重新运行此脚本" 