package com.loongwalk.flutter_neteasequickpass;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;
import android.util.DisplayMetrics;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by hzhuqi on 2019/1/29.
 */
public class Utils {


    /**
     * 将dp值转换为px值
     *
     * @param dipValue dp值
     * @return px
     */
    public static int dip2px(Context appContext, float dipValue) {
        try {
            final float scale = appContext.getResources().getDisplayMetrics().density;
            return (int) (dipValue * scale + 0.5f);
        } catch (Exception e) {
            return (int) dipValue;
        }
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static float px2dip(Context appContext, float pxValue) {
        final float scale = appContext.getResources().getDisplayMetrics().density;
        return pxValue / scale + 0.5f;
    }

    public static int getScreenPxWidth(Context appContext) {
        DisplayMetrics dm = appContext.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenDpWidth(Context appContext) {
        DisplayMetrics dm = appContext.getResources().getDisplayMetrics();
        return (int)(dm.widthPixels/dm.density + 0.5f);
    }

    public static int getScreenPxHeight(Context appContext) {
        DisplayMetrics dm = appContext.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public static int getScreenDpHeight(Context appContext) {
        DisplayMetrics dm = appContext.getResources().getDisplayMetrics();
        return (int)(dm.heightPixels/dm.density + 0.5f);
    }
}
