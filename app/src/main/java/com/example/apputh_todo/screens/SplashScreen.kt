package com.example.apputh_todo.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apputh_todo.controllers.OnboardingController
import com.example.apputh_todo.core.AppRoutes
import com.example.apputh_todo.core.AppTheme
import com.example.apputh_todo.core.NavigatorController
import com.example.apputh_todo.widgets.CheckmarkIcon
import kotlinx.coroutines.delay

class SplashScreen : AppScreen() {

    override val routeName: String = AppRoutes.SPLASH

    @Composable
    override fun Build(navigator: NavigatorController) {
        val context = LocalContext.current
        var startProgress by remember { mutableStateOf(false) }

        val progress by animateFloatAsState(
            targetValue = if (startProgress) 1f else 0f,
            animationSpec = tween(durationMillis = 2500, easing = LinearEasing),
            label = "splash_progress"
        )

        LaunchedEffect(Unit) {
            startProgress = true
            delay(1000L)
            // Kiểm tra cờ SharedPreferences: nếu đã xem onboarding thì vào thẳng Home
            val hasSeen = OnboardingController.hasSeenOnboarding(context)
            val nextRoute = if (hasSeen) AppRoutes.HOME else AppRoutes.ONBOARDING_1
            // Dùng pushReplacement (không cho phép nhấn Back quay lại Splash)
            navigator.pushReplacement(nextRoute)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.PrimaryBlue)
                .systemBarsPadding(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))

                // Icon App: khối bo góc trắng, bên trong có 2 dòng checklist
                SplashAppIcon()

                Spacer(modifier = Modifier.height(22.dp))

                // Tên ứng dụng
                Text(
                    text = "UTH Todo",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Tagline
                Text(
                    text = "Việc học, việc nhà – gọn gàng mỗi ngày",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.weight(1f))

                // Thanh progress bar mảnh chạy dần ở gần đáy
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .width(96.dp)
                        .height(3.5.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = Color.White,
                    trackColor = Color.White.copy(alpha = 0.25f),
                    strokeCap = StrokeCap.Round
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Phiên bản 2.1.0",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 11.5.sp
                )

                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }

    /**
     * Khối bo góc trắng ở giữa màn Splash chứa 2 dòng checklist:
     * - Dòng 1: hình tròn tích xanh + thanh ngang xanh
     * - Dòng 2: hình tròn rỗng + thanh ngang xám
     */
    @Composable
    private fun SplashAppIcon() {
        Surface(
            modifier = Modifier.size(84.dp),
            shape = RoundedCornerShape(22.dp),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 18.dp),
                verticalArrangement = Arrangement.Center
            ) {
                // Dòng 1: Tích xanh
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(AppTheme.PrimaryBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        CheckmarkIcon(
                            modifier = Modifier.size(8.5.dp),
                            color = Color.White,
                            strokeWidthDp = 2.0f
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(AppTheme.PrimaryBlue)
                    )
                }

                Spacer(modifier = Modifier.height(11.dp))

                // Dòng 2: Tròn rỗng
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .border(
                                width = 1.6.dp,
                                color = Color(0xFF94A3B8),
                                shape = CircleShape
                            )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .height(5.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color(0xFFCBD5E1))
                    )
                }
            }
        }
    }
}
