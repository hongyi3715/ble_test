package com.lq.lib_logger.file

import android.content.Context
import com.lq.lib_logger.config.LogFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.File
import kotlin.math.log

internal object LogFileManager {

    private var logDir: File? = null
    private const val MAX_LOG_FILE = 10
    private const val MAX_FILE_SIZE = 3 * 1024 * 1024L

    private var cleaning = false
    private val cleanScope = CoroutineScope(Dispatchers.IO)

    fun init(context: Context) {
        val dir = File(context.getExternalFilesDir("logs"), "runtime")
        if (!dir.exists()) dir.mkdirs()
        logDir = dir
        cleanupOldLogs()
    }

    private fun cleanupOldLogs() {
        if(cleaning || logDir ==null) return
        cleanScope.launch {
            cleaning = true
            val files = logDir?.listFiles()?.filter { it.name.startsWith("log_") }?.sortedBy { it.name } ?: return@launch
            if (files.size > MAX_LOG_FILE) {
                files.take(files.size - MAX_LOG_FILE).forEach { it.delete() }
            }
            cleaning = false
        }
    }

    fun getLogFile(): File {
        val baseName = "log_${LogFormatter.currentDay}"
        val existingFiles = logDir?.listFiles()
            ?.filter { it.name.startsWith(baseName) && it.name.endsWith(".txt") }
            ?.sortedBy { it.name } ?: emptyList()

        if (existingFiles.isEmpty()) {
            return File(logDir, "$baseName.txt")
        }

        val latestFile = existingFiles.last()
        if (latestFile.length() < MAX_FILE_SIZE) {
            return latestFile
        }

        val nextIndex = existingFiles.size
        val newFileName = "${baseName}_$nextIndex.txt"
        val newFile = File(logDir, newFileName)
        newFile.createNewFile()
        return newFile
    }


    fun stopClean(){
        cleaning = false
        cleanScope.cancel()
    }
}