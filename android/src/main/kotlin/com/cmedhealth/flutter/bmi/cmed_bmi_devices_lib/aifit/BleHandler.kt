package com.cmedhealth.flutter.bmi.cmed_bmi_devices_lib.aifit

import aicare.net.cn.iweightlibrary.AiFitSDK
import aicare.net.cn.iweightlibrary.entity.*
import aicare.net.cn.iweightlibrary.utils.AicareBleConfig
import aicare.net.cn.iweightlibrary.utils.ParseData
import aicare.net.cn.iweightlibrary.wby.WBYService.WBYBinder
import android.content.Context
import android.util.Log
import com.cmedhealth.flutter.bmi.cmed_bmi_devices_lib.R
import com.google.gson.Gson
import kotlin.math.roundToInt

class BleHandler(
    context: Context,
    private val aiFitWeightCallback: AiFitWeightCallback,
    user: User?
) {
    interface AiFitWeightCallback {
        fun handleMessage(msg: String?)
        fun onGetWeight(weight: WeightData?)
        fun onGetFatData(bodyFatData: BodyFatData?)
    }

    private val TAG = "BleHandler"
    private var bleProfileService: BleProfileService<WBYBinder>? = null
    private var binder: WBYBinder? = null
    private val user: User?

    init {
        AiFitSDK.getInstance().init(context)
        init(context)
        this.user = user
        startScan()
    }

    private fun init(context: Context) {
        bleProfileService = object : BleProfileService<WBYBinder>(context) {
            override fun onError(errorMessage: String?, errorCode: Int) {
                if (errorCode == DEVICE_NOT_FOUND) aiFitWeightCallback.handleMessage(errorMessage)
                else if (errorCode == BLUETOOTH_ERROR) aiFitWeightCallback.handleMessage(
                    errorMessage
                )
            }

            override fun onGetWeightData(weightData: WeightData?) {
                if (weightData == null) return
                if (weightData.cmdType == 2 && binder != null && user != null) {
                    binder!!.syncUser(user)
                }
                aiFitWeightCallback.onGetWeight(weightData)
            }

            override fun onGetSettingStatus(status: Int) {
                when (status) {
                    AicareBleConfig.SettingStatus.NORMAL -> Log.v(
                        TAG,
                        "${R.string.settings_status} ${context.getString(R.string.normal)}"
                    )
                    AicareBleConfig.SettingStatus.LOW_POWER -> {
                        Log.v(
                            TAG,
                            "${R.string.settings_status} ${context.getString(R.string.low_power)}"
                        )
                        aiFitWeightCallback.handleMessage("screen_off")
                    }
                    AicareBleConfig.SettingStatus.LOW_VOLTAGE -> {
                        Log.v(
                            TAG,
                            "${R.string.settings_status} ${context.getString(R.string.low_voltage)}"
                        )
                        aiFitWeightCallback.handleMessage("low_voltage")
                    }
                    AicareBleConfig.SettingStatus.ERROR -> {
                        Log.v(
                            TAG,
                            "${R.string.settings_status} ${context.getString(R.string.error)}"
                        )
                        //                        Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_SHORT).show();
                        aiFitWeightCallback.handleMessage("weighing_overload")
                    }
                    AicareBleConfig.SettingStatus.TIME_OUT -> {
                        Log.v(
                            TAG,
                            "${R.string.settings_status} ${context.getString(R.string.time_out)}"
                        )
                        aiFitWeightCallback.handleMessage("weighing_timeout")
                    }
                    AicareBleConfig.SettingStatus.SET_UNIT_SUCCESS -> Log.v(
                        TAG,
                        "${R.string.settings_status} ${context.getString(R.string.set_unit_success)}"
                    )
                    AicareBleConfig.SettingStatus.SET_UNIT_FAILED -> Log.v(
                        TAG,
                        "${R.string.settings_status} ${context.getString(R.string.set_unit_failed)}"
                    )
                    AicareBleConfig.SettingStatus.SET_USER_SUCCESS -> Log.v(
                        TAG,
                        "${R.string.settings_status} ${context.getString(R.string.set_user_success)}"
                    )
                    AicareBleConfig.SettingStatus.SET_USER_FAILED -> Log.v(
                        TAG,
                        "${R.string.settings_status} ${context.getString(R.string.set_user_failed)}"
                    )
                    AicareBleConfig.SettingStatus.ADC_MEASURED_ING -> {
                        aiFitWeightCallback.handleMessage("analyzing")
                        Log.v(
                            TAG,
                            "${R.string.settings_status} ${context.getString(R.string.adc_measured_ind)}"
                        )
                    }
                    AicareBleConfig.SettingStatus.ADC_ERROR -> {
                        Log.v(
                            TAG,
                            "${R.string.settings_status} + ${context.getString(R.string.adc_error)}"
                        )
                        aiFitWeightCallback.handleMessage("impedance_measurement_failed")
                    }
                }
            }

            override fun onGetResult(resultCode: Int, result: String?) {}
            override fun onGetFatData(status: Boolean, bodyFatData: BodyFatData?) {
                aiFitWeightCallback.onGetFatData(bodyFatData)
            }

            override fun onGetDecimalInfo(decimalInfo: DecimalInfo?) {
                aiFitWeightCallback.handleMessage("Connected")
            }

            override fun onGetAlgorithmInfo(algorithmInfo: AlgorithmInfo?) {
                if (user != null && algorithmInfo != null) {
                    Log.v("FAT_DATA", "For "+user.toString())
                    val bodyFatData = AicareBleConfig
                        .getBodyFatData(
                            algorithmInfo.algorithmId,
                            user.sex,
                            user.age,
                            ParseData.getKgWeight(algorithmInfo.weight, algorithmInfo.decimalInfo).toDouble(),
                            user.height,
                            algorithmInfo.adc
                        )
                    val bodyFatData1 = BodyFatData()
                    bodyFatData1.age = user.age
                    bodyFatData1.weight = algorithmInfo.weight
                    bodyFatData1.sex = user.sex
                    bodyFatData1.height = user.height
                    bodyFatData1.adc = algorithmInfo.adc
                    bodyFatData1.bfr = bodyFatData.bfr
                    bodyFatData1.bm = bodyFatData.bm
                    bodyFatData1.bmi = bodyFatData.bmi
                    bodyFatData1.bmr = bodyFatData.bmr.toDouble()
                    bodyFatData1.bodyAge = bodyFatData.bodyAge
                    bodyFatData1.pp = bodyFatData.pp
                    bodyFatData1.rom = bodyFatData.rom
                    bodyFatData1.vwc = bodyFatData.vwc
                    bodyFatData1.sfr = bodyFatData.sfr
                    bodyFatData1.uvi = bodyFatData.uvi
                    bodyFatData1.decimalInfo = algorithmInfo.decimalInfo
                    bodyFatData1.number = 1
                    bodyFatData1.date = ParseData.getDate()
                    bodyFatData1.time = ParseData.getTime()

                    Log.v("FAT_DATA", "From Device: "+bodyFatData1.toString())

                    onGetFatData(false, bodyFatData1)
                }
            }

            override fun onServiceBinded(wbyBinder: WBYBinder) {
                binder = wbyBinder
            }

            override fun onServiceUnbinded() {
                aiFitWeightCallback.handleMessage("Disconnected")
                binder = null
            }

            override fun getAicareDevice(broadData: BroadData?) {
                if (broadData?.name == "SWAN" && !isDeviceConnected) {
                    aiFitWeightCallback.handleMessage("Device Found")
                    startConnect(broadData.address)
                    aiFitWeightCallback.handleMessage("Connecting")
                    stopScan()
                }
            }

            override fun onStateChanged(state: Int) {
                super.onStateChanged(state)
                when (state) {
                    aicare.net.cn.iweightlibrary.bleprofile.BleProfileService.STATE_CONNECTED -> Log.v(
                        TAG,
                        "state_connected"
                    )
                    aicare.net.cn.iweightlibrary.bleprofile.BleProfileService.STATE_DISCONNECTED -> Log.v(
                        TAG,
                        "state_disconnected"
                    )
                    aicare.net.cn.iweightlibrary.bleprofile.BleProfileService.STATE_SERVICES_DISCOVERED -> Log.v(
                        TAG,
                        "state_service_discovered"
                    )
                    aicare.net.cn.iweightlibrary.bleprofile.BleProfileService.STATE_INDICATION_SUCCESS -> {
                        if (binder != null && user != null) binder!!.syncUser(user)
                        Log.v(TAG, "state_indication_success")
                    }
                    aicare.net.cn.iweightlibrary.bleprofile.BleProfileService.STATE_TIME_OUT -> Log.v(
                        TAG,
                        "state_time_out"
                    )
                    aicare.net.cn.iweightlibrary.bleprofile.BleProfileService.STATE_CONNECTING -> Log.v(
                        TAG,
                        "state_connecting"
                    )
                }
            }
        }
    }

    fun startScan() {
        aiFitWeightCallback.handleMessage("Searching")
        bleProfileService?.startScan()
    }

    fun stopScan() {
        bleProfileService?.stopScan()
    }

    fun disconnect() {
        if (binder != null) {
            binder?.disconnect()
        }
    }
}