/**
 * 自定义的alertdialog
 */
package cn.sunline.uicommonlib.ui.widget;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import cn.sunline.uicommonlib.R;
import cn.sunline.uicommonlib.utils.CommonTool;
import cn.sunline.uicommonlib.utils.MethodsCompat;


public class CustomAlertDialog extends Dialog {

	/**
	 * @param context
	 */
	public CustomAlertDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param theme
	 */
	public CustomAlertDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param cancelable
	 * @param cancelListener
	 */
	public CustomAlertDialog(Context context, boolean cancelable,
                             OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}
	
	public static class Builder{
		private Context context;
		private String title;
		private String message;
		private String positiveButtonText;
		private String negativeButtonText;
		private View contentView;

        private boolean isOkDismissDialog  = true;
		
		private OnClickListener positiveButtonClickListener, negativeButtonClickListener;
		
		public Builder(Context context){
			this.context = context;
		}
		
        /**
         * Set the message to display using the given resource id.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setMessage(int messageId) {
            this.message =(String)context.getText(messageId);
            return this;
        }
        
        /**
         * Set the message to display.
          *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setMessage(String message) {
        	this.message =message;
            return this;
        }
        
        /**
         * Set the title using the given resource id.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTitle(int titleId) {
        	 this.title =(String)context.getText(titleId);
            return this;
        }
        
        /**
         * Set the title displayed in the {@link Dialog}.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTitle(String title) {
        	 this.title =title;
            return this;
        }
        
        public Builder setContentView(View contentView){
        	this.contentView = contentView;
        	return this;
        }
        
        public Builder setPositiveButton(int textId, final OnClickListener listener) {
            positiveButtonText = (String)context.getText(textId);
            this.positiveButtonClickListener = listener;
            return this;
        }
        
        public Builder setPositiveButton(String text, final OnClickListener listener) {
        	positiveButtonText = text;
            this.positiveButtonClickListener = listener;
            return this;
        }
        
        public Builder setNegativeButton(int textId, final OnClickListener listener) {
            negativeButtonText = (String)context.getText(textId);
            this.negativeButtonClickListener = listener;
            return this;
        }
        
        public Builder setNegativeButton(String text, final OnClickListener listener) {
        	negativeButtonText = text;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public void setOkDismissDialog(boolean dismiss){
            isOkDismissDialog = dismiss;
        }
        
        /**
         * Create the custom dialog
         */
        public CustomAlertDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CustomAlertDialog dialog = new CustomAlertDialog(context,
                    R.style.style_dialog);
            dialog.setCanceledOnTouchOutside(false);
            
  
            
            View layout = inflater.inflate(R.layout.custom_alertdialog, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//            // set the dialog title
//            ((TextView) layout.findViewById(R.id.title)).setText(title);
            // set the confirm button
            if (positiveButtonText != null) {
            	 Button pbtn = ((Button) layout.findViewById(R.id.positiveButton));
            	 pbtn.getPaint().setFakeBoldText(true);
            	 pbtn.setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                   
                	pbtn .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    if(isOkDismissDialog) {
                                        dialog.dismiss();
                                    }
                                    positiveButtonClickListener.onClick(
                                            dialog, 
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.positiveButton).setVisibility(
                        View.GONE);
            }
            // set the cancel button
            if (negativeButtonText != null) {
            	 Button nbtn = ((Button) layout.findViewById(R.id.negativeButton));
            	 nbtn.setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                	nbtn.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    negativeButtonClickListener.onClick(
                                            dialog, 
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
            	Button nbtn = ((Button) layout.findViewById(R.id.negativeButton));
                // if no confirm button just set the visibility to GONE
            	nbtn.setVisibility(
                        View.GONE);
            }

            if(positiveButtonText == null || negativeButtonText == null){
                layout.findViewById(R.id.vertical_line_divider).setVisibility(View.GONE);
            }else{
                layout.findViewById(R.id.vertical_line_divider).setVisibility(View.VISIBLE);
            }

            // set the content message
            if (message != null) {
                ((TextView) layout.findViewById(
                        R.id.msgtv)).setText(message);
            } else {
                 layout.findViewById(
                        R.id.msgtv).setVisibility(View.GONE);
                if (contentView != null) {
                    // if no message set
                    // add the contentView to the dialog body
                    ((LinearLayout) layout.findViewById(R.id.content))
                            .removeAllViews();
                    ((LinearLayout) layout.findViewById(R.id.content))
                            .addView(contentView,
                                    new LayoutParams(
                                            LayoutParams.MATCH_PARENT,
                                            LayoutParams.WRAP_CONTENT));
                }
            }
            
            if(title != null){
            	  ((TextView) layout.findViewById(
                          R.id.titletv)).setText(title);
            }else{
                layout.findViewById(R.id.titletv).setVisibility(View.GONE);
            }
            dialog.setContentView(layout);

            final ScrollView sv = (ScrollView) layout.findViewById(R.id.sv);
            sv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                public void onGlobalLayout() {
                    if(MethodsCompat.isMethodsCompat(Build.VERSION_CODES.JELLY_BEAN)) {
                        sv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }else{
                        sv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }

                    int needMaxHeight  = CommonTool.dip2px(sv.getContext(),300);
                    int height = sv.getMeasuredHeight();

                    if(height > needMaxHeight){
                        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)sv.getLayoutParams();
                        lp.height = needMaxHeight;
                    }
                }
            });
            
            /*
             * 将对话框的大小按屏幕大小的百分比设置
             */
            WindowManager m = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
            WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); // 获取对话框当前的参数值
            p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8
            dialog.getWindow().setAttributes(p);
            return dialog;
        }
		
	}

}
