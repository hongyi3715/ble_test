package com.lq.ble_core.command

import com.lq.ble_core.data.BleDeviceInfo
import com.lq.ble_core.data.BleHeartRate
import com.lq.ble_core.util.byte2Int
import com.lq.ble_core.util.byteArrayOfInt32
import com.lq.lib_logger.LogUtil

object BleProtocolManager {

    fun sendCommand(bleKey: BleKey,keyFlag: BleKeyFlag, data: ByteArray? = null,isReply:Boolean = false,isNack:Boolean=false) {
        val requestData = BleProtocolProcessor.generateRequest(bleKey, keyFlag, data,isReply,isNack)
    }

    fun onCharacteristicChanged(data: ByteArray) {
        BleProtocolProcessor.parseResponse(data)?.let {
            handleBleDto(it)
        }
    }

    private fun handleBleDto(data: BleDto) {
        val dto = data.copy()
        val bleKey = BleKey.getBleKey((dto.cmd to dto.key).byte2Int())
        val bleKeyFlag = dto.keyFlag
        val status = dto.data.isNotEmpty() && dto.data.first() == 0.toByte()
        val hasPayLoadData = dto.payLoadLength>4 //如果负载长度只有4，那说明只有一个00没有正儿八经的数据
        LogUtil.d("当前接收信息: BleDto:$dto")
        when (bleKey) {
            BleKey.BIND -> {
                if(dto.data.isEmpty()) return
                if(bleKeyFlag == BleKeyFlag.UPDATE){
                    sendCommand(
                        bleKey,
                        bleKeyFlag,
                        null,
                        true
                    )

                    if(status && hasPayLoadData){
                        val bleDeviceInfo = BleDeviceInfo().apply { decode(data.data) }
                        sendCommand(
                            BleKey.LOGIN,
                            BleKeyFlag.CREATE,
                            bleDeviceInfo.mId.byteArrayOfInt32()
                        ) //绑定成功后登录
                        LogUtil.i("当前数据解析:$bleDeviceInfo")
                    }
                }

            }
            BleKey.PAIR -> {
            }
            BleKey.LOGIN->{
                sendCommand(
                    BleKey.HEART_RATE,
                    BleKeyFlag.READ
                ) //查询心率问题
            }
            BleKey.HEART_RATE->{
                if(bleKeyFlag == BleKeyFlag.READ){
                    val heartRate = BleHeartRate().decode(data.data)

                }
            }
            BleKey.NONE ->{

            }
        }
    }


}