package com.lq.ble_core.connect

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattService

/*
* 低功耗的操作
* 扫描——连接——连接状态变更——发现服务——协商mtu——打开通知订阅数据变更——发送数据
* */
sealed class ConnectStatus {
    data object Idle : ConnectStatus()

    data class Connecting(val device: BluetoothDevice) : ConnectStatus()

    data class Connected(val device: BluetoothDevice) : ConnectStatus()
    data class ServicesDiscovered(
        val device: BluetoothDevice,
        val services: List<BluetoothGattService>
    ) : ConnectStatus()

    data class MtuNegotiated(
        val device: BluetoothDevice,
        val services: List<BluetoothGattService>,
        val mtu: Int
    ) : ConnectStatus()

    data class Ready(
        val device: BluetoothDevice,
        val services: List<BluetoothGattService>,
        val mtu: Int
    ) : ConnectStatus()

    data object Disconnecting : ConnectStatus()

    data class Disconnected(val device: BluetoothDevice?) : ConnectStatus()
    data class Error(val message: String, val device: BluetoothDevice?) : ConnectStatus()
}