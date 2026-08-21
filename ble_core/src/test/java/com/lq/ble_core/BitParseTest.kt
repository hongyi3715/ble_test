package com.lq.ble_core
import com.lq.ble_core.parse.BitParser
import com.lq.ble_core.data.BleDeviceInfo
import com.lq.ble_core.data.BleHeartRate
import com.lq.ble_core.util.hexToByteArray
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertThrows
import org.junit.Test


class BitParseTest {


    companion object{
        fun byteArrayOfInts(vararg ints:Int): ByteArray{
            return ints.map{ it.toByte()}.toByteArray()
        }
    }

    @Test
    fun `测试单个数据解析是否正常`() {
        val data = byteArrayOfInts(0x12, 0x34)

        val result = BitParser.readInt8(data, 0)

        assertEquals(0x12, result.value)
    }

    @Test
    fun `测试int8按无符号解析`() {
        val data = byteArrayOfInts(0xFF)
        val result = BitParser.readInt8(data, 0)
        assertEquals(255, result.value)
    }

    @Test
    fun `测试int16按无符号解析`() {
        val data = byteArrayOfInts(0xFF, 0xFF)
        val result = BitParser.readInt16(data, 0)
        assertEquals(65535, result.value)
    }

    @Test
    fun `测试int32边界值解析`() {
        val maxData = byteArrayOfInts(0x7F, 0xFF, 0xFF, 0xFF)
        val minData = byteArrayOfInts(0x80, 0x00, 0x00, 0x00)
        assertEquals(Int.MAX_VALUE, BitParser.readInt32(maxData, 0).value)
        assertEquals(Int.MIN_VALUE, BitParser.readInt32(minData, 0).value)
    }

    @Test
    fun `测试跨字节非对齐读取Int8`() {
        val data = byteArrayOfInts(0xAA, 0xCC)
        val result = BitParser.readInt8(data, 4)
        assertEquals(0xAC, result.value)
    }

    @Test
    fun `测试跨字节非对齐读取Int16`() {
        val data = byteArrayOfInts(0x12, 0x34, 0x56)
        val result = BitParser.readInt16(data, 4)
        assertEquals(0x2345, result.value)
    }

    @Test
    fun `测试readBoolean读取位值`() {
        val data = byteArrayOfInts(0x80)
        assertTrue(BitParser.readBoolean(data, 0).value)
        assertTrue(!BitParser.readBoolean(data, 1).value)
    }

    @Test
    fun `边界情况-越界起始位应抛异常`() {
        assertThrows(ArrayIndexOutOfBoundsException::class.java) {
            BitParser.readInt8(byteArrayOfInts(0x00), 8)
        }
    }

    @Test
    fun `测试解析列表是否正常——解析结果是否符合预期`(){
        val testData = byteArrayOf(
            // 第1个心率数据：时间=1000秒, 心率=75, 类型=0
            0x00, 0x00, 0x03, 0xE8.toByte(), // 时间 1000 (0x000003E8)
            0x4B,                            // 心率 75 (0x4B)
            0x00,                            // 类型 0

            // 第2个心率数据：时间=2000秒, 心率=120, 类型=1
            0x00, 0x00, 0x07, 0xD0.toByte(), // 时间 2000 (0x000007D0)
            0x78,                            // 心率 120 (0x78)
            0x01,                            // 类型 1

            // 第3个心率数据：时间=3000秒, 心率=65, 类型=0
            0x00, 0x00, 0x0B, 0xB8.toByte(), // 时间 3000 (0x00000BB8)
            0x41,                            // 心率 65 (0x41)
            0x00                             // 类型 0
        )
        val emptyResult = BleHeartRate.parseRateList(byteArrayOf())
        assertTrue(emptyResult.isEmpty())

        val result = BleHeartRate.parseRateList(testData)

        assertEquals(3, result.size)

        assertEquals(75, result[0].mBpm)
        assertEquals(120, result[1].mBpm)
        assertEquals(65, result[2].mBpm)
    }

    @Test
    fun `边界情况——列表数据不完整——不足当前字节数`(){
        val incompleteData = byteArrayOf(0x00, 0x00, 0x03, 0xE8.toByte(), 0x4B)
        val result = BleHeartRate.parseRateList(incompleteData)
        assertEquals(1, result.size)
        assertEquals(1000, result[0].mTime)
        assertEquals(75, result[0].mBpm)
        assertEquals(0,result[0].mType)
    }

    @Test
    fun `边界情况-仅有时间字段不足位数补0`() {
        val incompleteData = byteArrayOf(0x00, 0x00, 0x03, 0xE8.toByte())
        val result = BleHeartRate.parseRateList(incompleteData)
        assertEquals(1, result.size)
        assertEquals(1000, result[0].mTime)
        assertEquals(0, result[0].mBpm)
        assertEquals(0, result[0].mType)
    }

    @Test
    fun `边界情况-尾部残余字节也应生成最后一项`() {
        val data = byteArrayOf(
            0x00, 0x00, 0x03, 0xE8.toByte(),
            0x4B,
            0x00,
            0x01
        )
        val result = BleHeartRate.parseRateList(data)
        assertEquals(2, result.size)
        assertEquals(1000, result[0].mTime)
        assertEquals(75, result[0].mBpm)
        assertEquals(0, result[0].mType)
        assertEquals(0, result[1].mTime)
        assertEquals(1, result[1].mBpm)
        assertEquals(0, result[1].mType)
    }

    @Test
    fun `边界情况-包含无效数据`() {
        val dataWithGarbage = byteArrayOf(
            0x00, 0x00, 0x03, 0xE8.toByte(), 0x4B, 0x00, // 有效数据
            0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(),
            0xFF.toByte(), // 垃圾数据
            0x00, 0x00, 0x07, 0xD0.toByte(), 0x78, 0x01  // 有效数据
        )
        val result = BleHeartRate.parseRateList(dataWithGarbage)
        assertEquals(3, result.size)
        assertEquals(1000, result[0].mTime)
        assertEquals(-1, result[1].mTime)
        assertEquals(2000, result[2].mTime)
    }

    @Test
    fun `极端值-最大最小值`() {
        val extremeData = byteArrayOf(
            0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), // time = -1
            0x00, // bpm = 0
            0x00  // type = 0
        )
        val result = BleHeartRate.parseRateList(extremeData)
        assertEquals(1, result.size)
        assertEquals(-1, result[0].mTime)  // 明确断言期望值
        assertEquals(0, result[0].mBpm)
    }

    @Test
    fun `测试BleDeviceInfo中的UB6 Pro的数据解析是否正常且不足位数补0`(){
        val data = ("00ffffffff05ff05020503050b050d0505050e0509005542362050726f0043303a30433a42433" +
                "a36323a38383a4139004a4c3730370055423650726f0055423650726f5f41757261666974000004006" +
                "343303a30433a42433a36323a38383a413900010101010101000001000001000000010000010100010" +
                "00000000000000100000000000000010000000000010000010000000000000000000100000000000000" +
                "00000000000100000000000000000000000000000001000000000000000000000000000100000000" +
                "0101000000000000000000").hexToByteArray()
        val deviceInfo = BleDeviceInfo().apply {
            decode(data)
        }
        println("deviceInfo:$deviceInfo")
    }


}
