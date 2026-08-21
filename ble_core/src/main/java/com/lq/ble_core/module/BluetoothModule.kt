package com.lq.ble_core.module

import android.content.Context
import com.lq.ble_core.BleManager
import com.lq.ble_core.connect.BleConnector
import com.lq.ble_core.scan.BleScanner
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class BluetoothModule {

    @Provides
    @Singleton
    fun provideBleScanner(@ApplicationContext context: Context): BleScanner = BleScanner(context)


    @Provides
    @Singleton
    fun provideBleConnector(@ApplicationContext context: Context):BleConnector = BleConnector(context)


    @Provides
    @Singleton
    fun provideBleManager(scanner: BleScanner,connector: BleConnector): BleManager = BleManager(scanner,connector)

}