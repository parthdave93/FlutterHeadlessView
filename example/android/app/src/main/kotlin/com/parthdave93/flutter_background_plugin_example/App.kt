package com.parthdave93.flutter_background_plugin_example

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import io.flutter.app.FlutterApplication
import io.flutter.plugin.common.PluginRegistry
import io.flutter.plugins.GeneratedPluginRegistrant
import java.util.concurrent.TimeUnit

class App : FlutterApplication(), PluginRegistry.PluginRegistrantCallback {
    override fun registerWith(registry: PluginRegistry?) {
        GeneratedPluginRegistrant.registerWith(registry)
    }

    companion object{
        const val SHARED_PREF = "shared_pref"
        lateinit var appInstace: App
        lateinit var pluginregistrantCallbacks: PluginRegistry.PluginRegistrantCallback
    }

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    override fun onCreate() {
        super.onCreate()
        appInstace = this
        pluginregistrantCallbacks = this
        val uploadWorkRequest = OneTimeWorkRequestBuilder<UploadWorker>()
                .setInitialDelay(2, TimeUnit.MINUTES)
                .build()
        WorkManager.getInstance(this@App).enqueue(uploadWorkRequest)
    }
}