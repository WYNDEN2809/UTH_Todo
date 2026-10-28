package com.example.apputh_todo.controllers

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.apputh_todo.data.repository.FirebaseTodoRepository
import com.example.apputh_todo.data.repository.TodoRepository
import com.example.apputh_todo.models.TodoCategory
import com.example.apputh_todo.models.TodoItem
import com.example.apputh_todo.models.TodoPriority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * ============================================================================
 * [OOP - ENCAPSULATION (TÍNH ĐÓNG GÓI)]
 * `TodoController` quản lý trạng thái danh sách Todo và điều phối việc đẩy dữ
 * liệu lên Firebase Cloud Firestore thông qua `TodoRepository`.
 * ============================================================================
 */
class TodoController(
    context: Context,
    private val scope: CoroutineScope,
    private val repository: TodoRepository = FirebaseTodoRepository(context)
) {
    private val _todos = mutableStateListOf<TodoItem>()
    val todos: List<TodoItem> get() = _todos

    var isLoading: Boolean by mutableStateOf(false)
        private set

    var statusMessage: String? by mutableStateOf(null)
        private set

    var isFirebaseReady: Boolean by mutableStateOf(repository.isFirebaseConfigured)
        private set

    var currentUserId: String by mutableStateOf(repository.currentUserId)
        private set

    private var realtimeJob: Job? = null

    init {
        if (isFirebaseReady) {
            startRealtimeSync()
        } else {
            statusMessage = "Chưa có file app/google-services.json. Đang chạy chế độ xem trước cục bộ."
        }
    }

    /**
     * Bắt đầu lắng nghe dữ liệu realtime từ collection `todos` trên Cloud Firestore
     */
    fun startRealtimeSync() {
        if (!repository.isFirebaseConfigured) return
        realtimeJob?.cancel()
        realtimeJob = scope.launch {
            repository.observeTodosRealtime().collectLatest { remoteTodos ->
                _todos.clear()
                _todos.addAll(remoteTodos)
            }
        }
    }

    /**
     * Đăng nhập ẩn danh qua Firebase Auth để lấy UID và đồng bộ dữ liệu
     */
    fun signInAnonymously() {
        scope.launch {
            isLoading = true
            val result = repository.ensureSignedIn()
            isLoading = false
            result.onSuccess { uid ->
                currentUserId = uid
                statusMessage = "Đã xác thực Firebase Auth! UID: ${uid.take(10)}..."
                startRealtimeSync()
            }.onFailure { err ->
                statusMessage = "Lỗi Firebase Auth: ${err.localizedMessage}"
            }
        }
    }

    /**
     * Thêm một công việc mới và đẩy lên Cloud Firestore
     */
    fun addTodo(
        title: String,
        description: String,
        category: TodoCategory,
        priority: TodoPriority = TodoPriority.MEDIUM
    ) {
        if (title.isBlank()) {
            statusMessage = "Vui lòng nhập tiêu đề công việc."
            return
        }

        scope.launch {
            isLoading = true
            val result = repository.addTodo(
                title = title,
                description = description,
                category = category,
                priority = priority
            )
            isLoading = false

            result.onSuccess { uploadedTodo ->
                statusMessage = "Đã đẩy '${uploadedTodo.title}' lên Cloud Firestore!"
                if (!isFirebaseReady) {
                    _todos.add(0, uploadedTodo)
                }
            }.onFailure { err ->
                // Lưu tạm cục bộ nếu chưa có google-services.json để dễ test UI
                val localTodo = TodoItem(
                    userId = currentUserId,
                    title = title.trim(),
                    description = description.trim(),
                    category = category,
                    priority = priority
                )
                _todos.add(0, localTodo)
                statusMessage = "Chưa kết nối Firebase (${err.localizedMessage}). Đã thêm tạm vào danh sách local."
            }
        }
    }

    /**
     * Đẩy bộ 3 công việc mẫu UTH Todo lên Cloud Firestore
     */
    fun uploadSampleTodosToFirebase() {
        scope.launch {
            isLoading = true
            val result = repository.uploadSampleTodos()
            isLoading = false

            result.onSuccess { count ->
                statusMessage = "Đã đẩy thành công $count công việc mẫu lên Cloud Firestore (collection 'todos')!"
            }.onFailure { err ->
                val samples = TodoItem.sampleTodos(currentUserId)
                _todos.addAll(0, samples)
                statusMessage = "Chưa kết nối Firebase: ${err.localizedMessage}. Đã nạp ${samples.size} Todo mẫu vào danh sách local."
            }
        }
    }

    /**
     * Đổi trạng thái hoàn thành của công việc
     */
    fun toggleTodoCompleted(todo: TodoItem) {
        val newStatus = !todo.isCompleted
        scope.launch {
            val result = repository.toggleTodoCompleted(todo.id, newStatus)
            result.onSuccess {
                statusMessage = if (newStatus) "Đã đánh dấu hoàn thành trên Firestore!" else "Đã bỏ đánh dấu hoàn thành."
            }.onFailure {
                val index = _todos.indexOfFirst { it.id == todo.id }
                if (index != -1) {
                    _todos[index] = _todos[index].copy(isCompleted = newStatus)
                }
            }
        }
    }

    /**
     * Xóa công việc khỏi Firestore
     */
    fun deleteTodo(todo: TodoItem) {
        scope.launch {
            val result = repository.deleteTodo(todo.id)
            result.onSuccess {
                statusMessage = "Đã xóa '${todo.title}' khỏi Firestore."
            }.onFailure {
                _todos.removeAll { it.id == todo.id }
                statusMessage = "Đã xóa '${todo.title}' khỏi danh sách local."
            }
        }
    }
}
