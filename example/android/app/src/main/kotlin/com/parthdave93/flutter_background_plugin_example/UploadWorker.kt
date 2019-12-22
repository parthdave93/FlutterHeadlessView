package com.parthdave93.flutter_background_plugin_example

import android.app.IntentService
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry
import io.flutter.view.FlutterCallbackInformation
import io.flutter.view.FlutterMain
import io.flutter.view.FlutterNativeView
import io.flutter.view.FlutterRunArguments

class UploadWorker(appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams), MethodChannel.MethodCallHandler {
    val SHARED_PREF = "com.example.background_process"
    val obj = Object()

    override fun doWork(): Result {
        // Do the work here--in this case, upload the images.

        Log.e("method call", "UploadWorker->doWork")
        synchronized(obj){
            Handler(Looper.getMainLooper()).post {
                startBackgroundIsolate()
            }
        }
        Log.e("method call", "UploadWorker->return success")
        // Indicate whether the task finished successfully with the Result
        return Result.success()
    }

    private fun startBackgroundIsolate() {
        Log.e("method call", "UploadWorker->startBackgroundIsolate")

        FlutterMain.ensureInitializationComplete(applicationContext, null)

        val preferences = applicationContext.getSharedPreferences(SHARED_PREF, IntentService.MODE_PRIVATE)
        val callbackHandle = preferences.getLong("callback", 0)
        if(callbackHandle == 0L)
            return
        val callbackObj = FlutterCallbackInformation.lookupCallbackInformation(callbackHandle)
        val backgroundProcessView = FlutterNativeView(applicationContext, true)
        val path = FlutterMain.findAppBundlePath()
        val args = FlutterRunArguments()
        args.bundlePath = path
        args.entrypoint = callbackObj.callbackName
        args.libraryPath = callbackObj.callbackLibraryPath

        backgroundProcessView.runFromBundle(args)
        val backgroundChannel = MethodChannel(backgroundProcessView, "backgroundCallChannel")
        backgroundChannel?.setMethodCallHandler(this)
        App.pluginregistrantCallbacks.registerWith(backgroundProcessView.pluginRegistry)
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        Log.e("method call", "method called from flutter to native: ${call.method}")
        obj.notify()
        result.success(true)
    }
}