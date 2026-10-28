package com.example.apputh_todo.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apputh_todo.core.AppTheme

/**
 * ============================================================================
 * [OOP - INHERITANCE (TÍNH KẾ THỪA) & POLYMORPHISM (TÍNH ĐA HÌNH)]
 * `CategoryIllustration` kế thừa từ `IllustrationWidget` và override `Render()`
 * để hiển thị 2 card có tag "Học tập" (xanh) và "Cuộc sống" (cam) ở Bước 2.
 * ============================================================================
 */
class CategoryIllustration : IllustrationWidget() {

    @Composable
    override fun Render() {
        IllustrationContainer {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CategoryCard(
                    tagText = "Học tập",
                    tagBgColor = AppTheme.StudyTagBg,
                    tagTextColor = AppTheme.StudyTagText,
                    lineFraction = 0.82f
                )
                CategoryCard(
                    tagText = "Cuộc sống",
                    tagBgColor = AppTheme.LifeTagBg,
                    tagTextColor = AppTheme.LifeTagText,
                    lineFraction = 0.68f
                )
            }
        }
    }

    @Composable
    private fun CategoryCard(
        tagText: String,
        tagBgColor: Color,
        tagTextColor: Color,
        lineFraction: Float
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.Start
            ) {
                // Tag phân loại nhóm công việc
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(tagBgColor)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = tagText,
                        color = tagTextColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Dòng skeleton mô phỏng tiêu đề việc cần làm
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
