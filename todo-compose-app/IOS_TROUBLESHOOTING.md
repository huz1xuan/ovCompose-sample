# iOS 构建问题解决方案

## 🚨 常见问题

### 1. Multiple commands produce 错误

**错误信息**:
```
Multiple commands produce '/Users/mac/Library/Developer/Xcode/DerivedData/iosApp-xxx/Build/Products/Debug-iphonesimulator/ComposeSample.app/compose-resources'
```

**原因**: 多个构建命令都在生成相同的资源文件，通常是由于CocoaPods配置冲突导致的。

**解决方案**:

#### 方法1: 使用修复脚本（推荐）
```bash
./fix-ios-build.sh
```

#### 方法2: 手动清理
```bash
# 1. 清理Gradle构建
./gradlew clean

# 2. 清理iOS项目
cd iosApp
pod deintegrate
rm -rf Pods Podfile.lock

# 3. 重新生成Framework
cd ..
./gradlew :composeApp:generateDummyFramework

# 4. 重新安装Pods
cd iosApp
pod install
```

#### 方法3: 清理Xcode缓存
```bash
# 删除DerivedData
rm -rf ~/Library/Developer/Xcode/DerivedData

# 在Xcode中清理构建文件夹
# Product -> Clean Build Folder
```

### 2. Framework 找不到

**错误信息**:
```
Framework not found: ComposeApp
```

**解决方案**:
```bash
# 确保Framework已生成
./gradlew :composeApp:linkDebugFrameworkIosArm64

# 重新安装Pods
cd iosApp && pod install
```

### 3. Pod 安装失败

**错误信息**:
```
Pod installation failed
```

**解决方案**:
```bash
# 更新CocoaPods
sudo gem install cocoapods

# 清理并重新安装
cd iosApp
pod deintegrate
rm -rf Pods Podfile.lock
pod install
```

## 🔧 修复脚本说明

### fix-ios-build.sh
这个脚本会自动执行以下步骤：
1. 清理所有构建文件
2. 重新生成Framework
3. 重新安装Pods
4. 打开Xcode项目

### build-ios.sh
这个脚本用于正常的iOS构建流程：
1. 编译ComposeApp Framework
2. 生成Dummy Framework
3. 安装iOS依赖
4. 打开Xcode项目

## 📋 预防措施

### 1. 定期清理
```bash
# 每周清理一次
./gradlew clean
cd iosApp && pod deintegrate && pod install
```

### 2. 更新依赖
```bash
# 更新CocoaPods
sudo gem update cocoapods

# 更新Pod依赖
cd iosApp && pod update
```

### 3. 检查配置
确保以下文件配置正确：
- `composeApp/composeApp.podspec`
- `iosApp/podfile`
- `iosApp/iosApp.xcodeproj`

## 🎯 最佳实践

### 1. 构建顺序
```bash
# 正确的构建顺序
./gradlew :composeApp:generateDummyFramework
cd iosApp && pod install
open iosApp.xcworkspace
```

### 2. 开发流程
1. 修改Compose代码
2. 重新生成Framework
3. 在Xcode中构建和测试

### 3. 版本控制
- 不要提交 `Pods/` 目录
- 提交 `Podfile.lock` 文件
- 定期更新依赖版本

## 📞 获取帮助

如果问题仍然存在：

1. **检查日志**: 查看Xcode的构建日志
2. **清理缓存**: 删除DerivedData和清理构建文件夹
3. **重新开始**: 使用 `fix-ios-build.sh` 脚本
4. **检查版本**: 确保所有工具版本兼容

## 🔍 调试命令

```bash
# 查看Framework内容
ls -la composeApp/build/cocoapods/framework/ComposeApp.framework/

# 查看Pod配置
cd iosApp && pod spec which composeApp

# 查看构建日志
xcodebuild -workspace iosApp.xcworkspace -scheme iosApp -configuration Debug build
``` 