package cn.sunline.uicommonlib.common;

import android.content.Context;
import android.content.DialogInterface;

import cn.sunline.uicommonlib.ui.widget.CustomAlertDialog;

public class UIHelper {
    public static CustomAlertDialog.Builder createAlertDialogBuilder(Context context, String title, String msg, String positiveText, String negativeText,
                                         final Runnable positiveRunnable, final Runnable negativeRunnable) {
        DialogInterface.OnClickListener mOnclickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == DialogInterface.BUTTON_POSITIVE){
                    dialog.dismiss();
                    if(null != positiveRunnable){
                        positiveRunnable.run();
                    }
                }else if(which == DialogInterface.BUTTON_NEGATIVE){
                    dialog.dismiss();
                    if(null != negativeRunnable){
                        negativeRunnable.run();
                    }
                }


            }
        };

        final CustomAlertDialog.Builder dialogBuilder = new CustomAlertDialog.Builder(context);

        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(msg);
        dialogBuilder.setPositiveButton(positiveText,mOnclickListener);
        dialogBuilder.setNegativeButton(negativeText ,mOnclickListener);

        return dialogBuilder;

    }
}
