package com.cmed.plugin.lib.flutter.cmed_health_flutter_devices_lib.utils

import android.content.Context
import java.io.IOException

object LocalJsonParser {
    fun getJsonString(fileName: String, context: Context): String? {
        var json: String? = null
        try {
//            val inputStream = CMEDCoreApp_.getInstance().assets.open(fileName)
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return json
    }

}
