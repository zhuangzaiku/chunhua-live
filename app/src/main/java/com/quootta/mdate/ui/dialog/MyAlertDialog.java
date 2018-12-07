package com.quootta.mdate.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.quootta.mdate.R;

/**
 * Created by kky on 2016/4/6.
 */
public class MyAlertDialog extends AlertDialog {

    private Context mContext;
    private OnPositiveAlterListener onPositiveAlterListener;
    private OnNegativeAlterListener onNegativeAlterListener;

    public MyAlertDialog(Context context, OnPositiveAlterListener onPositiveAlterListener) {
        super(context);
        mContext = context;
        this.onPositiveAlterListener = onPositiveAlterListener;
        initView();
    }

    public void setOnNegativeAlterListener(OnNegativeAlterListener onNegativeAlterListener) {
        this.onNegativeAlterListener = onNegativeAlterListener;
    }

    public interface OnPositiveAlterListener {
        void onAlter();
    }
    public interface OnNegativeAlterListener {
        void onAlter();
    }

    private void initView() {
        setButton(BUTTON_POSITIVE, mContext.getString(R.string.ensure), new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onPositiveAlterListener.onAlter();
                dismiss();
            }
        });

        setButton(BUTTON_NEGATIVE, mContext.getString(R.string.cancel), new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (onNegativeAlterListener != null)
                    onNegativeAlterListener.onAlter();
                dismiss();
            }
        });
    }
}
