package com.cmedhealth.flutter.bmi.cmed_bmi_devices_lib

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionHandler {
    companion object {
        val BLUETOOTH_PERMISSION_REQUEST = 91
        val AUDIO_PERMISSION_REQUEST = 99

        fun getBluetoothPermission(context: Context): Boolean {

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {

                return if (ContextCompat.checkSelfPermission(
                        context as Activity, Manifest.permission.BLUETOOTH_SCAN
                    ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                        context, Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                        context, Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                        context, Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        context, arrayOf(
                            Manifest.permission.BLUETOOTH_SCAN,
                            Manifest.permission.BLUETOOTH_CONNECT,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                        ), BLUETOOTH_PERMISSION_REQUEST
                    )
                    false
                } else {
                    true
                }
            } else {
                return if (ContextCompat.checkSelfPermission(
                        context as Activity, Manifest.permission.BLUETOOTH
                    ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                        context, Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                        context, Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        context, arrayOf(
                            Manifest.permission.BLUETOOTH,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                        ), BLUETOOTH_PERMISSION_REQUEST
                    )
                    false
                } else {
                    true
                }
            }
        }

        fun getAudioPermission(context: Context): Boolean {
            return if (ContextCompat.checkSelfPermission(context as Activity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context as Activity, Manifest.permission.MODIFY_AUDIO_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    context,
                    arrayOf(
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.MODIFY_AUDIO_SETTINGS
                    ), AUDIO_PERMISSION_REQUEST
                )
                false
            } else {
                true
            }
        }
    }
}