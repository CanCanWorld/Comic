package com.zrq.comic.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.telephony.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

/**
 * 基站信息
 * isp:00移动,01联通,02电信,404未知 -- 对应mnc
 * cellid 基站编号
 * lac 扇区号
 */
object CellInfo {

    const val CELL_ID = 0
    const val LAC = 1
    const val MNC = 2
    private lateinit var telephonyManager: TelephonyManager
    private const val TAG = "CellInfo"

    fun init(context: Context) {
        telephonyManager = context.getSystemService(AppCompatActivity.TELEPHONY_SERVICE) as TelephonyManager
    }

    //获取基站信息--选择信号最强的返回
    @SuppressLint("MissingPermission")
    fun getCellInfo(): Array<String> {

        var finalMnc = "-1"
        var finalLac = "-1"
        var finalCellId = "-1"
        var finalRssi = -999   //信号强度
        var result: Array<String> = arrayOf(finalCellId, finalLac, finalMnc)

        var mnc = -1
        var lac = -1
        var cellId = -1
        var rssi = -999   //信号强度

        val infos = telephonyManager.allCellInfo ?: return result

        for (i in infos.indices) {

            when (val info = infos[i]) {
                //判断主流通信技术
                //电信2g
                is CellInfoCdma -> {
                    val cellIdentityCdma = info.cellIdentity
                    mnc = cellIdentityCdma.systemId
                    lac = cellIdentityCdma.networkId
                    cellId = cellIdentityCdma.basestationId
                    rssi = info.cellSignalStrength.cdmaDbm
                }
                //2g
                is CellInfoGsm -> {
                    val cellIdentityGsm = info.cellIdentity
                    mnc = cellIdentityGsm.mnc
                    lac = cellIdentityGsm.lac
                    cellId = cellIdentityGsm.cid
                    rssi = info.cellSignalStrength.dbm
                }
                //3g-4g
                is CellInfoLte -> {
                    val cellIdentityLte = info.cellIdentity
                    mnc = cellIdentityLte.mnc
                    lac = cellIdentityLte.tac
                    cellId = cellIdentityLte.ci
                    rssi = info.cellSignalStrength.dbm
                }
                //3g
                is CellInfoWcdma -> {
                    val cellIdentityWcdma = info.cellIdentity
                    mnc = cellIdentityWcdma.mnc
                    lac = cellIdentityWcdma.lac
                    cellId = cellIdentityWcdma.cid
                    rssi = info.cellSignalStrength.dbm
                }
                else -> {
                    break
                }
            }

            if (mnc == Int.MAX_VALUE) {
                break
            }

            if (i == 0 || finalRssi < rssi) {
                finalRssi = rssi
                finalLac = lac.toString()
                finalCellId = cellId.toString()
                finalMnc = when (mnc) {
                    0, 2, 4, 7 -> "00"
                    1, 6, 9 -> "01"
                    3, 5, 11 -> "02"
                    else -> "404"
                }
                if (!checkSimCard()) {
                    finalMnc = "404"
                }
            }
        }
        result = arrayOf(finalCellId, finalLac, finalMnc)
        return result
    }

    /**
     * 判断是否有sim卡 -- 无sim卡时mnc返回404
     */
    @SuppressLint("HardwareIds")
    private fun checkSimCard(): Boolean {
        val sim = telephonyManager.subscriberId
        return if (sim == null || sim.isEmpty()) {
            false
        } else {
            true
        }
    }

}