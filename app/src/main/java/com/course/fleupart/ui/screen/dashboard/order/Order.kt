package com.course.fleupart.ui.screen.dashboard.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun Order(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier.fillMaxSize().background(color = Color.Yellow),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Lorem Ipsum is simply dumm unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
        )
    }
}