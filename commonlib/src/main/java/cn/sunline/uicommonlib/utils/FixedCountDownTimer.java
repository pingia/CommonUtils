package cn.sunline.uicommonlib.utils;

import android.os.CountDownTimer;
import android.os.Handler;


 abstract public class FixedCountDownTimer extends CountDownTimer {

    private final long mCountDownInterval;
    private int mTotalSeconds;

    public FixedCountDownTimer(long millisInFuture, long countDownInterval){
        super(millisInFuture + countDownInterval, countDownInterval);
        this.mCountDownInterval = countDownInterval;
        this.mTotalSeconds = (int)(millisInFuture/countDownInterval);
    }


    @Override
    public void onTick(long l) {
        //解决不能倒计时到0的问题
        int currentSecond = (int)(l/mCountDownInterval);
        onSecondTick(currentSecond, mTotalSeconds);
        if (l / mCountDownInterval-1 == 0){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onFinish();
                }
            }, mCountDownInterval);
        }
    }

    abstract public void onSecondTick(int currentSecond, int totalSeconds);
}
