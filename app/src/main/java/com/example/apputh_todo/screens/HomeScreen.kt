package com.example.apputh_todo.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apputh_todo.controllers.OnboardingController
import com.example.apputh_todo.controllers.TodoController
import com.example.apputh_todo.core.AppRoutes
import com.example.apputh_todo.core.AppTheme
import com.example.apputh_todo.core.NavigatorController
import com.example.apputh_todo.models.TodoCategory
import com.example.apputh_todo.models.TodoItem
import com.example.apputh_todo.widgets.CheckmarkIcon

/**
 * ============================================================================
 * [OOP - INHERITANCE (TÍNH KẾ THỪA)]
 * `HomeScreen` kế thừa từ lớp trừu tượng `AppScreen`.
 * Tích hợp `TodoController` -> `TodoRepository` -> `FirestoreTodoService`
 * để thêm mới, đồng bộ và đẩy dữ liệu Todo lên Firebase Cloud Firestore.
 * ============================================================================
 */
class HomeScreen : AppScreen() {

    override val routeName: String = AppRoutes.HOME

    @Composable
    override fun Build(navigator: NavigatorController) {
        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        val todoController = remember { TodoController(context = context, scope = scope) }

        var titleInput by remember { mutableStateOf("") }
        var descInput by remember { mutableStateOf("") }
        var selectedCategory by remember { mutableStateOf(TodoCategory.STUDY) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
                .systemBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // 1. Header & Trạng thái kết nối Firebase
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(AppTheme.PrimaryBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        CheckmarkIcon(
                            modifier = Modifier.size(18.dp),
                            color = Color.White,
                            strokeWidthDp = 2.6f
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "UTH Todo V2",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextDark
                        )
                        Text(
                            text = if (todoController.isFirebaseReady) {
                                "Firebase: Đã kết nối (${todoController.currentUserId.take(8)})"
                            } else {
                                "Firebase: Chờ file google-services.json"
                            },
                            fontSize = 12.sp,
                            color = if (todoController.isFirebaseReady) Color(0xFF16A34A) else Color(0xFFD97706),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                TextButton(
                    onClick = {
                        OnboardingController.resetOnboardingSeen(context)
                        navigator.pushNamedAndRemoveUntil(AppRoutes.SPLASH)
                    }
                ) {
                    Text(
                        text = "Xem lại Onboarding",
                        fontSize = 12.sp,
                        color = AppTheme.PrimaryBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 2. Banner thông báo trạng thái đẩy dữ liệu lên Firebase
            todoController.statusMessage?.let { msg ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(AppTheme.LightBlueBg)
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = msg,
                        fontSize = 13.sp,
                        color = AppTheme.PrimaryBlue,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // 3. Card Tạo & Đẩy Todo lên Firebase
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Thêm công việc & Đẩy lên Firebase",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.TextDark
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = titleInput,
                        onValueChange = { titleInput = it },
                        label = { Text("Tiêu đề công việc") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = descInput,
                        onValueChange = { descInput = it },
                        label = { Text("Mô tả chi tiết (tùy chọn)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Chọn nhóm: Học tập / Cuộc sống / Công việc
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TodoCategory.entries.forEach { category ->
                            FilterChip(
                                selected = selectedCategory == category,
                                onClick = { selectedCategory = category },
                                label = { Text(category.displayName, fontSize = 12.sp) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                todoController.addTodo(
                                    title = titleInput,
                                    description = descInput,
                                    category = selectedCategory
                                )
                                if (titleInput.isNotBlank()) {
                                    titleInput = ""
                                    descInput = ""
                                }
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AppTheme.PrimaryBlue)
                        ) {
                            if (todoController.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                            }
                            Text("Up lên Firebase", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }

                        OutlinedButton(
                            onClick = { todoController.uploadSampleTodosToFirebase() },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Up 3 Todo mẫu",
                                fontSize = 13.sp,
                                color = AppTheme.PrimaryBlue,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    TextButton(
                        onClick = { todoController.signInAnonymously() },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(
                            text = "Xác thực Firebase Auth (Anonymous)",
                            fontSize = 12.sp,
                            color = AppTheme.TextGrey
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Danh sách Todo (Realtime từ Firestore hoặc Local Preview)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Danh sách công việc (${todoController.todos.size})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.TextDark
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (todoController.todos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Chưa có công việc nào.\nNhấn 'Up 3 Todo mẫu' hoặc thêm mới ở trên để đẩy lên Firebase.",
                        fontSize = 14.sp,
                        color = AppTheme.TextGrey
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(todoController.todos, key = { it.id }) { item ->
                        TodoItemRow(
                            item = item,
                            onToggle = { todoController.toggleTodoCompleted(item) },
                            onDelete = { todoController.deleteTodo(item) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TodoItemRow(
    item: TodoItem,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    val isStudy = item.category == TodoCategory.STUDY
    val tagBg = if (isStudy) AppTheme.StudyTagBg else AppTheme.LifeTagBg
    val tagText = if (isStudy) AppTheme.StudyTagText else AppTheme.LifeTagText

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.isCompleted,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(checkedColor = AppTheme.PrimaryBlue)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (item.isCompleted) AppTheme.TextGrey else AppTheme.TextDark,
                    textDecoration = if (item.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (item.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.description,
                        fontSize = 13.sp,
                        color = AppTheme.TextGrey,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(tagBg)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = item.category.displayName,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = tagText
                    )
                }
            }

            TextButton(onClick = onDelete) {
                Text(
                    text = "Xóa",
                    fontSize = 12.sp,
                    color = Color(0xFFEF4444)
                )
            }
        }
    }
}
