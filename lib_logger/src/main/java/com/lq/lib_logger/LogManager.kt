package com.lq.lib_logger

import android.content.Context
import android.util.Log
import com.lq.lib_logger.config.LogConfig
import com.lq.lib_logger.config.LogLevel
import com.lq.lib_logger.file.LogDispatcher
import com.lq.lib_logger.file.LogFileManager
import com.lq.lib_logger.file.LogFileWriter

object LogManager {
    
    fun init(context: Context){
        LogFileManager.init(context)
    }

    fun setLogLevel(level: LogLevel){
        LogConfig.logLevel = level
    }

    fun openLog(){
        LogDispatcher.start()
        Log.d("LogManager","打开日志记录")
    }

    fun shutdown(){
        LogDispatcher.stop()
        LogFileWriter.close()
        LogFileManager.stopClean()
        Log.d("LogManager","关闭日志系统")
    }
}