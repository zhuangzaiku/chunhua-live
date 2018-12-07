package com.quootta.mdate.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.ui.view.VideoRecorderView;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.ToastUtil;

import butterknife.Bind;

public class RecordVideoActivity extends BaseActivity {
    @Bind(R.id.videoRecorderView)
    VideoRecorderView mRecorderView;
    @Bind(R.id.shoot_button)
    Button mShootBtn;
    private boolean isFinish = true;
    @Override
    protected void init() {
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_record_video;
    }

    @Override
    protected void initData() {
        ViewGroup.LayoutParams layoutParams = mRecorderView.getLayoutParams();
        LogUtil.d(TAG, "height:" + layoutParams.height);
        LogUtil.d(TAG, "width:" + layoutParams.width);

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            finishActivity();
        }
    };

    private void finishActivity() {
        if (isFinish) {
            mRecorderView.stop();
            // 返回到播放页面
            Intent intent = new Intent();
            Log.d("TAG", mRecorderView.getmRecordFile().getAbsolutePath());
            intent.putExtra("path", mRecorderView.getmRecordFile().getAbsolutePath());
            setResult(RESULT_OK,intent);
        }
        isFinish = false;
        finish();
    }

    @Override
    protected void setListener() {
        mShootBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mRecorderView.record(new VideoRecorderView.OnRecordFinishListener() {
                        @Override
                        public void onRecordFinish() {
                            handler.sendEmptyMessage(1);
                        }
                    });
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (mRecorderView.getTimeCount() > 2){
                        handler.sendEmptyMessage(1);
                    } else {
                        if (mRecorderView.getmRecordFile() != null)
                            mRecorderView.getmRecordFile().delete();
                        mRecorderView.stop();
                        ToastUtil.showToast(getString(R.string.app_tips_text65));
                        finishActivity();
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        isFinish = true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        isFinish = false;
        mRecorderView.stop();
    }
}
