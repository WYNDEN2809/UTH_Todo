package com.example.apputh_todo.data.repository

import android.content.Context
import com.example.apputh_todo.data.firebase.FirebaseAuthService
import com.example.apputh_todo.data.firebase.FirebaseManager
import com.example.apputh_todo.data.firebase.FirestoreTodoService
import com.example.apputh_todo.models.TodoCategory
import com.example.apputh_todo.models.TodoItem
import com.example.apputh_todo.models.TodoPriority
import kotlinx.coroutines.flow.Flow

/**
 * ============================================================================
 * [OOP - ABSTRACTION & POLYMORPHISM (TRỪU TƯỢNG & ĐA HÌNH)]
 * Interface `TodoRepository` định nghĩa các hành vi chuẩn để quản lý và đồng bộ
 * dữ liệu Todo lên Cloud.
 * ============================================================================
 */
interface TodoRepository {
    val isFirebaseConfigured: Boolean
    val currentUserId: String

    suspend fun ensureSignedIn(): Result<String>
    suspend fun addTodo(
        title: String,
        description: String,
        category: TodoCategory,
        priority: TodoPriority = TodoPriority.MEDIUM
    ): Result<TodoItem>

    suspend fun uploadSampleTodos(): Result<Int>
    suspend fun fetchTodos(): Result<List<TodoItem>>
    fun observeTodosRealtime(): Flow<List<TodoItem>>
    suspend fun toggleTodoCompleted(todoId: String, isCompleted: Boolean): Result<Unit>
    suspend fun deleteTodo(todoId: String): Result<Unit>
}

/**
 * ============================================================================
 * Triển khai `TodoRepository` sử dụng Firebase Authentication & Cloud Firestore.
 * ============================================================================
 */
class FirebaseTodoRepository(
    private val context: Context,
    private val authService: FirebaseAuthService = FirebaseAuthService(context),
    private val firestoreService: FirestoreTodoService = FirestoreTodoService(context)
) : TodoRepository {

    override val isFirebaseConfigured: Boolean
        get() = FirebaseManager.isConfigured(context)

    override val currentUserId: String
        get() = authService.currentUserId

    override suspend fun ensureSignedIn(): Result<String> {
        if (authService.isSignedIn) {
            return Result.success(authService.currentUserId)
        }
        return authService.signInAnonymously()
    }

    override suspend fun addTodo(
        title: String,
        description: String,
        category: TodoCategory,
        priority: TodoPriority
    ): Result<TodoItem> {
        val todo = TodoItem(
            userId = currentUserId,
            title = title.trim(),
            description = description.trim(),
            category = category,
            priority = priority
        )
        return firestoreService.uploadTodo(todo)
    }

    override suspend fun uploadSampleTodos(): Result<Int> {
        val samples = TodoItem.sampleTodos(userId = currentUserId)
        return firestoreService.uploadBatchTodos(samples)
    }

    override suspend fun fetchTodos(): Result<List<TodoItem>> {
        return firestoreService.fetchTodos(userId = null)
    }

    override fun observeTodosRealtime(): Flow<List<TodoItem>> {
        return firestoreService.observeTodosRealtime(userId = null)
    }

    override suspend fun toggleTodoCompleted(todoId: String, isCompleted: Boolean): Result<Unit> {
        return firestoreService.updateCompletedStatus(todoId, isCompleted)
    }

    override suspend fun deleteTodo(todoId: String): Result<Unit> {
        return firestoreService.deleteTodo(todoId)
    }
}
