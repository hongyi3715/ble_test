package com.lq.ble_core.util

import com.lq.ble_core.parse.ParsingScope
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.collections.joinToString
import kotlin.text.format
import kotlin.to

fun ByteArray.formatString(separator: String = ""): String {
    return joinToString(separator) { "%02X".format(it) }
}

fun Byte.toBigHexString(): String = "0x%02X".format(this)

fun Int.toBigHexString(): String = "0x%02X".format(this)


fun Pair<Byte, Byte>.byte2Int():Int{
    return ((first.toInt() and 0xFF ) shl 8) or (second.toInt() and 0xFF)
}

fun String.hexToByteArray(): ByteArray {
    require(length % 2 == 0) { "Hex string must have even length" }

    return chunked(2)
        .map { it.toInt(16).toByte() }
        .toByteArray()
}

/*对于一个字节来说，不存在大端序和小端序,不需要处理字节序*/
fun Int.byteArrayOf8(): ByteArray {
     return byteArrayOf(this.toByte())
}

fun Int.byteArrayOf16(order: ByteOrder = ByteOrder.BIG_ENDIAN): ByteArray{
    val bytes = ByteArray(2)
    val value = this
    if(order  == ByteOrder.BIG_ENDIAN){
        bytes[0] = (value ushr 8).toByte()
        bytes[1] = value.toByte()
    }else{
        bytes[0] = value.toByte()
        bytes[1] = (value ushr 8).toByte()
    }
    return bytes
}

fun Int.byteArrayOfInt32(order: ByteOrder = ByteOrder.BIG_ENDIAN): ByteArray {
    val bytes = ByteArray(4)
    val value = this
    if (order == ByteOrder.BIG_ENDIAN) {
        bytes[0] = (value ushr 24).toByte()
        bytes[1] = (value ushr 16).toByte()
        bytes[2] = (value ushr 8).toByte()
        bytes[3] = value.toByte()
    } else {
        bytes[0] = value.toByte()
        bytes[1] = (value ushr 8).toByte()
        bytes[2] = (value ushr 16).toByte()
        bytes[3] = (value ushr 24).toByte()
    }
    return bytes
}

fun ByteArray.readCString(offset: Int): Pair<String, Int> {
    var end = offset
    while (end < size && this[end] != offset.toByte()) end++
    val str = String(this, offset, end - offset, Charsets.UTF_8)
    return str to (end + 1) // 返回下一个字节偏移
}

/*
* 将数据直接转换成解析类，这里每一次转换都会生成一个新的对象，这个对象持有byteArray的引用
* 如果多个大ByteArray 比如size超过300
* Android中大对象的阈值通常是：- Android 5.0-7.0: 6KB - Android 8.0+: 12KB
* 也就是说目前的ble信息不会直接把BitReader分配到老年代
* 我们的高频解析时大概在1s 30个解析数据，且最大值为200B,频率和内存都不会达到危险期，
* 这里暂时直接创建新对象，后续可用 对象池+内存复用 or 无状态解析
* */

fun <T> ByteArray.parse(block: ParsingScope.()->T):T{
    println("当前字节内容:${this.formatString()}")
    return ParsingScope(this).block()
}

inline fun <T> ByteArray.parseList(block: ParsingScope.() -> T): List<T> {
    val parser = ParsingScope(this)
    return parser.parseList(block)
}

fun Short.toByteArray(byteOrder: ByteOrder = ByteOrder.BIG_ENDIAN): ByteArray {
    return ByteBuffer.allocate(2)
        .order(byteOrder)
        .putShort(this)
        .array()
}
