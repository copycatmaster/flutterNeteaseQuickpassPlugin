package com.loongwalk.flutter_neteasequickpass;


import java.util.HashMap;
import androidx.annotation.NonNull;
//import android.app.Application;
import android.content.Context;
import android.util.Log;
import io.flutter.embedding.android.FlutterActivity;

import com.netease.nis.quicklogin.QuickLogin;
import com.netease.nis.quicklogin.helper.UnifyUiConfig;
import com.netease.nis.quicklogin.listener.QuickLoginPreMobileListener;
import com.netease.nis.quicklogin.listener.QuickLoginTokenListener;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** FlutterNeteasequickpassPlugin */
public class FlutterNeteasequickpassPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private QuickLogin login;
  private static FlutterActivity flutterActivity;
  private static Context appContext;
  private static boolean tokenResultReturn = false;

  public static void setTokenResultReturn(boolean b) {
      tokenResultReturn = b;
  }
  public static boolean getTokenResultReturn() {
      return tokenResultReturn;
  }

  public static void registerWith(FlutterActivity registry,Context app_context) {
      flutterActivity = registry;
      appContext = app_context;
//    PluginRegistry.Registrar registrar = registry.registrarFor(CHANNEL);
//    MethodChannel methodChannel = new MethodChannel(registrar.messenger(), CHANNEL);
//    MyFlutterPlugin myFlutterPlugin = new MyFlutterPlugin(registrar.activity());
//    methodChannel.setMethodCallHandler(myFlutterPlugin);
  }


  public static HashMap<String,Object> packResult(final String code,final  String msg,final  HashMap<String,Object> data) {
    return new HashMap<String,Object>(){{
      put("code",code);
      put("msg",msg);
      put("data",data);
    }};
  }


  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "flutter_neteasequickpass");
    channel.setMethodCallHandler(this);


  }




  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result2) {
    final Result result = result2;
    final FlutterNeteasequickpassPlugin that = this;
    //Log.i("flutter","call name is "+call.method);
    if (call.method.equals("prefetchToken")) {

      if(login==null) {
          result.error("uninitialize","未初始化",null);
          return;
      }
      //Log.i("flutter","call prefetchToken");
      login.prefetchMobileNumber(new QuickLoginPreMobileListener() {
        @Override
        public void onGetMobileNumberSuccess(final String YDToken, final String mobileNumber) {
          //Log.i("flutter", String.format("yd token is:%s mobileNumber is:%s", YDToken, mobileNumber));
          result.success(packResult("ok","ok",new HashMap<String,Object>(){{
            put("ydToken",YDToken);
            put("mobileNumber",mobileNumber);
          }}));
        }
        @Override
        public void onGetMobileNumberError(final String YDToken, final String msg) {
            //Log.i("flutter","  call prefetchToken failed !!"+msg);
            if(msg.contains("网络") || msg.contains("java.net") ) {
                result.error("net_error",msg,new HashMap<String,Object>(){{
                  put("ydToken",YDToken);
                }});
            } else {
                result.error("error",msg,new HashMap<String,Object>(){{
                  put("ydToken",YDToken);
                }});
            }

        }
      });
    } else if (call.method.equals("launchLogin")) {
        if(login==null) {
          result.error("uninitialize","未初始化",null);
          return;
        }
        that.setTokenResultReturn(false);
        login.onePass(new QuickLoginTokenListener() {
          @Override
          public void onGetTokenSuccess(final String YDToken, final String accessCode) {
            //Log.i("flutter", String.format("yd token is:%s accessCode is:%s", YDToken, accessCode));
            //tokenValidate(YDToken, accessCode, true);
            if(that.getTokenResultReturn()) {
              return;
            }
            that.setTokenResultReturn(true);
            result.success(packResult("ok","ok",new HashMap<String,Object>(){{
              put("ydToken",YDToken);
              put("accessCode",accessCode);
            }}));
          }

          @Override
          public void onGetTokenError(final String YDToken, String msg) {
            //Log.d("flutter", "获取运营商授权码失败:" + msg);
            if(that.getTokenResultReturn()) {
              return;
            }
            that.setTokenResultReturn(true);
            result.error("error",msg,new HashMap<String,Object>(){{
              put("ydToken",YDToken);
            }});
          }
          @Override
          public void onCancelGetToken() {
            //Log.i("flutter", "用户取消登录");
            if(that.getTokenResultReturn()) {
              return;
            }
            that.setTokenResultReturn(true);
            result.error("cancel","用户取消登录",new HashMap<String,Object>(){{
            }});
          }
        });
        //Log.i("flutter","call launchLogin");
    } else if (call.method.equals("quitLogin")) {
        if(login==null) {
          result.error("uninitialize","未初始化",null);
          return;
        }
        //Log.i("flutter","call quitLogin");
        login.quitActivity();
        result.success(packResult("ok","ok",new HashMap<String,Object>(){{
        }}));
    } else if (call.method.equals("initialize")) {

      final String businessId = call.argument("businessId");
      //Log.i("flutter","call initialize businessId is "+businessId);
      login = QuickLogin.getInstance(appContext, businessId);// BUSINESS_ID为从易盾官网申请的业务id
      result.success(packResult("ok","ok",new HashMap<String,Object>(){{
          put("businessId",businessId);
      }}));
    } else {
      result.notImplemented();
    }

  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
}
