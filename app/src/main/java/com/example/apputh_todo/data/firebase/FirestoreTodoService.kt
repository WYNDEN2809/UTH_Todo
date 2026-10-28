package com.example.apputh_todo.data.firebase

import android.content.Context
import com.example.apputh_todo.models.TodoItem
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * ============================================================================
 * Service giao tiếp trực tiếp với Cloud Firestore (collection `todos`).
 * Chịu trách nhiệm:
 *  - Đẩy (upload/create/update) 1 hoặc nhiều `TodoItem` lên Firestore
 *  - Lắng nghe dữ liệu Todo theo thời gian thực (`observeTodos`)
 *  - Cập nhật trạng thái hoàn thành (`updateCompletedStatus`)
 *  - Xóa công việc (`deleteTodo`)
 * ============================================================================
 */
class FirestoreTodoService(private val context: Context) {

    /**
     * Đẩy 1 công việc (`TodoItem`) lên Cloud Firestore (collection `todos/{todo.id}`)
     */
    suspend fun uploadTodo(todo: TodoItem): Result<TodoItem> {
        val firestore = FirebaseManager.getFirestoreOrNull(context)
            ?: return Result.failure(
                IllegalStateException("Chưa cấu hình google-services.json trong thư mục app/.")
            )

        return try {
            firestore.collection(FirebaseManager.COLLECTION_TODOS)
                .document(todo.id)
                .set(todo.toFirestoreMap())
                .await()
            Result.success(todo)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Đẩy hàng loạt công việc (`List<TodoItem>`) lên Cloud Firestore bằng WriteBatch
     */
    suspend fun uploadBatchTodos(todos: List<TodoItem>): Result<Int> {
        val firestore = FirebaseManager.getFirestoreOrNull(context)
            ?: return Result.failure(
                IllegalStateException("Chưa cấu hình google-services.json trong thư mục app/.")
            )

        return try {
            val batch = firestore.batch()
            val collectionRef = firestore.collection(FirebaseManager.COLLECTION_TODOS)
            for (todo in todos) {
                val docRef = collectionRef.document(todo.id)
                batch.set(docRef, todo.toFirestoreMap())
            }
            batch.commit().await()
            Result.success(todos.size)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Lấy danh sách Todo từ Cloud Firestore (1 lần)
     */
    suspend fun fetchTodos(userId: String? = null): Result<List<TodoItem>> {
        val firestore = FirebaseManager.getFirestoreOrNull(context)
            ?: return Result.failure(
                IllegalStateException("Chưa cấu hình google-services.json trong thư mục app/.")
            )

        return try {
            var query: Query = firestore.collection(FirebaseManager.COLLECTION_TODOS)
            if (!userId.isNullOrBlank()) {
                query = query.whereEqualTo(TodoItem.FIELD_USER_ID, userId)
            }
            val snapshot = query.get().await()
            val items = snapshot.documents
                .mapNotNull { TodoItem.fromFirestoreDocument(it) }
                .sortedByDescending { it.createdAt }
            Result.success(items)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Lắng nghe danh sách Todo thay đổi theo thời gian thực (Realtime SnapshotListener)
     */
    fun observeTodosRealtime(userId: String? = null): Flow<List<TodoItem>> = callbackFlow {
        val firestore = FirebaseManager.getFirestoreOrNull(context)
        if (firestore == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        var query: Query = firestore.collection(FirebaseManager.COLLECTION_TODOS)
        if (!userId.isNullOrBlank()) {
            query = query.whereEqualTo(TodoItem.FIELD_USER_ID, userId)
        }

        val registration = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val items = snapshot.documents
                    .mapNotNull { TodoItem.fromFirestoreDocument(it) }
                    .sortedByDescending { it.createdAt }
                trySend(items)
            }
        }

        awaitClose {
            registration.remove()
        }
    }

    /**
     * Cập nhật trạng thái hoàn thành (`isCompleted`) của một Todo trên Firestore
     */
    suspend fun updateCompletedStatus(todoId: String, isCompleted: Boolean): Result<Unit> {
        val firestore = FirebaseManager.getFirestoreOrNull(context)
            ?: return Result.failure(
                IllegalStateException("Chưa cấu hình google-services.json trong thư mục app/.")
            )

        return try {
            firestore.collection(FirebaseManager.COLLECTION_TODOS)
                .document(todoId)
                .update(
                    mapOf(
                        TodoItem.FIELD_IS_COMPLETED to isCompleted,
                        TodoItem.FIELD_UPDATED_AT to System.currentTimeMillis()
                    )
                )
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Xóa 1 Todo khỏi Cloud Firestore theo `todoId`
     */
    suspend fun deleteTodo(todoId: String): Result<Unit> {
        val firestore = FirebaseManager.getFirestoreOrNull(context)
            ?: return Result.failure(
                IllegalStateException("Chưa cấu hình google-services.json trong thư mục app/.")
            )

        return try {
            firestore.collection(FirebaseManager.COLLECTION_TODOS)
                .document(todoId)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
