package com.quootta.mdate.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.FrameLayout;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.ui.fragment.SignUpFirstFragment;

import butterknife.Bind;

public class SignActivity<T> extends BaseActivity {

    @Bind(R.id.container)
    FrameLayout container;
    private FragmentManager mFragmentManager;


    @Override
    protected void init() {
        mFragmentManager=getSupportFragmentManager();

    }

    @Override
    protected int getRootView() {
        return R.layout.activity_sign;
    }

    @Override
    protected void initData() {
        changeFragment(SignUpFirstFragment.newInstance());
    }

    @Override
    protected void setListener() {

    }

    public void changeFragment(Fragment fragment){
        mFragmentManager.beginTransaction().replace(R.id.container,fragment).commit();
    }
}
