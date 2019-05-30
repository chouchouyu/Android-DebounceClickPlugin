package com.github.susan.demo;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.github.susan.debounceclick.java.DebounceClickMark;

public class DialogUtil {
    public static void show2Button(Context context, String content, String confirmText, String cancelText, final View.OnClickListener confirmListener, final View.OnClickListener cancelListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_2botton, null);
        ((TextView) view.findViewById(R.id.dialog_2button_text)).setText(content);
        Button confirm = view.findViewById(R.id.dialog_2button_confirm);
        Button cancel = view.findViewById(R.id.dialog_2button_cancel);
        confirm.setText(confirmText);
        cancel.setText(cancelText);
        final Dialog dialog = showFullScreenDialog(context, view, false);
        Log.d("TESTSTTST", "1");
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmListener != null) {
                    Log.d("TESTSTTST", v.getId() + "id----confirm");
                    confirmListener.onClick(v);
                }
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TESTSTTST", v.getId() + "id----cancel");
                if (cancelListener != null) {
                    cancelListener.onClick(v);
                }
                dialog.dismiss();
            }
        });


    }

    public static Dialog showFullScreenDialog(Context context, View view, boolean cancelable) {
        return showFullScreenDialog(context, view, cancelable, true, true);
    }

    public static Dialog showFullScreenDialog(Context context, View view, boolean cancelable, boolean isWidthFull, boolean isHeightFull) {
        Dialog dialog = new Dialog(context, R.style.fullScreenDialog);
        try {
            dialog.setContentView(view);
            dialog.setCancelable(cancelable);
            dialog.setCanceledOnTouchOutside(cancelable);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = dialog.getWindow();
            dialog.show();
            window.getDecorView().setPadding(0, 0, 0, 0);
            lp.copyFrom(window.getAttributes());
            if (isWidthFull && isHeightFull) {
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            } else if (isHeightFull) {
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            } else if (isWidthFull) {
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            } else {
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            }
            window.setAttributes(lp);
        } catch (Exception e) {
        }

        return dialog;
    }


}
