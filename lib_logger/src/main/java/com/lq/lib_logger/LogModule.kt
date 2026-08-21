package com.lq.lib_logger

import com.lq.lib_logger.encryptor.AesEncryptor
import com.lq.lib_logger.encryptor.EncryptorDecorator

internal class LogModule {

    fun provideLog(): LogInterface{
        return LqLog("LqModule")
    }

    fun provideEncryptorLog(log: LqLog, encryptor: AesEncryptor): LogInterface{
        return EncryptorDecorator(log,encryptor)
    }
}