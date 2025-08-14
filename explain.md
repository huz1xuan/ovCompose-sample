# ovCompose示例工程详细说明文档

## 项目概述

ovCompose (online-video-compose) 是腾讯视频团队基于Compose Multiplatform生态系统开发的跨平台开发框架。该框架解决了Jetbrains Compose Multiplatform在HarmonyOS平台支持不足以及iOS混合布局渲染限制的问题，使企业能够更容易地构建完全跨平台的应用。

## 架构设计

### 1. 整体架构

ovCompose示例工程采用Kotlin Multiplatform架构，支持以下平台：
- **Android**: 原生Android平台支持
- **iOS**: 支持UIKit和Skia两种渲染模式
- **HarmonyOS**: 通过ArkUI集成支持

### 2. 项目结构

```
ovCompose-sample/
├── composeApp/                    # 核心Compose Multiplatform模块
│   ├── src/
│   │   ├── commonMain/           # 跨平台共享代码
│   │   ├── androidMain/          # Android平台特定代码
│   │   ├── iosMain/              # iOS平台特定代码
│   │   └── ohosArm64Main/        # HarmonyOS平台特定代码
├── harmonyApp/                   # HarmonyOS原生应用
├── iosApp/                       # iOS原生应用
└── gradle/                       # 构建配置
```

### 3. 核心模块设计

#### 3.1 数据层 (Data Layer)
- **DisplayItem.kt**: 定义展示项数据结构
- **DisplaySection.kt**: 定义展示分组数据结构
- 采用不可变数据类设计，确保状态管理的一致性

#### 3.2 展示层 (Presentation Layer)
- **MainPage.kt**: 主页面组件，负责整体布局和导航
- **DisplaySections.kt**: 展示分组配置，定义各个功能模块
- **sectionItem/**: 具体功能组件实现

#### 3.3 平台适配层 (Platform Adaptation Layer)
- **Platform.kt**: 平台抽象接口
- 各平台特定实现文件

## 核心功能实现方式

### 1. 状态管理

项目采用Compose原生的状态管理方案：

```kotlin
// 使用mutableStateOf进行状态管理
var openedExample: DisplayItem? by remember { mutableStateOf(null) }

// 使用mutableStateListOf管理列表状态
val childCheckedStates = remember { mutableStateListOf(false, false, false) }
```

### 2. 组件化设计

#### 2.1 组件拆分原则
- **单一职责**: 每个组件只负责特定功能
- **可复用性**: 组件设计为可复用的UI单元
- **平台无关**: 核心组件在commonMain中实现

#### 2.2 组件层次结构
```
MainPage
├── TopAppBar (导航栏)
├── AnimatedContent (内容区域)
│   ├── LazyColumn (列表视图)
│   │   └── Section (分组)
│   │       └── BoxItem (功能项)
│   └── Box (详情视图)
│       └── target.content() (具体功能组件)
```

### 3. 导航系统

#### 3.1 页面导航
- 使用`AnimatedContent`实现页面切换动画
- 支持前进/后退导航
- 集成平台特定的返回键处理

#### 3.2 返回键处理
```kotlin
@Composable
internal expect fun BackHandler(enable: Boolean, onBack: () -> Unit)
```

### 4. 资源管理

#### 4.1 图片资源
- 使用`@OptIn(ExperimentalResourceApi::class)`注解
- 通过`rememberLocalImage()`加载本地图片
- 支持多平台资源适配

#### 4.2 资源组织
```
composeResources/
└── drawable/
    ├── animation.png
    ├── balls.png
    ├── buttons.png
    └── ...
```

## ovCompose的具体应用

### 1. 跨平台渲染

#### 1.1 Android平台
- 使用标准Compose渲染
- 支持Material Design组件
- 集成Activity生命周期

#### 1.2 iOS平台
- **UIKit渲染模式**: 减少内存消耗，适合低端设备
- **Skia渲染模式**: 保持跨平台一致性
- 支持UIViewController和UIView两种容器

```kotlin
// UIKit渲染模式
fun MainViewController() = ComposeUIViewController(
    configure = { onFocusBehavior = OnFocusBehavior.DoNothing }
) {
    MainPage(false)
}

// Skia渲染模式
fun SkiaRenderMainViewController() = ComposeUIViewController(
    configure = {
        onFocusBehavior = OnFocusBehavior.DoNothing
        renderBackend = RenderBackend.Skia
    }
) {
    MainPage(true)
}
```

#### 1.3 HarmonyOS平台
- 通过ArkUI集成
- 支持原生ArkUI组件互操作
- 使用C++桥接层

```kotlin
@CName("MainArkUIViewController")
fun MainArkUIViewController(env: napi_env): napi_value {
    initMainHandler(env)
    return ComposeArkUIViewController(env) { MainPage() }
}
```

### 2. 原生组件互操作

#### 2.1 ArkUI集成
- 支持在Compose中嵌入ArkUI组件
- 使用`ArkUIView`和`ArkUINodeHandle`
- 支持ArkUI-TS和ArkUI-C两种方式

#### 2.2 生命周期管理
- 自动处理页面生命周期
- 支持内存管理和资源释放
- 平台特定的生命周期适配

### 3. 构建系统

#### 3.1 Gradle配置
- 使用Kotlin DSL配置
- 支持多平台编译
- 自动化资源发布

#### 3.2 依赖管理
```kotlin
commonMain.dependencies {
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(compose.material)
    implementation(compose.ui)
    implementation(libs.kotlinx.coroutines.core)
}
```

## 最佳实践

### 1. 代码组织
- 按功能模块组织代码
- 使用包结构清晰分离关注点
- 保持组件职责单一

### 2. 状态管理
- 使用Compose原生状态管理
- 避免过度复杂的状态设计
- 合理使用remember和derivedStateOf

### 3. 性能优化
- 使用LazyColumn进行列表优化
- 合理使用Composable重组
- 平台特定的性能优化

### 4. 平台适配
- 使用expect/actual机制处理平台差异
- 保持核心逻辑跨平台一致
- 平台特定功能合理抽象

## 技术特点

### 1. 跨平台一致性
- 统一的UI组件库
- 一致的状态管理方案
- 平台无关的业务逻辑

### 2. 性能优化
- iOS双渲染模式支持
- 内存使用优化
- 渲染性能调优

### 3. 开发体验
- 热重载支持
- 统一的开发工具链
- 完善的调试支持

## 总结

ovCompose示例工程展示了如何构建一个完整的跨平台应用，通过合理的架构设计、组件化开发和平台适配，实现了在Android、iOS和HarmonyOS三个平台上的统一开发体验。该工程为开发者提供了丰富的示例和最佳实践，是学习和使用ovCompose框架的优秀参考。