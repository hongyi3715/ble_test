package com.lq.lib_logger

import android.util.Log
import com.lq.lib_logger.config.LogConfig
import com.lq.lib_logger.config.LogFormatter
import com.lq.lib_logger.config.LogLevel
import com.lq.lib_logger.file.LogDispatcher

class LqLog(myTag:String = "DefaultLq") : LogInterface{
    @Volatile
    private var tag = myTag

    override fun logv(msg: String) =  log(LogLevel.VERBOSE, msg)
    override fun logd(msg: String) =  log(LogLevel.DEBUG, msg)
    override fun loge(msg: String) =  log(LogLevel.ERROR, msg)
    override fun logi(msg: String) =  log(LogLevel.INFO, msg)
    override fun logw(msg: String) =  log(LogLevel.WARNING, msg)


    fun setTag(data: String){
        tag = data
    }

    override fun logJson(msg: String) {
        val jsonMsg = LogFormatter.formatJson(msg)
        log(LogLevel.DEBUG,jsonMsg)
    }

   private fun log(level: LogLevel, msg: String){
         if(!LogConfig.isDebug || level.priority< LogConfig.logLevel.priority) return
         val formatMsg = LogFormatter.getFormatedMsg(tag,level,msg)
        when(level){
            LogLevel.VERBOSE->Log.v(tag,formatMsg)
            LogLevel.DEBUG -> Log.d(tag,formatMsg)
            LogLevel.INFO -> Log.i(tag,formatMsg)
            LogLevel.ERROR -> Log.e(tag,formatMsg)
            LogLevel.WARNING -> Log.w(tag,formatMsg)
            LogLevel.NONE -> {}
        }
         if(LogConfig.logToFile) LogDispatcher.submitLog(formatMsg)
    }

}