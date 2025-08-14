/*
 * Tencent is pleased to support the open source community by making ovCompose available.
 * Copyright (C) 2025 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tencent.todo.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.tencent.todo.data.Priority
import com.tencent.todo.data.TodoFilter
import com.tencent.todo.data.TodoItem
import com.tencent.todo.storage.TodoStorage
import com.tencent.todo.storage.TodoStorageFactory
import com.tencent.todo.utils.getCurrentTimeMillis
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Todo应用的状态管理类
 */
class TodoViewModel {
    
    private val storage: TodoStorage = TodoStorageFactory.createTodoStorage()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    
    init {
        // 初始化时加载数据
        loadTodos()
    }
    
    // Todo列表状态
    private val _todos = mutableStateListOf<TodoItem>()
    val todos: SnapshotStateList<TodoItem> = _todos
    
    // 当前过滤器
    var currentFilter by mutableStateOf(TodoFilter.ALL)
        private set
    
    // 搜索关键词
    private var _searchQuery by mutableStateOf("")
    val searchQuery: String get() = _searchQuery
    
    // 是否显示添加对话框
    private var _showAddDialog by mutableStateOf(false)
    val showAddDialog: Boolean get() = _showAddDialog
    
    // 是否显示详情对话框
    private var _showDetailDialog by mutableStateOf(false)
    val showDetailDialog: Boolean get() = _showDetailDialog
    
    // 当前选中的Todo项
    private var _selectedTodo by mutableStateOf<TodoItem?>(null)
    val selectedTodo: TodoItem? get() = _selectedTodo
    
    // 统计信息
    val totalCount: Int get() = todos.size
    val completedCount: Int get() = todos.count { it.isCompleted }
    val activeCount: Int get() = totalCount - completedCount
    val highPriorityCount: Int get() = todos.count { it.priority == Priority.HIGH || it.priority == Priority.URGENT }
    
    /**
     * 获取过滤后的Todo列表
     */
    fun getFilteredTodos(): List<TodoItem> {
        var filtered = when (currentFilter) {
            TodoFilter.ALL -> todos
            TodoFilter.ACTIVE -> todos.filter { !it.isCompleted }
            TodoFilter.COMPLETED -> todos.filter { it.isCompleted }
            TodoFilter.HIGH_PRIORITY -> todos.filter { it.priority == Priority.HIGH || it.priority == Priority.URGENT }
        }
        
        // 应用搜索过滤
        if (searchQuery.isNotBlank()) {
            filtered = filtered.filter { todo ->
                todo.title.contains(searchQuery, ignoreCase = true) ||
                todo.description.contains(searchQuery, ignoreCase = true) ||
                todo.tags.any { it.contains(searchQuery, ignoreCase = true) }
            }
        }
        
        return filtered.sortedWith(compareBy({ !it.isCompleted }, { it.priority.ordinal }, { it.createdAt }))
    }
    
    /**
     * 添加新的Todo项
     */
    fun addTodo(
        title: String,
        description: String = "",
        priority: Priority = Priority.MEDIUM,
        dueDate: Long? = null,
        tags: List<String> = emptyList()
    ) {
        val newTodo = TodoItem.create(
            title = title,
            description = description,
            priority = priority,
            dueDate = dueDate,
            tags = tags
        )
        _todos.add(newTodo)
        saveTodos()
        hideAddDialog()
    }
    
    /**
     * 更新Todo项
     */
    fun updateTodo(todo: TodoItem) {
        val index = _todos.indexOfFirst { it.id == todo.id }
        if (index != -1) {
            _todos[index] = todo
            saveTodos()
        }
    }
    
    /**
     * 删除Todo项
     */
    fun deleteTodo(todoId: String) {
        _todos.removeAll { it.id == todoId }
        saveTodos()
    }
    
    /**
     * 切换Todo完成状态
     */
    fun toggleTodoCompletion(todoId: String) {
        val index = _todos.indexOfFirst { it.id == todoId }
        if (index != -1) {
            val todo = _todos[index]
            val updatedTodo = todo.copy(
                isCompleted = !todo.isCompleted,
                completedAt = if (!todo.isCompleted) getCurrentTimeMillis() else null
            )
            _todos[index] = updatedTodo
            saveTodos()
        }
    }
    
    /**
     * 设置过滤器
     */
    fun setFilter(filter: TodoFilter) {
        currentFilter = filter
    }
    
    /**
     * 设置搜索关键词
     */
    fun setSearchQuery(query: String) {
        _searchQuery = query
    }
    
    /**
     * 显示添加对话框
     */
    fun showAddDialog() {
        _showAddDialog = true
    }
    
    /**
     * 隐藏添加对话框
     */
    fun hideAddDialog() {
        _showAddDialog = false
    }
    
    /**
     * 显示详情对话框
     */
    fun showDetailDialog(todo: TodoItem) {
        _selectedTodo = todo
        _showDetailDialog = true
    }
    
    /**
     * 隐藏详情对话框
     */
    fun hideDetailDialog() {
        _showDetailDialog = false
        _selectedTodo = null
    }
    
    /**
     * 清空已完成的Todo项
     */
    fun clearCompleted() {
        _todos.removeAll { it.isCompleted }
        saveTodos()
    }
    
    /**
     * 标记所有Todo为完成
     */
    fun markAllAsCompleted() {
        val now = getCurrentTimeMillis()
        for (i in _todos.indices) {
            if (!_todos[i].isCompleted) {
                _todos[i] = _todos[i].copy(
                    isCompleted = true,
                    completedAt = now
                )
            }
        }
        saveTodos()
    }
    
    /**
     * 标记所有Todo为未完成
     */
    fun markAllAsActive() {
        for (i in _todos.indices) {
            if (_todos[i].isCompleted) {
                _todos[i] = _todos[i].copy(
                    isCompleted = false,
                    completedAt = null
                )
            }
        }
        saveTodos()
    }
    
    /**
     * 加载Todo数据
     */
    private fun loadTodos() {
        coroutineScope.launch {
            try {
                val loadedTodos = storage.loadTodos()
                _todos.clear()
                _todos.addAll(loadedTodos)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    /**
     * 保存Todo数据
     */
    private fun saveTodos() {
        coroutineScope.launch {
            try {
                storage.saveTodos(todos.toList())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    /**
     * 清除所有数据
     */
    fun clearAllData() {
        coroutineScope.launch {
            try {
                storage.clearAll()
                _todos.clear()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

/**
 * 创建TodoViewModel的Composable函数
 */
@Composable
fun rememberTodoViewModel(): TodoViewModel {
    return remember { TodoViewModel() }
} 