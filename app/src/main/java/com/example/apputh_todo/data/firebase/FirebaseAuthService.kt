package com.example.apputh_todo.data.firebase

import android.content.Context
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

/**
 * ============================================================================
 * Service xử lý xác thực người dùng qua Firebase Authentication.
 * Hỗ trợ:
 *  - Đăng nhập ẩn danh (`signInAnonymously`) để đẩy Todo theo UID thiết bị
 *  - Đăng nhập / Đăng ký bằng Email & Password
 *  - Đăng xuất
 * ============================================================================
 */
class FirebaseAuthService(private val context: Context) {

    val currentUser: FirebaseUser?
        get() = FirebaseManager.getAuthOrNull(context)?.currentUser

    val currentUserId: String
        get() = currentUser?.uid ?: "guest_local"

    val isSignedIn: Boolean
        get() = currentUser != null

    /**
     * Đăng nhập ẩn danh trên Firebase Auth để lấy UID riêng cho người dùng hiện tại
     */
    suspend fun signInAnonymously(): Result<String> {
        val auth = FirebaseManager.getAuthOrNull(context)
            ?: return Result.failure(
                IllegalStateException("Chưa tìm thấy cấu hình Firebase (google-services.json trong thư mục app/).")
            )

        return try {
            val authResult = auth.signInAnonymously().await()
            val uid = authResult.user?.uid ?: "anonymous"
            Result.success(uid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Đăng nhập bằng Email và Mật khẩu
     */
    suspend fun signInWithEmail(email: String, password: String): Result<String> {
        val auth = FirebaseManager.getAuthOrNull(context)
            ?: return Result.failure(
                IllegalStateException("Chưa tìm thấy cấu hình Firebase (google-services.json trong thư mục app/).")
            )

        return try {
            val authResult = auth.signInWithEmailAndPassword(email.trim(), password).await()
            Result.success(authResult.user?.uid ?: "")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Đăng ký tài khoản mới bằng Email và Mật khẩu
     */
    suspend fun signUpWithEmail(email: String, password: String): Result<String> {
        val auth = FirebaseManager.getAuthOrNull(context)
            ?: return Result.failure(
                IllegalStateException("Chưa tìm thấy cấu hình Firebase (google-services.json trong thư mục app/).")
            )

        return try {
            val authResult = auth.createUserWithEmailAndPassword(email.trim(), password).await()
            Result.success(authResult.user?.uid ?: "")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Đăng xuất khỏi Firebase Auth
     */
    fun signOut() {
        FirebaseManager.getAuthOrNull(context)?.signOut()
    }
}
