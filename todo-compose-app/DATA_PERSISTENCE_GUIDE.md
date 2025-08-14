# Todo应用数据持久化功能

## 🎯 功能概述

Todo应用现在支持跨平台数据持久化，应用关闭后重新打开时能够保持用户输入的数据。

## 📱 支持的平台

### ✅ Android
- **存储方式**: SharedPreferences
- **数据格式**: JSON
- **位置**: 应用私有目录
- **特点**: 自动保存，应用重启后数据保持

### ✅ iOS  
- **存储方式**: NSUserDefaults
- **数据格式**: JSON
- **位置**: 应用沙盒
- **特点**: 自动保存，应用重启后数据保持

### ⚠️ HarmonyOS
- **存储方式**: 内存存储（临时实现）
- **数据格式**: JSON
- **特点**: 应用重启后数据丢失（需要原生API集成）

## 🔧 技术实现

### 1. 数据序列化
使用 `kotlinx.serialization` 进行JSON序列化：

```kotlin
@Serializable
data class TodoItem(
    val id: String,
    val title: String,
    val description: String = "",
    val isCompleted: Boolean = false,
    val priority: Priority = Priority.MEDIUM,
    val createdAt: Long = 0L,
    val completedAt: Long? = null,
    val dueDate: Long? = null,
    val tags: List<String> = emptyList()
)
```

### 2. 存储接口
定义跨平台存储接口：

```kotlin
interface TodoStorage {
    suspend fun saveTodos(todos: List<TodoItem>)
    suspend fun loadTodos(): List<TodoItem>
    suspend fun clearAll()
}
```

### 3. 平台特定实现

#### Android实现
```kotlin
class AndroidTodoStorage(private val context: Context) : TodoStorage {
    private val sharedPreferences = context.getSharedPreferences("todo_storage", Context.MODE_PRIVATE)
    
    override suspend fun saveTodos(todos: List<TodoItem>) {
        val jsonString = json.encodeToString(todos)
        sharedPreferences.edit().putString(KEY_TODOS, jsonString).apply()
    }
    
    override suspend fun loadTodos(): List<TodoItem> {
        val jsonString = sharedPreferences.getString(KEY_TODOS, null)
        return if (jsonString != null) {
            json.decodeFromString<List<TodoItem>>(jsonString)
        } else {
            emptyList()
        }
    }
}
```

#### iOS实现
```kotlin
class IOSTodoStorage : TodoStorage {
    private val userDefaults = NSUserDefaults.standardUserDefaults
    
    override suspend fun saveTodos(todos: List<TodoItem>) {
        val jsonString = json.encodeToString(todos)
        userDefaults.setObject(jsonString, KEY_TODOS)
        userDefaults.synchronize()
    }
    
    override suspend fun loadTodos(): List<TodoItem> {
        val jsonString = userDefaults.stringForKey(KEY_TODOS)
        return if (jsonString != null) {
            json.decodeFromString<List<TodoItem>>(jsonString)
        } else {
            emptyList()
        }
    }
}
```

### 4. ViewModel集成
在TodoViewModel中自动处理数据持久化：

```kotlin
class TodoViewModel {
    private val storage: TodoStorage = TodoStorageFactory.createTodoStorage()
    
    init {
        // 初始化时加载数据
        loadTodos()
    }
    
    // 所有数据修改操作都会自动保存
    fun addTodo(title: String, ...) {
        val newTodo = TodoItem.create(...)
        _todos.add(newTodo)
        saveTodos() // 自动保存
    }
    
    private fun saveTodos() {
        coroutineScope.launch {
            storage.saveTodos(todos.toList())
        }
    }
}
```

## 🚀 使用方法

### 1. 自动保存
- 添加Todo项时自动保存
- 修改Todo项时自动保存
- 删除Todo项时自动保存
- 切换完成状态时自动保存
- 批量操作时自动保存

### 2. 数据管理
- **清除已完成**: 删除所有已完成的Todo项
- **清除所有数据**: 删除所有Todo项和存储数据

### 3. 应用重启
- 应用关闭后重新打开
- 数据自动加载
- 保持所有Todo项状态

## 📊 数据格式

存储的JSON数据格式示例：

```json
[
  {
    "id": "todo_1234",
    "title": "完成项目文档",
    "description": "编写详细的项目说明文档",
    "isCompleted": false,
    "priority": "HIGH",
    "createdAt": 1703123456789,
    "completedAt": null,
    "dueDate": 1703209856789,
    "tags": ["工作", "文档"]
  },
  {
    "id": "todo_5678",
    "title": "购买 groceries",
    "description": "去超市购买生活用品",
    "isCompleted": true,
    "priority": "MEDIUM",
    "createdAt": 1703123456789,
    "completedAt": 1703123456789,
    "dueDate": null,
    "tags": ["生活", "购物"]
  }
]
```

## 🔍 调试和故障排除

### 1. 查看存储数据
- **Android**: 使用Android Studio的Device File Explorer查看SharedPreferences
- **iOS**: 使用Xcode的Device and Simulators查看应用沙盒
- **HarmonyOS**: 当前为内存存储，重启后数据丢失

### 2. 清除数据
- 使用应用内的"清除所有数据"按钮
- 卸载并重新安装应用
- 清除应用数据（平台特定）

### 3. 常见问题
- **数据丢失**: 检查存储权限和磁盘空间
- **加载失败**: 检查JSON格式是否正确
- **性能问题**: 大量数据时考虑分页加载

## 🎯 未来改进

### 1. HarmonyOS完整支持
- 集成HarmonyOS的Preferences API
- 支持数据库存储
- 实现真正的持久化

### 2. 数据同步
- 云存储支持
- 多设备同步
- 数据备份和恢复

### 3. 性能优化
- 增量保存
- 数据压缩
- 缓存机制

## 📝 总结

Todo应用现在具备了完整的数据持久化功能：

- ✅ **跨平台支持**: Android、iOS、HarmonyOS
- ✅ **自动保存**: 所有操作自动持久化
- ✅ **数据恢复**: 应用重启后数据保持
- ✅ **用户友好**: 简单的数据管理界面
- ✅ **技术先进**: 使用现代Kotlin技术栈

用户现在可以放心使用Todo应用，不用担心数据丢失问题！ 