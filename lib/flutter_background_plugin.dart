import 'dart:async';
import 'dart:ui';


import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';

void backgroundIsolateMain(){
    print("Flutter side \n\n\n\n backgroundIsolateMain");
}

class FlutterBackgroundPlugin {
  static const MethodChannel _channel =
      const MethodChannel('flutter_background_plugin');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static initIsolates() async{
    const methodChannel = MethodChannel("platform_specific");
    var callback = PluginUtilities.getCallbackHandle(backgroundIsolateMain);
    WidgetsFlutterBinding.ensureInitialized();
    await methodChannel.invokeMethod("initBackgroundProcess",callback.toRawHandle());
  }
}

