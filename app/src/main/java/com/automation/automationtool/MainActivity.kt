//MainActivity.kt

package com.automation.automationtool

import android.os.Bundle
import android.content.Context
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.automation.automationtool.AuthManager
import androidx.appcompat.app.AppCompatActivity



class AuthManager(private val context: Context) {

    fun checkAuthorizationStatus(callback: (Boolean) -> Unit) {
        // 检查应用授权状态的逻辑
        // ...
        val isAuthorized = checkAuthorizationLogic()
        callback(isAuthorized)
    }

    private fun checkAuthorizationLogic(): Boolean {
        // 实现检查授权状态的逻辑
        // ...
        return true // 这里仅为示例,返回true表示已授权
    }
}



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    var showAuthDialog by remember { mutableStateOf(false) }

    Column {
        AutomationButton(
            text = "开始自动化服务",
            onClick = {
                AutomationService.start(context)
                Toast.makeText(context, "自动化服务已启动", Toast.LENGTH_SHORT).show()
            }
        )
        AutomationButton(
            text = "停止自动化服务",
            onClick = {
                AutomationService.stop(context)
                Toast.makeText(context, "自动化服务已停止", Toast.LENGTH_SHORT).show()
            }
        )
        if (showAuthDialog) {
            AuthorizationDialog(onDismissRequest = { showAuthDialog = false })
        }
    }

    CheckAuthorizationAndMaybeShowDialog(context) { authorized ->
        showAuthDialog = !authorized
    }
}

@Composable
fun AutomationButton(text: String, onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text)
    }
}

@Composable
fun CheckAuthorizationAndMaybeShowDialog(context: Context, onAuthorizationChecked: (Boolean) -> Unit) {
    val authManager = AuthManager(context)
    LaunchedEffect(key1 = context) {
        authManager.checkAuthorizationStatus { authorized ->
            onAuthorizationChecked(authorized)
        }
    }
}

@Composable
fun AuthorizationDialog(onDismissRequest: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("授权") },
        text = { Text("应用需要授权才能继续使用") },
        confirmButton = {
            Button(onClick = onDismissRequest) {
                Text("确认")
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text("取消")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainScreen()
}
