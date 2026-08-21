package com.lq.ble_core.command

import com.lq.ble_core.util.CRCUtil
import com.lq.ble_core.util.getBit
import com.lq.ble_core.util.toBigHexString
import kotlin.collections.contentEquals
import kotlin.collections.contentHashCode
import kotlin.jvm.javaClass
import kotlin.text.toHexString
import kotlin.text.trimIndent


data class BleDto(
    val magic: Byte,
    val protocol:Byte,
    val payLoadLength: Short,
    val crc: Short,
    val cmd: Byte,
    val key:Byte,
    val keyFlag: BleKeyFlag,
    val data: ByteArray
){

    val isReply :Boolean
        get() = protocol.getBit(4)

    val isNack :Boolean
        get() = protocol.getBit(5)

    override fun toString(): String {
        return """
            BleDto {
                magic: ${magic.toBigHexString()}
                protocol: 0x${protocol.toHexString() }
                payLoadLength: 0x${payLoadLength.toHexString()} ($payLoadLength bytes)
                crc: 0x${crc.toHexString() }
                cmd: 0x${cmd.toHexString()}
                key: 0x${key.toHexString()}
                keyFlag: ${keyFlag.name} (${keyFlag.flag.toBigHexString()})
                data: [${data.toHexString()}]
            }
        """.trimIndent()
    }

     fun validateCrc(data: ByteArray): Boolean {
        if (data.size < 6) return false
        val receivedCrc = ((data[4].toInt() and 0xFF) shl 8) or (data[5].toInt() and 0xFF)
        val calculatedCrc = CRCUtil.calculateCrc16(data, 6)
        return calculatedCrc == receivedCrc
    }



    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BleDto

        if (magic != other.magic) return false
        if (protocol != other.protocol) return false
        if (payLoadLength != other.payLoadLength) return false
        if (crc != other.crc) return false
        if (cmd != other.cmd) return false
        if (key != other.key) return false
        if (keyFlag != other.keyFlag) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result: Int = magic.hashCode()
        result = 31 * result + protocol
        result = 31 * result + payLoadLength
        result = 31 * result + crc
        result = 31 * result + cmd
        result = 31 * result + key
        result = 31 * result + keyFlag.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }


}