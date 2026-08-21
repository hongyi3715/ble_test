package com.lq.ble_core.scan

sealed class BleScanStatus {

    data object Idle : BleScanStatus()
    data object Scanning : BleScanStatus()
    data class Error(val errorMsg:String) : BleScanStatus()
}


