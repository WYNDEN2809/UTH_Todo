package com.example.apputh_todo.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.apputh_todo.core.AppTheme

/**
 * ============================================================================
 * [OOP - ABSTRACTION (TÍNH TRỪU TƯỢNG)]
 * Lớp trừu tượng `IllustrationWidget` định nghĩa "hợp đồng" (contract) cho phần
 * hình minh họa ở giữa các màn hình Onboarding. Mọi lớp con kế thừa bắt buộc
 * phải triển khai phương thức `@Composable abstract fun Render()`.
 * ============================================================================
 */
abstract class IllustrationWidget {

    /**
     * Phương thức trừu tượng để vẽ nội dung minh họa riêng biệt của từng bước
     */
    @Composable
    abstract fun Render()

    /**
     * Khung nền xanh nhạt bo góc 24.dp dùng chung cho cả 3 màn hình Onboarding
     */
    @Composable
    protected fun IllustrationContainer(
        content: @Composable BoxScope.() -> Unit
    ) {
        Box(
            modifier = Modifier
                .size(width = 240.dp, height = 250.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(AppTheme.LightBlueBg)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            contentAlignment = Alignment.Center,
            content = content
        )
    }
}
