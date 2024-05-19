package com.automation.automationtool

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppManager(private val context: Context) {

    private suspend fun isAppInstalled(packageName: String): Boolean = withContext(Dispatchers.IO) {
        try {
            context.packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private suspend fun installApp(packageName: String) {
        // TODO: 实现应用安装逻辑
        Log.d("AppManager", "Installing app: $packageName")
    }

    suspend fun checkAndInstallApps(appList: List<String>) = withContext(Dispatchers.IO) {
        appList.forEach { packageName ->
            if (!isAppInstalled(packageName)) {
                installApp(packageName)
            }
        }
    }
}