package com.lq.lib_logger.encryptor

internal interface EncryptorInterface {

    fun encrypt(content: String): String

    fun decrypt(content:String): String
}