package com.quootta.mdate.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.quootta.mdate.R;


public class MyMessageDialog extends AlertDialog{

    private Context context;
    private String title;
    private String message;

    public MyMessageDialog(Context context, String title, String message) {
        super(context);
        this.context = context;
        this.title = title;
        this.message = message;
        initView();
    }

    private void initView() {
        setTitle(title);
        setMessage(message);

        setButton(BUTTON_POSITIVE, context.getString(R.string.ensure), new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
    }
}
