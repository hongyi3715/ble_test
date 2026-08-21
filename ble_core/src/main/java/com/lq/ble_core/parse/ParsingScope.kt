package com.lq.ble_core.parse

import java.nio.charset.Charset

/*
* 单线程操作，不考虑并发 蓝牙通信是串行的
* 但是留一个bitParse这里是无状态的解析，如果后续需要并发，且对性能要求高，可直接用
* 不足的位数补0
* */
class ParsingScope(private val data: ByteArray) {
     var position: Int = 0
     val totalBits get() = data.size * 8

    private val default :Int = 0;
    fun int8(): Int = readWithDefault(8, default) { BitParser.readInt8(it, position).value }

    fun int16():Int = readWithDefault(16, default) { BitParser.readInt16(it, position).value }

    fun int24():Int = readWithDefault(24, default) { BitParser.readInt24(it, position).value }
    fun int32():Int = readWithDefault(32, default) { BitParser.readInt32(it, position).value }

    fun int64():Long = readWithDefault(64, 0L) { BitParser.readInt64(it, position).value }

    fun intChar8(): Char = (BitParser.readInt8(data, position).value and(0xff)).toChar()

    fun readStringUntilByte(byte:Byte,charset:Charset=  Charsets.UTF_8):String{
        return readByteArrayUntilByte(byte).let {
            if(it.isEmpty()){
                ""
            }else{
                String(it,charset)
            }
        }
    }

    fun readByteArrayUntilByte(byte: Byte): ByteArray {
        val byteIndex = position ushr 3
        for (index in byteIndex until data.size) {
            if (data[index] == byte) {
                val length = index - byteIndex
                val bytes = ByteArray(length)
                System.arraycopy(data, byteIndex, bytes, 0, length)
                position = (byteIndex + length + 1) shl 3
                return bytes
            }
        }
        return ByteArray(0)
    }


    inline fun <T> parseList(block: ParsingScope.() -> T): List<T> {
        val result = mutableListOf<T>()
        while (position < totalBits) {
            val item = block()
            result.add(item)
        }
        return result
    }



    private inline fun <T> readWithDefault(bits: Int, default: T, block: (ByteArray) -> T): T {
        return if (position + bits <= totalBits) {
            block(data).also { position += bits }
        } else {
            default
        }
    }

}