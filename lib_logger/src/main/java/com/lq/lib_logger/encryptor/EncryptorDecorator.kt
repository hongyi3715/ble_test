package com.lq.lib_logger.encryptor

import com.lq.lib_logger.LogInterface
import com.lq.lib_logger.config.LogFormatter

internal class EncryptorDecorator(private val wrapped: LogInterface,private val encryptor: EncryptorInterface): LogInterface {

    override fun logd(msg: String) {
        wrapped.logd(encryptor.encrypt(msg))
    }

    override fun loge(msg: String) {
        wrapped.loge(encryptor.encrypt(msg))
    }

    override fun logw(msg: String) {
        wrapped.logw(encryptor.encrypt(msg))
    }

    override fun logi(msg: String) {
        wrapped.logi(encryptor.encrypt(msg))
    }

    override fun logv(msg: String) {
        wrapped.logv(encryptor.encrypt(msg))
    }

    override fun logJson(msg: String) {
        val result = encryptor.encrypt(LogFormatter.formatJson(msg))
        wrapped.logd(result)
    }
}