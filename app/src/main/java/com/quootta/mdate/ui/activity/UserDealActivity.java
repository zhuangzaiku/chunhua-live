package com.quootta.mdate.ui.activity;

import android.webkit.WebView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;

import butterknife.Bind;

public class UserDealActivity extends BaseActivity {
    @Bind(R.id.userdeal)
    WebView userdeal;


    @Override
    protected void init() {

    }

    @Override
    protected int getRootView() {
        return R.layout.activity_userdeal;
    }

    @Override
    protected void initData() {
        userdeal.loadUrl("http:///public/server.html");
    }

    @Override
    protected void setListener() {

    }



}
