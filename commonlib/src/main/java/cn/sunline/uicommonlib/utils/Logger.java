package cn.sunline.uicommonlib.utils;

import android.util.Log;

import cn.sunline.uicommonlib.BuildConfig;


public class Logger {

    public static void d(String tag, String msg){
        if(BuildConfig.DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void w(String tag, String msg){
        if(BuildConfig.DEBUG) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg){
        if(BuildConfig.DEBUG) {
            Log.e(tag, msg);
        }
    }
}
