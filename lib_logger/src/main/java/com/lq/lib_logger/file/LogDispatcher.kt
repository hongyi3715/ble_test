package com.lq.lib_logger.file

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

internal object  LogDispatcher {
    private val channel = Channel<String>(capacity = Channel.UNLIMITED)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var isRunning = false

    fun start() {
        if (isRunning) return
        isRunning = true
        scope.launch {
            for (msg in channel) {
                LogFileWriter.writeLog(msg)
            }
        }
    }

    fun stop() {
        isRunning = false
        channel.close()
        scope.cancel()
    }

     fun submitLog(msg: String) {
        channel.trySend(msg)
    }
}