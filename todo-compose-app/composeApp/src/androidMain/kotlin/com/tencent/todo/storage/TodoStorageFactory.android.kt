package com.tencent.todo.storage

import android.content.Context

actual object TodoStorageFactory {
    private var context: Context? = null
    
    fun initialize(context: Context) {
        this.context = context
    }
    
    actual fun createTodoStorage(): TodoStorage {
        val ctx = context ?: throw IllegalStateException("Context not initialized. Call TodoStorageFactory.initialize() first.")
        return AndroidTodoStorage(ctx)
    }
} 