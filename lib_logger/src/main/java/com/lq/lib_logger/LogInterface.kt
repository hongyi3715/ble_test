package com.lq.lib_logger

interface LogInterface {

    fun logd(msg: String)

    fun loge(msg:String)

    fun logw(msg:String)

    fun logi(msg:String)

    fun logv(msg:String)

    fun logJson(msg:String)


}