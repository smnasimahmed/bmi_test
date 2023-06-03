package com.cmedhealth.flutter.bmi.cmed_bmi_devices_lib.aifit

import aicare.net.cn.iweightlibrary.entity.*
import aicare.net.cn.iweightlibrary.utils.AicareBleConfig
import aicare.net.cn.iweightlibrary.utils.ParseData
import aicare.net.cn.iweightlibrary.wby.WBYService
import aicare.net.cn.iweightlibrary.wby.WBYService.WBYBinder
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.*
import android.os.Handler
import android.os.IBinder
import android.text.TextUtils
import android.util.Log
import androidx.annotation.RequiresPermission
import java.util.*

abstract class BleProfileService<E : WBYBinder?> internal constructor(private val context: Context) {
    private var mService: E? = null
    private var mIsScanning = false
    private var bluetoothLeScanner: BluetoothLeScanner? = null
    private var adapter: BluetoothAdapter? = null
    private val mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            mService = service as E
            val bleService: E = mService as E
            onServiceBinded(bleService)
            if (bleService!!.isConnected) {
                onStateChanged(1)
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mService = null
            onServiceUnbinded()
        }
    }
    private val handler = Handler()
    private val startScanRunnable = Runnable { startScan() }
    private val stopScanRunnable = Runnable {
        stopScan()
        onError("Device Not Found", DEVICE_NOT_FOUND)
    }
    private val leScanCallback: ScanCallback = object : ScanCallback() {
        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            Log.e("BleProfileService", "onLeScan")
            if (result.device != null) {
                Log.e(
                    "BleProfileService",
                    "address: " + result.device.address + "; name: " + result.device
                )
                Log.e(
                    "BleProfileService",
                    ParseData.byteArr2Str(Objects.requireNonNull(result.scanRecord)?.bytes)
                )
                var broadData: BroadData? = null
                if (result.device.name != null && result.device.name.equals(
                        "SWAN",
                        ignoreCase = true
                    )
                ) broadData = AicareBleConfig.getBroadData(
                    result.device, result.rssi, result.scanRecord!!
                        .bytes
                )
                if (broadData != null) {
                    val mainHandler = Handler(context.mainLooper)
                    val bData: BroadData = broadData
                    val myRunnable = Runnable { getAicareDevice(bData) } // This is your code
                    mainHandler.post(myRunnable)
                }
            }
        }
    }

    @RequiresPermission("android.permission.BLUETOOTH_ADMIN")
    protected fun bluetoothStateChanged(state: Int) {
        if (state == 13) {
            if (mService != null) {
                mService!!.disconnect()
            }
            stopScan()
        }
    }

    protected open fun onStateChanged(state: Int) {
        if (state == 0) {
            unbindService()
        }
    }

    private fun unbindService() {
        try {
            context.unbindService(mServiceConnection)
            mService = null
            onServiceUnbinded()
        } catch (ignored: IllegalArgumentException) {
        }
    }

    protected abstract fun onError(errorMessage: String?, errorCode: Int)
    protected abstract fun onGetWeightData(weightData: WeightData?)
    protected abstract fun onGetSettingStatus(status: Int)
    protected abstract fun onGetResult(resultCode: Int, result: String?)
    protected abstract fun onGetFatData(status: Boolean, bodyFatData: BodyFatData?)
    protected fun onGetAuthData(
        sources: ByteArray?,
        bleReturn: ByteArray?,
        encrypt: ByteArray?,
        isEquals: Boolean
    ) {
    }

    protected fun onGetDID(did: Int) {}
    protected fun onGetCMD(cmd: String?) {}
    protected abstract fun onGetDecimalInfo(decimalInfo: DecimalInfo?)
    protected abstract fun onGetAlgorithmInfo(algorithmInfo: AlgorithmInfo?)

    init {
        onInitialize()
        bindService(null)
        val mCommonBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val action = intent.action
                val did: Int
                if ("android.bluetooth.adapter.action.STATE_CHANGED" == action) {
                    did = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", -1)
                    bluetoothStateChanged(did)
                } else {
                    val result: String?
                    if ("aicare.net.cn.fatscale.action.CONNECT_STATE_CHANGED" == action) {
                        did = intent.getIntExtra("aicare.net.cn.fatscale.extra.CONNECT_STATE", -1)
                        result =
                            intent.getStringExtra("aicare.net.cn.fatscale.extra.DEVICE_ADDRESS")
                        onStateChanged(did)
                    } else {
                        val cmd: String?
                        if ("aicare.net.cn.fatscale.action.CONNECT_ERROR" == action) {
                            cmd = intent.getStringExtra("aicare.net.cn.fatscale.extra.ERROR_MSG")
                            val errCode =
                                intent.getIntExtra("aicare.net.cn.fatscale.extra.ERROR_CODE", -1)
                            onError(cmd, errCode)
                        } else if ("aicare.net.cn.fatscale.action.WEIGHT_DATA" == action) {
                            val weightData =
                                intent.getSerializableExtra("aicare.net.cn.fatscale.extra.WEIGHT_DATA") as WeightData?
                            onGetWeightData(weightData)
                        } else if ("aicare.net.cn.fatscale.action.SETTING_STATUS_CHANGED" == action) {
                            did = intent.getIntExtra(
                                "aicare.net.cn.fatscale.extra.SETTING_STATUS",
                                -1
                            )
                            onGetSettingStatus(did)
                        } else if ("aicare.net.cn.fatscale.action.RESULT_CHANGED" == action) {
                            did =
                                intent.getIntExtra("aicare.net.cn.fatscale.extra.RESULT_INDEX", -1)
                            result = intent.getStringExtra("aicare.net.cn.fatscale.extra.RESULT")
                            onGetResult(did, result)
                        } else if ("aicare.net.cn.fatscale.action.FAT_DATA" == action) {
                            val isHistory = intent.getBooleanExtra(
                                "aicare.net.cn.fatscale.extra.IS_HISTORY",
                                false
                            )
                            val bodyFatData =
                                intent.getSerializableExtra("aicare.net.cn.fatscale.extra.FAT_DATA") as BodyFatData?
                            onGetFatData(isHistory, bodyFatData)
                        } else if ("aicare.net.cn.fatscale.action.AUTH_DATA" == action) {
                            val sources =
                                intent.getByteArrayExtra("aicare.net.cn.fatscale.extra.SOURCE_DATA")
                            val bleReturn =
                                intent.getByteArrayExtra("aicare.net.cn.fatscale.extra.BLE_DATA")
                            val encrypt =
                                intent.getByteArrayExtra("aicare.net.cn.fatscale.extra.ENCRYPT_DATA")
                            val isEquals = intent.getBooleanExtra(
                                "aicare.net.cn.fatscale.extra.IS_EQUALS",
                                false
                            )
                            onGetAuthData(sources, bleReturn, encrypt, isEquals)
                        } else if ("aicare.net.cn.fatscale.action.DID" == action) {
                            did = intent.getIntExtra("aicare.net.cn.fatscale.extra.DID", -1)
                            onGetDID(did)
                        } else if ("aicare.net.cn.fatscale.action.DECIMAL_INFO" == action) {
                            val decimalInfo =
                                intent.getSerializableExtra("aicare.net.cn.fatscale.extra.DECIMAL_INFO") as DecimalInfo?
                            onGetDecimalInfo(decimalInfo)
                        } else if ("aicare.net.cn.fatscale.action.CMD" == action) {
                            cmd = intent.getStringExtra("aicare.net.cn.fatscale.extra.CMD")
                            onGetCMD(cmd)
                        } else if ("aicare.net.cn.fatscale.action.ALGORITHM_INFO" == action) {
                            val algorithmInfo =
                                intent.getSerializableExtra("aicare.net.cn.fatscale.extra.ALGORITHM_INFO") as AlgorithmInfo?
                            onGetAlgorithmInfo(algorithmInfo)
                        }
                    }
                }
            }
        }
        context.registerReceiver(mCommonBroadcastReceiver, makeIntentFilter())
    }

    protected fun bindService(address: String?) {
        val service = Intent(context, WBYService::class.java)
        if (!TextUtils.isEmpty(address)) {
            service.putExtra("aicare.net.cn.fatscale.extra.DEVICE_ADDRESS", address)
            context.startService(service)
        }
        context.bindService(service, mServiceConnection, 0)
    }

    protected fun onInitialize() {
        val bluetoothManager =
            (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager)
        adapter = bluetoothManager.adapter
        bluetoothLeScanner = adapter?.bluetoothLeScanner
    }

    protected abstract fun onServiceBinded(wbyBinder: E)
    protected abstract fun onServiceUnbinded()
    fun startConnect(address: String?) {
        stopScan()
        bindService(address)
    }

    val isDeviceConnected: Boolean
        get() = mService != null && mService!!.isConnected
    val isBLEEnabled: Boolean
        get() {
            val bluetoothManager =
                (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager)
            val adapter = bluetoothManager.adapter
            return adapter != null && adapter.isEnabled
        }

    @SuppressLint("MissingPermission")
    fun startScan() {
        if (isBLEEnabled) {
            if (!mIsScanning) {
                bluetoothLeScanner!!.startScan(leScanCallback)
                mIsScanning = true
                handler.postDelayed(stopScanRunnable, SCAN_DURATION.toLong())
            }
        } else {
            onError("Bluetooth Error", BLUETOOTH_ERROR)
        }
    }

    @SuppressLint("MissingPermission")
    fun stopScan() {
        handler.removeCallbacks(startScanRunnable)
        handler.removeCallbacks(stopScanRunnable)
        if (mIsScanning) {
            if (adapter != null) {
                if (adapter!!.isEnabled) {
                    bluetoothLeScanner!!.stopScan(leScanCallback)
                } else {
                    onError("Bluetooth Error", BLUETOOTH_ERROR)
                }
            }
            mIsScanning = false
        }
    }

    protected abstract fun getAicareDevice(broadData: BroadData?)

    companion object {
        const val DEVICE_NOT_FOUND = 99
        const val BLUETOOTH_ERROR = 97
        private const val SCAN_DURATION = 10000
        private fun makeIntentFilter(): IntentFilter {
            val intentFilter = IntentFilter()
            intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED")
            intentFilter.addAction("aicare.net.cn.fatscale.action.CONNECT_STATE_CHANGED")
            intentFilter.addAction("aicare.net.cn.fatscale.action.CONNECT_ERROR")
            intentFilter.addAction("aicare.net.cn.fatscale.action.WEIGHT_DATA")
            intentFilter.addAction("aicare.net.cn.fatscale.action.SETTING_STATUS_CHANGED")
            intentFilter.addAction("aicare.net.cn.fatscale.action.RESULT_CHANGED")
            intentFilter.addAction("aicare.net.cn.fatscale.action.FAT_DATA")
            intentFilter.addAction("aicare.net.cn.fatscale.action.AUTH_DATA")
            intentFilter.addAction("aicare.net.cn.fatscale.action.DID")
            intentFilter.addAction("aicare.net.cn.fatscale.action.DECIMAL_INFO")
            intentFilter.addAction("aicare.net.cn.fatscale.action.CMD")
            intentFilter.addAction("aicare.net.cn.fatscale.action.ALGORITHM_INFO")
            return intentFilter
        }
    }
}