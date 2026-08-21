package com.lq.ble_core.data

import android.text.TextUtils
import com.lq.ble_core.parse.BleDecoder
import com.lq.ble_core.parse.ParsingScope
import com.lq.ble_core.util.parse

import java.util.*
import kotlin.collections.chunked
import kotlin.collections.map
import kotlin.collections.toList
import kotlin.text.uppercase

/**
 * 蓝牙设备相关信息，设备确认绑定后会返回该对象。
 */
data class BleDeviceInfo(
    var mId: Int = 0,
    /**
     * 设备支持读取的数据列表。
     */
    var mDataKeys: List<Int> = listOf(),

    /**
     * 设备蓝牙名，如果设备支持自定义蓝牙名，则表示原始的蓝牙名。
     */
    var mBleName: String = "",

    /**
     * 自定义蓝牙名，仅限部分设备支持。
     */
    var mBleCustomName: String = "",

    /**
     * 设备蓝牙4.0地址。
     */
    var mBleAddress: String = "",

    /**
     * 芯片平台，[PLATFORM_NORDIC]、[PLATFORM_REALTEK]、[PLATFORM_MTK]或[PLATFORM_GOODIX]。
     */
    var mPlatform: String = "",

    /**
     * 设备原型，代表是基于哪款设备开发，[PROTOTYPE_10G]、[PROTOTYPE_R4]或[PROTOTYPE_R5]等。
     */
    var mPrototype: String = "",

    /**
     * 固件标记，固件那边所说的制造商，但严格来说，制造商表述并不恰当，且避免与后台数据结构中的分销商语义冲突，
     * 因为其仅仅用来区分固件，所以命名为FirmwareFlag，与[mBleName]一起确定唯一固件。
     */
    var mFirmwareFlag: String = "",

    /**
     * aGps文件类型，不同读GPS芯片需要下载不同的aGps文件，[AGPS_EPO]、[AGPS_UBLOX]或[AGPS_AGNSS]等，
     * 如果为0，代表不支持GPS。
     */
    var mAGpsType: Int = 0,

    /**
     * 发送[BleCommand.IO]的Buffer大小，见[BleConnector.sendStream]。
     */
    var mIOBufferSize: Long = 0L,

    /**
     * 表盘类型，[WATCH_0]、[WATCH_1]或[WATCH_2]。
     */
    var mWatchFaceType: Int = 0,

    /**
     * 设备蓝牙3.0地址。
     */
    var mClassicAddress: String = "",

    /**
     * 是否显示数字电量  [DIGITAL_POWER_VISIBLE] [DIGITAL_POWER_HIDE]
     */
    var mHideDigitalPower: Int = 0,

    /**
     * 是否显示防丢开关  [ANTI_LOST_VISIBLE] [ANTI_LOST_HIDE]
     */
    var mShowAntiLostSwitch: Int = 0,

    /**
     * 支持的睡眠算法类型  [SUPPORT_NEW_SLEEP_ALGORITHM_0] [SUPPORT_NEW_SLEEP_ALGORITHM_1]
     */
    var mSleepAlgorithmType: Int = 0,

    /**
     * 是否支持日期格式设置 [SUPPORT_DATE_FORMAT_0] [SUPPORT_DATE_FORMAT_1]
     */
    var mSupportDateFormatSet: Int = 0,

    /**
     * 是否支持读取设备信息。在之前, APP只能在绑定设备时被动接收到设备信息, 导致如果固件升级时修改了设备信息，APP不重新绑定
     * 就获取不了新的设备信息。加上该标记后, APP读取固件版本时, 如果发现与之前的版本不一致, 就主动更新下设备信息
     */
    var mSupportReadDeviceInfo: Int = 0,

    /**
     * 是否支持温度单位设置 [SUPPORT_TEMPERATURE_UNIT_0] [SUPPORT_TEMPERATURE_UNIT_1]
     */
    var mSupportTemperatureUnitSet: Int = 0,

    /**
     * 是否支持喝水提醒的设置 [SUPPORT_DRINK_WATER_0] [SUPPORT_DRINK_WATER_1]
     */
    var mSupportDrinkWaterSet: Int = 0,

    /**
     * 是否支持切换经典蓝牙开关指令 [SUPPORT_CHANGE_CLASSIC_BLUETOOTH_STATE_0] [SUPPORT_CHANGE_CLASSIC_BLUETOOTH_STATE_1]
     */
    var mSupportChangeClassicBluetoothState: Int = 0,

    /**
     * 是否支持app运动指令 [SUPPORT_APP_SPORT_0] [SUPPORT_APP_SPORT_1]
     */
    var mSupportAppSport: Int = 0,

    /**
     * 是否支持血氧的设置 [SUPPORT_BLOOD_OXYGEN_0] [SUPPORT_BLOOD_OXYGEN_1]
     */
    var mSupportBloodOxyGenSet: Int = 0,

    /**
     * 是否支持洗手提醒的设置 [SUPPORT_WASH_0] [SUPPORT_WASH_1]
     */
    var mSupportWashSet: Int = 0,

    /**
     * 是否支持按需获取天气 [SUPPORT_REQUEST_REALTIME_WEATHER_0] [SUPPORT_REQUEST_REALTIME_WEATHER_1]
     */
    var mSupportRequestRealtimeWeather: Int = 0,

    /**
     * 是否支持HID [SUPPORT_HID_0] [SUPPORT_HID_1]
     */
    var mSupportHID: Int = 0,

    /**
     * 是否支持iBeacon设置 [SUPPORT_IBEACON_SET_0] [SUPPORT_IBEACON_SET_1] [SUPPORT_IBEACON_SET_2]
     */
    var mSupportIBeaconSet: Int = 0,

    /**
     * 是否支持设置表盘id [SUPPORT_WATCHFACE_ID_0] [SUPPORT_WATCHFACE_ID_1]
     */
    var mSupportWatchFaceId: Int = 0,

    /**
     * 是否支持新的传输方式，目前只有ios用到，这里安卓只占个位
     */
    var mSupportNewTransportMode: Int = 0,

    /**
     * 是否支持杰里sdk传输 [SUPPORT_JL_TRANSPORT_0] [SUPPORT_JL_TRANSPORT_1]
     */
    var mSupportJLTransport: Int = 0,

    /**
     * 是否支持找手表 [SUPPORT_FIND_WATCH_0] [SUPPORT_FIND_WATCH_1]
     */
    var mSupportFindWatch: Int = 0,

    /**
     * 是否支持世界时钟 [SUPPORT_WORLD_CLOCK_0] [SUPPORT_WORLD_CLOCK_1]
     */
    var mSupportWorldClock: Int = 0,

    /**
     * 是否支持股票 [SUPPORT_STOCK_0] [SUPPORT_STOCK_1]
     */
    var mSupportStock: Int = 0,

    /**
     * 是否短信快捷回复 [SUPPORT_SMS_QUICK_REPLY_0] [SUPPORT_SMS_QUICK_REPLY_1]
     */
    var mSupportSMSQuickReply: Int = 0,

    /**
     * 是否支持App勿扰时间段的设置 [SUPPORT_NO_DISTURB_0] [SUPPORT_NO_DISTURB_1]
     */
    var mSupportNoDisturbSet: Int = 0,

    /**
     * 是否支持手表密码锁 [SUPPORT_SET_WATCH_PASSWORD_0] [SUPPORT_SET_WATCH_PASSWORD_1]
     */
    var mSupportSetWatchPassword: Int = 0,

    /**
     * 是否支持手机控制手表测量心率，血氧，血压 [SUPPORT_REALTIME_MEASUREMENT_0] [SUPPORT_REALTIME_MEASUREMENT_1]
     */
    var mSupportRealTimeMeasurement: Int = 0,

    /**
     * 是否支持省电模式 [SUPPORT_POWER_SAVE_MODE_0] [SUPPORT_POWER_SAVE_MODE_1]
     */
    var mSupportPowerSaveMode: Int = 0,

    /**
     * 是否支持LoveTap [SUPPORT_LOVE_TAP_0] [SUPPORT_LOVE_TAP_1]
     */
    var mSupportLoveTap: Int = 0,

    /**
     * 是否支持Newsfeed [SUPPORT_NEWS_FEED_0] [SUPPORT_NEWS_FEED_1]
     */
    var mSupportNewsfeed: Int = 0,

    /**
     * 是否支持吃药提醒 [SUPPORT_MEDICATION_REMINDER_0] [SUPPORT_MEDICATION_REMINDER_1]
     */
    var mSupportMedicationReminder: Int = 0,

    /**
     * 是否支持同步二维码 [SUPPORT_QRCODE_0] [SUPPORT_QRCODE_1]
     */
    var mSupportQrcode: Int = 0,

    /**
     * 是否支持新的天气协议（支持7天) [SUPPORT_WEATHER2_0] [SUPPORT_WEATHER2_1]
     */
    var mSupportWeather2: Int = 0,

    /**
     * 是否支持支付宝 [SUPPORT_ALIPAY_0] [SUPPORT_ALIPAY_1]
     */
    var mSupportAlipay: Int = 0,

    /**
     * 是否支持待机设置 [SUPPORT_STANDBY_SET_0] [SUPPORT_STANDBY_SET_1]
     */
    var mSupportStandbySet: Int = 0,

    /**
     * 是否支持2d加速 [SUPPORT_2D_ACCELERATION_0] [SUPPORT_2D_ACCELERATION_1]
     */
    var mSupport2DAcceleration: Int = 0,

    /**
     * 是否支持涂鸦授权码修改 [SUPPORT_TUYA_KEY_0] [SUPPORT_TUYA_KEY_1]
     */
    var mSupportTuyaKey: Int = 0,

    /**
     * 是否支持吃药闹钟 [SUPPORT_MEDICATION_ALARM_0] [SUPPORT_MEDICATION_ALARM_1]
     */
    var mSupportMedicationAlarm: Int = 0,

    /**
     * 是否支持读取获取手表字库/UI/语言包状态信息 [SUPPORT_READ_PACKAGE_STATUS_0] [SUPPORT_READ_PACKAGE_STATUS_1]
     */
    var mSupportReadPackageStatus: Int = 0,

    /**
     * 支持的联系人数量，如果返回0，默认20条；返回大于0，则size = value * 10
     */
    var mSupportContactSize: Int = 0,

    /**
     * 是否支持语音功能 [SUPPORT_VOICE_0] [SUPPORT_VOICE_1]
     */
    var mSupportVoice: Int = 0,

    /**
     * 是否支持导航功能 [SUPPORT_NAVIGATION_0] [SUPPORT_NAVIGATION_1]
     */
    var mSupportNavigation: Int = 0,

    /**
     * 是否支持心率预警设置 [SUPPORT_HR_WARN_SET_0] [SUPPORT_HR_WARN_SET_1]
     */
    var mSupportHrWarnSet: Int = 0,

    /**
     * 如果有Ble名称,表示app可修改蓝牙名,返回的是原始ble名字;不需要修改蓝牙名，直接配置为空
     * 之前通过固件标记加分割符获取默认名的方式可能要废弃
     */
    private var mBleDefaultName: String = "",

    /**
     * 是否支持音乐传输 [SUPPORT_MUSIC_TRANSFER_0] [SUPPORT_MUSIC_TRANSFER_1]
     */
    var mSupportMusicTransfer: Int = 0,

    /**
     * 是否支持App勿扰时间段的设置和总开关设置 [SUPPORT_NO_DISTURB2_0] [SUPPORT_NO_DISTURB2_1]
     */
    var mSupportNoDisturbSet2: Int = 0,

    /**
     * 是否支持SOS设置 [SUPPORT_SOS_SET_0] [SUPPORT_SOS_SET_1]
     */
    @Deprecated("已废弃")
    var mSupportSOSSet: Int = 0,

    /**
     * 是否支持获取语言列表 [SUPPORT_READ_LANGUAGES_0] [SUPPORT_READ_LANGUAGES_1]
     * 使用[BleKey.DEVICE_LANGUAGES]
     */
    var mSupportReadLanguages: Int = 0,

    /**
     * 是否支持女性生理期提醒设置 [SUPPORT_GIRLCARE_REMINDER_0] [SUPPORT_GIRLCARE_REMINDER_1]
     */
    var mSupportGirlCareReminder: Int = 0,

    /**
     * 是否支持信息提醒APP开关设置2，目前只有ios用到，这里安卓只占个位
     */
    var mSupportAppPushSwitch: Int = 0,

    /**
     * 收款码二维码数量，0表示不支持，需要使用 [BleKey.QRCODE2]
     */
    var mSupportReceiptCodeSize: Int = 0,

    /**
     * 是否支持游戏时长提醒设置 [SUPPORT_GAME_TIME_REMINDER_0] [SUPPORT_GAME_TIME_REMINDER_1]
     */
    var mSupportGameTimeReminder: Int = 0,

    /**
     * 我的名片二维码数量，0表示不支持，需要使用 [BleKey.QRCODE2]
     */
    var mSupportMyCardCodeSize: Int = 0,

    /**
     * 是否支持实时传输运动数据给APP [SUPPORT_DEVICE_SPORT_DATA_0] [SUPPORT_DEVICE_SPORT_DATA_1]
     */
    var mSupportDeviceSportData: Int = 0,

    /**
     * 是否支持电子书传输 [SUPPORT_EBOOK_TRANSFER_0] [SUPPORT_EBOOK_TRANSFER_1]
     */
    var mSupportEbookTransfer: Int = 0,

    /**
     * 是否支持双击亮屏 [SUPPORT_DOUBLE_SCREEN_0] [SUPPORT_DOUBLE_SCREEN_1]
     */
    var mSupportDoubleScreen: Int = 0,

    /**
     * 是否支持自定义开机LOGO [SUPPORT_CUSTOM_LOGO_0] [SUPPORT_CUSTOM_LOGO_1]
     */
    var mSupportCustomLogo: Int = 0,

    /**
     * 是否支持app设置压力定时测量 [SUPPORT_PRESSURE_TIMING_MEASUREMENT_0] [SUPPORT_PRESSURE_TIMING_MEASUREMENT_1]
     */
    var mSupportPressureTimingMeasurement: Int = 0,

    /**
     * 是否支持定时待机表盘设置 [SUPPORT_TIMER_STANDBYSET_0] [SUPPORT_TIMER_STANDBYSET_1]
     */
    var mSupportTimerStandbySet: Int = 0,

    /**
     * 是否支持SOS设置 [SUPPORT_SOS_SET_0] [SUPPORT_SOS_SET_1]
     * 后续判读是否支持支持SOS设置使用这个，用法不变
     */
    var mSupportSOSSet2: Int = 0,

    /**
     * 是否支持跌落设置 [SUPPORT_FALL_SET_0] [SUPPORT_FALL_SET_1]
     */
    var mSupportFallSet: Int = 0,

    /**
     * 是否支持骑行和步行 [SUPPORT_WALK_BIKE_0] [SUPPORT_WALK_BIKE_1]
     */
    var mSupportWalkAndBike: Int = 0,

    /**
     * 是否支持连接提醒，目前是多设备切换后连接上时发出提醒 [SUPPORT_CONNECT_REMINDER_0] [SUPPORT_CONNECT_REMINDER_1]
     */
    var mSupportConnectReminder: Int = 0,

    /**
     * 是否支持读取SDCard信息 [SUPPORT_SDCARD_INFO_0] [SUPPORT_SDCARD_INFO_1]
     */
    var mSupportSDCardInfo: Int = 0,

    /**
     * 是否支持来电铃声设置[SUPPORT_INCOMING_CALL_RING_0] [SUPPORT_INCOMING_CALL_RING_1]
     */
    var mSupportIncomingCallRing: Int = 0,

    /**
     * 是否支持消息亮屏提醒设置[SUPPORT_NOTIFICATION_LIGHT_SCREEN_SET_0] [SUPPORT_NOTIFICATION_LIGHT_SCREEN_SET_1]
     */
    var mSupportNotificationLightScreenSet: Int = 0,

    /**
     * 是否支持血压标定[SUPPORT_BLOOD_PRESSURE_CALIBRATION_0] [SUPPORT_BLOOD_PRESSURE_CALIBRATION_1]
     */
    var mSupportBloodPressureCalibration: Int = 0,

    /**
     * 是否支持传输ota文件，目前双备份设备ota使用[BleKey.OTA_FILE]
     * [SUPPORT_OTA_FILE_0] [SUPPORT_OTA_FILE_1]
     */
    var mSupportOTAFile: Int = 0,

    /**
     * 是否支持传输GPS固件文件[BleKey.GPS_FIRMWARE_FILE]
     * [SUPPORT_GPS_FIRMWARE_FILE_0] [SUPPORT_GPS_FIRMWARE_FILE_1]
     */
    var mSupportGPSFirmwareFile: Int = 0,

    /**
     * 是否支持GOMORE设置
     * [SUPPORT_GOMORE_SET_0] [SUPPORT_GOMORE_SET_1]
     */
    var mSupportGoMoreSet: Int = 0,

    /**
     * 是否支持来电铃声和震动设置
     * [SUPPORT_RING_VIBRATION_SET_0] [SUPPORT_RING_VIBRATION_SET_1]
     */
    var mSupportRingVibrationSet: Int = 0,

    /**
     * 是否支持网络
     * [SUPPORT_NETWORK_0] [SUPPORT_NETWORK_1]
     */
    var mSupportNetwork: Int = 0,

    /**
     * 是否支持联系人排序
     * [SUPPORT_CONTACT_SORT_0] [SUPPORT_CONTACT_SORT_1]
     */
    var mSupportContactSort: Int = 0,

    /**
     * 二维码最大数量 [BleKey.QRCODE2]
     */
    var mQrcodeSize: Int = 0,

    /**
     * 二维码内容最大字节 [BleKey.QRCODE2]
     */
    var mQrcodeContentSize: Int = 0,

    /**
     * 是否支持字符串二维码，不支持就用点阵数据
     * [SUPPORT_STRING_QRCODE_0] [SUPPORT_STRING_QRCODE_1]
     */
    var mSupportStringQrcode: Int = 0,

    /**
     * 是否支持设置表盘索引，表盘切换 [BleWatchFaceIndex]
     * [SUPPORT_WATCHFACE_INDEX_0] [SUPPORT_WATCHFACE_INDEX_1]
     */
    var mSupportWatchFaceIndex: Int = 0,

    /**
     * 是否支持SOS紧急联系⼈，最多支持5个联系人，前面的sos只支持1个
     * [SUPPORT_SOS_CONTACT_0] [SUPPORT_SOS_CONTACT_1]
     */
    var mSupportSosContact: Int = 0,

    /**
     * 是否支持生理期月报
     * [SUPPORT_GIRL_CARE_MONTHLY_0] [SUPPORT_GIRL_CARE_MONTHLY_1]
     */
    var mSupportGirlCareMonthly: Int = 0,

    /**
     * 是否支持佩戴方式
     * [SUPPORT_WEAR_WAY_0] [SUPPORT_WEAR_WAY_1]
     */
    var mSupportWearWay: Int = 0,

    /**
     * 是否支持翻腕亮屏2 [SUPPORT_GESTUREWAKE2_0] [SUPPORT_GESTUREWAKE2_1]
     */
    var mSupportGestureWake2: Int = 0,

    /**
     * 是否支持导航图片 [SUPPORT_NAV_IMAGE_0] [SUPPORT_NAV_IMAGE_1]
     */
    var mSupportNavImage: Int = 0,

    /**
     * 是否支持语音长度按照单包最长长度发送 [SUPPORT_VOICE_MAX_LENGTH_0] [SUPPORT_VOICE_MAX_LENGTH_1]
     */
    var mSupportVoiceMaxLength: Int = 0,

    /**
     * 是否支持有声读物 [SUPPORT_AUDIO_BOOKS_0] [SUPPORT_AUDIO_BOOKS_1]
     */
    var mSupportAudioBooks: Int = 0,

    /**
     * 是否支持学习卡片 [SUPPORT_STUDY_CARDS_0] [SUPPORT_STUDY_CARDS_1]
     */
    var mSupportStudyCards: Int = 0,

    /**
     * 是否支持AppStore [SUPPORT_APP_STORE_0] [SUPPORT_NAV_IMAGE_1]
     */
    var mSupportAppStore: Int = 0,

    /**
     * 是否支持上交大算法 [SUPPORT_SHSY_ALGORITHM_0] [SUPPORT_SHSY_ALGORITHM_1]
     */
    var mSupportSHSYAlgorithm: Int = 0,

    /**
     * 是否支持朝拜设置 [SUPPORT_QIBLA_SET_0] [SUPPORT_QIBLA_SET_1]
     */
    var mSupportQiblaSet: Int = 0,

    /**
     * 是否支持测量血糖，使用第三方算法 [SUPPORT_MEASUREMENT_BLOOD_GLUCOSE_0] [SUPPORT_MEASUREMENT_BLOOD_GLUCOSE_1]
     */
    var mSupportMeasurementBloodGlucose: Int = 0,

    /**
     * 是否支持游戏控制 [SUPPORT_GAME_CONTROL_0] [SUPPORT_GAME_CONTROL_1]
     */
    var mSupportGameControls: Int = 0,

    /**
     * 是否支持读取电池记录数据 [SUPPORT_BATTERY_USAGE_0] [SUPPORT_BATTERY_USAGE_1]
     */
    var mSupportBatteryUsage: Int = 0,

    /**
     * 是否支持AI翻译功能 [SUPPORT_AI_TRANSLATION_0] [SUPPORT_AI_TRANSLATION_1]
     */
    var mSupportAITranslation: Int = 0,

    /**
     * 是否支持同声传译功能 [SUPPORT_SIMULTANEOUS_TRANSLATION_0] [SUPPORT_SIMULTANEOUS_TRANSLATION_1]
     */
    var mSupportSimultaneousTranslation: Int = 0,

    /**
     * 是否支持触控设置 [SUPPORT_TOUCH_SET_0] [SUPPORT_TOUCH_SET_1]
     */
    var mSupportTouchSet: Int = 0,

    /**
     * 是否支持IMEI设置 [SUPPORT_IMEI_SET_0] [SUPPORT_IMEI_SET_1]
     */
    var mSupportIMEISet: Int = 0,

    /**
     * 是否支持古兰经 [SUPPORT_QURAN_0] [SUPPORT_QURAN_1]
     */
    var mSupportQuran: Int = 0,

    /**
     * 是否支持AGPS后台传输 [SUPPORT_SYNC_AGPS_IN_BACKGROUND_0] [SUPPORT_SYNC_AGPS_IN_BACKGROUND_1]
     */
    var mSupportSyncAGPSInBackground: Int = 0,

    /**
     * 是否支持还原出厂 [SUPPORT_RESTORE_FACTORY_0] [SUPPORT_RESTORE_FACTORY_1]
     */
    var mSupportRestoreFactory: Int = 0,

    /**
     * 是否支持录音笔记 [SUPPORT_RECORD_NOTE_0] [SUPPORT_RECORD_NOTE_1]
     */
    var mSupportRecordNote: Int = 0,

    /**
     * 是否支持睡眠评分 [SUPPORT_SLEEP_SCORE_0] [SUPPORT_SLEEP_SCORE_1]
     */
    var mSupportSleepScore: Int = 0,

    /**
     * 是否支持WatchFace2 [SUPPORT_WATCHFACE2_0] [SUPPORT_WATCHFACE2_1]
     */
    var mSupportWatchface2: Int = 0,

    /**
     * 是否支持AI教练 [SUPPORT_AI_COACH_0] [SUPPORT_AI_COACH_1]
     */
    var mSupportAICoach: Int = 0,

    /**
     * 是否支持跨应用翻译 [SUPPORT_CROSS_APP_TRANSLATION_0] [SUPPORT_CROSS_APP_TRANSLATION_1]
     */
    var mSupportCrossAppTranslation: Int = 0,

    /**
     * 是否支持放松提醒 [SUPPORT_RELAX_REMINDER_0] [SUPPORT_RELAX_REMINDER_1]
     */
    var mSupportRelaxReminder: Int = 0,

    /**
     * 是否支持电量2，支持电压 [SUPPORT_POWER2_0] [SUPPORT_POWER2_1]
     */
    var mSupportPower2: Int = 0,

    /**
     * 是否支持微信，设备可以通过服务器收发微信消息 [SUPPORT_WECHAT_0] [SUPPORT_WECHAT_1]
     */
    var mSupportWeChat: Int = 0,

    /**
     * 是否支持WhatsApp，设备可以通过服务器收发WhatsApp消息 [SUPPORT_WHATSAPP_0] [SUPPORT_WHATSAPP_1]
     */
    var mSupportWhatsApp: Int = 0,

    /**
     * 是否支持获取语言列表2 [SUPPORT_READ_LANGUAGES2_0] [SUPPORT_READ_LANGUAGES2_1]
     * 使用[BleKey.DEVICE_LANGUAGES2]
     */
    var mSupportReadLanguages2: Int = 0,

    /**
     * 是否支持APP时间制式设置 [SUPPORT_TIME_FORMAT_0] [SUPPORT_TIME_FORMAT_1]
     */
    var mSupportTimeFormat: Int = 0,

    /**
     * 是否支持APP设置震动强度开关 [SUPPORT_VIBRATION_INTENSITY_0] [SUPPORT_VIBRATION_INTENSITY_1]
     */
    var mSupportVibrationIntensity: Int = 0,

    /**
     * 是否支持APP设置高反辅助开关 [SUPPORT_HIGH_ANTI_ASSIST_0] [SUPPORT_HIGH_ANTI_ASSIST_1]
     */
    var mSupportHighAntiAssist: Int = 0,

    /**
     * 是否支持微信支付开关 [SUPPORT_WECHAT_PAY_0] [SUPPORT_WECHAT_PAY_1]
     */
    var mSupportWechatPay: Int = 0,

    /**
     * 是否支持活力值功能 [SUPPORT_VITALITY_VALUE_0] [SUPPORT_VITALITY_VALUE_1]
     */
    var mSupportVitalityValue: Int = 0,

    /**
     * 是否支持设置心率检测模式开关 [SUPPORT_HR_DETECTION_MODE_0] [SUPPORT_HR_DETECTION_MODE_1]
     */
    var mSupportHRDetectionMode: Int = 0,

    /**
     * 是否支持AI Coach2 [SUPPORT_AI_COACH_V2_0] [SUPPORT_AI_COACH_V2_1]
     */
    var mSupportAICoachV2: Int = 0,

    /**
     * 是否支持呼吸率功能 [SUPPORT_RESPIRATORY_RATE_0] [SUPPORT_RESPIRATORY_RATE_1]
     */
    var mSupportRespiratoryRate: Int = 0,

    /**
     * 是否支持体力功能 [SUPPORT_PHYSICAL_STRENGTH_0] [SUPPORT_PHYSICAL_STRENGTH_1]
     */
    var mSupportPhysicalStrength: Int = 0,

    /**
     * 是否支持IPC，带摄像头 [SUPPORT_IPC_0] [SUPPORT_IPC_1]
     */
    var mSupportIPC: Int = 0,

    /**
     * 是否支持视频通话, 设备可以直接远程通话 [SUPPORT_VIDEO_CALLS_0] [SUPPORT_VIDEO_CALLS_1]
     */
    var mSupportVideoCalls: Int = 0,

    /**
     * 是否支持录音 [SUPPORT_RECORD_AUDIO_0] [SUPPORT_RECORD_AUDIO_1]
     * 使用[BleRecordAudioSet]
     */
    var mSupportRecordAudio: Int = 0,

    /**
     * 是否支持活动识别 [SUPPORT_ACTIVITY_RECOGNITION_0] [SUPPORT_ACTIVITY_RECOGNITION_1]
     */
    var mSupportActivityRecognition: Int = 0

    ) : BleDecoder{



    companion object {
        //固件标记中原始蓝牙名的分割符
        const val RAW_NAME_SEPARATOR = "<>"

        const val PLATFORM_NORDIC = "Nordic"
        const val PLATFORM_REALTEK = "Realtek"
        const val PLATFORM_MTK = "MTK"
        const val PLATFORM_GOODIX = "Goodix" // 汇顶
        const val PLATFORM_JL = "JL" // 杰里
        const val PLATFORM_JL707 = "JL707" //杰里新一代主控
        const val PLATFORM_SIFLI = "SIFLI" // SIFLI
        const val PLATFORM_ZKLX = "ZKLX" // 中科蓝讯
        const val PLATFORM_JL707LITE = "JL707Lite" //杰里新一代主控, 如戒指

        // Nordic
        const val PROTOTYPE_10G = "SMA-10G"
        const val PROTOTYPE_GTM5 = "SMA-GTM5"
        const val PROTOTYPE_F1N = "SMA-F1N"
        const val PROTOTYPE_ND09 = "SMA-ND09"
        const val PROTOTYPE_ND08 = "SMA-ND08"
        const val PROTOTYPE_FA86 = "SMA_FA_86"
        const val PROTOTYPE_MC11 = "SMA_MC_11"

        // Realtek
        const val PROTOTYPE_R4 = "SMA-R4"
        const val PROTOTYPE_R5 = "SMA-R5"
        const val PROTOTYPE_B5CRT = "SMA-B5CRT"
        const val PROTOTYPE_F1RT = "SMA-F1RT"
        const val PROTOTYPE_F2 = "SMA-F2"
        const val PROTOTYPE_F3C = "SMA-F3C"
        const val PROTOTYPE_F3R = "SMA-F3R"
        const val PROTOTYPE_R7 = "SMA-R7"
        const val PROTOTYPE_F13 = "SMA-F13"
        const val PROTOTYPE_R10 = "R10"
        const val PROTOTYPE_F6 = "F6"
        const val PROTOTYPE_R9 = "R9"
        const val PROTOTYPE_F7 = "F7"
        const val PROTOTYPE_SW01 = "SMA-SW01"
        const val PROTOTYPE_REALTEK_GTM5 = "REALTEK_GTM5"
        const val PROTOTYPE_F1 = "SMA-F1"
        const val PROTOTYPE_F2D = "SMA-F2D"
        const val PROTOTYPE_F2R = "SMA-F2R"
        const val PROTOTYPE_T78 = "T78"
        const val PROTOTYPE_F5 = "F5"
        const val PROTOTYPE_V1 = "SMA-V1"
        const val PROTOTYPE_Y1 = "Y1"
        const val PROTOTYPE_F3_LH = "F3-LH"
        const val PROTOTYPE_V2 = "V2"
        const val PROTOTYPE_Y3 = "Y3"
        const val PROTOTYPE_R3PRO = "R3Pro"
        const val PROTOTYPE_R10PRO = "R10Pro"
        const val PROTOTYPE_Y2 = "Y2"
        const val PROTOTYPE_F2PRO = "F2Pro"
        const val PROTOTYPE_S2 = "S2"
        const val PROTOTYPE_B9 = "B9"
        const val PROTOTYPE_F13J = "F13J"
        const val PROTOTYPE_R11 = "R11"
        const val PROTOTYPE_V5 = "V5"
        const val PROTOTYPE_V3 = "V3"
        const val PROTOTYPE_LG19T = "SMA-LG19T"
        const val PROTOTYPE_MATCH_S1 = "Match_S1"
        const val PROTOTYPE_S03 = "SMA_S03"
        const val PROTOTYPE_F2K = "F2K"
        const val PROTOTYPE_W9 = "W9"
        const val PROTOTYPE_R11S = "R11S"
        const val PROTOTYPE_EXPLORER = "Explorer"
        const val PROTOTYPE_NY58 = "NY58"
        const val PROTOTYPE_F12 = "F12"
        const val PROTOTYPE_F11 = "F11"
        const val PROTOTYPE_F13A = "F13A"
        const val PROTOTYPE_AM01 = "AM01"
        const val PROTOTYPE_F2R_DK = "F2R"
        const val PROTOTYPE_F1_DK = "F1"
        const val PROTOTYPE_S4 = "S4"
        const val PROTOTYPE_R6_PRO_DK = "R6_PRO_DK"
        const val PROTOTYPE_GB1 = "GB1"
        const val PROTOTYPE_SPORT4 = "Sport4"

        // Goodix
        const val PROTOTYPE_R3H = "R3H"
        const val PROTOTYPE_R3Q = "R3Q"

        // MTK
        const val PROTOTYPE_F3 = "F3"
        const val PROTOTYPE_M3 = "M3"
        const val PROTOTYPE_M4 = "M4"
        const val PROTOTYPE_M6 = "M6"
        const val PROTOTYPE_M7 = "M7"
        const val PROTOTYPE_R2 = "R2"
        const val PROTOTYPE_M6C = "M6C"
        const val PROTOTYPE_M7C = "M7C"
        const val PROTOTYPE_M7S = "M7S"
        const val PROTOTYPE_M4S = "M4S"
        const val PROTOTYPE_M4C = "M4C"
        const val PROTOTYPE_M5C = "M5C"

        // JL
        const val PROTOTYPE_R9J = "R9J"
        const val PROTOTYPE_F13B = "F13B"
        const val PROTOTYPE_A7 = "A7"
        const val PROTOTYPE_A8 = "A8"
        const val PROTOTYPE_AM01J = "AM01J"
        const val PROTOTYPE_F17 = "F17"
        const val PROTOTYPE_AM02J = "AM02J"
        const val PROTOTYPE_HW01 = "HW01"
        const val PROTOTYPE_F12PRO = "F12Pro"
        const val PROTOTYPE_K18 = "K18"
        const val PROTOTYPE_AM05 = "AM05"
        const val PROTOTYPE_K30 = "K30"
        const val PROTOTYPE_FC1 = "FC1"
        const val PROTOTYPE_FC2 = "FC2"
        const val PROTOTYPE_FT5 = "FT5"
        const val PROTOTYPE_R16 = "R16"
        const val PROTOTYPE_A8_ULTRA_PRO = "A8_Ultra_Pro"
        const val PROTOTYPE_AM08 = "AM08"
        const val PROTOTYPE_A9MINI = "A9mini"
        const val PROTOTYPE_JX621D = "JX621D"
        const val PROTOTYPE_V61 = "V61"
        const val PROTOTYPE_AM11 = "AM11"
        const val PROTOTYPE_AW37 = "AW37"
        const val PROTOTYPE_WS001 = "WS001"
        const val PROTOTYPE_X2 = "X2" //充电仓
        const val PROTOTYPE_X3 = "X3" //充电仓
        const val PROTOTYPE_X6 = "X6" //充电仓
        const val PROTOTYPE_AM25 = "AM25"
        const val PROTOTYPE_AM22 = "AM22" //AM05带耳机版
        const val PROTOTYPE_B9C_JL = "B9C_JL" //B9C_JL
        const val PROTOTYPE_K10 = "K10"
        const val PROTOTYPE_X5L = "X5L" //充电仓
        const val PROTOTYPE_BC01 = "BC01" //骨传导耳机
        const val PROTOTYPE_F35 = "F35"
        const val PROTOTYPE_C31 = "C31"
        const val PROTOTYPE_AM36 = "AM36"
        const val PROTOTYPE_AM23_PRO = "AM23Pro"
        const val PROTOTYPE_UB6_MINI = "UB6Mini"//尺寸 390X450  预览图尺寸 250X288
        const val PROTOTYPE_UB6_PRO = "UB6Pro"//尺寸 410X502 预览图尺寸 260X318


        //无屏幕手环
        const val PROTOTYPE_M100 = "M100"

        // 眼镜
        const val PROTOTYPE_SG02G = "SG02G"
        const val PROTOTYPE_SG02J = "SG02J"
        const val PROTOTYPE_SG02W = "SG02W"
        const val PROTOTYPE_SG03J = "SG03J"

        // SIFLI
        const val PROTOTYPE_SF15GUC = "SF15GUC"

        // ZKLX
        const val PROTOTYPE_RN01 = "RN01"

        const val AGPS_NONE = 0 // 无GPS芯片
        const val AGPS_EPO = 1 // MTK EPO
        const val AGPS_UBLOX = 2
        const val AGPS_AGNSS = 6 // 中科微
        const val AGPS_EPO2 = 7 // Airoha EPO
        const val AGPS_LTO = 8 // 博通 LTO
        const val AGPS_6228 = 9 // 6228

        const val WATCH_0 = 0           // 不支持表盘
        const val WATCH_1 = 1           // Q3表盘
        const val WATCH_2 = 2           //MTK标准化表盘
        const val WATCH_3 = 3           //Realtek bmp格式表盘 方形
        const val WATCH_4 = 4           //MTK-小尺寸表盘 要求表盘文件不超过40K
        const val WATCH_5 = 5           //MTK-表盘文件分辨率320x385
        const val WATCH_6 = 6           //MTK-表盘文件分辨率320x363
        const val WATCH_7 = 7           //Realtek bmp格式表盘 圆形
        const val WATCH_8 = 8           //汇顶平台表盘
        const val WATCH_9 = 9          //瑞昱R6,R8球拍屏，240x240
        const val WATCH_10 = 10        //瑞昱240*280方形表盘BMP格式（单蓝牙）（中间件项目，表盘需字节对齐）
        const val WATCH_11 = 11        //瑞昱bmp格式表盘, 圆形表盘  240*240，双模蓝牙
        const val WATCH_12 = 12        //瑞昱bmp格式表盘，方形表盘 240*240 双模蓝牙
        const val WATCH_13 = 13        //MTK 240x240-新表盘
        const val WATCH_14 = 14        //瑞昱80*160方形表盘BMP格式
        const val WATCH_15 = 15        //360x360 BMP 圆形-目前应用于瑞昱平台
        const val WATCH_16 = 16        //瑞昱240*280方形表盘BMP格式（双蓝牙）
        const val WATCH_17 = 17        //瑞昱 454x454 圆形 双蓝牙 R9 （中间件项目，表盘需字节对齐）
        const val WATCH_18 = 18        //瑞昱 240x240 圆形 单蓝牙 GTM5（中间件项目，表盘需字节对齐）
        const val WATCH_19 = 19        //瑞昱240*280方形表盘BMP格式（单蓝牙）
        const val WATCH_20 = 20        //瑞昱240*280方形表盘BMP格式（双蓝牙）
        const val WATCH_21 = 21        //瑞昱240*295方形表盘BMP格式（单蓝牙）（中间件项目，表盘需字节对齐）
        const val WATCH_99 = 99        //返回99的直接从服务器获取

        const val DIGITAL_POWER_VISIBLE = 0 //显示
        const val DIGITAL_POWER_HIDE = 1

        const val ANTI_LOST_VISIBLE = 1 //防丢显示
        const val ANTI_LOST_HIDE = 0    //防丢默认隐藏

        const val SUPPORT_NEW_SLEEP_ALGORITHM_0 = 0    //新版睡眠算法-不支持-默认
        const val SUPPORT_NEW_SLEEP_ALGORITHM_1 = 1   //新版睡眠算法

        const val SUPPORT_DATE_FORMAT_1 = 1   //支持
        const val SUPPORT_DATE_FORMAT_0 = 0   //默认不支持

        const val SUPPORT_READ_DEVICE_INFO_1 = 1   //支持
        const val SUPPORT_READ_DEVICE_INFO_0 = 0   //默认不支持

        const val SUPPORT_TEMPERATURE_UNIT_1 = 1   //支持
        const val SUPPORT_TEMPERATURE_UNIT_0 = 0   //默认不支持

        const val SUPPORT_DRINK_WATER_1 = 1   //支持
        const val SUPPORT_DRINK_WATER_0 = 0   //默认不支持

        const val SUPPORT_CHANGE_CLASSIC_BLUETOOTH_STATE_1 = 1   //支持
        const val SUPPORT_CHANGE_CLASSIC_BLUETOOTH_STATE_0 = 0   //默认不支持

        const val SUPPORT_APP_SPORT_1 = 1   //支持
        const val SUPPORT_APP_SPORT_0 = 0   //默认不支持

        const val SUPPORT_BLOOD_OXYGEN_1 = 1   //支持
        const val SUPPORT_BLOOD_OXYGEN_0 = 0   //默认不支持

        const val SUPPORT_WASH_1 = 1   //支持
        const val SUPPORT_WASH_0 = 0   //默认不支持

        const val SUPPORT_REQUEST_REALTIME_WEATHER_1 = 1   //支持
        const val SUPPORT_REQUEST_REALTIME_WEATHER_0 = 0   //默认不支持

        const val SUPPORT_HID_1 = 1   //支持
        const val SUPPORT_HID_0 = 0   //默认不支持

        const val SUPPORT_IBEACON_SET_2 = 2   //支持, 发送8个字节，安卓的默认全0
        const val SUPPORT_IBEACON_SET_1 = 1   //支持
        const val SUPPORT_IBEACON_SET_0 = 0   //默认不支持

        const val SUPPORT_WATCHFACE_ID_1 = 1   //支持
        const val SUPPORT_WATCHFACE_ID_0 = 0   //默认不支持

        const val SUPPORT_JL_TRANSPORT_0 = 0 //默认不支持
        const val SUPPORT_JL_TRANSPORT_1 = 1 //支持

        const val SUPPORT_FIND_WATCH_0 = 0 //默认不支持
        const val SUPPORT_FIND_WATCH_1 = 1 //不支持

        const val SUPPORT_WORLD_CLOCK_0 = 0 //默认不支持
        const val SUPPORT_WORLD_CLOCK_1 = 1 //支持

        const val SUPPORT_STOCK_0 = 0 //默认不支持
        const val SUPPORT_STOCK_1 = 1 //支持

        const val SUPPORT_SMS_QUICK_REPLY_0 = 0 //默认不支持
        const val SUPPORT_SMS_QUICK_REPLY_1 = 1 //支持

        const val SUPPORT_NO_DISTURB_0 = 0 //默认不支持
        const val SUPPORT_NO_DISTURB_1 = 1 //支持

        const val SUPPORT_SET_WATCH_PASSWORD_0 = 0 //默认不支持
        const val SUPPORT_SET_WATCH_PASSWORD_1 = 1 //支持

        const val SUPPORT_REALTIME_MEASUREMENT_0 = 0 //默认不支持
        const val SUPPORT_REALTIME_MEASUREMENT_1 = 1 //支持

        const val SUPPORT_POWER_SAVE_MODE_0 = 0 //默认不支持
        const val SUPPORT_POWER_SAVE_MODE_1 = 1 //支持

        const val SUPPORT_LOVE_TAP_0 = 0 //默认不支持
        const val SUPPORT_LOVE_TAP_1 = 1 //支持

        const val SUPPORT_NEWS_FEED_0 = 0 //默认不支持
        const val SUPPORT_NEWS_FEED_1 = 1 //支持

        const val SUPPORT_MEDICATION_REMINDER_0 = 0 //默认不支持
        const val SUPPORT_MEDICATION_REMINDER_1 = 1 //支持

        const val SUPPORT_QRCODE_0 = 0 //默认不支持
        const val SUPPORT_QRCODE_1 = 1 //支持

        const val SUPPORT_WEATHER2_0 = 0 //默认不支持
        const val SUPPORT_WEATHER2_1 = 1 //支持

        const val SUPPORT_ALIPAY_0 = 0 //默认不支持
        const val SUPPORT_ALIPAY_1 = 1 //支持

        const val SUPPORT_STANDBY_SET_0 = 0 //默认不支持
        const val SUPPORT_STANDBY_SET_1 = 1 //支持

        const val SUPPORT_2D_ACCELERATION_0 = 0 //默认不支持
        const val SUPPORT_2D_ACCELERATION_1 = 1 //支持

        const val SUPPORT_TUYA_KEY_0 = 0 //默认不支持
        const val SUPPORT_TUYA_KEY_1 = 1 //支持

        const val SUPPORT_MEDICATION_ALARM_0 = 0 //默认不支持
        const val SUPPORT_MEDICATION_ALARM_1 = 1 //支持

        const val SUPPORT_READ_PACKAGE_STATUS_0 = 0 //默认不支持
        const val SUPPORT_READ_PACKAGE_STATUS_1 = 1 //支持

        const val SUPPORT_VOICE_0 = 0 //默认不支持
        const val SUPPORT_VOICE_1 = 1 //支持

        const val SUPPORT_NAVIGATION_0 = 0//默认不支持
        const val SUPPORT_NAVIGATION_1 = 1//支持

        const val SUPPORT_HR_WARN_SET_0 = 0//默认不支持
        const val SUPPORT_HR_WARN_SET_1 = 1//支持

        const val SUPPORT_MUSIC_TRANSFER_0 = 0//默认不支持
        const val SUPPORT_MUSIC_TRANSFER_1 = 1//支持

        const val SUPPORT_NO_DISTURB2_0 = 0//默认不支持
        const val SUPPORT_NO_DISTURB2_1 = 1//支持

        const val SUPPORT_SOS_SET_0 = 0//默认不支持
        const val SUPPORT_SOS_SET_1 = 1//支持

        const val SUPPORT_READ_LANGUAGES_0 = 0//默认不支持
        const val SUPPORT_READ_LANGUAGES_1 = 1//支持

        const val SUPPORT_GIRLCARE_REMINDER_0 = 0//默认不支持
        const val SUPPORT_GIRLCARE_REMINDER_1 = 1//支持

        const val SUPPORT_GAME_TIME_REMINDER_0 = 0//默认不支持
        const val SUPPORT_GAME_TIME_REMINDER_1 = 1//支持

        const val SUPPORT_DEVICE_SPORT_DATA_1 = 1   //支持
        const val SUPPORT_DEVICE_SPORT_DATA_0 = 0   //默认不支持

        const val SUPPORT_EBOOK_TRANSFER_0 = 0//默认不支持
        const val SUPPORT_EBOOK_TRANSFER_1 = 1//支持

        const val SUPPORT_DOUBLE_SCREEN_1 = 1   //支持
        const val SUPPORT_DOUBLE_SCREEN_0 = 0   //默认不支持

        const val SUPPORT_CUSTOM_LOGO_1 = 1   //支持
        const val SUPPORT_CUSTOM_LOGO_0 = 0   //默认不支持

        const val SUPPORT_PRESSURE_TIMING_MEASUREMENT_1 = 1   //支持
        const val SUPPORT_PRESSURE_TIMING_MEASUREMENT_0 = 0   //默认不支持

        const val SUPPORT_TIMER_STANDBYSET_1 = 1   //支持
        const val SUPPORT_TIMER_STANDBYSET_0 = 0   //默认不支持

        const val SUPPORT_FALL_SET_0 = 0//默认不支持
        const val SUPPORT_FALL_SET_1 = 1//支持

        const val SUPPORT_WALK_BIKE_0 = 0//默认不支持
        const val SUPPORT_WALK_BIKE_1 = 1//支持

        const val SUPPORT_CONNECT_REMINDER_0 = 0//默认不支持
        const val SUPPORT_CONNECT_REMINDER_1 = 1//支持

        const val SUPPORT_SDCARD_INFO_0 = 0//默认不支持
        const val SUPPORT_SDCARD_INFO_1 = 1//支持

        const val SUPPORT_INCOMING_CALL_RING_0 = 0 //默认不支持
        const val SUPPORT_INCOMING_CALL_RING_1 = 1 //支持

        const val SUPPORT_NOTIFICATION_LIGHT_SCREEN_SET_0 = 0 //默认不支持
        const val SUPPORT_NOTIFICATION_LIGHT_SCREEN_SET_1 = 1 //支持

        const val SUPPORT_BLOOD_PRESSURE_CALIBRATION_0 = 0 //默认不支持
        const val SUPPORT_BLOOD_PRESSURE_CALIBRATION_1 = 1 //支持

        const val SUPPORT_OTA_FILE_0 = 0 //默认不支持
        const val SUPPORT_OTA_FILE_1 = 1 //支持

        const val SUPPORT_GPS_FIRMWARE_FILE_0 = 0 //默认不支持
        const val SUPPORT_GPS_FIRMWARE_FILE_1 = 1 //支持

        const val SUPPORT_GOMORE_SET_0 = 0 //默认不支持
        const val SUPPORT_GOMORE_SET_1 = 1 //支持

        const val SUPPORT_RING_VIBRATION_SET_0 = 0 //默认不支持
        const val SUPPORT_RING_VIBRATION_SET_1 = 1 //支持

        const val SUPPORT_NETWORK_0 = 0 //默认不支持
        const val SUPPORT_NETWORK_1 = 1 //支持

        const val SUPPORT_CONTACT_SORT_0 = 0 //默认不支持
        const val SUPPORT_CONTACT_SORT_1 = 1 //支持

        const val SUPPORT_STRING_QRCODE_0 = 0 //默认不支持
        const val SUPPORT_STRING_QRCODE_1 = 1 //支持

        const val SUPPORT_WATCHFACE_INDEX_0 = 0 //默认不支持
        const val SUPPORT_WATCHFACE_INDEX_1 = 1 //支持

        const val SUPPORT_SOS_CONTACT_0 = 0 //默认不支持
        const val SUPPORT_SOS_CONTACT_1 = 1 //支持

        const val SUPPORT_GIRL_CARE_MONTHLY_0 = 0 //默认不支持
        const val SUPPORT_GIRL_CARE_MONTHLY_1 = 1 //支持

        const val SUPPORT_WEAR_WAY_0 = 0 //默认不支持
        const val SUPPORT_WEAR_WAY_1 = 1 //支持

        const val SUPPORT_GESTUREWAKE2_0 = 0 //默认不支持
        const val SUPPORT_GESTUREWAKE2_1 = 1 //支持

        const val SUPPORT_NAV_IMAGE_0 = 0 //默认不支持
        const val SUPPORT_NAV_IMAGE_1 = 1 //支持

        const val SUPPORT_VOICE_MAX_LENGTH_0 = 0 //不支持
        const val SUPPORT_VOICE_MAX_LENGTH_1 = 1 //支持

        const val SUPPORT_AUDIO_BOOKS_0 = 0 //默认不支持
        const val SUPPORT_AUDIO_BOOKS_1 = 1 //支持

        const val SUPPORT_STUDY_CARDS_0 = 0 //默认不支持
        const val SUPPORT_STUDY_CARDS_1 = 1 //支持

        const val SUPPORT_APP_STORE_0 = 0 //默认不支持
        const val SUPPORT_APP_STORE_1 = 1 //支持

        const val SUPPORT_SHSY_ALGORITHM_0 = 0 //默认不支持
        const val SUPPORT_SHSY_ALGORITHM_1 = 1 //支持

        const val SUPPORT_QIBLA_SET_0 = 0 //默认不支持
        const val SUPPORT_QIBLA_SET_1 = 1 //支持

        const val SUPPORT_MEASUREMENT_BLOOD_GLUCOSE_0 = 0 //默认不支持
        const val SUPPORT_MEASUREMENT_BLOOD_GLUCOSE_1 = 1 //支持

        const val SUPPORT_GAME_CONTROL_0 = 0 //默认不支持
        const val SUPPORT_GAME_CONTROL_1 = 1 //支持

        const val SUPPORT_BATTERY_USAGE_0 = 0 //默认不支持
        const val SUPPORT_BATTERY_USAGE_1 = 1 //支持

        const val SUPPORT_AI_TRANSLATION_0 = 0 //默认不支持
        const val SUPPORT_AI_TRANSLATION_1 = 1 //支持

        const val SUPPORT_SIMULTANEOUS_TRANSLATION_0 = 0 //默认不支持
        const val SUPPORT_SIMULTANEOUS_TRANSLATION_1 = 1 //支持

        const val SUPPORT_TOUCH_SET_0 = 0 //默认不支持
        const val SUPPORT_TOUCH_SET_1 = 1 //支持

        const val SUPPORT_IMEI_SET_0 = 0 //默认不支持
        const val SUPPORT_IMEI_SET_1 = 1 //支持

        const val SUPPORT_QURAN_0 = 0 //默认不支持
        const val SUPPORT_QURAN_1 = 1 //支持

        const val SUPPORT_SYNC_AGPS_IN_BACKGROUND_0 = 0 //默认不支持
        const val SUPPORT_SYNC_AGPS_IN_BACKGROUND_1 = 1 //支持

        const val SUPPORT_RESTORE_FACTORY_0 = 0 //默认不支持
        const val SUPPORT_RESTORE_FACTORY_1 = 1 //支持

        const val SUPPORT_RECORD_NOTE_0 = 0 //默认不支持
        const val SUPPORT_RECORD_NOTE_1 = 1 //支持

        const val SUPPORT_SLEEP_SCORE_0 = 0 //默认不支持
        const val SUPPORT_SLEEP_SCORE_1 = 1 //支持

        const val SUPPORT_WATCHFACE2_0 = 0 //默认不支持
        const val SUPPORT_WATCHFACE2_1 = 1 //支持

        const val SUPPORT_AI_COACH_0 = 0 //默认不支持
        const val SUPPORT_AI_COACH_1 = 1 //支持

        const val SUPPORT_CROSS_APP_TRANSLATION_0 = 0 //默认不支持
        const val SUPPORT_CROSS_APP_TRANSLATION_1 = 1 //支持

        const val SUPPORT_RELAX_REMINDER_0 = 0 //默认不支持
        const val SUPPORT_RELAX_REMINDER_1 = 1 //支持

        const val SUPPORT_POWER2_0 = 0 //默认不支持
        const val SUPPORT_POWER2_1 = 1 //支持

        const val SUPPORT_WECHAT_0 = 0 //默认不支持
        const val SUPPORT_WECHAT_1 = 1 //支持

        const val SUPPORT_WHATSAPP_0 = 0 //默认不支持
        const val SUPPORT_WHATSAPP_1 = 1 //支持 WhatsApp

        const val SUPPORT_READ_LANGUAGES2_0 = 0//默认不支持
        const val SUPPORT_READ_LANGUAGES2_1 = 1//支持

        const val SUPPORT_TIME_FORMAT_0 = 0//默认不支持
        const val SUPPORT_TIME_FORMAT_1 = 1//支持

        const val SUPPORT_VIBRATION_INTENSITY_0 = 0//默认不支持
        const val SUPPORT_VIBRATION_INTENSITY_1 = 1//支持

        const val SUPPORT_HIGH_ANTI_ASSIST_0 = 0//默认不支持
        const val SUPPORT_HIGH_ANTI_ASSIST_1 = 1//支持

        const val SUPPORT_WECHAT_PAY_0 = 0//默认不支持
        const val SUPPORT_WECHAT_PAY_1 = 1//支持

        const val SUPPORT_VITALITY_VALUE_0 = 0//默认不支持
        const val SUPPORT_VITALITY_VALUE_1 = 1//支持

        const val SUPPORT_HR_DETECTION_MODE_0 = 0//默认不支持
        const val SUPPORT_HR_DETECTION_MODE_1 = 1//支持

        const val SUPPORT_AI_COACH_V2_0 = 0//默认不支持
        const val SUPPORT_AI_COACH_V2_1 = 1//支持

        const val SUPPORT_RESPIRATORY_RATE_0 = 0//默认不支持
        const val SUPPORT_RESPIRATORY_RATE_1 = 1//支持

        const val SUPPORT_PHYSICAL_STRENGTH_0 = 0//默认不支持
        const val SUPPORT_PHYSICAL_STRENGTH_1 = 1//支持

        const val SUPPORT_IPC_0 = 0//默认不支持
        const val SUPPORT_IPC_1 = 1//支持

        const val SUPPORT_VIDEO_CALLS_0 = 0//默认不支持
        const val SUPPORT_VIDEO_CALLS_1 = 1//支持

        const val SUPPORT_RECORD_AUDIO_0 = 0//默认不支持
        const val SUPPORT_RECORD_AUDIO_1 = 1//支持

        const val SUPPORT_ACTIVITY_RECOGNITION_0 = 0//默认不支持
        const val SUPPORT_ACTIVITY_RECOGNITION_1 = 1//支持
    }

    override fun decode(byteArray: ByteArray){
         byteArray.parse {
            val endFlag = int8()
            println("结束符:$endFlag")
            mId = int32()
            mDataKeys = readByteArrayUntilByte(0).toList().chunked(2).map {
                ((it[0].toInt() and 0xff) shl 8) or (it[1].toInt() and 0xff )
            }
            mBleName = readStringUntilByte(0)
            mBleAddress = readStringUntilByte(0).uppercase(Locale.getDefault())
            mPlatform = readStringUntilByte(0)
            mPrototype = readStringUntilByte(0)
            mFirmwareFlag = readStringUntilByte(0)
            mAGpsType = int8()
            mIOBufferSize = int16().toLong()
            mWatchFaceType = int8()
            mClassicAddress = readStringUntilByte(0).uppercase(Locale.getDefault())
            mHideDigitalPower = int8()
            mShowAntiLostSwitch = int8()
            mSleepAlgorithmType = int8()
            mSupportDateFormatSet = int8()
            mSupportReadDeviceInfo = int8()
            mSupportTemperatureUnitSet = int8()
            mSupportDrinkWaterSet = int8()
            mSupportChangeClassicBluetoothState = int8()
            mSupportAppSport = int8()
            mSupportBloodOxyGenSet = int8()
            mSupportWashSet = int8()
            mSupportRequestRealtimeWeather = int8()
            mSupportHID = int8()
            mSupportIBeaconSet = int8()
            mSupportWatchFaceId = int8()
            mSupportNewTransportMode = int8()
            mSupportJLTransport = int8()
            mSupportFindWatch = int8()
            mSupportWorldClock = int8()
            mSupportStock = int8()
            mSupportSMSQuickReply = int8()
            mSupportNoDisturbSet = int8()
            mSupportSetWatchPassword = int8()
            mSupportRealTimeMeasurement = int8()
            mSupportPowerSaveMode = int8()
            mSupportLoveTap = int8()
            mSupportNewsfeed = int8()
            mSupportMedicationReminder = int8()
            mSupportQrcode = int8()
            mSupportWeather2 = int8()
            mSupportAlipay = int8()
            mSupportStandbySet = int8()
            mSupport2DAcceleration = int8()
            mSupportTuyaKey = int8()
            mSupportMedicationAlarm = int8()
            mSupportReadPackageStatus = int8()
            mSupportContactSize = int8() * 10
            mSupportVoice = int8()
            mSupportNavigation = int8()
            mSupportHrWarnSet = int8()
            mSupportMusicTransfer = int8()
            mSupportNoDisturbSet2 = int8()
            mSupportSOSSet = int8()
            mSupportReadLanguages = int8()
            mSupportGirlCareReminder = int8()
            mSupportAppPushSwitch = int8()
            mSupportReceiptCodeSize = int8()
            mSupportGameTimeReminder = int8()
            mSupportMyCardCodeSize = int8()
            mSupportDeviceSportData = int8()
            mSupportEbookTransfer = int8()
            mSupportDoubleScreen = int8()
            mSupportCustomLogo = int8()
            mSupportPressureTimingMeasurement = int8()
            mSupportTimerStandbySet = int8()
            mSupportSOSSet2 = int8()
            mSupportFallSet = int8()
            mSupportWalkAndBike = int8()
            mSupportConnectReminder = int8()
            mSupportSDCardInfo = int8()
            mSupportIncomingCallRing = int8()
            mSupportNotificationLightScreenSet = int8()
            mSupportBloodPressureCalibration = int8()
            mSupportOTAFile = int8()
            mSupportGPSFirmwareFile = int8()
            mSupportGoMoreSet = int8()
            mSupportRingVibrationSet = int8()
            mSupportNetwork = int8()
            mSupportContactSort = int8()
            mQrcodeSize = int8()
            mQrcodeContentSize = int8()
            mSupportStringQrcode = int8()
            mSupportWatchFaceIndex = int8()
            mSupportSosContact = int8()
            mSupportGirlCareMonthly = int8()
            mSupportWearWay = int8()
            mSupportGestureWake2 = int8()
            mSupportNavImage = int8()
            mSupportVoiceMaxLength = int8()
            mSupportAudioBooks = int8()
            mSupportStudyCards = int8()
            mSupportAppStore = int8()
            mSupportSHSYAlgorithm = int8()
            mSupportQiblaSet = int8()
            mSupportMeasurementBloodGlucose = int8()
            mSupportGameControls = int8()
            mSupportBatteryUsage = int8()
            mSupportAITranslation = int8()
            mSupportSimultaneousTranslation = int8()
            mSupportTouchSet = int8()
            mSupportIMEISet = int8()
            mSupportQuran = int8()
            mSupportSyncAGPSInBackground = int8()
            mSupportRestoreFactory = int8()
            mSupportRecordNote = int8()
            mSupportSleepScore = int8()
            mSupportWatchface2 = int8()
            mSupportAICoach = int8()
            mSupportCrossAppTranslation = int8()
            mSupportRelaxReminder = int8()
            mSupportPower2 = int8()
            mSupportWeChat = int8()
            mSupportWhatsApp = int8()
            mSupportReadLanguages2 = int8()
            mSupportTimeFormat = int8()
            mSupportVibrationIntensity = int8()
            mSupportHighAntiAssist = int8()
            mSupportWechatPay = int8()
            mSupportVitalityValue = int8()
            mSupportHRDetectionMode = int8()
            mSupportAICoachV2 = int8()
            mSupportRespiratoryRate = int8()
            mSupportPhysicalStrength = int8()
            mSupportIPC = int8()
            mSupportVideoCalls = int8()
            mSupportRecordAudio = int8()
            mSupportActivityRecognition = int8()
        }
    }

    /*
    * 重置一些内容
    * */
    private fun resetBleInfo(parse: ParsingScope){
        if (mFirmwareFlag.contains(RAW_NAME_SEPARATOR)) {
            //获取原始蓝牙名
            val rawName = mFirmwareFlag.split(RAW_NAME_SEPARATOR)[1]
            //rawName不为空且与mBleName不同，说明mBleName是自定义蓝牙名需要重新调整
            if (!TextUtils.isEmpty(rawName) && rawName != mBleName) {
                mBleCustomName = mBleName//先保存自定义名字
                mBleName = rawName//强制改为原始蓝牙名
            }
        }
        //获取默认蓝牙名，即原始蓝牙名，自定义蓝牙名字的设备，后面可能用这种方式调整一下
        mBleDefaultName = parse.readStringUntilByte(0)
        if (!TextUtils.isEmpty(mBleDefaultName)) {
            mBleCustomName = mBleName//先保存自定义名字
            mBleName = mBleDefaultName//强制改为原始蓝牙名
        }
    }

}
