package cn.sunline.uicommonlib.utils;

import android.annotation.TargetApi;
import android.app.Notification;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;
import android.widget.RemoteViews;

import java.io.File;

public class MethodsCompat {

    public static boolean isMethodsCompat(int VersionCode) {
        int currentVersion = Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR_MR1)
    public static Bitmap getThumbnail(ContentResolver cr, long origId, int kind, BitmapFactory.Options options) {
        return MediaStore.Images.Thumbnails.getThumbnail(cr, origId, kind, options);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Drawable getDrawable(Context context, int drawableId) {
        if (isMethodsCompat(Build.VERSION_CODES.LOLLIPOP)) {
            return context.getResources().getDrawable(drawableId, null);
        } else {
            return context.getResources().getDrawable(drawableId);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static int getColor(Context context, int colorResId) {
        if (isMethodsCompat(Build.VERSION_CODES.M)) {
            return context.getResources().getColor(colorResId, null);
        } else {
            return context.getResources().getColor(colorResId);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static ColorStateList getColorStateList(Context context, int colorStateResId){
        if (isMethodsCompat(Build.VERSION_CODES.M)) {
            return context.getResources().getColorStateList(colorStateResId, null);
        }else{
            return context.getResources().getColorStateList(colorStateResId);
        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void setBgDrawable(View view, Drawable drawable) {
        if (isMethodsCompat(Build.VERSION_CODES.JELLY_BEAN)) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }

    }

    @TargetApi(8)
    public static File getExternalCacheDir(Context context) {
        return context.getExternalCacheDir();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void setNotifitionView(Notification notification, RemoteViews myNotificationView) {
        if (isMethodsCompat(Build.VERSION_CODES.JELLY_BEAN)) {
            notification.bigContentView = myNotificationView;
        }
    }
}
