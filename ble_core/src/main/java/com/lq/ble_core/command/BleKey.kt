package com.lq.ble_core.command

enum class BleKey( val key:Int) {

    BIND(0x0301), //会弹窗绑定
    LOGIN(0x0302), //直连
    PAIR(0x0303),


    HEART_RATE(0x0503),
    NONE(0xffff);

    val commandValue : Byte
        get() = key.shr(8).toByte()

    val keyValue : Byte
        get() = key.toByte()


    companion object{
        fun getBleKey(value:Int): BleKey{
            return entries.find { it.key == value } ?:NONE
        }
    }



}
