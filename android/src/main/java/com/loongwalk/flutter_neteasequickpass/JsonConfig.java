package com.loongwalk.flutter_neteasequickpass;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import com.netease.nis.quicklogin.helper.UnifyUiConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

//int rgba = Color.parseColor(rgbaStr);
//        int argb = (rgba >>> 8) | (rgba << (32 - 8));

public class JsonConfig {
    private String default_navBackIcon = null;
    private int default_navBackIconWidth = 25;
    private int default_navBackIconHeight = 25;
    private int default_navBackgroundColor = Color.WHITE;
    private String default_navTitle = null;
    private int default_navTitleColor = Color.BLACK;
    private boolean default_isHideNav = true;
    private String default_logoIconName = null;
    private int default_logoWidth = 0;
    private int default_logoHeight = 0;
    private int default_logoTopYOffset = 0;
    private int default_logoBottomYOffset = 0;
    private int default_logoXOffset = 0;
    private boolean default_isHideLogo = true;
    private int default_maskNumberColor = Color.BLACK;
    private int default_maskNumberSize = 16;
    private int default_maskNumberTopYOffset = 10;
    private int default_maskNumberBottomYOffset = 0;
    private int default_maskNumberXOffset = 0;
    private int default_sloganSize = 0;
    private int default_sloganColor = Color.BLACK;
    private int default_sloganTopYOffset = 0;
    private int default_sloganBottomYOffset = 0;
    private int default_sloganXOffset =0;
    private String default_loginBtnText = "一键登录";
    private int default_loginBtnTextSize = 12;
    private int default_loginBtnTextColor = -1;
    private int default_loginBtnWidth;
    private int default_loginBtnHeight;
    private String default_loginBtnBackgroundRes;
    private int default_loginBtnTopYOffset;
    private int default_loginBtnBottomYOffset;
    private int default_loginBtnXOffset;
    private int default_privacyTextColor = Color.BLACK;
    private int default_privacyProtocolColor = Color.GRAY;
    private int default_privacySize;
    private int default_privacyTopYOffset;
    private int default_privacyBottomYOffset;
    private int default_privacyXOffset;
    private boolean default_privacyState = true;
    private boolean default_isHidePrivacyCheckBox = false;
    private boolean default_isPrivacyTextGravityCenter = false;
    private String default_checkedImageName = "yd_checkbox_checked";
    private String default_unCheckedImageName = "yd_checkbox_unchecked";
    private String default_privacyTextStart = "登录即同意";
    private String default_protocolText;
    private String default_protocolLink;
    private String default_protocol2Text;
    private String default_protocol2Link;
    private String default_privacyTextEnd = "且授权使用本机号码登录";
    private String default_protocolNavTitle;
    private String default_protocolNavBackIcon;
    private int default_protocolNavColor;
    private boolean default_isDialogMode = true;
    private int default_dialogWidth;
    private int default_dialogHeight;
    private int default_dialogX=0;
    private int default_dialogY=0;
    private boolean default_isBottomDialog = false;
    public int screenWidth = 0;
    public int screenHeight = 0;
    public Context appContext;
    JsonConfig(Context AppContext) {
        appContext = AppContext;
        this.screenWidth = Utils.getScreenDpWidth(appContext);
        this.screenHeight = Utils.getScreenDpHeight(appContext);
        this.default_dialogWidth = (int)(this.screenWidth*0.85);
        this.default_dialogHeight = (int)(this.screenHeight*0.4);
        this.default_loginBtnWidth = (int)(this.screenWidth*0.5);
        this.default_loginBtnHeight = (int)(this.screenWidth*0.10);
        this.default_maskNumberTopYOffset = (int)(this.screenHeight*0.1);
        this.default_maskNumberBottomYOffset = (int)(this.screenHeight*0.2);
        this.default_loginBtnBottomYOffset = (int)(this.screenHeight*0.1);
    }

