package com.lq.ble_core.data

import android.bluetooth.BluetoothDevice

data class BleDevice(
    val address: String? = "", //设备地址 也是mac
    val rssi: Int? = 0, //信号量
    val deviceName: String? = " ", //设备名称
    val type: Int? = BluetoothDevice.DEVICE_TYPE_UNKNOWN, //设备类型
    val device: BluetoothDevice?=null
)