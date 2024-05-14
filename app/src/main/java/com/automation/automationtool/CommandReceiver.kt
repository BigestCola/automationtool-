// CommandReceiver.kt

package com.automation.automationtool

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.AsyncTask

class CommandReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val command = intent.getStringExtra("command")
        val isShellCommand = intent.getBooleanExtra("is_shell", false)

        if (command != null) {
            executeCommand(context, command, isShellCommand)
        }
    }

    private fun executeCommand(context: Context, command: String, isShellCommand: Boolean) {
        val task = object : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void?): Void? {
                if (isShellCommand) {
                    executeShellCommand(command)
                } else {
                    executeAdbCommand(command)
                }
                return null
            }
        }
        task.execute()
    }

    private fun executeShellCommand(command: String) {
        // 执行Shell命令
        // ...
    }

    private fun executeAdbCommand(command: String) {
        // 执行ADB命令
        // ...
    }
}