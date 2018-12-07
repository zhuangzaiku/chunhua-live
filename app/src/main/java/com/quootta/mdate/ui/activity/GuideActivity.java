package com.quootta.mdate.ui.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.base.BaseFragment;
import com.quootta.mdate.ui.adapter.MyPagerAdapter;
import com.quootta.mdate.ui.fragment.Guide1Fragment;
import com.quootta.mdate.ui.fragment.Guide2Fragment;
import com.quootta.mdate.ui.view.IndicatorView;
import com.quootta.mdate.ui.view.MyViewPager;
import com.quootta.mdate.utils.ActivityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
//暂时不适用本Activity 2016.10.18
public class GuideActivity extends BaseActivity {

    @Bind(R.id.vp_guide)
    MyViewPager vpGuide;

    @Bind(R.id.id_indicator)
    IndicatorView indicatorView;

    @Bind(R.id.btn_login)
    Button btnLogin;

    @Bind(R.id.btn_sign_in)
    Button btnSignIn;

    private MyPagerAdapter pagerAdapter;
    private List<BaseFragment> fragmentList;
    private FragmentManager fm;

    @Override
    protected void init() {
        fragmentList = new ArrayList<BaseFragment>();
        fragmentList.add(new Guide1Fragment());
        fragmentList.add(new Guide2Fragment());
        fm = getSupportFragmentManager();

    }

    @Override
    protected int getRootView() {
        return R.layout.activity_guide;
    }

    @Override
    protected void initData() {
        pagerAdapter = new MyPagerAdapter(fm, fragmentList);
        vpGuide.setOffscreenPageLimit(2);
        vpGuide.setNoScroll(false);
        vpGuide.setAdapter(pagerAdapter);
        indicatorView.setViewPager(vpGuide);
    }

    @Override
    protected void setListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent guideIntent = new Intent(GuideActivity.this, OtherLoginActivity.class);
                ActivityUtil.finishActivty();
                startActivity(guideIntent);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(GuideActivity.this, SignActivity.class);
                startActivity(signUpIntent);

            }
        });
    }
}
