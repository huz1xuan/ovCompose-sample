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

package com.tencent.todo.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tencent.todo.ui.components.TodoActionButtons
import com.tencent.todo.ui.components.TodoFilterBar
import com.tencent.todo.ui.components.TodoList
import com.tencent.todo.ui.components.TodoSearchBar
import com.tencent.todo.ui.components.TodoStatsCard
import com.tencent.todo.ui.dialogs.AddTodoDialog
import com.tencent.todo.ui.dialogs.TodoDetailDialog
import com.tencent.todo.viewmodel.TodoViewModel

/**
 * Todo应用的主内容组件
 */
@Composable
fun TodoMainContent(viewModel: TodoViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 统计信息
        TodoStatsCard(viewModel = viewModel)
        
        // 过滤器
        TodoFilterBar(viewModel = viewModel)
        
        // 搜索栏
        TodoSearchBar(viewModel = viewModel)
        
        // Todo列表
        TodoList(
            viewModel = viewModel,
            modifier = Modifier.weight(1f)
        )
        
        // 操作按钮
        TodoActionButtons(viewModel = viewModel)
    }
    
    // 对话框
    if (viewModel.showAddDialog) {
        AddTodoDialog(viewModel = viewModel)
    }
    
    if (viewModel.showDetailDialog) {
        viewModel.selectedTodo?.let { todo ->
            TodoDetailDialog(
                todo = todo,
                viewModel = viewModel
            )
        }
    }
} 