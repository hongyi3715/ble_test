package com.lq.ble_core.connect

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothProfile
import android.content.pm.PackageManager
import android.os.Build
import android.content.Context
import android.Manifest
import com.lq.lib_logger.LogUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BleConnector @Inject constructor(@param:ApplicationContext private val context: Context) : Connectable {

    private lateinit var gatt: BluetoothGatt

    private val _connectStateFlow = MutableStateFlow<ConnectStatus>(ConnectStatus.Idle)

    override val connectStateFlow: StateFlow<ConnectStatus> = _connectStateFlow.asStateFlow()

    private var autoConnect: Boolean = false

    private var bleDevice:BluetoothDevice ?=null

    private fun hasBluetoothConnectPermission(): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.S ||
                context.checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
    }

    private fun missingBluetoothConnectPermission(message: String) {
        _connectStateFlow.value = ConnectStatus.Error(message, bleDevice)
    }

    private val gattCallback: BluetoothGattCallback = object : BluetoothGattCallback() {

        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            LogUtil.d("onConnectionStateChange: $newState")
            handleConnectionState(gatt, status, newState)
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            negotiateMtu(gatt,status)
        }


        override fun onMtuChanged(gatt: BluetoothGatt?, mtu: Int, status: Int) {
            super.onMtuChanged(gatt, mtu, status)
            LogUtil.d("onMtuChanged: $mtu")
            enableNotifications()
        }

        override fun onDescriptorWrite(
            gatt: BluetoothGatt?,
            descriptor: BluetoothGattDescriptor?,
            status: Int
        ) {
            super.onDescriptorWrite(gatt, descriptor, status)
            LogUtil.d("onDescriptorWrite: $status")
        }


        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray
        ) {
            super.onCharacteristicChanged(gatt, characteristic, value)
            LogUtil.d("onCharacteristicChanged: ${String(value)}")
        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            super.onCharacteristicWrite(gatt, characteristic, status)
            LogUtil.d("onCharacteristicWrite: $status")
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray,
            status: Int
        ) {
            super.onCharacteristicRead(gatt, characteristic, value, status)
            LogUtil.d("onCharacteristicRead: $status")
        }

    }

    private fun handleConnectionState(gatt: BluetoothGatt?, state: Int, newState: Int) {
        if (state == newState || gatt == null) return

        when (newState) {
            BluetoothProfile.STATE_CONNECTED -> { //设备连接
                LogUtil.d("onConnectionStateChange: 设备连接")
                val device = bleDevice ?: return
                _connectStateFlow.value = ConnectStatus.Connected(device)
                if (!hasBluetoothConnectPermission()) {
                    missingBluetoothConnectPermission("Missing BLUETOOTH_CONNECT permission")
                    return
                }
                try {
                    gatt.discoverServices()
                } catch (e: SecurityException) {
                    missingBluetoothConnectPermission(e.message ?: "Missing BLUETOOTH_CONNECT permission")
                }
            }

            BluetoothProfile.STATE_DISCONNECTED->{
                _connectStateFlow.value = ConnectStatus.Disconnected(bleDevice)
                LogUtil.d("onConnectionStateChange: 设备断联")
            }

            BluetoothProfile.STATE_CONNECTING->{
                val device = bleDevice ?: return
                _connectStateFlow.value = ConnectStatus.Connecting(device)
            }

            BluetoothProfile.STATE_DISCONNECTING->{
                _connectStateFlow.value = ConnectStatus.Disconnecting
            }
        }
    }

    private fun enableNotifications() {
        if (!hasBluetoothConnectPermission()) {
            missingBluetoothConnectPermission("Missing BLUETOOTH_CONNECT permission")
            return
        }
        val service = gatt.getService(UUID.fromString(BleServiceCharacteristic.BLE_SERVICE))
        val characteristic = service?.getCharacteristic(UUID.fromString(BleServiceCharacteristic.BLE_CH_NOTIFY))
        val descriptor = characteristic?.getDescriptor(UUID.fromString(BleServiceCharacteristic.BLE_DESCRIPTION))
        if (characteristic == null || descriptor == null) return
        try {
            gatt.setCharacteristicNotification(characteristic, true)
            if (Build.VERSION.SDK_INT >= 33) {
                gatt.writeDescriptor(descriptor, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
            } else {
                descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                gatt.writeDescriptor(descriptor)
            }
        } catch (e: SecurityException) {
            missingBluetoothConnectPermission(e.message ?: "Missing BLUETOOTH_CONNECT permission")
        }
    }

    private fun negotiateMtu(gatt: BluetoothGatt?,status:Int) {
        if(gatt == null) return
        if (status == BluetoothGatt.GATT_SUCCESS) {
            LogUtil.d("Services discovered successfully")
            val device = bleDevice ?: return
            _connectStateFlow.value = ConnectStatus.ServicesDiscovered(device, gatt.services ?: emptyList())
            if (!hasBluetoothConnectPermission()) {
                missingBluetoothConnectPermission("Missing BLUETOOTH_CONNECT permission")
                return
            }
            try {
                gatt.requestMtu(512)
            } catch (e: SecurityException) {
                missingBluetoothConnectPermission(e.message ?: "Missing BLUETOOTH_CONNECT permission")
            }
        } else {
            _connectStateFlow.value = ConnectStatus.Error("Service discovery failed: $status",bleDevice)
        }
        LogUtil.d("onServicesDiscovered: $status")
    }


    override fun connect(device: BluetoothDevice?) {
        if (device == null) {
            _connectStateFlow.value = ConnectStatus.Error("Device is null",device)
            return
        }
        bleDevice = device
        _connectStateFlow.value = ConnectStatus.Connecting(device)
        if (!hasBluetoothConnectPermission()) {
            missingBluetoothConnectPermission("Missing BLUETOOTH_CONNECT permission")
            return
        }
        try {
            gatt = device.connectGatt(context, autoConnect, gattCallback)
        } catch (e: SecurityException) {
            missingBluetoothConnectPermission(e.message ?: "Missing BLUETOOTH_CONNECT permission")
        }
    }

    override fun disconnect() {
        _connectStateFlow.value = ConnectStatus.Disconnected(bleDevice)
    }

    fun writeCharacteristic(byteArray: ByteArray){
        if(!isConnected()) return
        if (!hasBluetoothConnectPermission()) {
            missingBluetoothConnectPermission("Missing BLUETOOTH_CONNECT permission")
            return
        }
        gatt.getService(UUID.fromString(BleServiceCharacteristic.BLE_SERVICE))?.
        getCharacteristic(UUID.fromString(BleServiceCharacteristic.BLE_CH_WRITE))?.let {
            try {
                val result = if (Build.VERSION.SDK_INT >= 33) {
                    gatt.writeCharacteristic(it, byteArray, BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT)
                } else {
                    it.writeType = BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
                    it.value = byteArray
                    if (gatt.writeCharacteristic(it)) 0 else -1
                }
                LogUtil.d("尝试写入内容:$result")
            } catch (e: SecurityException) {
                missingBluetoothConnectPermission(e.message ?: "Missing BLUETOOTH_CONNECT permission")
            }
        }
    }

}
