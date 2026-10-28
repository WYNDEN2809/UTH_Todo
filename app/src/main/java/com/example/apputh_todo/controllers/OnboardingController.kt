package com.example.apputh_todo.controllers

import android.content.Context
import com.example.apputh_todo.R
import com.example.apputh_todo.core.AppRoutes
import com.example.apputh_todo.core.NavigatorController
import com.example.apputh_todo.models.FinalOnboardingPage
import com.example.apputh_todo.models.OnboardingPage

/**
 * ============================================================================
 * [OOP - ENCAPSULATION (TÍNH ĐÓNG GÓI)]
 * `OnboardingController` giữ `_currentIndex` và danh sách 3 đối tượng `_pages`
 * ở phạm vi `private`, chỉ cung cấp các phương thức điều khiển:
 * `next()`, `previous()`, `skip()`, `completeToHome()`.
 * ============================================================================
 */
class OnboardingController(
    initialIndex: Int,
    private val navigator: NavigatorController,
    private val context: Context
) {
    // Biến trạng thái đóng gói private
    private var _currentIndex: Int = initialIndex.coerceIn(0, _pages.size - 1)

    // Getter công khai
    val currentIndex: Int get() = _currentIndex
    val pages: List<OnboardingPage> get() = _pages
    val currentPage: OnboardingPage get() = _pages[_currentIndex]

    /**
     * Đi tới bước Onboarding kế tiếp (Bước 1 -> Bước 2 -> Bước 3)
     */
    fun next() {
        if (_currentIndex < _pages.size - 1) {
            _currentIndex++
            navigator.pushNamed(routeForStep(_currentIndex))
        }
    }

    /**
     * Quay lại bước trước đó (hỗ trợ nút Back hệ thống ở bước 2, 3)
     */
    fun previous(): Boolean {
        return if (_currentIndex > 0 && navigator.canPop()) {
            _currentIndex--
            navigator.pop()
        } else {
            false
        }
    }

    /**
     * "Bỏ qua": Lưu cờ đã xem và nhảy thẳng tới Home, xóa toàn bộ stack onboarding
     */
    fun skip() {
        markOnboardingAsSeen(context)
        navigator.pushNamedAndRemoveUntil(AppRoutes.HOME)
    }

    /**
     * "Bắt đầu ngay" ở bước cuối: Lưu cờ đã xem, chuyển sang Home và không cho quay lại
     */
    fun completeToHome() {
        markOnboardingAsSeen(context)
        navigator.pushNamedAndRemoveUntil(AppRoutes.HOME)
    }

    private fun routeForStep(index: Int): String {
        return when (index) {
            0 -> AppRoutes.ONBOARDING_1
            1 -> AppRoutes.ONBOARDING_2
            else -> AppRoutes.ONBOARDING_3
        }
    }

    companion object {
        private const val PREFS_NAME = "uth_todo_prefs"
        private const val KEY_SEEN_ONBOARDING = "has_seen_onboarding_v2"

        /**
         * ====================================================================
         * Khởi tạo 3 đối tượng (3 objects) từ cùng 1 class `OnboardingPage`,
         * mỗi đối tượng truyền vào 3 tham số: (picture, title: String, description: String)
         * ====================================================================
         */
        private val _pages: List<OnboardingPage> = listOf(
            // Object 1: Bước 1
            OnboardingPage(
                _picture = R.drawable.img_onboarding_1,
                _title = "Mọi việc, gọn trong một chỗ",
                _description = "Ghi lại bài tập, việc nhà, lịch hẹn. Không còn quên việc nào.",
                _buttonText = "Tiếp tục"
            ),
            // Object 2: Bước 2
            OnboardingPage(
                _picture = R.drawable.img_onboarding_2,
                _title = "Chia theo Học tập và Cuộc sống",
                _description = "Mỗi việc có nhóm riêng, nhìn là biết hôm nay cần lo gì.",
                _buttonText = "Tiếp tục"
            ),
            // Object 3: Bước 3 (Kế thừa OnboardingPage và override onNext)
            FinalOnboardingPage(
                picture = R.drawable.img_onboarding_3,
                title = "Nhắc đúng lúc",
                description = "Bật thông báo để UTH Todo nhắc bạn trước hạn chót.",
                buttonText = "Bắt đầu ngay"
            )
        )

        /**
         * [Nâng cao] Lưu cờ "đã xem onboarding" vào SharedPreferences
         */
        fun markOnboardingAsSeen(context: Context) {
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(KEY_SEEN_ONBOARDING, true)
                .apply()
        }

        /**
         * [Nâng cao] Kiểm tra xem người dùng đã hoàn thành/bỏ qua onboarding chưa
         */
        fun hasSeenOnboarding(context: Context): Boolean {
            return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getBoolean(KEY_SEEN_ONBOARDING, false)
        }

        /**
         * Xóa cờ đã xem để dễ dàng kiểm thử lại luồng từ màn Home
         */
        fun resetOnboardingSeen(context: Context) {
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .remove(KEY_SEEN_ONBOARDING)
                .apply()
        }
    }
}
