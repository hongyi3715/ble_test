package me.feature.best.practice

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.le.ScanResult
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lq.ble_core.BleManager
import com.lq.lib_logger.LogUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BleAddDeviceViewModel @Inject constructor(
    private val bleManager: BleManager
) : ViewModel() {

    private val _scanList = MutableStateFlow<List<ScanResult>>(emptyList())
    val scanList = _scanList.asStateFlow()

     val scanResultList = mutableListOf<ScanResult>()

    init {
        collectData()
        startScan()
    }

    @SuppressLint("MissingPermission")
    private fun collectData() = viewModelScope.launch(Dispatchers.IO) {
        launch {
            bleManager.scanResult.collect {
                updateScanResult(it)
            }
        }
        launch {
            bleManager.scanState.collect {
                LogUtil.d("collectState: $it")
            }
        }
    }

    fun startScan() {
        bleManager.startScan()
    }

    fun connect(device: ScanResult) {
        bleManager.connect(device.device)
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private fun updateScanResult(it: ScanResult){
        if(it.device.name.isNullOrEmpty()) return
        scanResultList.add(it)
        _scanList.value = scanResultList
        LogUtil.d("collectResult: ${it.device.name} ${it.device.address} ${it.rssi}")
    }

}
