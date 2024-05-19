package com.automation.automationtool

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

data class CDKeyResponse(
    val id: Int,
    val key: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("expires_at") val expiresAt: String,
    @SerializedName("created_by") val createdBy: Int,
    @SerializedName("used_by") val usedBy: String?,
    @SerializedName("used_at") val usedAt: String?,
    @SerializedName("validity_days") val validityDays: Int,
    @SerializedName("is_used_field") val isUsedField: Boolean
)

class AuthManager(private val context: Context) {
    private val client = HttpClient(Android)
    private val baseUrl = "http://192.168.3.87:8200"
    private val gson = Gson()

    private suspend fun verifyCDKey(cdKey: String): Boolean = withContext(Dispatchers.IO) {
        try {
            Log.d("AuthManager", "Verifying CDKey: $cdKey")
            val response: HttpResponse = client.get("$baseUrl/cdkey") {
                parameter("cd_key", cdKey)
            }

            if (!response.status.isSuccess()) {
                Log.e("AuthManager", "Error response from server: ${response.status}")
                return@withContext false
            }

            val responseString: String = response.bodyAsText()
            Log.d("AuthManager", "Received response: $responseString")

            val cdKeyResponse = gson.fromJson(responseString, CDKeyResponse::class.java)
            !cdKeyResponse.isUsedField
        } catch (e: Exception) {
            Log.e("AuthManager", "Error while verifying CDKey", e)
            false
        }
    }

    fun checkAuthorizationStatus(callback: (Boolean) -> Unit) {
        val cdKey = getCDKeyFromPreferences()
        callback(cdKey != null)
    }

    suspend fun authorizeCDKey(cdKey: String, callback: (Boolean) -> Unit) {
        val isValid = verifyCDKey(cdKey)
        if (isValid) {
            saveCDKeyToPreferences(cdKey)
        }
        withContext(Dispatchers.Main) {
            callback(isValid)
        }
    }

    internal fun getCDKeyFromPreferences(): String? {
        val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("cd_key", null)
    }

    private fun saveCDKeyToPreferences(cdKey: String) {
        val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("cd_key", cdKey).apply()
    }

    private fun parseDate(dateString: String): Date? {
        return try {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.getDefault()).parse(dateString)
        } catch (e: Exception) {
            Log.e("AuthManager", "Error parsing date", e)
            null
        }
    }
}