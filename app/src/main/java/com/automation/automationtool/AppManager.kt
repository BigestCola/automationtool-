// AppManager.kt

package com.automation.automationtool

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInstaller
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

class AppManager(private val context: Context) {

    fun installApp(apkFile: String) {
        val file = File(apkFile)
        val intent = Intent(Intent.ACTION_VIEW)
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        intent.setDataAndType(uri, "application/vnd.android.package-archive")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(intent)
    }

    fun uninstallApp(packageName: String) {
        val intent = Intent(Intent.ACTION_DELETE)
        intent.data = Uri.parse("package:$packageName")
        context.startActivity(intent)
    }
}