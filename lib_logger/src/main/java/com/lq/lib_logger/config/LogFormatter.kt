package com.lq.lib_logger.config

import android.icu.text.SimpleDateFormat
import android.os.Looper
import org.json.JSONObject
import java.util.Date
import java.util.Locale
import java.util.concurrent.CountDownLatch

internal object LogFormatter {
     const val RESET = "\u001B "
     const val RED = "\u001B "
     const val GREEN = "\u001B "
     const val YELLOW = "\u001B "
     const val BLUE = "\u001B "
     const val PURPLE = "\u001B "

     private val localDefault = Locale.getDefault()

     val currentTime: String? get()= SimpleDateFormat("yyyy_MM_dd HH:mm:ss",localDefault ).format(
         Date()
     )
     val currentDay: String? = SimpleDateFormat("yyyy_MM_dd", localDefault).format(Date())


     private fun getLevelColor(level: LogLevel): String{
          return when(level){
               LogLevel.VERBOSE -> YELLOW
               LogLevel.DEBUG -> GREEN
               LogLevel.INFO -> BLUE
               LogLevel.WARNING->PURPLE
               LogLevel.ERROR -> RED
               LogLevel.NONE -> RESET
          }
     }

     fun getFormatedMsg(tag:String, level: LogLevel, msg: String): String{
          val color = getLevelColor(level)
          val formatMsg = "[$tag]  [$level] $currentTime $msg \n"
          return formatMsg
     }


     fun formatJson(json:String): String{
          return try {
               val jsonObject = JSONObject(json)
               jsonObject.toString(4) // 缩进4格
          } catch (_: Exception) {
               json
          }
     }





}