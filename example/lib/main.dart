import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_background_plugin/flutter_background_plugin.dart';

void main() {
  FlutterBackgroundPlugin.initIsolates();
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
    model = Model()..text = "text widget";
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await FlutterBackgroundPlugin.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  Model model;

  @override
  Widget build(BuildContext context) {
    print("Build method of main class called\n");

    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: CustomLevel2(model),
      ),
    );
  }

}

class CustomLevel2 extends StatefulWidget {
  Model model;

  CustomLevel2(this.model);

  @override
  State<StatefulWidget> createState() {
    return CustomState();
  }

}

class CustomState extends State<CustomLevel2> {

  @override
  void initState() {
    startTimer();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    print("Build method of CustomState class called\n");
    return CupertinoButton(
      child: Text(widget.model.text),
    );
//    return Text(widget.model.text);
  }


  int count = 0;
  bool updateOrModifie = false;

  void startTimer() async {
    count++;
    updateOrModifie = !updateOrModifie;
    if (updateOrModifie)
      widget.model = Model()..text = "text widget $count";
    else
      widget.model.text = "text widget $count";
    setState(() {});
    await Future.delayed(Duration(seconds: 10));
    startTimer();
  }
}

class Model {
  String text;
}
