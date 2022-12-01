package com.zrq.comic.util

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import kotlin.math.*

/**
 * gps信息
 * 优先gps定位，如果获取到了gps，就不用再获取基站
 * gps采用84标准的即可，不用再转换，默认应该就是这个模式
 * 如果是gps定位，当检测到上次和本次距离大约1000m就上传
 */
object GpsInfo {

    private const val TAG = "GpsInfo"
    const val LONGITUDE = 0
    const val LATITUDE = 1
    private lateinit var locationManager: LocationManager

    fun init(context: Context) {
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    @SuppressLint("MissingPermission")
    fun getGpsInfo(): Array<String> {
        var result: Array<String> = arrayOf("-1", "-1")

        val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        if (location != null) {
            val latitude = location.latitude //经度
            val longitude = location.longitude //纬度
            result = arrayOf(latitude.toString(), longitude.toString())
        }
        return result
    }

    // WGS84标准参考椭球中的地球长半径(单位:米)
    private const val EARTH_RADIUS_WGS84 = 6378137.0

    /**
     * 计算两个坐标的距离(粗略计算，单位:米)
     * 计算公式参照 google map 的距离计算
     *
     * @param lat1 坐标1纬度
     * @param lng1 坐标1经度
     * @param lat2 坐标2纬度
     * @param lng2 坐标2经度
     * @return
     */
    fun distance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
        val radLat1 = Math.toRadians(lat1)
        val radLat2 = Math.toRadians(lat2)
        val a = radLat1 - radLat2
        val b = Math.toRadians(lng1) - Math.toRadians(lng2)
        val s = 2 * asin(sqrt(sin(a / 2).pow(2.0) + cos(radLat1) * cos(radLat2) * sin(b / 2).pow(2.0)))
        return (s * EARTH_RADIUS_WGS84).roundToInt().toDouble()
    }

}