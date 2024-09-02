package com.cmedhealth.flutter.bmi.cmed_bmi_devices_lib.aifit


import aicare.net.cn.iweightlibrary.entity.User
import aicare.net.cn.iweightlibrary.entity.WeightData
import android.app.Activity
import android.content.Context
import android.util.Log
import com.cmed.plugin.lib.flutter.cmed_health_flutter_devices_lib.utils.CMEDUser
import com.cmed.plugin.lib.flutter.cmed_health_flutter_devices_lib.utils.GenderEnum
import com.cmedhealth.flutter.bmi.cmed_bmi_devices_lib.PermissionHandler
import com.google.gson.Gson
import kotlin.math.roundToInt

class CMEDAiFitWeightHandler internal constructor(
    private val context: Context,
    private val callback: FrecomDeviceCallback,
) : BleHandler.AiFitWeightCallback {
    private var bleHandler: BleHandler? = null
    private var user: CMEDUser = CMEDUser(1, GenderEnum.MALE.name, 10585, 0L, 167.0, 60.0)

    fun setUser(user: CMEDUser?) {
        if (user != null) {
            this.user = user
        }
    }

    fun connect() {
        if (!PermissionHandler.getBluetoothPermission(context)) {
            (context as Activity).runOnUiThread {
                callback.onGetResponse("${ResponseEnum.CS_PERMISSION_DENIED}:Permission denied")
            }
            return
        }
        bleHandler = BleHandler(context, this, getMappedDeviceUser(user))
    }

    private fun getMappedDeviceUser(user: CMEDUser): User {
        val weight: Double = user.weightInKg
        val height: Double = user.heightInCm
        val age: Int = user.ageInDays
        return User(
            1, user.getGenderIndex() ?: 1, age,
            height.roundToInt(), (weight * 2.205).roundToInt(), 0
        )
    }

    fun disconnect() {
        bleHandler!!.disconnect()
    }

    fun stopScan() {
        bleHandler!!.stopScan()
    }

    override fun handleMessage(msg: String?) {
        Log.v("CMEDAiFitHandler", msg!!)
        (context as Activity).runOnUiThread {
            when (msg) {
                "BleDisabled", "Bluetooth Error" -> callback.onGetResponse("${ResponseEnum.CS_WEIGHT_BLE_DISABLED}:$msg")
                "Searching" -> callback.onGetResponse("${ResponseEnum.CS_WEIGHT_ACTION_SEARCHING}:$msg")
                "Device Found" -> callback.onGetResponse("${ResponseEnum.CS_WEIGHT_ACTION_DEVICE_FOUND}:$msg")
                "Device Not Found" ->
                    callback.onGetResponse("${ResponseEnum.CS_WEIGHT_ACTION_DEVICE_NOT_FOUND}:$msg")

                "Connecting" ->
                    callback.onGetResponse("${ResponseEnum.CS_WEIGHT_ACTION_CONNECTING}:$msg")

                "Connected" -> {
                    callback.onGetResponse("${ResponseEnum.CS_WEIGHT_ACTION_CONNECTED}:$msg")
                }

                "analyzing" -> {
                    callback.onGetResponse("${ResponseEnum.CS_ANALYZING}:$msg")
                }

                "screen_off" -> {
                    callback.onGetResponse("${ResponseEnum.CS_SCREEN_OFF}:$msg")
                }

                "low_voltage" -> {
                    callback.onGetResponse("${ResponseEnum.CS_LOW_VOLTAGE}:$msg")
                }

                "weighing_overload" -> {
                    callback.onGetResponse("${ResponseEnum.CS_WEIGHING_OVERLOAD}:$msg")
                }

                "weighing_timeout" -> {
                    callback.onGetResponse("${ResponseEnum.CS_WEIGHING_TIMEOUT}:$msg")
                }

                "impedance_measurement_failed" -> {
                    callback.onGetResponse("${ResponseEnum.CS_IMPEDANCE_MEASUREMENT_FAILED}:$msg")
                }

                "Disconnected" -> {
                    callback.onGetResponse("${ResponseEnum.CS_WEIGHT_ACTION_DISCONNECTED}:$msg")
                }
            }
        }
    }

    override fun onGetWeight(weight: WeightData?) {
        (context as Activity).runOnUiThread {
            callback.onGetResponse("${ResponseEnum.CS_ONLINE_WEIGHT}:${getFormattedWeight(weight!!)}")
        }
    }

    override fun onGetFatData(bodyFatData: BodyFatData?) {
        val fatData = Gson().toJson((CalcParam(bodyFatData))
        Log.v("FAT_DATA", fatData.toString())
        (context as Activity).runOnUiThread {
            callback.onGetResponse("${ResponseEnum.CS_FAT_DATA}:$fatData")
        }
    }

    private fun getFormattedWeight(weightData: WeightData): String {
        return (weightData.weight / 10).toString()
    }
}
