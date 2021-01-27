package com.loongwalk.flutter_neteasequickpass;


import java.util.HashMap;
import java.lang.System;
import androidx.annotation.NonNull;
//import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.graphics.Color;
import android.util.DisplayMetrics;
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
  private String lastYdToken = null;
  private long lastYdTokenTime = 0;

  private static Context appContext;
  private static boolean tokenResultReturn = false;

  public static void setTokenResultReturn(boolean b) {
      tokenResultReturn = b;
  }
  public static boolean getTokenResultReturn() {
      return tokenResultReturn;
  }

  public static HashMap<String,Object> packResult(final String code,final  String msg,final  HashMap<String,Object> data) {
    return new HashMap<String,Object>(){{
      put("code",code);
      put("msg",msg);
      put("data",data);
    }};
  }
  public static UnifyUiConfig getUiConfig() {
//    ImageView closeBtn = new ImageView(context);
//    closeBtn.setImageResource(R.drawable.close);
//    closeBtn.setScaleType(ImageView.ScaleType.FIT_XY);
//    closeBtn.setBackgroundColor(Color.TRANSPARENT);
//    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(50, 50);
//    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.CENTER_VERTICAL);
//    layoutParams.topMargin = 30;
//    layoutParams.rightMargin = 50;
//    closeBtn.setLayoutParams(layoutParams);

//    LayoutInflater inflater = LayoutInflater.from(context);
//    RelativeLayout otherLoginRel = (RelativeLayout) inflater.inflate(R.layout.custom_other_login, null);
//    RelativeLayout.LayoutParams layoutParamsOther = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//    layoutParamsOther.setMargins(0, 0, 0, Utils.dip2px(context, 130));
//    layoutParamsOther.addRule(RelativeLayout.CENTER_HORIZONTAL);
//    layoutParamsOther.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    //otherLoginRel.setLayoutParams(layoutParamsOther);
//    ImageView wx = otherLoginRel.findViewById(R.id.weixin);
//    ImageView qq = otherLoginRel.findViewById(R.id.qq);
//    ImageView wb = otherLoginRel.findViewById(R.id.weibo);
//    wx.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        Toast.makeText(context, "微信登录", Toast.LENGTH_SHORT).show();
//      }
//    });
//    qq.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        Toast.makeText(context, "qq登录", Toast.LENGTH_SHORT).show();
//
//      }
//    });
//    wb.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        Toast.makeText(context, "微博登录", Toast.LENGTH_SHORT).show();
//      }
//    });
    int X_OFFSET = 0;
    int BOTTOM_OFFSET = 0;
    UnifyUiConfig uiConfig = new UnifyUiConfig.Builder()
            // 状态栏
//            .setStatusBarColor(Color.RED)
//            .setStatusBarDarkColor(true)
            // 设置导航栏
            .setHideNavigation(true)
//            .setNavigationTitle("一键登录/注册")
//            //.setNavigationTitleColor(Color.RED)
//            .setNavigationBackgroundColor(Color.BLUE)
//            .setNavigationIcon("yd_checkbox_checked2")
//            .setNavigationBackIconWidth(25)
//            .setNavigationBackIconHeight(25)
            //.setHideNavigation(false)
            // 设置logo
            //.setLogoIconName("ico_logo")
//            .setLogoWidth(70)
//            .setLogoHeight(70)
//            .setLogoXOffset(X_OFFSET)
//            .setLogoTopYOffset(50)
//            .setHideLogo(false)
            //手机掩码
            .setMaskNumberColor(Color.RED)
            .setMaskNumberSize(15)
            .setMaskNumberXOffset(X_OFFSET)
            .setMaskNumberTopYOffset(120)
            .setMaskNumberBottomYOffset(BOTTOM_OFFSET)
            // 认证品牌
            .setSloganSize(15)
            .setSloganColor(Color.RED)
            .setSloganXOffset(X_OFFSET)
            .setSloganTopYOffset(200)
            .setSloganBottomYOffset(BOTTOM_OFFSET)
            // 登录按钮
            .setLoginBtnText("一键登录")
            .setLoginBtnTextColor(Color.WHITE)
            //.setLoginBtnBackgroundRes("btn_shape_login_onepass")
            .setLoginBtnWidth(100)
            .setLoginBtnHeight(45)
            .setLoginBtnTextSize(15)
            //.setLoginBtnXOffset(X_OFFSET)
            //.setLoginBtnTopYOffset(250)
            .setLoginBtnBottomYOffset(BOTTOM_OFFSET)
            // 隐私栏
            .setPrivacyTextStart("登录即同意")
            .setProtocolText("服务条款一")
            .setProtocolLink("https://www.baidu.com")
            .setProtocol2Text("服务条款二")
            .setProtocol2Link("https://www.baidu.com")
            //.setPrivacyTextEnd("且授权易盾一键登录SDK使用本机号码")
            .setPrivacyTextColor(Color.RED)
            .setPrivacyProtocolColor(Color.GREEN)
//                .setHidePrivacyCheckBox(false)
            .setPrivacyXOffset(X_OFFSET)
            .setPrivacyState(true)
            .setPrivacySize(12)
//                .setPrivacyTopYOffset(510)
            .setPrivacyBottomYOffset(20)
            .setPrivacyTextGravityCenter(true)
            .setCheckedImageName("yd_checkbox_checked2")
            .setUnCheckedImageName("yd_checkbox_unchecked2")
            // 协议详情页导航栏
            .setProtocolPageNavTitle("一键登录服务条款")
            //.setProtocolPageNavBackIcon("yd_checkbox_checked")
            .setProtocolPageNavColor(Color.BLUE)

//                .setBackgroundImage("bg1")
            // 自定义控件
//            .addCustomView(otherLoginRel, "relative", UnifyUiConfig.POSITION_IN_BODY, null)
//            .addCustomView(closeBtn, "close_btn", UnifyUiConfig.POSITION_IN_TITLE_BAR, new LoginUiHelper.CustomViewListener() {
//              @Override
//              public void onClick(Context context, View view) {
//                Toast.makeText(context, "点击了右上角X按钮", Toast.LENGTH_SHORT).show();
//              }
//            })
            .build(appContext);
    return uiConfig;
  }


  public static UnifyUiConfig getDialogUiConfig() {
//    ImageView closeBtn = new ImageView(context);
//    closeBtn.setImageResource(R.drawable.close);
//    closeBtn.setScaleType(ImageView.ScaleType.FIT_XY);
//    closeBtn.setBackgroundColor(Color.TRANSPARENT);
//    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(60, 60);
//    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.CENTER_VERTICAL);
//    layoutParams.topMargin = 30;
//    layoutParams.rightMargin = 50;
//    closeBtn.setLayoutParams(layoutParams);

//    LayoutInflater inflater = LayoutInflater.from(context);
//    RelativeLayout otherLoginRel = (RelativeLayout) inflater.inflate(R.layout.custom_other_login, null);
//    RelativeLayout.LayoutParams layoutParamsOther = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//    layoutParamsOther.setMargins(0, 0, 0, Utils.dip2px(context, 100));
//    layoutParamsOther.addRule(RelativeLayout.CENTER_HORIZONTAL);
//    layoutParamsOther.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//    otherLoginRel.setLayoutParams(layoutParamsOther);
//    ImageView wx = otherLoginRel.findViewById(R.id.weixin);
//    ImageView qq = otherLoginRel.findViewById(R.id.qq);
//    ImageView wb = otherLoginRel.findViewById(R.id.weibo);
//    wx.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        Toast.makeText(context, "微信登录", Toast.LENGTH_SHORT).show();
//      }
//    });
//    qq.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        Toast.makeText(context, "qq登录", Toast.LENGTH_SHORT).show();
//
//      }
//    });
//    wb.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        Toast.makeText(context, "微博登录", Toast.LENGTH_SHORT).show();
//      }
//    });
    int X_OFFSET = 0;
    DisplayMetrics dm = new DisplayMetrics();
    int screenWidth = Utils.getScreenDpWidth(appContext);
    int screenHeight = Utils.getScreenDpHeight(appContext);
    int dialogWidth = (int) ( screenWidth * 0.85);
    int dialogHeight = (int) ( screenHeight * 0.5);
    UnifyUiConfig uiConfig = new UnifyUiConfig.Builder()
            // 设置导航栏
//            .setNavigationTitle("一键登录/注册")
//            .setNavigationTitleColor(Color.RED)
//            .setNavigationBackgroundColor(Color.BLUE)
//            .setNavigationIcon("yd_checkbox_checked")
            .setHideNavigation(true)
            // 设置logo
//            .setLogoIconName("ico_logo")
//            .setLogoWidth(50)
//            .setLogoHeight(50)
//            .setLogoXOffset(X_OFFSET)
//            .setLogoTopYOffset(15)
////                .setLogoBottomYOffset(300)
            .setHideLogo(true)
            //手机掩码
            .setMaskNumberColor(Color.BLACK)
            .setMaskNumberSize(15)
            .setMaskNumberTopYOffset(100)
            .setMaskNumberXOffset(X_OFFSET)
            // 认证品牌
//            .setSloganSize(15)
//            .setSloganColor(Color.BLACK)
//            .setSloganTopYOffset(70)
//            .setSloganXOffset(X_OFFSET)
            // 登录按钮
            .setLoginBtnText("一键登录")
            .setLoginBtnTextColor(Color.BLACK)
            .setLoginBtnBackgroundRes("btn_shape_login_onepass")
            .setLoginBtnWidth(100)
            .setLoginBtnHeight(40)
            .setLoginBtnTextSize(15)
            .setLoginBtnXOffset(X_OFFSET)
            .setLoginBtnTopYOffset(150)
            // 隐私栏
            //.setPrivacyTextStart("登录即同意")
            //.setProtocolText("服务条款一")
            //.setProtocolLink("https://www.baidu.com")
            //.setProtocol2Text("服务条款二")
            //.setProtocol2Link("https://www.baidu.com")
            .setProtocolPageNavTitle("")
            .setProtocolPageNavHeight(0)
            .setProtocolPageNavTitleSize(0)
            .setPrivacyTextEnd("且授权Bee.com使用本机号码")
            .setPrivacyMarginRight((int)(screenWidth*0.1))
            //.setPrivacyMarginLeft(30)
            .setPrivacyTextColor(Color.GRAY)
            .setPrivacyProtocolColor(Color.BLUE)
            .setHidePrivacyCheckBox(true)
            .setPrivacyXOffset((int)(screenWidth*0.1))
            .setPrivacyState(true)
            .setPrivacySize(12)
            .setPrivacyBottomYOffset((int)(screenHeight*0.05))
            .setPrivacyTextGravityCenter(true)
            //.setCheckedImageName("yd_checkbox_checked")
            //.setUnCheckedImageName("yd_checkbox_unchecked")
            // 协议详情页导航栏
            //.setProtocolPageNavTitle("易盾一键登录SDK服务条款")
            //.setProtocolPageNavBackIcon("yd_checkbox_checked")
            //.setProtocolPageNavColor(Color.BLUE)

//            .addCustomView(otherLoginRel, "relative", UnifyUiConfig.POSITION_IN_BODY, null)
//            .addCustomView(closeBtn, "close_btn", UnifyUiConfig.POSITION_IN_TITLE_BAR, new LoginUiHelper.CustomViewListener() {
//              @Override
//              public void onClick(Context context, View view) {
//                Toast.makeText(context.getApplicationContext(), "点击了右上角X按钮", Toast.LENGTH_SHORT).show();
//              }
//            })
            .setDialogMode(true, dialogWidth, dialogHeight, 0, 0, false)
            .build(appContext);
    return uiConfig;
  }

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "flutter_neteasequickpass");
    channel.setMethodCallHandler(this);
    appContext = flutterPluginBinding.getApplicationContext();

  }




  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result2) {
    final Result result = result2;
    final FlutterNeteasequickpassPlugin that = this;
    Log.i("flutter","call name is "+call.method);
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
          lastYdToken = YDToken;
          lastYdTokenTime = System.currentTimeMillis();

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
        if(lastYdToken == null || lastYdTokenTime - System.currentTimeMillis() > 2*60*1000 ) {
          result.error("needPrefetchToken","需要调用prefetchToken",null);
          return;
        }

        that.setTokenResultReturn(false);
        login.onePass(new QuickLoginTokenListener() {
          @Override
          public void onGetTokenSuccess(final String YDToken, final String accessCode) {
            Log.i("flutter", String.format("yd token is:%s accessCode is:%s", YDToken, accessCode));
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
            Log.i("flutter", "获取运营商授权码失败:" + msg);
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
            Log.i("flutter", "用户取消登录");
            if(that.getTokenResultReturn()) {
              return;
            }
            that.setTokenResultReturn(true);
            result.error("cancel","用户取消登录",new HashMap<String,Object>(){{
            }});
          }
        });
        Log.i("flutter","call launchLogin");
    } else if (call.method.equals("quitLogin")) {
        if(login==null) {
          result.error("uninitialize","未初始化",null);
          return;
        }
        Log.i("flutter","call quitLogin");
        login.quitActivity();
        result.success(packResult("ok","ok",new HashMap<String,Object>(){{
        }}));
    } else if (call.method.equals("initialize")) {
      final String businessId = call.argument("businessId");
      if(login!=null) {
        result.success(packResult("ok","ok",new HashMap<String,Object>(){{
          put("businessId",businessId);
        }}));
        return;
      }

      Log.i("flutter","call initialize businessId is "+businessId);
      login = QuickLogin.getInstance(appContext, businessId);// BUSINESS_ID为从易盾官网申请的业务id
      //login.setUnifyUiConfig(getUiConfig());
      login.setUnifyUiConfig(getDialogUiConfig());
      result.success(packResult("ok","ok",new HashMap<String,Object>(){{
          put("businessId",businessId);
      }}));
    } else {
      final String methodName = call.method;
      result.error("nomethod","没有该方法",new HashMap<String,Object>(){{
        put("methodName",methodName);
      }});

    }

  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
}
