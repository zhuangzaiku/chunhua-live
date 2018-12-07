package com.quootta.mdate.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.base.BaseFragment;
import com.quootta.mdate.domain.RankingListData;
import com.quootta.mdate.ui.adapter.RankingListContentAdapter;
import com.android.volley.RequestQueue;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Ryon on 2016/9/2/0002.
 */
public class RankingListFragment extends BaseFragment {
    @Bind(R.id.ranking_tab)
    TabLayout rankingTab;
    @Bind(R.id.ranking_vp)
    ViewPager rankingVp;
    @Bind(R.id.ranking_details)
    ImageView rankingDetails;
    private RequestQueue requestQueue;
    private RankingListData rankingListData;
    private Context baseContext;
    private LinearLayoutManager linearLayoutManager;
    private RankingListContentAdapter rankingListContentAdapter;
    private String msg;
    private int page;
    /**
     * 预初始化
     */
    @Override
    protected void init() {
        requestQueue = BaseApp.getRequestQueue();

        Bundle bundle=getArguments();
        page=bundle.getInt("page");

    }

    /**
     * 设置布局
     *
     * @return
     */
    @Override
    protected int getRootView() {
        return R.layout.fragment_rankinglist;
    }

    @Override
    protected void setListener() {

    }

    /**
     * 请求数据，设置UI
     *
     * @param view
     */
    @Override
    protected void initData(View view) {
        baseContext = view.getContext();

        EventBus.getDefault().register(this);

        //给排行榜添加滑动的fragement
        rankingListContentAdapter = new RankingListContentAdapter(getChildFragmentManager());
        rankingVp.setAdapter(rankingListContentAdapter);
        rankingTab.setupWithViewPager(rankingVp);
        rankingTab.setTabMode(TabLayout.MODE_FIXED);

        rankingVp.setCurrentItem(page);

    }

    @OnClick(R.id.ranking_details)
    public void onClick() {
        Toast.makeText(baseContext,msg,Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = org.greenrobot.eventbus.ThreadMode.MAIN)
    public void onEventMes(RankingListData data){
        msg=data.getData().getDesc();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}