    private int getWidth(JSONObject jsonObject, String name,int defaultValue) {
        int value = jsonObject.optInt(name,defaultValue);
        String temp = jsonObject.optString(name+"%",null);
        if(temp!=null) {
            int tempInt = Integer.parseInt(temp);
            if (tempInt>=0 && tempInt<= 100) {
                value = (int)(screenWidth*tempInt/100);
            }
        }
        return value;
    }

    private int getHeight(JSONObject jsonObject, String name,int defaultValue) {
        int value = jsonObject.optInt(name,defaultValue);
        String temp = jsonObject.optString(name+"%",null);
        if(temp!=null) {
            int tempInt = Integer.parseInt(temp);
            if (tempInt>=0 && tempInt<= 100) {
                value = (int)(screenHeight*tempInt/100);
            }
        }
        return value;
    }

    private int getColor(JSONObject jsonObject, String name,int defaultValue) {
        int value = jsonObject.optInt(name,defaultValue);
        String rgbaStr = jsonObject.optString(name+"#",null);
        if(rgbaStr!=null) {
            try {
                int rgba = Color.parseColor(rgbaStr);
                int argb = (rgba >>> 8) | (rgba << (32 - 8));
                value = argb;
            } catch(Exception e) {

            }
        }
        return value;
    }

