package com.lq.lib_logger

object LogUtil {
    private val logUtil = LqLog()

    fun setLogTag(tag:String) = logUtil.setTag(tag)

    fun v(msg:String) = logUtil.logv(msg)

    fun d(msg:String) = logUtil.logd(msg)

    fun i(msg:String) = logUtil.logi(msg)

    fun e(msg:String) = logUtil.loge(msg)

}