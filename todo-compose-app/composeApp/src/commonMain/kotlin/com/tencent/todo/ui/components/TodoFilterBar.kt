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

package com.tencent.todo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tencent.todo.data.TodoFilter
import com.tencent.todo.viewmodel.TodoViewModel

/**
 * Todo过滤器栏
 */
@Composable
fun TodoFilterBar(viewModel: TodoViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            text = "全部",
            isSelected = viewModel.currentFilter == TodoFilter.ALL,
            onClick = { viewModel.setFilter(TodoFilter.ALL) }
        )
        
        FilterChip(
            text = "进行中",
            isSelected = viewModel.currentFilter == TodoFilter.ACTIVE,
            onClick = { viewModel.setFilter(TodoFilter.ACTIVE) }
        )
        
        FilterChip(
            text = "已完成",
            isSelected = viewModel.currentFilter == TodoFilter.COMPLETED,
            onClick = { viewModel.setFilter(TodoFilter.COMPLETED) }
        )
        
        FilterChip(
            text = "高优先级",
            isSelected = viewModel.currentFilter == TodoFilter.HIGH_PRIORITY,
            onClick = { viewModel.setFilter(TodoFilter.HIGH_PRIORITY) }
        )
    }
}

@Composable
private fun FilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                color = if (isSelected) MaterialTheme.colors.primary else Color.LightGray.copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color.White else MaterialTheme.colors.onSurface
        )
    }
} 