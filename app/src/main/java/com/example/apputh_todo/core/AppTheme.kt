package com.example.apputh_todo.core

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Cấu hình màu sắc và kiểu chữ (Font SansSerif / Inter / Poppins) theo thiết kế UTH Todo V2.
 */
object AppTheme {
    // Màu chủ đạo xanh dương #3B5BDB
    val PrimaryBlue = Color(0xFF3B5BDB)
    val LightBlueBg = Color(0xFFEEF2FF)
    val TextDark = Color(0xFF1E293B)
    val TextGrey = Color(0xFF64748B)
    val DotInactive = Color(0xFFCBD5E1)
    val SkeletonLine = Color(0xFFE2E8F0)

    // Màu Tag ở màn hình Onboarding 2
    val StudyTagBg = Color(0xFFDBE4FF)
    val StudyTagText = Color(0xFF3B5BDB)
    val LifeTagBg = Color(0xFFFFF3BF)
    val LifeTagText = Color(0xFFD97706)

    private val LightColorScheme = lightColorScheme(
        primary = PrimaryBlue,
        onPrimary = Color.White,
        background = Color.White,
        surface = Color.White,
        onSurface = TextDark,
        onSurfaceVariant = TextGrey
    )

    val AppTypography = Typography(
        headlineMedium = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            lineHeight = 29.sp,
            color = TextDark
        ),
        bodyMedium = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Normal,
            fontSize = 14.5.sp,
            lineHeight = 21.sp,
            color = TextGrey
        )
    )

    @Composable
    fun UthTodoTheme(
        darkTheme: Boolean = isSystemInDarkTheme(),
        content: @Composable () -> Unit
    ) {
        MaterialTheme(
            colorScheme = LightColorScheme,
            typography = AppTypography,
            content = content
        )
    }
}
