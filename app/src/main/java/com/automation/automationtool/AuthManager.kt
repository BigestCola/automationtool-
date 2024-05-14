import android.content.Context
import android.os.AsyncTask
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

class AuthManager(private val context: Context) {

    companion object {
        private const val AUTH_SERVER_URL = "http://192.168.3.87:8200/"
    }

    private val cdkeyService: CDKeyService = RetrofitClient.createService(CDKeyService::class.java)

    fun checkCDKeyValidity(callback: (Boolean, String?) -> Unit) {
        val cdkey = getCDKeyFromPreferences()
        if (cdkey.isNullOrEmpty()) {
            callback(false, null)
        } else {
            verifyCDKey(cdkey, callback)
        }
    }

    fun saveCDKeyToPreferences(cdkey: String) {
        // 将CDKey保存到SharedPreferences
        // ...
    }

    private fun getCDKeyFromPreferences(): String? {
        // 从SharedPreferences获取CDKey
        // ...
        return null
    }

    private fun verifyCDKey(cdkey: String, callback: (Boolean, String?) -> Unit) {
        val deviceId = getDeviceId()
        val appVersion = getAppVersion()

        val request = CDKeyVerifyRequest(cdkey, deviceId, appVersion)
        val call = cdkeyService.verifyCDKey(request)

        call.enqueue(object : Callback<CDKeyVerifyResponse> {
            override fun onResponse(call: Call<CDKeyVerifyResponse>, response: Response<CDKeyVerifyResponse>) {
                if (response.isSuccessful) {
                    val verifyResponse = response.body()
                    if (verifyResponse != null) {
                        if (verifyResponse.status == 1) {
                            // CDKey验证成功
                            callback(true, verifyResponse.expire_time)
                        } else {
                            // CDKey验证失败
                            callback(false, verifyResponse.error)
                        }
                    } else {
                        callback(false, "Empty response")
                    }
                } else {
                    callback(false, "HTTP error ${response.code()}")
                }
            }

            override fun onFailure(call: Call<CDKeyVerifyResponse>, t: Throwable) {
                callback(false, t.message)
            }
        })
    }

    private fun getDeviceId(): String {
        // 获取设备ID
        // ...
        return "device_id"
    }

    private fun getAppVersion(): String {
        // 获取应用版本号
        // ...
        return "app_version"
    }
}

interface CDKeyService {
    @POST("/cdkey/verify/")
    fun verifyCDKey(
        @Body request: CDKeyVerifyRequest
    ): Call<CDKeyVerifyResponse>
}

data class CDKeyVerifyRequest(
    val cdkey: String,
    val device_id: String,
    val app_version: String
)

data class CDKeyVerifyResponse(
    val status: Int,
    val error: String?,
    val expire_time: String?
)

object RetrofitClient {
    private const val BASE_URL = "http://192.168.3.87:8200/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }
}