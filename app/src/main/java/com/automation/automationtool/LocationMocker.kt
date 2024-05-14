// LocationMocker.kt

package com.automation.automationtool

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.SystemClock
import java.util.*

class LocationMocker(private val context: Context) {

    companion object {
        private const val LOCATION_SERVER_URL = "http://example.com/location"
    }

    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    fun mockLocation() {
        val realLocation = getRealLocation()
        uploadRealLocation(realLocation)

        val mockedLocation = generateMockedLocation()
        setMockedLocation(mockedLocation)
    }

    private fun getRealLocation(): Location {
        // 这里需要添加获取真实位置的实际逻辑
        // 以下是一个示例实现，返回一个固定位置

        val location = Location(LocationManager.GPS_PROVIDER) // 创建一个新的位置对象
        location.latitude = 37.4219999  // 纬度，示例为谷歌总部的纬度
        location.longitude = -122.0840575  // 经度，示例为谷歌总部的经度
        location.accuracy = 20.0f  // 精度，示例值
        location.time = System.currentTimeMillis()  // 设置当前时间为位置的时间戳
        location.elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()  // 设置系统启动后的纳秒数

        return location  // 返回位置对象
    }


    private fun uploadRealLocation(location: Location) {
        // 上传真实位置到服务器
        // ...
    }

    private fun generateMockedLocation(): Location {
        // 生成伪造位置
        val random = Random()
        val latitude = random.nextDouble() * 180 - 90
        val longitude = random.nextDouble() * 360 - 180
        val accuracy = random.nextFloat() * 50

        val mockedLocation = Location(LocationManager.GPS_PROVIDER)
        mockedLocation.latitude = latitude
        mockedLocation.longitude = longitude
        mockedLocation.accuracy = accuracy
        mockedLocation.time = System.currentTimeMillis()
        mockedLocation.elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()

        return mockedLocation
    }

    private fun setMockedLocation(location: Location) {
        // 设置伪造位置
        locationManager.addTestProvider(LocationManager.GPS_PROVIDER, false, false, false, false, true, true, true, 0, 5)
        locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true)
        locationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, location)
    }
}