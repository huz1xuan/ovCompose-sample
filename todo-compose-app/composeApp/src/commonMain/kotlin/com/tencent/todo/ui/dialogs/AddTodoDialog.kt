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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tencent.todo.data.Priority
import com.tencent.todo.viewmodel.TodoViewModel

/**
 * 添加Todo对话框
 */
@Composable
fun AddTodoDialog(viewModel: TodoViewModel) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(Priority.MEDIUM) }
    var showPriorityDropdown by remember { mutableStateOf(false) }
    
    AlertDialog(
        onDismissRequest = { viewModel.hideAddDialog() },
        title = {
            Text(
                text = "添加新的Todo",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 标题输入
                Column {
                    Text(
                        text = "标题",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    BasicTextField(
                        value = title,
                        onValueChange = { title = it },
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            color = MaterialTheme.colors.onSurface
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = Color.LightGray.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .padding(8.dp)
                            ) {
                                innerTextField()
                            }
                        }
                    )
                }
                
                // 描述输入
                Column {
                    Text(
                        text = "描述（可选）",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    BasicTextField(
                        value = description,
                        onValueChange = { description = it },
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            color = MaterialTheme.colors.onSurface
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = Color.LightGray.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .padding(8.dp)
                            ) {
                                innerTextField()
                            }
                        }
                    )
                }
                
                // 优先级选择
                Column {
                    Text(
                        text = "优先级",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Box {
                        Button(
                            onClick = { showPriorityDropdown = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp)
                        ) {
                            Text(
                                text = priority.displayName,
                                color = Color.White
                            )
                        }
                        
                        DropdownMenu(
                            expanded = showPriorityDropdown,
                            onDismissRequest = { showPriorityDropdown = false }
                        ) {
                            Priority.values().forEach { priorityOption ->
                                DropdownMenuItem(
                                    onClick = {
                                        priority = priorityOption
                                        showPriorityDropdown = false
                                    }
                                ) {
                                    Text(
                                        text = priorityOption.displayName,
                                        color = Color(0xFF4CAF50) // Use a default color
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) {
                        viewModel.addTodo(
                            title = title,
                            description = description,
                            priority = priority
                        )
                    }
                }
            ) {
                Text("添加")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { viewModel.hideAddDialog() }
            ) {
                Text("取消")
            }
        }
    )
} 