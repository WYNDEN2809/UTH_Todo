package com.example.apputh_todo.screens

import androidx.compose.runtime.Composable
import com.example.apputh_todo.core.NavigatorController

/**
 * ============================================================================
 * [OOP - ABSTRACTION (TÍNH TRỪU TƯỢNG)]
 * Lớp trừu tượng `AppScreen` định nghĩa "hợp đồng" chung cho mọi màn hình
 * trong ứng dụng: mỗi màn hình bắt buộc phải có `routeName` và triển khai
 * phương thức `@Composable abstract fun Build(navigator: NavigatorController)`.
 * ============================================================================
 */
abstract class AppScreen {
    abstract val routeName: String

    @Composable
    abstract fun Build(navigator: NavigatorController)
}
