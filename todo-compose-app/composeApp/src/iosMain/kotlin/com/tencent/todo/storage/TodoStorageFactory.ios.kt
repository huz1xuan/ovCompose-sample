package com.tencent.todo.storage

actual object TodoStorageFactory {
    actual fun createTodoStorage(): TodoStorage {
        return IOSTodoStorage()
    }
} 