package com.tencent.todo.storage

/**
 * TodoStorage工厂类
 * 提供平台特定的存储实现
 */
expect object TodoStorageFactory {
    fun createTodoStorage(): TodoStorage
} 