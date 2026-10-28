package com.example.apputh_todo.core

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.apputh_todo.screens.AppScreen
import com.example.apputh_todo.screens.HomeScreen
import com.example.apputh_todo.screens.OnboardingScreen
import com.example.apputh_todo.screens.SplashScreen

/**
 * Định nghĩa tập trung tất cả tên Route (Named Routes) và hàm `onGenerateRoute`.
 */
object AppRoutes {
    const val SPLASH = "/splash"
    const val ONBOARDING_1 = "/onboarding/1"
    const val ONBOARDING_2 = "/onboarding/2"
    const val ONBOARDING_3 = "/onboarding/3"
    const val HOME = "/home"

    /**
     * Khởi tạo màn hình `AppScreen` tương ứng với tên route được yêu cầu
     */
    fun onGenerateRoute(routeName: String): AppScreen {
        return when (routeName) {
            SPLASH -> SplashScreen()
            ONBOARDING_1 -> OnboardingScreen(stepIndex = 0)
            ONBOARDING_2 -> OnboardingScreen(stepIndex = 1)
            ONBOARDING_3 -> OnboardingScreen(stepIndex = 2)
            HOME -> HomeScreen()
            else -> SplashScreen()
        }
    }
}

/**
 * Loại hiệu ứng chuyển trang
 */
enum class TransitionType {
    FADE,
    SLIDE_FORWARD,
    SLIDE_BACKWARD
}

/**
 * Bộ điều khiển điều hướng quản lý Back Stack (thuần Jetpack Compose, không cần thư viện ngoài)
 */
class NavigatorController(initialRoute: String = AppRoutes.SPLASH) {
    private val _backStack = mutableStateListOf(initialRoute)

    var lastTransition: TransitionType by mutableStateOf(TransitionType.FADE)
        private set

    val currentRoute: String
        get() = _backStack.last()

    /**
     * Đi tới route mới (giữ màn hiện tại trong stack để có thể Back)
     */
    fun pushNamed(routeName: String) {
        lastTransition = TransitionType.SLIDE_FORWARD
        _backStack.add(routeName)
    }

    /**
     * Thay thế màn hiện tại bằng route mới (dùng cho Splash -> Onboarding 1, không back lại được)
     */
    fun pushReplacement(routeName: String) {
        lastTransition = TransitionType.FADE
        if (_backStack.isNotEmpty()) {
            _backStack[_backStack.lastIndex] = routeName
        } else {
            _backStack.add(routeName)
        }
    }

    /**
     * Nhảy thẳng tới đích và xóa toàn bộ stack trước đó (dùng cho "Bỏ qua" và "Bắt đầu ngay")
     */
    fun pushNamedAndRemoveUntil(routeName: String) {
        lastTransition = TransitionType.FADE
        _backStack.clear()
        _backStack.add(routeName)
    }

    /**
     * Quay về màn trước đó trong stack
     */
    fun pop(): Boolean {
        if (_backStack.size > 1) {
            lastTransition = TransitionType.SLIDE_BACKWARD
            _backStack.removeAt(_backStack.lastIndex)
            return true
        }
        return false
    }

    fun canPop(): Boolean = _backStack.size > 1
}

/**
 * Host hiển thị màn hình hiện tại thông qua `AppRoutes.onGenerateRoute` kèm hiệu ứng:
 * - Fade cho Splash -> Onboarding / Home
 * - Slide ngang cho các bước Onboarding
 */
@Composable
fun AppNavigationHost() {
    val navigator = remember { NavigatorController(initialRoute = AppRoutes.SPLASH) }

    AnimatedContent(
        targetState = navigator.currentRoute,
        transitionSpec = {
            buildTransition(navigator.lastTransition)
        },
        label = "app_route_transition"
    ) { routeName ->
        val screen: AppScreen = AppRoutes.onGenerateRoute(routeName)
        screen.Build(navigator = navigator)
    }
}

private fun buildTransition(type: TransitionType): ContentTransform {
    return when (type) {
        TransitionType.FADE -> {
            fadeIn(animationSpec = tween(420)) togetherWith
                fadeOut(animationSpec = tween(320))
        }
        TransitionType.SLIDE_FORWARD -> {
            (slideInHorizontally(
                animationSpec = tween(350),
                initialOffsetX = { fullWidth -> fullWidth }
            ) + fadeIn(tween(350))) togetherWith
                (slideOutHorizontally(
                    animationSpec = tween(350),
                    targetOffsetX = { fullWidth -> -fullWidth / 3 }
                ) + fadeOut(tween(350)))
        }
        TransitionType.SLIDE_BACKWARD -> {
            (slideInHorizontally(
                animationSpec = tween(320),
                initialOffsetX = { fullWidth -> -fullWidth / 3 }
            ) + fadeIn(tween(320))) togetherWith
                (slideOutHorizontally(
                    animationSpec = tween(320),
                    targetOffsetX = { fullWidth -> fullWidth }
                ) + fadeOut(tween(320)))
        }
    }
}
