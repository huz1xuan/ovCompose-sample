# Todo Compose App

基于ovCompose框架开发的跨平台Todo应用，支持Android、iOS和HarmonyOS平台。

## 项目特性

- **跨平台支持**: 使用ovCompose框架，一套代码运行在Android、iOS和HarmonyOS平台
- **现代化UI**: 采用Material Design设计语言，提供一致的用户体验
- **完整功能**: 支持Todo的增删改查、过滤、搜索等完整功能
- **状态管理**: 使用Compose原生状态管理，响应式UI更新
- **组件化设计**: 模块化组件设计，易于维护和扩展

## 功能特性

### 核心功能
- ✅ 添加新的Todo项目
- ✅ 标记Todo为完成/未完成
- ✅ 删除Todo项目
- ✅ 查看Todo详情
- ✅ 设置优先级（低、中、高、紧急）

### 管理功能
- ✅ 按状态过滤（全部、进行中、已完成、高优先级）
- ✅ 搜索Todo项目
- ✅ 批量操作（全部完成、清空已完成）
- ✅ 统计信息展示

### 用户体验
- ✅ 响应式布局
- ✅ 流畅的动画效果
- ✅ 直观的操作界面
- ✅ 实时状态更新

## 技术架构

### 架构设计
```
TodoApp
├── data/           # 数据模型层
│   └── TodoItem.kt # Todo数据模型
├── viewmodel/      # 状态管理层
│   └── TodoViewModel.kt # 状态管理
├── ui/             # 界面层
│   ├── TodoApp.kt  # 主应用组件
│   ├── TodoMainContent.kt # 主内容组件
│   └── components/ # UI组件
│       ├── TodoStatsCard.kt # 统计卡片
│       ├── TodoFilterBar.kt # 过滤器
│       ├── TodoSearchBar.kt # 搜索栏
│       ├── TodoList.kt # Todo列表
│       ├── TodoItemCard.kt # Todo项目卡片
│       └── TodoActionButtons.kt # 操作按钮
└── dialogs/        # 对话框组件
    ├── AddTodoDialog.kt # 添加对话框
    └── TodoDetailDialog.kt # 详情对话框
```

### 状态管理
- 使用`TodoViewModel`统一管理应用状态
- 采用Compose原生状态管理方案
- 响应式数据流，自动UI更新

### 组件设计
- 遵循单一职责原则
- 组件高度可复用
- 清晰的组件层次结构

## 构建和运行

### 环境要求
- Android Studio Arctic Fox或更高版本
- Kotlin 2.0.21-KBA-005
- ovCompose 1.6.1-KBA-002

### Android平台
```bash
./gradlew :composeApp:assembleDebug
```

### iOS平台
```bash
./gradlew :composeApp:linkDebugFrameworkIosArm64
```

### HarmonyOS平台
```bash
./gradlew :composeApp:linkDebugSharedOhosArm64
```

## 项目结构

```
todo-compose-app/
├── composeApp/                    # 核心Compose模块
│   ├── src/
│   │   ├── commonMain/           # 跨平台共享代码
│   │   │   └── kotlin/com/tencent/todo/
│   │   │       ├── data/         # 数据模型
│   │   │       ├── viewmodel/    # 状态管理
│   │   │       ├── ui/           # 界面组件
│   │   │       └── utils/        # 工具类
│   │   ├── androidMain/          # Android平台特定代码
│   │   ├── iosMain/              # iOS平台特定代码
│   │   └── ohosArm64Main/        # HarmonyOS平台特定代码
│   └── build.gradle.kts          # 模块构建配置
├── gradle/
│   └── libs.versions.toml        # 依赖版本管理
├── build.gradle.kts              # 根项目构建配置
├── settings.gradle.kts           # 项目设置
└── README.md                     # 项目说明
```

## 开发指南

### 添加新功能
1. 在`data`包中定义数据模型
2. 在`viewmodel`包中添加状态管理逻辑
3. 在`ui/components`包中创建UI组件
4. 在主界面中集成新组件

### 代码规范
- 遵循Kotlin编码规范
- 使用Compose最佳实践
- 保持组件职责单一
- 添加适当的注释

### 测试
- 单元测试：测试ViewModel逻辑
- UI测试：测试组件交互
- 集成测试：测试完整功能流程

## 贡献指南

1. Fork项目
2. 创建功能分支
3. 提交代码变更
4. 创建Pull Request

## 许可证

本项目采用Apache 2.0许可证，详见LICENSE文件。

## 联系方式

如有问题或建议，请通过以下方式联系：
- 提交Issue
- 发送邮件
- 参与讨论

---

基于ovCompose框架开发，感谢腾讯视频团队的开源贡献。 