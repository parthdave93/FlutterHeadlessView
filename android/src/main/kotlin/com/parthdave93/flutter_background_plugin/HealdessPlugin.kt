package com.parthdave93.flutter_background_plugin

import android.app.IntentService
import android.content.Context
import android.util.Log
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry

class HealdessPlugin(val context: Context) : MethodChannel.MethodCallHandler {

    val SHARED_PREF = "com.example.background_process"
    
    companion object {
        @JvmStatic
        fun registerWith(registrar: PluginRegistry.Registrar) {
            val channel = MethodChannel(registrar.messenger(), "platform_specific")
            val plugin = HealdessPlugin(registrar.context())
            channel.setMethodCallHandler(plugin)
        }
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        Log.e("method call", "method called from flutter to native: ${call.method}")
        if (call.method == "initBackgroundProcess") {
            val callbackHandle = call.arguments as Long
            executeBackgroundIsolate(context, callbackHandle)
        }else{
            result.success(true)
        }
    }

    private fun executeBackgroundIsolate(context: Context, callbackHandle: Long) {
        val preferences = context.getSharedPreferences(SHARED_PREF, IntentService.MODE_PRIVATE)
        preferences.edit().putLong("callback", callbackHandle).apply()
    }
}