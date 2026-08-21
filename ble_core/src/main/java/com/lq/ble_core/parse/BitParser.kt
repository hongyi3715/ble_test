package com.lq.ble_core.parse


/*
* 后续使用ksp+kotlinpoet做
* */
object BitParser {

    fun readInt8(data: ByteArray,startBit:Int) : ParseResult<Int>{
        val result = readBits(data,startBit,8)
        return ParseResult(result.value.toInt(),result.nextPosition)
    }

    fun readInt16(data: ByteArray,startBit:Int) : ParseResult<Int>{
        val result = readBits(data,startBit,16)
        return ParseResult(result.value.toInt(),result.nextPosition)
    }

    fun readInt24(data: ByteArray,startBit:Int) : ParseResult<Int>{
        val result = readBits(data,startBit,24)
        return ParseResult(result.value.toInt(),result.nextPosition)
    }

    fun readInt32(data: ByteArray,startBit:Int) : ParseResult<Int> {
        val result = readBits(data, startBit, 32)
        return ParseResult(result.value.toInt(), result.nextPosition)
    }

    fun readInt64(data: ByteArray,startBit:Int) : ParseResult<Long>{
        val result = readBits(data,startBit,64)
        return ParseResult(result.value,result.nextPosition)
    }

    fun readInt8ToChar(data: ByteArray,startBit:Int) : ParseResult<Char> {
        val result = readBits(data,startBit,8)
        return ParseResult(result.value.toChar(),result.nextPosition)
    }

    fun readInt16ToChar(data: ByteArray,startBit:Int) : ParseResult<Char> {
        val result = readBits(data,startBit,16)
        return ParseResult(result.value.toChar(),result.nextPosition)
    }

    fun readInt24ToChar(data: ByteArray,startBit:Int) : ParseResult<Char> {
        val result = readBits(data, startBit, 24)
        return ParseResult(result.value.toChar(), result.nextPosition)
    }

    fun readInt32ToChar(data: ByteArray,startBit:Int) : ParseResult<Char> {
        val result = readBits(data, startBit, 32)
        return ParseResult(result.value.toChar(), result.nextPosition)
    }

    fun readBoolean(data: ByteArray,startBit:Int) : ParseResult<Boolean> {
        val result = readBits(data, startBit, 1)
        return ParseResult(result.value.toInt() == 1,result.nextPosition)
    }


    private fun readBits(data: ByteArray,startBit:Int,bitCount:Int) : ParseResult<Long>{
        require(bitCount in 1..64) { "bitCount must be in 1..64" }

        var result = 0L
        var bitRemaining = bitCount
        var currentBit = startBit
        while (bitRemaining>0){
            val byteIndex = currentBit ushr 3
            val bytePos = currentBit and 7
            val bitLeftInByte = 8 - bytePos
            val bitToRead = minOf(bitRemaining,bitLeftInByte)


            val shift = bitLeftInByte - bitToRead
            val mask = ((1 shl bitToRead )- 1) and 0xFF shl shift
            val chunk = data[byteIndex].toInt() and 0xFF and mask ushr shift
            result = (result shl bitToRead) or chunk.toLong()

            bitRemaining -= bitToRead
            currentBit += bitToRead
        }

        return ParseResult(result,currentBit)
    }

}