    public UnifyUiConfig buildUiConfig(String content) {
        // 以下操作用getXX不用optXX防止QA修改配置文件误删除某些选项而未测到
        this.screenWidth = Utils.getScreenDpWidth(appContext);
        this.screenHeight = Utils.getScreenDpHeight(appContext);
        JSONObject jsonObject = new JSONObject();
        if (content!=null){
            try {
                jsonObject = new JSONObject(content);
            } catch(JSONException e) {

            }
        }
        Log.i("uiconfig","build ui config :"+content);
        String navBackIcon = jsonObject.optString("navBackIcon",default_navBackIcon);
        Log.i("uiconfig","navBackIcon="+navBackIcon);
        int tempInt = 0;
        int navBackIconWidth = getWidth(jsonObject,"navBackIconWidth",default_navBackIconWidth);
        Log.i("uiconfig","navBackIconWidth="+navBackIconWidth);
        int navBackIconHeight = getHeight(jsonObject,"navBackIconHeight",default_navBackIconHeight);
        if(navBackIconHeight==0) {
            navBackIconHeight = navBackIconWidth;
        }
        Log.i("uiconfig","navBackIconHeight="+navBackIconHeight);
        int navBackgroundColor = getColor(jsonObject,"navBackgroundColor",default_navBackgroundColor);
        Log.i("uiconfig","navBackgroundColor="+navBackgroundColor);
        String navTitle = jsonObject.optString("navTitle",default_navTitle);
        Log.i("uiconfig","navTitle="+navTitle);
        int navTitleColor = getColor(jsonObject,"navTitleColor",default_navTitleColor);
        Log.i("uiconfig","navTitleColor="+navTitleColor);
        boolean isHideNav = jsonObject.optBoolean("isHideNav",default_isHideNav);
        Log.i("uiconfig","isHideNav="+isHideNav);
        String logoIconName = jsonObject.optString("logoIconName",default_logoIconName);
        Log.i("uiconfig","logoIconName="+logoIconName);
        int logoWidth = getWidth(jsonObject,"logoWidth",default_logoWidth);
        Log.i("uiconfig","logoWidth="+logoWidth);
        int logoHeight = getHeight(jsonObject,"logoHeight",default_logoHeight);
        if(logoHeight==0) {
            logoHeight = logoWidth;
        }
        Log.i("uiconfig","logoHeight="+logoHeight);
        int logoTopYOffset = getHeight(jsonObject,"logoTopYOffset",default_logoTopYOffset);
        Log.i("uiconfig","logoTopYOffset="+logoTopYOffset);
        int logoBottomYOffset = getHeight(jsonObject,"logoBottomYOffset",default_logoBottomYOffset);
        Log.i("uiconfig","logoBottomYOffset="+logoBottomYOffset);
        int logoXOffset = getWidth(jsonObject,"logoXOffset",default_logoXOffset);
        Log.i("uiconfig","logoXOffset="+logoXOffset);
        boolean isHideLogo = jsonObject.optBoolean("isHideLogo",default_isHideLogo);
        Log.i("uiconfig","isHideLogo="+isHideLogo);
        int maskNumberColor = getColor(jsonObject,"maskNumberColor",default_maskNumberColor);
        Log.i("uiconfig","maskNumberColor="+maskNumberColor);
        int maskNumberSize = getWidth(jsonObject,"maskNumberSize",default_maskNumberSize);
        Log.i("uiconfig","maskNumberSize="+maskNumberSize);
        int maskNumberTopYOffset = getHeight(jsonObject,"maskNumberTopYOffset",default_maskNumberTopYOffset);
        Log.i("uiconfig","maskNumberTopYOffset="+maskNumberTopYOffset);
        int maskNumberBottomYOffset = getHeight(jsonObject,"maskNumberBottomYOffset",default_maskNumberBottomYOffset);
        Log.i("uiconfig","maskNumberBottomYOffset="+maskNumberBottomYOffset);
        int maskNumberXOffset = getWidth(jsonObject,"maskNumberXOffset",default_maskNumberXOffset);
        Log.i("uiconfig","maskNumberXOffset="+maskNumberXOffset);
        int sloganSize = getWidth(jsonObject,"sloganSize",default_sloganSize);
        Log.i("uiconfig","sloganSize="+sloganSize);
        int sloganColor = getColor(jsonObject,"sloganColor",default_sloganColor);
        Log.i("uiconfig","sloganColor="+sloganColor);
        int sloganTopYOffset = getHeight(jsonObject,"sloganTopYOffset",default_sloganTopYOffset);
        Log.i("uiconfig","sloganTopYOffset="+sloganTopYOffset);
        int sloganBottomYOffset = getHeight(jsonObject,"sloganBottomYOffset",default_sloganBottomYOffset);
        Log.i("uiconfig","navBackgroundColor="+navBackgroundColor);
        int sloganXOffset = getWidth(jsonObject,"sloganXOffset",default_sloganXOffset);
        Log.i("uiconfig","sloganBottomYOffset="+sloganBottomYOffset);
        String loginBtnText = jsonObject.optString("loginBtnText",default_loginBtnText);
        Log.i("uiconfig","loginBtnText="+loginBtnText);
        int loginBtnTextSize = getWidth(jsonObject,"loginBtnTextSize",default_loginBtnTextSize);
        Log.i("uiconfig","loginBtnTextSize="+loginBtnTextSize);
        int loginBtnTextColor = getColor(jsonObject,"loginBtnTextColor",default_loginBtnTextColor);
        Log.i("uiconfig","loginBtnTextColor="+loginBtnTextColor);
        int loginBtnWidth = getWidth(jsonObject,"loginBtnWidth",default_loginBtnWidth);
        Log.i("uiconfig","loginBtnWidth="+loginBtnWidth);
        int loginBtnHeight = getHeight(jsonObject,"loginBtnHeight",default_loginBtnHeight);
        if(loginBtnHeight==0) {
            loginBtnHeight = (int)(loginBtnWidth * 0.75);
        }
        Log.i("uiconfig","loginBtnHeight="+loginBtnHeight);
        String loginBtnBackgroundRes = jsonObject.optString("loginBtnBackgroundRes",default_loginBtnBackgroundRes);
        Log.i("uiconfig","loginBtnBackgroundRes="+loginBtnBackgroundRes);
        int loginBtnTopYOffset = getHeight(jsonObject,"loginBtnTopYOffset",default_loginBtnTopYOffset);
        Log.i("uiconfig","loginBtnTopYOffset="+loginBtnTopYOffset);
        int loginBtnBottomYOffset = getHeight(jsonObject,"loginBtnBottomYOffset",default_loginBtnBottomYOffset);
        Log.i("uiconfig","loginBtnBottomYOffset="+loginBtnBottomYOffset);
        int loginBtnXOffset = getWidth(jsonObject,"loginBtnXOffset",default_loginBtnXOffset);
        Log.i("uiconfig","loginBtnXOffset="+loginBtnXOffset);
        int privacyTextColor = getColor(jsonObject,"privacyTextColor",default_privacyTextColor);
        Log.i("uiconfig","privacyTextColor="+privacyTextColor);
        int privacyProtocolColor = jsonObject.optInt("privacyProtocolColor",default_privacyProtocolColor);
        Log.i("uiconfig","privacyProtocolColor="+privacyProtocolColor);
        int privacySize = jsonObject.optInt("privacySize",default_privacySize);
        Log.i("uiconfig","privacySize="+privacySize);
        int privacyTopYOffset = jsonObject.optInt("privacyTopYOffset",default_privacyTopYOffset);
        Log.i("uiconfig","privacyTopYOffset="+privacyTopYOffset);
        int privacyBottomYOffset = jsonObject.optInt("privacyBottomYOffset",default_privacyBottomYOffset);
        Log.i("uiconfig","privacyBottomYOffset="+privacyBottomYOffset);
        int privacyXOffset = jsonObject.optInt("privacyXOffset",default_privacyXOffset);
        Log.i("uiconfig","privacyXOffset="+privacyXOffset);
        boolean privacyState = jsonObject.optBoolean("privacyState",default_privacyState);
        Log.i("uiconfig","privacyState="+privacyState);
        boolean isHidePrivacyCheckBox = jsonObject.optBoolean("isHidePrivacyCheckBox",default_isHidePrivacyCheckBox);
        Log.i("uiconfig","isHidePrivacyCheckBox="+isHidePrivacyCheckBox);
        boolean isPrivacyTextGravityCenter = jsonObject.optBoolean("isPrivacyTextGravityCenter",default_isPrivacyTextGravityCenter);
        Log.i("uiconfig","isPrivacyTextGravityCenter="+isPrivacyTextGravityCenter);
        String checkedImageName = jsonObject.optString("checkedImageName",default_checkedImageName);
        Log.i("uiconfig","checkedImageName="+checkedImageName);
        String unCheckedImageName = jsonObject.optString("unCheckedImageName",default_unCheckedImageName);
        Log.i("uiconfig","unCheckedImageName="+unCheckedImageName);
        String privacyTextStart = jsonObject.optString("privacyTextStart",default_privacyTextStart);
        Log.i("uiconfig","privacyTextStart="+privacyTextStart);
        String protocolText = jsonObject.optString("protocolText",default_protocolText);
        Log.i("uiconfig","protocolText="+protocolText);
        String protocolLink = jsonObject.optString("protocolLink",default_protocolLink);
        Log.i("uiconfig","protocolLink="+protocolLink);
        String protocol2Text = jsonObject.optString("protocol2Text",default_protocol2Text);
        Log.i("uiconfig","protocol2Text="+protocol2Text);
        String protocol2Link = jsonObject.optString("protocol2Link",default_protocol2Link);
        Log.i("uiconfig","protocol2Link="+protocol2Link);
        String privacyTextEnd = jsonObject.optString("privacyTextEnd",default_privacyTextEnd);
        Log.i("uiconfig","privacyTextEnd="+privacyTextEnd);
        String protocolNavTitle = jsonObject.optString("protocolNavTitle",default_protocolNavTitle);
        Log.i("uiconfig","protocolNavTitle="+protocolNavTitle);
        String protocolNavBackIcon = jsonObject.optString("protocolNavBackIcon",default_protocolNavBackIcon);
        Log.i("uiconfig","protocolNavBackIcon="+protocolNavBackIcon);
        int protocolNavColor = jsonObject.optInt("protocolNavColor",default_protocolNavColor);
        Log.i("uiconfig","protocolNavColor="+protocolNavColor);
        boolean isDialogMode = jsonObject.optBoolean("isDialogMode",default_isDialogMode);
        Log.i("uiconfig","isDialogMode="+isDialogMode);
        int dialogWidth = jsonObject.optInt("dialogWidth",default_dialogWidth);
        Log.i("uiconfig","dialogWidth="+dialogWidth);
        int dialogHeight = jsonObject.optInt("dialogHeight",default_dialogHeight);
        Log.i("uiconfig","dialogHeight="+dialogHeight);
        int dialogX = jsonObject.optInt("dialogX",default_dialogX);
        Log.i("uiconfig","dialogX="+dialogX);
        int dialogY = jsonObject.optInt("dialogY",default_dialogY);
        Log.i("uiconfig","dialogY="+dialogY);
        boolean isBottomDialog = jsonObject.optBoolean("isBottomDialog",default_isBottomDialog);
        Log.i("uiconfig","isBottomDialog="+isBottomDialog);

        UnifyUiConfig.Builder builder = new UnifyUiConfig.Builder()
                .setNavigationIcon(navBackIcon)
                .setNavigationBackIconWidth(navBackIconWidth)
                .setNavigationBackIconHeight(navBackIconHeight)
                .setNavigationBackgroundColor(navBackgroundColor)
                .setNavigationTitle(navTitle)
                .setNavigationTitleColor(navTitleColor)
                .setHideNavigation(isHideNav)

                .setLogoIconName(logoIconName)
                .setLogoWidth(logoWidth)
                .setLogoHeight(logoHeight)
                .setLogoTopYOffset(logoTopYOffset)
                .setLogoBottomYOffset(logoBottomYOffset)
                .setLogoXOffset(logoXOffset)
                .setHideLogo(isHideLogo)

                .setMaskNumberSize(maskNumberSize)
                .setMaskNumberColor(maskNumberColor)
                .setMaskNumberXOffset(maskNumberXOffset)
                .setMaskNumberTopYOffset(maskNumberTopYOffset)
                .setMaskNumberBottomYOffset(maskNumberBottomYOffset)

                .setSloganSize(sloganSize)
                .setSloganColor(sloganColor)
                .setSloganTopYOffset(sloganTopYOffset)
                .setSloganXOffset(sloganXOffset)
                .setSloganBottomYOffset(sloganBottomYOffset)

                .setLoginBtnText(loginBtnText)
                .setLoginBtnBackgroundRes(loginBtnBackgroundRes)
                .setLoginBtnTextColor(loginBtnTextColor)
                .setLoginBtnTextSize(loginBtnTextSize)
                .setLoginBtnHeight(loginBtnHeight)
                .setLoginBtnWidth(loginBtnWidth)
                .setLoginBtnTopYOffset(loginBtnTopYOffset)
                .setLoginBtnBottomYOffset(loginBtnBottomYOffset)
                .setLoginBtnXOffset(loginBtnXOffset)

                .setPrivacyTextColor(privacyTextColor)
                .setPrivacyProtocolColor(privacyProtocolColor)
                .setPrivacySize(privacySize)
                .setPrivacyTopYOffset(privacyTopYOffset)
                .setPrivacyBottomYOffset(privacyBottomYOffset)
                .setPrivacyXOffset(privacyXOffset)
                .setPrivacyState(privacyState)
                .setHidePrivacyCheckBox(isHidePrivacyCheckBox)
                .setPrivacyTextGravityCenter(isPrivacyTextGravityCenter)
                .setCheckedImageName(checkedImageName)
                .setUnCheckedImageName(unCheckedImageName)
                .setPrivacyTextStart(privacyTextStart)
                .setProtocolText(protocolText)
                .setProtocolLink(protocolLink)
                .setProtocol2Text(protocol2Text)
                .setProtocol2Link(protocol2Link)
                .setPrivacyTextEnd(privacyTextEnd)

                .setProtocolPageNavTitle(protocolNavTitle)
                .setProtocolPageNavBackIcon(protocolNavBackIcon)
                .setProtocolPageNavColor(protocolNavColor)
                .setDialogMode(isDialogMode, dialogWidth, dialogHeight, dialogX, dialogY, isBottomDialog);
        Log.i("builder ",builder.toString());
        return builder.build(appContext);
    }


}
