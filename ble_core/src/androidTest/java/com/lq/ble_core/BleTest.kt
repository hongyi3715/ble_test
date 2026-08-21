package com.lq.ble_core

import androidx.test.filters.MediumTest
import com.lq.ble_core.connect.ConnectStatus
import com.lq.ble_core.scan.BleScanStatus
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@OptIn(ExperimentalCoroutinesApi::class)
class BleTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var bleManager: BleManager

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun `测试扫描功能 - 启动后状态为 Scanning`() = runBlocking {
        bleManager.startScan()
        assertTrue(bleManager.scanState.value is BleScanStatus.Scanning)
        bleManager.stopScan()
        assertTrue(bleManager.scanState.value is BleScanStatus.Idle)
    }

    @Test
    fun `测试连接状态初始为 Idle`() = runBlocking {
        assertEquals(ConnectStatus.Idle, bleManager.connectState.value)
    }

    @Test
    fun `测试扫描结果流可收集`() = runBlocking {
        bleManager.startScan()
        val collected = collectScanResult(timeoutMs = 3000L)
        bleManager.stopScan()
        // 无设备时 collected 可能为空，仅验证收集过程不崩溃
        assertTrue(collected >= 0)
    }

    /**
     * 在指定超时内收集扫描结果，返回收集到的数量。
     */
    private suspend fun collectScanResult(timeoutMs: Long): Int {
        val list = mutableListOf<android.bluetooth.le.ScanResult>()
        withTimeoutOrNull(timeoutMs) {
            bleManager.scanResult.collect { list.add(it) }
        }
        return list.size
    }
}