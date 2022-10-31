import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:permission_handler/permission_handler.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({Key? key}) : super(key: key);

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  dynamic _batteryLevel;
  static const platform = MethodChannel('demo');

  @override
  void initState() {
    super.initState();
    checkPermission();
  }

  void checkPermission() async {
    if (await Permission.storage.status == PermissionStatus.denied) {
      Permission.storage.request();
    } else {
      getPhotos();
    }
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        appBar: AppBar(
          title: Text("Method channel Demo"),
        ),
        body: _batteryLevel.length < 1
            ? Center(
                child: Text(
                  "No Photo!",
                  style: TextStyle(fontSize: 20),
                ),
              )
            : GridView.builder(
                gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                    crossAxisCount: 3, mainAxisSpacing: 5, crossAxisSpacing: 5),
                itemCount: _batteryLevel.length,
                itemBuilder: (context, index) {
                  return Container(
                    height: 150,
                    color: Colors.black,
                    child: Image.file(
                      File("${_batteryLevel[index]}"),
                      fit: BoxFit.cover,
                    ),
                  );
                },
              ),
      ),
    );
  }

  Future<void> _getBatteryLevel() async {
    String batteryLevel;
    try {
      final int result = await platform.invokeMethod('getBatteryLevel');
      batteryLevel = 'Battery level at $result % .';
    } on PlatformException catch (e) {
      batteryLevel = "Failed to get battery level: '${e.message}'.";
    }

    setState(() {
      _batteryLevel = batteryLevel;
    });
  }

  Future<void> getPhotos() async {
    dynamic batteryLevel;
    try {
      dynamic result = await platform.invokeMethod('GetPhotos');
      batteryLevel = result;
    } on PlatformException catch (e) {
      batteryLevel = "No data";
    }
    setState(() {
      _batteryLevel = batteryLevel;
    });
  }
}
