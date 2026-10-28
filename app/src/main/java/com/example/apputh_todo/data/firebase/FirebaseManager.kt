package com.example.apputh_todo.data.firebase

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * ============================================================================
 * Singleton quản lý kết nối tới Firebase (Cloud Firestore & Firebase Auth).
 * Hỗ trợ kiểm tra an toàn xem `google-services.json` đã được cấu hình hay chưa
 * để tránh crash ứng dụng khi chạy thử trước khi thêm file cấu hình.
 * ============================================================================
 */
object FirebaseManager {
    const val COLLECTION_TODOS = "todos"
    const val COLLECTION_USERS = "users"

    /**
     * Kiểm tra xem FirebaseApp đã được khởi tạo thành công từ `google-services.json` chưa.
     */
    fun isConfigured(context: Context): Boolean {
        return try {
            FirebaseApp.getApps(context).isNotEmpty() || FirebaseApp.initializeApp(context) != null
        } catch (_: Exception) {
            false
        }
    }

    /**
     * Lấy instance của Cloud Firestore (trả về null nếu chưa có google-services.json)
     */
    fun getFirestoreOrNull(context: Context): FirebaseFirestore? {
        if (!isConfigured(context)) return null
        return try {
            FirebaseFirestore.getInstance()
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Lấy instance của FirebaseAuth (trả về null nếu chưa có google-services.json)
     */
    fun getAuthOrNull(context: Context): FirebaseAuth? {
        if (!isConfigured(context)) return null
        return try {
            FirebaseAuth.getInstance()
        } catch (_: Exception) {
            null
        }
    }
}
