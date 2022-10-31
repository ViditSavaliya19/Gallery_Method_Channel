
import 'package:flutter/material.dart';
import 'dart:async';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutternativedemo/screen/homeScreen.dart';
import 'package:flutternativedemo/screen/spleshScreen.dart';

void main() {
  runApp(
    MaterialApp(
      debugShowCheckedModeBanner: false,
      routes: {
        '/':(context)=>SpleshScreen(),
        '/home':(context)=>HomeScreen(),
      },
    ),
  );
}
