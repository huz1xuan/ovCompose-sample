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

package com.tencent.todo.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tencent.todo.data.TodoItem
import com.tencent.todo.viewmodel.TodoViewModel

/**
 * Todo详情对话框
 */
@Composable
fun TodoDetailDialog(
    todo: TodoItem,
    viewModel: TodoViewModel
) {
    AlertDialog(
        onDismissRequest = { viewModel.hideDetailDialog() },
        title = {
            Text(
                text = "Todo详情",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DetailRow("标题", todo.title)
                DetailRow("描述", todo.description.ifBlank { "无" })
                DetailRow("优先级", todo.priority.displayName)
                DetailRow("状态", if (todo.isCompleted) "已完成" else "进行中")
                DetailRow("创建时间", formatDateTime(todo.createdAt))
                
                todo.completedAt?.let { completedAt ->
                    DetailRow("完成时间", formatDateTime(completedAt))
                }
                
                todo.dueDate?.let { dueDate ->
                    DetailRow("截止时间", formatDateTime(dueDate))
                }
                
                if (todo.tags.isNotEmpty()) {
                    DetailRow("标签", todo.tags.joinToString(", "))
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { viewModel.hideDetailDialog() }
            ) {
                Text("关闭")
            }
        }
    )
}

@Composable
private fun DetailRow(
    label: String,
    value: String
) {
    Column {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.primary
        )
        Text(
            text = value,
            fontSize = 16.sp,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

private fun formatDateTime(timestamp: Long): String {
    // Simple timestamp formatting for cross-platform compatibility
    return "时间戳: $timestamp"
} 