package com.lq.ble_core.scan

import android.bluetooth.le.ScanResult
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface Scanner {

    val scanStateFlow : StateFlow<BleScanStatus>

    val scanResultsFlow : SharedFlow<ScanResult>

    fun startScan()

    fun stopScan()
}