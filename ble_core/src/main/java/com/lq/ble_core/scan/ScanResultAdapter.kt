package com.lq.ble_core.scan

import android.bluetooth.le.ScanResult
import androidx.compose.runtime.mutableStateListOf

class ScanResultAdapter {

    private val scanResultList = mutableStateListOf<ScanResult>()

    fun transfer(scanResult: ScanResult){
        val name = try {
            scanResult.device.name
        } catch (e: SecurityException) {
            null
        }
        if(name.isNullOrEmpty()) return
        if(scanResultList.contains(scanResult)){
            scanResultList.add(scanResult)
        }
    }

}
