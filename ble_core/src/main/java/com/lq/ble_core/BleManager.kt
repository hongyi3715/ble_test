package com.lq.ble_core

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import com.lq.ble_core.connect.BleConnector
import com.lq.ble_core.connect.ConnectStatus
import com.lq.ble_core.scan.BleScanStatus
import com.lq.ble_core.scan.BleScanner
import com.lq.ble_core.scan.ScanResultAdapter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/*
* 这里避免完全委托，使用外观
* */
class BleManager @Inject constructor(private val bleScanner: BleScanner,private val bleConnector: BleConnector) {

    private val scanResultAdapter by lazy { ScanResultAdapter() }

    val scanState : StateFlow<BleScanStatus> = bleScanner.scanStateFlow

    val connectState : StateFlow<ConnectStatus> = bleConnector.connectStateFlow

    val scanResult : Flow<ScanResult> get()= bleScanner.scanResultsFlow

    fun startScan(){
        if(scanState.value is BleScanStatus.Scanning) bleScanner.stopScan()
        bleScanner.startScan()
    }

    fun stopScan(){
        if(scanState.value is BleScanStatus.Scanning)
            bleScanner.stopScan()
    }

    fun connect(bluetoothDevice: BluetoothDevice?){
        if(bleConnector.isConnected()) return
        bleConnector.connect(bluetoothDevice)
    }

    fun disconnect(){
        if(bleConnector.isConnected()) bleConnector.disconnect()
    }


    fun sendCommand(){
        if(!bleConnector.isConnected()) return
    }

}