package com.tencent.todo.storage

import com.tencent.todo.data.TodoItem

/**
 * Todo数据存储接口
 * 定义跨平台的数据持久化操作
 */
interface TodoStorage {
    /**
     * 保存Todo列表
     */
    suspend fun saveTodos(todos: List<TodoItem>)
    
    /**
     * 加载Todo列表
     */
    suspend fun loadTodos(): List<TodoItem>
    
    /**
     * 清除所有数据
     */
    suspend fun clearAll()
}
