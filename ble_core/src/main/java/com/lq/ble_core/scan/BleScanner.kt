package com.lq.ble_core.scan

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import com.lq.lib_logger.LogUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BleScanner @Inject constructor(@param:ApplicationContext private val context: Context) :
    Scanner {

    private val bluetoothManager: BluetoothManager by lazy {
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    }
    private val bluetoothAdapter: BluetoothAdapter
        get() = bluetoothManager.adapter

    private val bluetoothLeScanner: BluetoothLeScanner
        get() = bluetoothAdapter.bluetoothLeScanner

    private val _scanStateFlow = MutableStateFlow<BleScanStatus>(BleScanStatus.Idle)

    override val scanStateFlow: StateFlow<BleScanStatus> = _scanStateFlow.asStateFlow()

    private val _scanResultsFlow = MutableSharedFlow<ScanResult>(extraBufferCapacity = 64)

    override val scanResultsFlow: SharedFlow<ScanResult> = _scanResultsFlow.asSharedFlow()

    private val bleScanCallback = object : ScanCallback() {
        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            _scanResultsFlow.tryEmit(result)
        }

        override fun onScanFailed(errorCode: Int) {
            _scanStateFlow.value = BleScanStatus.Error("Scan failed: $errorCode")
        }
    }

    private val scanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
        .setReportDelay(0)
        .build()

    private val scanFilters = emptyList<ScanFilter>()

    @SuppressLint("MissingPermission")
    override fun startScan() {
        if (!bluetoothAdapter.isEnabled) {
            _scanStateFlow.value = BleScanStatus.Error("Bluetooth not enabled")
            return
        }
        _scanStateFlow.value = BleScanStatus.Scanning
        try {
            bluetoothLeScanner.startScan(scanFilters, scanSettings, bleScanCallback)
        } catch (_: SecurityException) {
            _scanStateFlow.value = BleScanStatus.Error("Location Permission not granted")
        } catch (exception: Exception) {
            _scanStateFlow.value = BleScanStatus.Error("Scan start failed:${exception.message}")
        }
    }

    @SuppressLint("MissingPermission")
    override fun stopScan() {
        _scanStateFlow.value = BleScanStatus.Idle
        try {
            bluetoothLeScanner.stopScan(bleScanCallback)
        } catch (e: Exception) {
            // 忽略停止扫描时的异常
        }
    }


}
