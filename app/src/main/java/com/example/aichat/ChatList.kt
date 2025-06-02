package com.example.aichat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChatList(
    messages: List<ChatMessage>,
    listState: LazyListState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        itemsIndexed(messages) { index, message ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
            ) {
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + slideInHorizontally(
                        initialOffsetX = { fullWidth ->
                            if (message.isUser) fullWidth else -fullWidth
                        }
                    )
                ) {
                    ChatBubble(message)
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val bubbleColor = if (message.isUser) MaterialTheme.colorScheme.primary else Color(0xFFE0E0E0)
    val textColor = if (message.isUser) MaterialTheme.colorScheme.onPrimary else Color.Black
    Box(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .widthIn(max = 280.dp)
            .align(if (message.isUser) Alignment.CenterEnd else Alignment.CenterStart)
    ) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = bubbleColor,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            androidx.compose.material3.Text(
                text = message.text,
                color = textColor,
                fontSize = 16.sp,
                textAlign = if (message.isUser) TextAlign.End else TextAlign.Start
            )
        }
    }
} 