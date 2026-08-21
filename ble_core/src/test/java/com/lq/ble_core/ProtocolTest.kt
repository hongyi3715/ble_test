package com.lq.ble_core

import com.lq.ble_core.command.BleKey
import com.lq.ble_core.command.BleKeyFlag
import com.lq.ble_core.command.BleProtocolProcessor
import com.lq.ble_core.util.CRCUtil
import com.lq.ble_core.util.byteArrayOfInt32
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test
import kotlin.random.Random

class ProtocolTest {


    @Test
    fun `测试数据组装与解析问题`(){
        val sendData = Random.nextInt().byteArrayOfInt32()
        val sendReply = true
        val sendNack = false
        val protocol = BleProtocolProcessor.generateRequest(BleKey.BIND, BleKeyFlag.CREATE,sendData,sendReply,sendNack)
        val sendCrc = CRCUtil.calculateCrc16(protocol,6)
        println("当前协议:${protocol.toHexString()}")
        BleProtocolProcessor.parseResponse(protocol)?.apply {
            assertEquals(sendReply,isReply)
            assertEquals(sendNack,isNack)
            assertEquals(sendData.size,payLoadLength.toInt())
            assertEquals(BleKeyFlag.CREATE,keyFlag)
            assertEquals(BleKey.BIND.keyValue,key)
            assertEquals(BleKey.BIND.commandValue,cmd)
            assertEquals(sendData.size,data.size)
            assertEquals(sendCrc,crc.toInt())
            assertTrue(sendData.contentEquals(data))
        }
    }

    @Test
    fun `数据为空时应正确处理`(){
        val protocol = BleProtocolProcessor.generateRequest(BleKey.BIND, BleKeyFlag.CREATE,byteArrayOf(),false,false)
        BleProtocolProcessor.parseResponse(protocol)?.apply {
            assertEquals(0,data.size)
            assertTrue(data.isEmpty())
        }
    }

}