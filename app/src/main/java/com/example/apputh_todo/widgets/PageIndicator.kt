package com.example.apputh_todo.widgets

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.apputh_todo.core.AppTheme

/**
 * Hiển thị 3 dấu chấm chỉ trang (Page Indicator).
 * Dot đang được chọn sẽ dài ra (22.dp) và mang màu xanh chủ đạo #3B5BDB.
 */
@Composable
fun PageIndicator(
    totalDots: Int,
    currentIndex: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (index in 0 until totalDots) {
            val isSelected = index == currentIndex
            val dotWidth by animateDpAsState(
                targetValue = if (isSelected) 22.dp else 6.dp,
                animationSpec = tween(durationMillis = 250),
                label = "dot_width"
            )
            Box(
                modifier = Modifier
                    .width(dotWidth)
                    .height(6.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) AppTheme.PrimaryBlue else AppTheme.DotInactive
                    )
            )
        }
    }
}
