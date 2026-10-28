package com.example.apputh_todo.models

import androidx.annotation.DrawableRes
import com.example.apputh_todo.controllers.OnboardingController

/**
 * ============================================================================
 * [OOP - ENCAPSULATION (TÍNH ĐÓNG GÓI)]
 * Class `OnboardingPage` đóng gói dữ liệu của một trang Onboarding gồm đúng
 * 3 thành phần chính:
 *   1. `_picture`     (@DrawableRes Int - Hình ảnh minh họa)
 *   2. `_title`       (String - Tiêu đề)
 *   3. `_description` (String - Mô tả)
 *   (+ `_buttonText` mặc định là "Tiếp tục")
 *
 * Tất cả các thuộc tính đều được đặt là `private` và chỉ cho phép bên ngoài
 * truy cập thông qua các public getter (`picture`, `title`, `description`, `buttonText`).
 * ============================================================================
 */
open class OnboardingPage(
    @DrawableRes private val _picture: Int,
    private val _title: String,
    private val _description: String,
    private val _buttonText: String = "Tiếp tục"
) {
    // Public Getters (chỉ cho phép đọc - Read-only)
    @get:DrawableRes
    val picture: Int get() = _picture
    val title: String get() = _title
    val description: String get() = _description
    val buttonText: String get() = _buttonText

    /**
     * ========================================================================
     * [OOP - POLYMORPHISM (TÍNH ĐA HÌNH)]
     * Phương thức xử lý khi nhấn nút chính:
     * - Ở trang 1 và trang 2 (`OnboardingPage`): gọi `controller.next()` để sang bước kế.
     * - Ở trang 3 (`FinalOnboardingPage`): override `onNext()` để vào thẳng màn Home.
     * ========================================================================
     */
    open fun onNext(controller: OnboardingController) {
        controller.next()
    }
}

/**
 * ============================================================================
 * [OOP - INHERITANCE (TÍNH KẾ THỪA) & POLYMORPHISM (TÍNH ĐA HÌNH)]
 * `FinalOnboardingPage` kế thừa từ `OnboardingPage` (vẫn nhận `picture`, `title`,
 * `description`) và ghi đè (override) phương thức `onNext()` ở bước cuối để
 * chuyển sang màn Home thay vì sang bước kế.
 * ============================================================================
 */
class FinalOnboardingPage(
    @DrawableRes picture: Int,
    title: String,
    description: String,
    buttonText: String = "Bắt đầu ngay"
) : OnboardingPage(
    _picture = picture,
    _title = title,
    _description = description,
    _buttonText = buttonText
) {
    override fun onNext(controller: OnboardingController) {
        controller.completeToHome()
    }
}
