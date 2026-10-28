package com.example.apputh_todo.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import com.example.apputh_todo.core.AppTheme

/**
 * ============================================================================
 * [OOP - INHERITANCE (TÍNH KẾ THỪA) & POLYMORPHISM (TÍNH ĐA HÌNH)]
 * `ChecklistIllustration` kế thừa từ lớp trừu tượng `IllustrationWidget`
 * và override `Render()` để hiển thị 3 dòng checklist (1 dòng đã tích xanh).
 * ============================================================================
 */
class ChecklistIllustration : IllustrationWidget() {

    @Composable
    override fun Render() {
        IllustrationContainer {
            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ChecklistRow(isChecked = true, lineFraction = 0.76f)
                ChecklistRow(isChecked = false, lineFraction = 0.60f)
                ChecklistRow(isChecked = false, lineFraction = 0.68f)
            }
        }
    }

    @Composable
    private fun ChecklistRow(isChecked: Boolean, lineFraction: Float) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Hình tròn tích xanh hoặc tròn rỗng
                if (isChecked) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(AppTheme.PrimaryBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        CheckmarkIcon(
                            modifier = Modifier.size(10.dp),
                            color = Color.White
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .border(
                                width = 1.8.dp,
                                color = AppTheme.DotInactive,
                                shape = CircleShape
                            )
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Thanh ngang mô phỏng dòng chữ
                Box(
                    modifier = Modifier
                        .fillMaxWidth(lineFraction)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(AppTheme.SkeletonLine)
                )
            }
        }
    }
}

/**
 * Vẽ dấu tích (checkmark) sắc nét bằng Canvas không cần thư viện icon ngoài
 */
@Composable
fun CheckmarkIcon(
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    strokeWidthDp: Float = 2.2f
) {
    Canvas(modifier = modifier) {
        val path = Path().apply {
            moveTo(size.width * 0.15f, size.height * 0.52f)
            lineTo(size.width * 0.42f, size.height * 0.80f)
            lineTo(size.width * 0.88f, size.height * 0.22f)
        }
        drawPath(
            path = path,
            color = color,
            style = Stroke(
                width = strokeWidthDp.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}
