package cn.sunline.uicommonlib.utils;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import java.util.UUID;

/**
 * author：admin on 2017/8/16.
 * mail:magi525@163.com
 * function:
 */
public final class CommonTool {
    public  static double random(int min,int max) {
        return Math.random()*(max-min)+min;
    }


    // 获取UUID
    public static String genUUID() {
        UUID uuid = UUID.randomUUID();
        String s = uuid.toString();//
        int p = 0;
        int j = 0;
        char[] buf = new char[32];
        while (p < s.length()) {
            char c = s.charAt(p);
            p += 1;
            if (c == '-')
                continue;
            buf[j] = c;
            j += 1;
        }
        return new String(buf);
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getVersionCode(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getVersionName(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String version = pi.versionName;
            if (TextUtils.isEmpty(version)) {
                return "";
            } else {
                return version;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getActivityLabel(Activity activity){
        try {
            ActivityInfo ai = activity.getPackageManager().getActivityInfo(activity.getComponentName(), 0);
            int resId = ai.labelRes;
            return activity.getString(resId);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getApplicationLabel(Context context){
        try {
            ApplicationInfo pi = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
            int resId = pi.labelRes;
            return context.getString(resId);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void installApk(Context context, Uri apkfileUri) {

        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(apkfileUri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 复制文字到粘贴板
     * @param context
     * @param content
     */
    public static void copy(Context context,String content){
        // 从API11开始android推荐使用android.content.ClipboardManager
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setText(content);
    }

    /**
     * 开启相册
     * @param requestCode
     */
    public static void openPhotoSelector(Activity activity,int requestCode) {
        Intent frontIntent = new Intent();
        frontIntent.setAction(Intent.ACTION_PICK);
        frontIntent.setType("image/*");
        activity.startActivityForResult(frontIntent, requestCode);
    }
}
