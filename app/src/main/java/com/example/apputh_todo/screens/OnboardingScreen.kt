package com.example.apputh_todo.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apputh_todo.controllers.OnboardingController
import com.example.apputh_todo.core.AppRoutes
import com.example.apputh_todo.core.AppTheme
import com.example.apputh_todo.core.NavigatorController
import com.example.apputh_todo.models.OnboardingPage
import com.example.apputh_todo.widgets.CheckmarkIcon
import com.example.apputh_todo.widgets.PageIndicator
import com.example.apputh_todo.widgets.PrimaryButton

/**
 * ============================================================================
 * [OOP - INHERITANCE (TÍNH KẾ THỪA) & POLYMORPHISM (TÍNH ĐA HÌNH)]
 * - `OnboardingScreen` kế thừa từ lớp trừu tượng `AppScreen`.
 * - Thay vì tạo 3 màn hình giao diện thuần rời rạc, `OnboardingScreen` chỉ
 *   dùng duy nhất 1 khung giao diện và đọc dữ liệu từ 3 object của class
 *   `OnboardingPage(picture, title, description)`:
 *     + Hiển thị hình ảnh: `painterResource(id = currentPage.picture)`
 *     + Hiển thị tiêu đề:  `currentPage.title`
 *     + Hiển thị mô tả:    `currentPage.description`
 *     + Gọi đa hình:       `currentPage.onNext(controller)`
 * ============================================================================
 */
class OnboardingScreen(
    private val stepIndex: Int = 0
) : AppScreen() {

    override val routeName: String
        get() = when (stepIndex) {
            0 -> AppRoutes.ONBOARDING_1
            1 -> AppRoutes.ONBOARDING_2
            else -> AppRoutes.ONBOARDING_3
        }

    @Composable
    override fun Build(navigator: NavigatorController) {
        val context = LocalContext.current
        val controller = remember(stepIndex) {
            OnboardingController(
                initialIndex = stepIndex,
                navigator = navigator,
                context = context
            )
        }

        // Lấy danh sách 3 đối tượng (3 objects) OnboardingPage từ Controller
        val pages: List<OnboardingPage> = controller.pages
        val currentPage: OnboardingPage = pages[controller.currentIndex]

        // Hỗ trợ nút Back hệ thống ở bước 2 và bước 3 để quay về bước trước
        BackHandler(enabled = controller.currentIndex > 0) {
            controller.previous()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .systemBarsPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            // 1. Header: Logo nhỏ + "UTH Todo" (bên trái), nút "Bỏ qua" (bên phải)
            OnboardingHeader(onSkip = { controller.skip() })

            // 2. Giữa màn hình: Hiển thị thuộc tính `picture` của đối tượng OnboardingPage
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = currentPage.picture),
                    contentDescription = currentPage.title,
                    modifier = Modifier.size(width = 240.dp, height = 250.dp),
                    contentScale = ContentScale.Fit
                )
            }

            // 3. Phần dưới: Hiển thị `title` (String) và `description` (String) từ đối tượng OnboardingPage
            Text(
                text = currentPage.title,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = currentPage.description,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            PageIndicator(
                totalDots = pages.size,
                currentIndex = controller.currentIndex
            )

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                text = currentPage.buttonText,
                // [ĐA HÌNH]: Bước 1, 2 gọi OnboardingPage.onNext() (sang bước kế),
                // Bước 3 gọi FinalOnboardingPage.onNext() (vào Home, xóa stack).
                onClick = { currentPage.onNext(controller) }
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }

    @Composable
    private fun OnboardingHeader(onSkip: () -> Unit) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                MiniBrandLogo()

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                color = AppTheme.TextDark,
                                fontWeight = FontWeight.ExtraBold
                            )
                        ) {
                            append("UTH ")
                        }
                        withStyle(
                            SpanStyle(
                                color = AppTheme.PrimaryBlue,
                                fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append("Todo")
                        }
                    },
                    fontSize = 16.sp
                )
            }

            TextButton(onClick = onSkip) {
                Text(
                    text = "Bỏ qua",
                    color = AppTheme.TextGrey,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

    @Composable
    private fun MiniBrandLogo() {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(AppTheme.PrimaryBlue)
                .padding(horizontal = 5.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(3.5.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CheckmarkIcon(
                        modifier = Modifier.size(6.dp),
                        color = Color.White,
                        strokeWidthDp = 1.6f
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(Color.White, RoundedCornerShape(1.dp))
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .border(1.dp, Color.White.copy(alpha = 0.8f), CircleShape)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .height(2.dp)
                            .background(
                                Color.White.copy(alpha = 0.8f),
                                RoundedCornerShape(1.dp)
                            )
                    )
                }
            }
        }
    }
}
