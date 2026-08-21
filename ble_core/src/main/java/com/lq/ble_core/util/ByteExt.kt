package com.lq.ble_core.util


/*
* 获取字节某一位的数值判断是否为0
* */
fun Byte.getBit(position: Int): Boolean {
    return (this.toInt() and (1 shl position)) != 0
}