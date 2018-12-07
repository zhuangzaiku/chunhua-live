package com.quootta.mdate.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by Ryon on 2016/5/17/0017.
 */
public class MyItemDialog extends AlertDialog {

    private Context mContext;
    private String[] mItems;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onClick(DialogInterface dialog, int which);
    }

    public MyItemDialog(Context context, String[] items, OnItemClickListener onItemClickListener) {
        super(context);
        mContext = context;
        mItems = items;
        this.onItemClickListener = onItemClickListener;
        initDialog();
    }

    private void initDialog() {
        Builder builder = new AlertDialog.Builder(mContext);
        builder.setItems(mItems, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onItemClickListener.onClick(dialog, which);
                dismiss();
            }
        });
    }
}
