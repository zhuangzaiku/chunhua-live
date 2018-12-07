package com.quootta.mdate.base;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.quootta.mdate.utils.ActivityUtil;

import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

public abstract class BaseActivity extends AppCompatActivity {

    protected Context baseContext;
    protected String TAG;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        ActivityUtil.addActivity(this);
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        TAG = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        baseContext=this;
        init();
        setContentView(getRootView());
        ButterKnife.bind(this);
        initData();
        setListener();
    }

    /**
     * 加载UI前的预初始化
     */
    protected abstract void init();

    /**
     * 加载布局
     * @return 布局id
     */
    protected abstract int getRootView();

    /**
     * 请求数据，设置UI
     */
    protected abstract void initData();

    /**
     * 设置监听器
     */
    protected abstract void setListener();

    /**
     * activity销毁后的操作
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 结束activity
     */
    @Override
    public void finish() {
        super.finish();
        ActivityUtil.removeActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(baseContext);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(baseContext);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
