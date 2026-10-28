package com.example.apputh_todo.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apputh_todo.core.AppTheme

/**
 * ============================================================================
 * [OOP - INHERITANCE (TÍNH KẾ THỪA) & POLYMORPHISM (TÍNH ĐA HÌNH)]
 * `NotificationIllustration` kế thừa `IllustrationWidget` và override `Render()`
 * để hiển thị chuông thông báo và chip "Ôn bài Toán lúc 19:00" ở Bước 3.
 * ============================================================================
 */
class NotificationIllustration : IllustrationWidget() {

    @Composable
    override fun Render() {
        IllustrationContainer {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Hình tròn trắng chứa biểu tượng chuông thông báo
                Surface(
                    modifier = Modifier.size(72.dp),
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 3.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        BellOutlineIcon(
                            modifier = Modifier.size(30.dp),
                            color = AppTheme.PrimaryBlue
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Chip nhắc hẹn "Ôn bài Toán lúc 19:00"
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Text(
                        text = "Ôn bài Toán lúc 19:00",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextDark
                    )
                }
            }
        }
    }

    /**
     * Vẽ icon chuông thông báo thanh mảnh giống thiết kế bằng Canvas
     */
    @Composable
    private fun BellOutlineIcon(modifier: Modifier = Modifier, color: Color) {
        Canvas(modifier = modifier) {
            val w = size.width
            val h = size.height
            val stroke = Stroke(
                width = 2.1.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )

            // Thân chuông
            val bellPath = Path().apply {
                moveTo(w * 0.5f, h * 0.10f)
                lineTo(w * 0.5f, h * 0.16f)
                cubicTo(
                    w * 0.28f, h * 0.16f,
                    w * 0.22f, h * 0.34f,
                    w * 0.22f, h * 0.55f
                )
                lineTo(w * 0.14f, h * 0.72f)
                lineTo(w * 0.86f, h * 0.72f)
                lineTo(w * 0.78f, h * 0.55f)
                cubicTo(
                    w * 0.78f, h * 0.34f,
                    w * 0.72f, h * 0.16f,
                    w * 0.5f, h * 0.16f
                )
            }
            drawPath(path = bellPath, color = color, style = stroke)

            // Quả lắc chuông phía dưới
            val clapperPath = Path().apply {
                moveTo(w * 0.38f, h * 0.74f)
                cubicTo(
                    w * 0.40f, h * 0.88f,
                    w * 0.60f, h * 0.88f,
                    w * 0.62f, h * 0.74f
                )
            }
            drawPath(path = clapperPath, color = color, style = stroke)
        }
    }
}
