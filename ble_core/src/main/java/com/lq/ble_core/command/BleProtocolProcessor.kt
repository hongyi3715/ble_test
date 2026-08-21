package com.lq.ble_core.command

import com.lq.ble_core.util.CRCUtil
import java.nio.ByteBuffer

object BleProtocolProcessor {
    private const val MAGIC = 0xAB.toByte()
    private const val HEADER_VERSION = 1
    private const val REPLY_FLAG = 1 shl 4
    private const val NACK_FLAG = 1 shl 5
    private const val MAGIC_AND_PROTOCOL_LENGTH = 2
    private const val BEFORE_PAYLOAD_LENGTH = 4
    private const val BEFORE_CRC_LENGTH = MAGIC_AND_PROTOCOL_LENGTH + BEFORE_PAYLOAD_LENGTH
    private const val LENGTH_BEFORE_DATA = 9

    /**
     * 生成发送数据
     */
     fun generateRequest(
        bleKey: BleKey,
        keyFlag: BleKeyFlag,
        payload: ByteArray?,
        isReply:Boolean,
        isNack:Boolean
    ): ByteArray {
        val totalLength = LENGTH_BEFORE_DATA + (payload?.size ?: 0)
        val payloadSize = payload?.size ?: 0
        val data = ByteBuffer.allocate(totalLength).apply {
            put(MAGIC)
            put(createProtocolFlag(isReply,isNack).toByte())
            putShort(payloadSize.toShort())
            putShort(0) // CRC placeholder
            put(bleKey.commandValue)
            put(bleKey.keyValue)
            put(keyFlag.flag.toByte())
            payload?.let { put(it) }
        }.array()

        // 计算并设置 CRC
        val crc16 = CRCUtil.calculateCrc16(data, BEFORE_CRC_LENGTH)
        data[4] = (crc16 shr 8).toByte()
        data[5] = crc16.toByte()

        return data
    }

    /**
     * 解析接收数据
     */
     fun parseResponse(data: ByteArray): BleDto? {
        if (!validateCrc(data)) return null
        return try {
            BleDto(
                magic = data[0],
                protocol = data[1],
                payLoadLength = data.getShortAt(2),
                crc = data.getShortAt(4),
                cmd = data[6],
                key = data[7],
                keyFlag = BleKeyFlag.getBleKeyFlag(data[8].toInt() and 0xff),
                data = data.sliceArray(9 until data.size)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


     fun validateCrc(data: ByteArray): Boolean {
        if (data.size < 6) return false
        val receivedCrc = ((data[4].toInt() and 0xFF) shl 8) or (data[5].toInt() and 0xFF)
        val calculatedCrc = CRCUtil.calculateCrc16(data, 6)
        return calculatedCrc == receivedCrc
    }

     fun createProtocolFlag(isReply: Boolean, isNack: Boolean ): Int {
        var headerFlag = HEADER_VERSION
        if (isReply) headerFlag = headerFlag or REPLY_FLAG
        if (isNack) headerFlag = headerFlag or NACK_FLAG
        return headerFlag
    }

    private fun ByteArray.getShortAt(index: Int): Short {
        require(index + 1 < size) { "Index out of bounds" }
        return ((this[index].toInt() and 0xFF) shl 8 or (this[index + 1].toInt() and 0xFF)).toShort()
    }

    fun sendCommand(bleKey: BleKey,keyFlag: BleKeyFlag, data: ByteArray? = null,isReply:Boolean = false,isNack:Boolean=false) {
        val requestData = BleProtocolProcessor.generateRequest(bleKey, keyFlag, data,isReply,isNack)
    }
}