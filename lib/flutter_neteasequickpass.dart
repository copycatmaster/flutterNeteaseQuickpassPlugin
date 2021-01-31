
import 'dart:async';

import 'package:flutter/services.dart';

class FlutterNeteasequickpass {
  static const MethodChannel _channel =
      const MethodChannel('flutter_neteasequickpass');

  // static Future<String> platformVersion() async {
  //   final String version = await _channel.invokeMethod('getPlatformVersion');
  //   return version;
  // }

  static Future<Map> prefetchToken() async {
    try {
      return await _channel.invokeMethod('prefetchToken');
    } on PlatformException catch (e) {
      //print("Failed to call prefetchToken: ${e.message} ");
      return {"code":e.code,"msg":e.message,"data":{"exception":e,"detail":e.details}};
    }
  }
  static Future<Map> getISPName() async {
    try {
        return await _channel.invokeMethod('getOperatorType');
    } on PlatformException catch (e) {
        //print("Failed to call prefetchToken: ${e.message} ");
        return {"code":e.code,"msg":e.message,"data":{"exception":e,"detail":e.details}};
    }
  }
  static Future<Map> initialize(String businessId,[int timeout=10,String jsonConfig="{}"]) async {
    try {
      if (businessId == "" && businessId == null) {
        return {"code":"params","msg":"businessId不能为空"};
      }
      return await _channel.invokeMethod('initialize',{"businessId":businessId,"jsonConfig":jsonConfig,"timeout":timeout});
    } on PlatformException catch (e) {
      //print("Failed to call initialize: ${e.message} ");
      return {"code":e.code,"msg":e.message,"data":{"exception":e,"detail":e.details}};
    }
  }

  static Future<Map> launchLogin() async {
    try {
      return await _channel.invokeMethod('launchLogin');
    } on PlatformException catch (e) {
      //print("Failed to call launchLogin: ${e.message} ");
      return {"code":e.code,"msg":e.message,"data":{"exception":e,"detail":e.details}};
    }
  }

  static Future<Map> quitLogin() async {
    try {
      return await _channel.invokeMethod('quitLogin');
    } on PlatformException catch (e) {
      return {"code":e.code,"msg":e.message,"data":{"exception":e,"detail":e.details}};
    }
  }

}
