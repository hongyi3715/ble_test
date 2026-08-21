package com.lq.lib_logger.file

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File
import java.io.FileWriter

internal object LogFileWriter {
    private var logFileWriter: FileWriter? = null
    private val mutex = Mutex()
    private var currentFile: File? = null

    suspend fun writeLog(msg: String) {
        mutex.withLock {
            val file = LogFileManager.getLogFile()
            if (file.absolutePath  != currentFile?.absolutePath ) {
                logFileWriter?.close()
                logFileWriter = FileWriter(file, true)
                currentFile = file
            }
            logFileWriter?.apply {
                write(msg)
                flush()
            }
        }
    }

     fun close() {
        logFileWriter?.close()
        logFileWriter = null
        currentFile = null
    }
}