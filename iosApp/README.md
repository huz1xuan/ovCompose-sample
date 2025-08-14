# iOS 集成说明

## 📱 iOS 项目概述

这个iOS项目展示了如何将我们的Todo Compose App集成到原生iOS应用中。

## 🏗️ 项目结构

```
iosApp/
├── iosApp.xcodeproj/          # Xcode项目文件
├── iosApp.xcworkspace/        # Xcode工作空间（包含Pods）
├── iosApp/                    # iOS应用源代码
│   ├── iosApp.swift          # 应用入口点
│   ├── ContentView.swift     # 主视图（集成Compose）
│   └── Assets.xcassets/      # 应用资源
├── podfile                   # CocoaPods配置
└── Podfile.lock             # 依赖锁定文件
```

## 🚀 快速开始

### 1. 前置条件
- macOS 系统
- Xcode 14.0+
- CocoaPods
- 已编译的ComposeApp Framework

### 2. 安装依赖
```bash
cd iosApp
pod install
```

### 3. 打开项目
```bash
open iosApp.xcworkspace
```

### 4. 运行应用
在Xcode中选择iOS模拟器或真机，点击运行按钮。

## 🔧 集成方式

### 方式一：UIKit 渲染
```swift
struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }
}
```

### 方式二：Skia 渲染
```swift
struct ComposeSkiaView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.SkiaRenderViewController()
    }
}
```

## 📱 应用功能

应用包含两个标签页：
1. **Compose** - 使用UIKit渲染的Todo应用
2. **Skia** - 使用Skia渲染的Todo应用

## 🔄 构建流程

### 1. 编译ComposeApp Framework
```bash
cd ..  # 回到项目根目录
./gradlew :composeApp:linkDebugFrameworkIosArm64
```

### 2. 生成Dummy Framework（首次）
```bash
./gradlew :composeApp:generateDummyFramework
```

### 3. 安装Pods
```bash
cd iosApp
pod install
```

### 4. 在Xcode中构建
打开 `iosApp.xcworkspace` 并构建项目。

## 🛠️ 配置说明

### Podfile
```ruby
use_frameworks! :linkage => :static

target 'iosApp' do
    pod 'composeApp', :path => '../composeApp'
end
```

### composeApp.podspec
- 指定Framework路径
- 配置构建脚本
- 设置iOS部署目标为13.0

## 📋 支持的设备

- iPhone (iOS 13.0+)
- iPad (iOS 13.0+)
- iOS Simulator

## 🔍 调试技巧

### 1. 查看Framework
```bash
find composeApp/build -name "*.framework" -type d
```

### 2. 清理构建
```bash
./gradlew clean
cd iosApp && pod deintegrate && pod install
```

### 3. 重新生成Framework
```bash
./gradlew :composeApp:generateDummyFramework
cd iosApp && pod install
```

### 4. 修复构建问题
如果遇到 "Multiple commands produce" 错误：
```bash
# 使用修复脚本
../fix-ios-build.sh

# 或者手动清理
rm -rf ~/Library/Developer/Xcode/DerivedData
./gradlew clean
cd iosApp && pod deintegrate && rm -rf Pods Podfile.lock && pod install
```

## 🎯 技术特点

- **原生集成**: 通过UIViewControllerRepresentable集成
- **双渲染模式**: 支持UIKit和Skia两种渲染
- **SwiftUI支持**: 使用SwiftUI作为容器
- **自动构建**: 通过CocoaPods脚本自动构建Framework

## 📚 相关文档

- [ComposeApp构建说明](../COMPILATION_FIXES.md)
- [跨平台支持总结](../PLATFORM_SUMMARY.md)
- [项目架构说明](../PROJECT_SUMMARY.md) 