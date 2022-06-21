package com.example.workout_log.presentation.util

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.example.workout_log.ui.theme.Grey500
import org.burnoutcrew.reorderable.*

@Composable
fun <T : Any> ReorderableList(
    modifier: Modifier = Modifier,
    data: List<T>,
    onMove: (ItemPosition, ItemPosition) -> Unit,
    content: @Composable ColumnScope.(data: T) -> Unit
) {
    val state = rememberReorderableLazyListState(onMove = onMove)
    LazyColumn(
        state = state.listState,
        modifier = Modifier
            .reorderable(state)
            .detectReorderAfterLongPress(state)
    ) {
        items(data, { it }) { item ->
            ReorderableItem(state, key = item) { isDragging ->
                val elevation = animateDpAsState(if (isDragging) 16.dp else 0.dp)
                Column(
                    modifier = Modifier
                        .shadow(elevation.value)
                        .fillMaxWidth()
                        .background(Grey500)
                ) {
                    content(item)
                }
            }
        }
    }
}