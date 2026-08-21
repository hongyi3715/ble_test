package com.lq.ble_core

import androidx.test.filters.MediumTest
import com.lq.ble_core.scan.BleScanStatus
import com.lq.ble_core.scan.BleScanner
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@OptIn(ExperimentalCoroutinesApi::class)
class BleScannerTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var bleScanner: BleScanner

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun `测试蓝牙扫描启动后状态为 Scanning`() = runBlocking {
        bleScanner.startScan()
        assertEquals(BleScanStatus.Scanning, bleScanner.scanStateFlow.value)
        bleScanner.stopScan()
        assertEquals(BleScanStatus.Idle, bleScanner.scanStateFlow.value)
    }

    @Test
    fun `测试蓝牙扫描获取内容`() = runBlocking {
        bleScanner.startScan()
        assertEquals(BleScanStatus.Scanning, bleScanner.scanStateFlow.value)

        // 带超时收集首个扫描结果（无设备时可能收不到，仅验证不崩溃）
        val results = mutableListOf<android.bluetooth.le.ScanResult>()
        withTimeoutOrNull(5000L) {
            bleScanner.scanResultsFlow.collect { result ->
                results.add(result)
                assertNotNull(result.device)
                // 可选：打印便于调试
                // println("ScanDevice:${result.device.name} ${result.device.address} ${result.device.deviceType}")
            }
        }
        bleScanner.stopScan()
        assertEquals(BleScanStatus.Idle, bleScanner.scanStateFlow.value)
    }
}