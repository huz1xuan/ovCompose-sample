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

package com.tencent.todo.data

import androidx.compose.runtime.Immutable
import com.tencent.todo.utils.getCurrentTimeMillis

/**
 * Todo项目数据模型
 */
@Immutable
data class TodoItem(
    val id: String,
    val title: String,
    val description: String = "",
    val isCompleted: Boolean = false,
    val priority: Priority = Priority.MEDIUM,
    val createdAt: Long = 0L, // Will be set by platform-specific code
    val completedAt: Long? = null,
    val dueDate: Long? = null,
    val tags: List<String> = emptyList()
) {
    companion object {
        fun create(
            title: String,
            description: String = "",
            priority: Priority = Priority.MEDIUM,
            dueDate: Long? = null,
            tags: List<String> = emptyList()
        ): TodoItem {
            return TodoItem(
                id = generateId(),
                title = title,
                description = description,
                priority = priority,
                createdAt = getCurrentTimeMillis(),
                dueDate = dueDate,
                tags = tags
            )
        }

        private fun generateId(): String {
            return "todo_${(0..9999).random()}"
        }
    }
}

/**
 * 优先级枚举
 */
enum class Priority(val displayName: String, val color: String) {
    LOW("低", "#4CAF50"),
    MEDIUM("中", "#FF9800"),
    HIGH("高", "#F44336"),
    URGENT("紧急", "#9C27B0")
}

/**
 * Todo过滤器
 */
enum class TodoFilter {
    ALL,
    ACTIVE,
    COMPLETED,
    HIGH_PRIORITY
} 