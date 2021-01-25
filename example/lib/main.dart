import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_neteasequickpass/flutter_neteasequickpass.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _tokenMsg = '一键登录初始化中....';
  String _btnText = '一键登录初始化中..';
  bool _inited = false;

  Future<void> prefetchToken() async {
    var try_times = 0;
    do {
      try {
        Map tokenResult = await FlutterNeteasequickpass.prefetchToken();
        print("333=======");
        print(tokenResult);
        if (!mounted) return;
        if (tokenResult["code"].toString() == "ok") {
          print(" ok true ");
          setState(() {
            _tokenMsg = _tokenMsg + "\n"+ tokenResult['msg'];
            _btnText = "一键登录";
            _inited = true;
          });
          return;
        } else {
          print(" ok false ");
          setState(() {
            _tokenMsg = _tokenMsg + "\n"+ tokenResult['msg'];
            _btnText = "一键登录初始化中..重试"+try_times.toString()+"次";
          });
          if (tokenResult["code"].toString() != "net_error") {
            return;
          }
        }

      } on PlatformException catch(e){
        setState(() {
          _tokenMsg = _tokenMsg + "\n"+ e.message;
          _btnText = "一键登录初始化中..重试"+try_times.toString()+"次";
        });
        if (e.code != "net_error") {
          return;
        }
      }
      try_times += 1;
      if(try_times<3) {
        print("重试一次prefetchToken");
      }
    } while(try_times<3);
  }




  Future<void> runLogin() async {
    var try_times = 0;
    do {
      try {
        Map tokenResult = await FlutterNeteasequickpass.launchLogin();
        if (tokenResult["code"] == "ok") {
          setState(() {
            _tokenMsg = _tokenMsg + "\n" + tokenResult['msg'];
          });
          await FlutterNeteasequickpass.quitLogin();
          print("ydToken is:"+tokenResult['data']['ydToken']);
          print("accessCode is:"+tokenResult['data']['accessCode']);
          return;
        } else {
          setState(() {
            _tokenMsg = _tokenMsg + "\n" + tokenResult['msg'];
          });
          if (tokenResult["code"] != "net_error" && tokenResult["code"] != "cancel") {
            return;
          }
        }
      } catch(e) {
        setState(() {
          _tokenMsg = _tokenMsg + "\n" + e.message;
        });
        if (e.code != "net_error" && e.code != "cancel") {
          return;
        }
      }
      try_times+=1;
      if(try_times<3) {
        print("重试一次runLogin");
      }

    } while(try_times<3);
  }

  @override
  void initState() {
    print("===================================");
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String tokenMsg;
    String btnText;
    print ("222");
    try {
      String businessId =  "fa0cff55a1174fc2a63b247c8ee39da3";
      await FlutterNeteasequickpass.initialize(businessId);
      if (!mounted) return;
    } on PlatformException catch(e){
      print('Failed to call initialize.');
      setState(() {
        _tokenMsg = _tokenMsg+ e.message;
      });
    }
    print ("333");
    await prefetchToken();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: <Widget>[
                Text('Running on: $_tokenMsg\n'),
                FlatButton(
                    materialTapTargetSize: MaterialTapTargetSize.shrinkWrap,
                     color: Colors.white,
                    shape: RoundedRectangleBorder(
                      // side: BorderSide(
                      //   color: Colors.black12,
                      // ),
                      borderRadius: BorderRadius.circular(12),
                    ),
                    onPressed: () {
                      if (_inited) {
                          runLogin();
                      } else {

                      }

                    },
                    child: Text(_btnText),
                )
              ]
          )
        ),
      ),
    );
  }
}
