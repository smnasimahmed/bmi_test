package com.cmedhealth.flutter.bmi.cmed_bmi_devices_lib

import android.util.Log
import androidx.annotation.NonNull
import com.cmed.plugin.lib.flutter.cmed_health_flutter_devices_lib.utils.CMEDUser
import com.cmedhealth.flutter.bmi.cmed_bmi_devices_lib.aifit.CMEDAiFitWeightHandler
import com.cmedhealth.flutter.bmi.cmed_bmi_devices_lib.aifit.FrecomDeviceCallback
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** CmedBmiDevicesLibPlugin */
class CmedBmiDevicesLibPlugin : FlutterPlugin, MethodCallHandler, EventChannel.StreamHandler,
    ActivityAware,
    FrecomDeviceCallback {

    companion object {
        val TAG = CmedBmiDevicesLibPlugin::class.simpleName
    }
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel
    private lateinit var eventChannel: EventChannel
    private lateinit var cmedAiFitWeightHandler: CMEDAiFitWeightHandler
    private var eventSink: EventChannel.EventSink? = null

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel =
            MethodChannel(flutterPluginBinding.binaryMessenger, "cmed_bmi_devices_lib/method")
        channel.setMethodCallHandler(this)
        eventChannel =
            EventChannel(flutterPluginBinding.binaryMessenger, "cmed_bmi_devices_lib/event")
        eventChannel.setStreamHandler(this)
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        when (call.method) {
            "getPlatformVersion" -> {
                result.success("Android ${android.os.Build.VERSION.RELEASE}")
            }

            "setUser" -> {
                val userJson = call.argument<HashMap<String, String>>("user").toString()
                cmedAiFitWeightHandler.setUser(
                    Gson().fromJson(
                        userJson,
                        object : TypeToken<CMEDUser>() {}.type
                    )
                )
                result.success(true)
            }

            "connect" -> {
                Log.v(TAG, "connect called")
                cmedAiFitWeightHandler.connect()
                result.success("connect called")
            }

            "disconnect" -> {
                Log.v(TAG, "disconnect called")
                cmedAiFitWeightHandler.disconnect()
                result.success("disconnect called")
            }

            else -> {
                result.notImplemented()
            }
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
        eventSink = events
    }

    override fun onCancel(arguments: Any?) {
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        cmedAiFitWeightHandler = CMEDAiFitWeightHandler(binding.activity, this)
    }

    override fun onDetachedFromActivityForConfigChanges() {
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    }

    override fun onDetachedFromActivity() {
    }

    override fun onGetResponse(deviceResponse: String) {
        eventSink?.success(deviceResponse)
    }
}
