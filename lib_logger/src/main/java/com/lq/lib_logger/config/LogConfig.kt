package com.lq.lib_logger.config


internal object LogConfig {

    @Volatile
    var isDebug = true

    @Volatile
    var logToFile = true

    @Volatile
    var logLevel = LogLevel.VERBOSE

    @Volatile
    var needEncrypt = true





}