package com.quootta.mdate.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.ui.fragment.RankingListFragment;
import com.quootta.mdate.utils.ActivityUtil;

import butterknife.Bind;

public class RankingActivity extends BaseActivity {


    @Bind(R.id.iv_back_title_bar)ImageView iv_back;
    @Bind(R.id.tv_title_bar)TextView tv_title;
    @Bind(R.id.fl_content)
    FrameLayout flContent;

    private RankingListFragment rankingListFragment;
    private int page;
    @Override
    protected void init() {
        Intent intent=getIntent();
        page=intent.getIntExtra("page",0);
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_ranking;
    }

    @Override
    protected void initData() {
        initTitle();
        initContent();
    }

    private void initContent() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        rankingListFragment = new RankingListFragment();

        Bundle bundle=new Bundle();
        bundle.putInt("page",page);
        rankingListFragment.setArguments(bundle);

        transaction.replace(R.id.fl_content, rankingListFragment);
        transaction.commit();
    }

    private void initTitle() {
        iv_back.setVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.app_tips_text64));
    }

    @Override
    protected void setListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.finishActivty();
            }
        });

    }
}
