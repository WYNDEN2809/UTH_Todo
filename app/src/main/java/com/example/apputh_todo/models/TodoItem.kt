package com.example.apputh_todo.models

import com.google.firebase.firestore.DocumentSnapshot
import java.util.UUID

/**
 * Phân nhóm công việc theo đúng tinh thần Onboarding ("Chia theo Học tập và Cuộc sống")
 */
enum class TodoCategory(val displayName: String) {
    STUDY("Học tập"),
    LIFE("Cuộc sống"),
    WORK("Công việc");

    companion object {
        fun fromString(value: String?): TodoCategory {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: STUDY
        }
    }
}

/**
 * Mức độ ưu tiên của công việc
 */
enum class TodoPriority(val displayName: String) {
    HIGH("Cao"),
    MEDIUM("Trung bình"),
    LOW("Thấp");

    companion object {
        fun fromString(value: String?): TodoPriority {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: MEDIUM
        }
    }
}

/**
 * ============================================================================
 * Model `TodoItem` đại diện cho một document trong collection `todos` trên
 * Cloud Firestore.
 * ============================================================================
 */
data class TodoItem(
    val id: String = UUID.randomUUID().toString(),
    val userId: String = "anonymous",
    val title: String = "",
    val description: String = "",
    val category: TodoCategory = TodoCategory.STUDY,
    val priority: TodoPriority = TodoPriority.MEDIUM,
    val isCompleted: Boolean = false,
    val dueDateMillis: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    /**
     * Chuyển đổi đối tượng `TodoItem` sang `Map<String, Any?>` để đẩy lên Cloud Firestore
     */
    fun toFirestoreMap(): Map<String, Any?> {
        return mapOf(
            FIELD_ID to id,
            FIELD_USER_ID to userId,
            FIELD_TITLE to title,
            FIELD_DESCRIPTION to description,
            FIELD_CATEGORY to category.name,
            FIELD_PRIORITY to priority.name,
            FIELD_IS_COMPLETED to isCompleted,
            FIELD_DUE_DATE_MILLIS to dueDateMillis,
            FIELD_CREATED_AT to createdAt,
            FIELD_UPDATED_AT to updatedAt
        )
    }

    companion object {
        const val FIELD_ID = "id"
        const val FIELD_USER_ID = "userId"
        const val FIELD_TITLE = "title"
        const val FIELD_DESCRIPTION = "description"
        const val FIELD_CATEGORY = "category"
        const val FIELD_PRIORITY = "priority"
        const val FIELD_IS_COMPLETED = "isCompleted"
        const val FIELD_DUE_DATE_MILLIS = "dueDateMillis"
        const val FIELD_CREATED_AT = "createdAt"
        const val FIELD_UPDATED_AT = "updatedAt"

        /**
         * Khởi tạo `TodoItem` từ một `DocumentSnapshot` trả về bởi Cloud Firestore
         */
        fun fromFirestoreDocument(doc: DocumentSnapshot): TodoItem? {
            if (!doc.exists()) return null
            return TodoItem(
                id = doc.getString(FIELD_ID) ?: doc.id,
                userId = doc.getString(FIELD_USER_ID) ?: "anonymous",
                title = doc.getString(FIELD_TITLE) ?: "",
                description = doc.getString(FIELD_DESCRIPTION) ?: "",
                category = TodoCategory.fromString(doc.getString(FIELD_CATEGORY)),
                priority = TodoPriority.fromString(doc.getString(FIELD_PRIORITY)),
                isCompleted = doc.getBoolean(FIELD_IS_COMPLETED) ?: false,
                dueDateMillis = doc.getLong(FIELD_DUE_DATE_MILLIS),
                createdAt = doc.getLong(FIELD_CREATED_AT) ?: System.currentTimeMillis(),
                updatedAt = doc.getLong(FIELD_UPDATED_AT) ?: System.currentTimeMillis()
            )
        }

        /**
         * Danh sách dữ liệu mẫu UTH Todo để kiểm thử tính năng đẩy lên Firebase
         */
        fun sampleTodos(userId: String = "anonymous"): List<TodoItem> {
            val now = System.currentTimeMillis()
            return listOf(
                TodoItem(
                    id = UUID.randomUUID().toString(),
                    userId = userId,
                    title = "Hoàn thành bài tập lớn Lập trình Thiết bị Di động",
                    description = "Tích hợp Cloud Firestore & Firebase Auth cho ứng dụng UTH Todo",
                    category = TodoCategory.STUDY,
                    priority = TodoPriority.HIGH,
                    isCompleted = false,
                    createdAt = now - 3600_000L,
                    updatedAt = now
                ),
                TodoItem(
                    id = UUID.randomUUID().toString(),
                    userId = userId,
                    title = "Ôn tập Cấu trúc dữ liệu & Giải thuật",
                    description = "Làm 5 bài tập về đồ thị và cây nhị phân tìm kiếm",
                    category = TodoCategory.STUDY,
                    priority = TodoPriority.MEDIUM,
                    isCompleted = true,
                    createdAt = now - 7200_000L,
                    updatedAt = now
                ),
                TodoItem(
                    id = UUID.randomUUID().toString(),
                    userId = userId,
                    title = "Mua sắm đồ dùng cuối tuần",
                    description = "Mua sữa, trái cây và sổ tay ghi chú mới",
                    category = TodoCategory.LIFE,
                    priority = TodoPriority.LOW,
                    isCompleted = false,
                    createdAt = now - 10800_000L,
                    updatedAt = now
                )
            )
        }
    }
}
