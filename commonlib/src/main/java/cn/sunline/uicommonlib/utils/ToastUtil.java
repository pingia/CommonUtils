package cn.sunline.uicommonlib.utils;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;


public class ToastUtil {

    private static String oldMsg;
    protected static Toast toast = null;
    private static long oneTime = 0;
    private static long twoTime = 0;

    private static boolean toastIsShow = false;
    private static long toastShowTime = 0;
    private static long toastHiddenTime = 0;

    private static final IShowCallback toastCallback;

    public interface IShowCallback{
        void onShow();
        void onHidden();
    }

    static {
        toastCallback = new IShowCallback(){

            @Override
            public void onShow() {
                toastIsShow = true;
                toastShowTime = System.currentTimeMillis();
            }

            @Override
            public void onHidden() {
                toastIsShow = false;
                toastHiddenTime = System.currentTimeMillis();
            }
        };
    }

    public static void showToast(Context context, String s) {
        if(TextUtils.isEmpty(s)){
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), s, Toast.LENGTH_SHORT);

            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.R){
                View view = toast.getView();
                if(null == view) return;

                view.getViewTreeObserver().addOnWindowAttachListener(new ViewTreeObserver.OnWindowAttachListener() {
                    @Override
                    public void onWindowAttached() {
                        if(null != toastCallback){
                            toastCallback.onShow();
                        }
                    }

                    @Override
                    public void onWindowDetached() {
                        if(null != toastCallback){
                            toastCallback.onHidden();
                        }
                    }
                });
            }else{
                toast.addCallback(new Toast.Callback() {
                    @Override
                    public void onToastShown() {
                        super.onToastShown();
                        if(null != toastCallback){
                            toastCallback.onShow();
                        }
                    }

                    @Override
                    public void onToastHidden() {
                        super.onToastHidden();
                        if(null != toastCallback){
                            toastCallback.onHidden();
                        }
                    }
                });
            }

            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (s.equals(oldMsg)) {
                if (twoTime - oneTime > Math.abs(toastHiddenTime - toastShowTime)
                        && !toastIsShow) toast.show();
            } else {
                oldMsg = s;
                toast.setText(s);
                if(!toastIsShow) toast.show();
            }
        }
        oneTime = twoTime;
    }


    public static void showToast(Context context, int resId) {
        showToast(context, context.getString(resId));
    }

}
