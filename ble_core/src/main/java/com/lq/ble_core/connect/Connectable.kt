package com.lq.ble_core.connect

import android.bluetooth.BluetoothDevice
import kotlinx.coroutines.flow.StateFlow

interface Connectable {

    val connectStateFlow: StateFlow<ConnectStatus>

    fun connect(device: BluetoothDevice?)

    fun disconnect()

    fun isConnected(): Boolean =
        connectStateFlow.value is ConnectStatus.Connected ||
                connectStateFlow.value is ConnectStatus.ServicesDiscovered ||
                connectStateFlow.value is ConnectStatus.MtuNegotiated ||
                connectStateFlow.value is ConnectStatus.Ready

    fun getConnectedDevice(): BluetoothDevice? = when (val state = connectStateFlow.value) {
        is ConnectStatus.Connecting -> state.device
        is ConnectStatus.Connected -> state.device
        is ConnectStatus.ServicesDiscovered -> state.device
        is ConnectStatus.MtuNegotiated -> state.device
        is ConnectStatus.Ready -> state.device
        else -> null
    }

}