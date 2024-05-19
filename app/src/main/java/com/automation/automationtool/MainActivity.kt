// MainActivity.kt

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
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import kotlinx.coroutines.*
import android.util.Log



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
    val authManager = AuthManager(context)
    var isAuthorized by remember { mutableStateOf(false) }
    var showAuthDialog by remember { mutableStateOf(true) }

    // 在应用程序启动时检查 CDKey 是否已经存储
    LaunchedEffect(Unit) {
        val storedCDKey = authManager.getCDKeyFromPreferences()
        Log.d("MainScreen", "Stored CDKey: $storedCDKey")
        if (!storedCDKey.isNullOrEmpty()) {
            isAuthorized = true
            showAuthDialog = false
        }
    }

    if (!isAuthorized) {
        if (showAuthDialog) {
            AuthorizationDialog(
                onDismissRequest = { showAuthDialog = false },
                onAuthorizationChecked = { authorized ->
                    isAuthorized = authorized
                    showAuthDialog = !authorized
                }
            )
        } else {
            // 如果用户关闭了对话框但未授权,则退出应用
            LaunchedEffect(Unit) {
                (context as? ComponentActivity)?.finish()
            }
        }
    } else {
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
        }
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
fun AuthorizationDialog(
    onDismissRequest: () -> Unit,
    onAuthorizationChecked: (Boolean) -> Unit
) {
    val context = LocalContext.current
    var cdKey by remember { mutableStateOf("") }
    val authManager = AuthManager(context)
    var isVerifying by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var showErrorMessage by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("授权") },
        text = {
            Column {
                Text("应用需要授权才能继续使用")
                TextField(
                    value = cdKey,
                    onValueChange = { cdKey = it },
                    label = { Text("请输入CDKey") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { isVerifying = true },
                enabled = !isVerifying
            ) {
                Text("确认")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismissRequest,
                enabled = !isVerifying
            ) {
                Text("取消")
            }
        }
    )

    if (showErrorMessage) {
        Text("CDKey 验证失败,请输入有效的 CDKey。")
    }

    LaunchedEffect(isVerifying) {
        if (isVerifying) {
            coroutineScope.launch {
                authManager.authorizeCDKey(cdKey) { authorized ->
                    if (authorized) {
                        onAuthorizationChecked(true)
                    } else {
                        showErrorMessage = true
                    }
                    isVerifying = false
                }
            }
        }
    }
}

private fun saveCDKeyToPreferences(context: Context, cdKey: String) {
    val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().putString("cd_key", cdKey).apply()
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainScreen()
}