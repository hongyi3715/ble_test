package com.lq.ble_core.command

enum class BleKeyFlag(val flag:Int) {
    UPDATE(0x00),

    READ(0x10),

    CREATE(0x20),

    DELETE(0x30),

    RESET(0x40),

    UNKNOW(0xff);


    companion object{

        fun getBleKeyFlag(value:Int): BleKeyFlag{
            return entries.find { it.flag == value }?:UNKNOW
        }
    }
}