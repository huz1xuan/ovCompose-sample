package com.tencent.todo.storage

import android.content.Context
import android.content.SharedPreferences
import com.tencent.todo.data.TodoItem
import com.tencent.todo.data.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable

/**
 * Android平台的Todo存储实现
 * 使用SharedPreferences存储JSON数据
 */
class AndroidTodoStorage(private val context: Context) : TodoStorage {
    
    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("todo_storage", Context.MODE_PRIVATE)
    }
    
    private val json = Json { 
        ignoreUnknownKeys = true 
        isLenient = true
    }
    
    override suspend fun saveTodos(todos: List<TodoItem>) = withContext(Dispatchers.IO) {
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
            sharedPreferences.edit()
                .putString(KEY_TODOS, jsonString)
                .apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    override suspend fun loadTodos(): List<TodoItem> = withContext(Dispatchers.IO) {
        try {
            val jsonString = sharedPreferences.getString(KEY_TODOS, null)
            if (jsonString != null) {
                // 手动解析JSON
                parseTodosFromJson(jsonString)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    
    private fun parseTodosFromJson(jsonString: String): List<TodoItem> {
        // 简单的JSON解析实现
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
    
    override suspend fun clearAll() = withContext(Dispatchers.IO) {
        sharedPreferences.edit().clear().apply()
    }
    
    companion object {
        private const val KEY_TODOS = "todos"
    }
} 