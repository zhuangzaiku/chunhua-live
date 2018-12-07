package com.quootta.mdate.ui.dialog;

import android.app.ProgressDialog;
import android.content.Context;

import com.quootta.mdate.R;

/**
 * Created by kky on 2016/3/25.
 */
public class MyProgressDialog extends ProgressDialog {

    Context mContext;

    public MyProgressDialog(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    private void initView() {
        setProgress(STYLE_SPINNER);
        setProgressStyle(STYLE_SPINNER);
        setMessage(mContext.getString(R.string.loading));
        setIndeterminate(false);
    }


}
