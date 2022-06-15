package cn.sunline.uicommonlib.ui.widget;

import android.text.TextPaint;
import android.text.style.URLSpan;

/**
 * 带前景色的
 */
public class ForegroundColorURLSpan extends URLSpan {
    private int mTextColor;
    private boolean mUnderline;
    public ForegroundColorURLSpan(String str, int textColor, boolean underline){
        super(str);
        this.mTextColor = textColor;
        this.mUnderline = underline;
    }
    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        //这句话是关键
        ds.setColor(mTextColor);
        ds.setUnderlineText(this.mUnderline);
    }
}