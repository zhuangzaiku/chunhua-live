package com.quootta.mdate.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.quootta.mdate.ui.dialog.MyProgressDialog;
import com.quootta.mdate.utils.LogUtil;

import java.io.ByteArrayOutputStream;

/**
 * Created by ryon on 2016/11/10.
 */

public class ImageCompressTask extends AsyncTask<Void, Integer, byte[]> {

    private MyProgressDialog progressDialog;
    private Context baseContext;
    private Bitmap avatar;
    private OnCompressFinishListener listener;

    public ImageCompressTask(Context context, Bitmap bitmap, OnCompressFinishListener listener) {
        this.baseContext = context;
        this.avatar = bitmap;
        this.listener = listener;
    }

    public interface OnCompressFinishListener {
        void onFinish(byte[] bytes);
    }


    @Override
    protected void onPreExecute() {
        progressDialog = new MyProgressDialog(baseContext);
        progressDialog.show();
    }


    @Override
    protected byte[] doInBackground(Void... params) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        avatar.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        return baos.toByteArray();
    }

    @Override
    protected void onPostExecute(byte[] bytes) {
        listener.onFinish(bytes);
        LogUtil.d("ImageCompressTask", "onPostExecute");
        progressDialog.dismiss();
    }
}