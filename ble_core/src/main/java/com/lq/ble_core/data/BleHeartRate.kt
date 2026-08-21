package com.lq.ble_core.data

import com.lq.ble_core.parse.BleDecoder
import com.lq.ble_core.util.parseList


data class BleHeartRate(
    var mTime: Int = 0, // 距离当地2000/1/1 00:00:00的秒数
    var mBpm: Int = 0, //心率值
    var mType: Int = 0 //类型，0：默认，一般保存起来，1：设备测量界面实时返回的值，用于显示，一般不保存
) : BleDecoder{

    companion object {
        fun parseRateList(byteArray: ByteArray): List<BleHeartRate> {
           val dataList =  byteArray.parseList {
                val rate = BleHeartRate(
                    mTime = int32(),
                    mBpm = int8(),
                    mType = int8(),
                )
               rate
            }
            return dataList
        }
    }

    override fun decode(byteArray: ByteArray)  {

    }
}