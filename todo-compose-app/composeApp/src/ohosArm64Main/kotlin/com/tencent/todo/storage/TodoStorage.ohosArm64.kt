package com.tencent.todo.storage

import com.tencent.todo.data.TodoItem
import com.tencent.todo.data.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * HarmonyOS平台的Todo存储实现
 * 使用HarmonyOS的Preferences API实现真正的本地持久化存储
 */
class HarmonyOSTodoStorage : TodoStorage {
    
    // 使用内存缓存提高性能
    private var cachedTodos: List<TodoItem>? = null
    
    override suspend fun saveTodos(todos: List<TodoItem>) {
        withContext(Dispatchers.Default) {
            try {
                // 手动序列化为JSON
                val jsonString = todos.joinToString(prefix = "[", postfix = "]") { todo ->
                    """
                    {
                        "id": "${todo.id}",
                        "title": "${todo.title.replace("\"", "\\\"")}",
                        "description": "${todo.description.replace("\"", "\\\"")}",
                        "isCompleted": ${todo.isCompleted},
                        "priority": "${todo.priority.name}",
                        "createdAt": ${todo.createdAt},
                        "completedAt": ${todo.completedAt ?: "null"},
                        "dueDate": ${todo.dueDate ?: "null"},
                        "tags": [${todo.tags.joinToString { "\"$it\"" }}]
                    }
                    """.trimIndent()
                }
                
                // 使用HarmonyOS的Preferences API保存数据
                saveToPreferences(KEY_TODOS, jsonString)
                
                // 更新缓存
                cachedTodos = todos
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    override suspend fun loadTodos(): List<TodoItem> {
        return withContext(Dispatchers.Default) {
            try {
                // 先检查缓存
                cachedTodos?.let { return@withContext it }
                
                // 从Preferences读取数据
                val jsonString = loadFromPreferences(KEY_TODOS)
                if (jsonString != null) {
                    val todos = parseTodosFromJson(jsonString)
                    cachedTodos = todos
                    todos
                } else {
                    emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }
    
    override suspend fun clearAll() {
        withContext(Dispatchers.Default) {
            try {
                // 清除Preferences中的数据
                removeFromPreferences(KEY_TODOS)
                
                // 清除缓存
                cachedTodos = null
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    /**
     * 保存数据到HarmonyOS Preferences
     */
    private fun saveToPreferences(key: String, value: String) {
        HarmonyOSPreferences.putString(key, value)
    }
    
    /**
     * 从HarmonyOS Preferences读取数据
     */
    private fun loadFromPreferences(key: String): String? {
        return HarmonyOSPreferences.getString(key)
    }
    
    /**
     * 从HarmonyOS Preferences删除数据
     */
    private fun removeFromPreferences(key: String) {
        HarmonyOSPreferences.remove(key)
    }
    
    /**
     * 手动解析JSON数据
     */
    private fun parseTodosFromJson(jsonString: String): List<TodoItem> {
        val todos = mutableListOf<TodoItem>()
        val todoRegex = """\{[^}]+\}""".toRegex()
        val matches = todoRegex.findAll(jsonString)
        
        for (match in matches) {
            try {
                val todoJson = match.value
                val id = extractString(todoJson, "id")
                val title = extractString(todoJson, "title")
                val description = extractString(todoJson, "description")
                val isCompleted = extractBoolean(todoJson, "isCompleted")
                val priorityName = extractString(todoJson, "priority")
                val priority = Priority.valueOf(priorityName)
                val createdAt = extractLong(todoJson, "createdAt")
                val completedAt = extractNullableLong(todoJson, "completedAt")
                val dueDate = extractNullableLong(todoJson, "dueDate")
                val tags = extractStringList(todoJson, "tags")
                
                todos.add(TodoItem(
                    id = id,
                    title = title,
                    description = description,
                    isCompleted = isCompleted,
                    priority = priority,
                    createdAt = createdAt,
                    completedAt = completedAt,
                    dueDate = dueDate,
                    tags = tags
                ))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        
        return todos
    }
    
    private fun extractString(json: String, key: String): String {
        val regex = """"$key":\s*"([^"]*)"""".toRegex()
        return regex.find(json)?.groupValues?.get(1) ?: ""
    }
    
    private fun extractBoolean(json: String, key: String): Boolean {
        val regex = """"$key":\s*(true|false)""".toRegex()
        return regex.find(json)?.groupValues?.get(1)?.toBoolean() ?: false
    }
    
    private fun extractLong(json: String, key: String): Long {
        val regex = """"$key":\s*(\d+)""".toRegex()
        return regex.find(json)?.groupValues?.get(1)?.toLong() ?: 0L
    }
    
    private fun extractNullableLong(json: String, key: String): Long? {
        val regex = """"$key":\s*(null|\d+)""".toRegex()
        val value = regex.find(json)?.groupValues?.get(1)
        return if (value == "null") null else value?.toLong()
    }
    
    private fun extractStringList(json: String, key: String): List<String> {
        val regex = """"$key":\s*\[([^\]]*)\]""".toRegex()
        val arrayContent = regex.find(json)?.groupValues?.get(1) ?: ""
        val stringRegex = """"([^"]*)"""".toRegex()
        return stringRegex.findAll(arrayContent).map { it.groupValues[1] }.toList()
    }
    
    companion object {
        private const val KEY_TODOS = "todos"
    }
} 