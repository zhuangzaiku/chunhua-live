package com.quootta.mdate.ui.fragment;


import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.view.View;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseFragment;
import com.quootta.mdate.ui.adapter.MyPagerAdapter;
import com.quootta.mdate.ui.view.MyViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class DiscoveryFragment extends BaseFragment {


    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.view_pager)
    MyViewPager viewPager;

    private List<BaseFragment> main_fragments;

    @Override
    protected int getRootView() {
        return R.layout.fragment_discovery;
    }

    @Override
    protected void setListener() {

    }


    @Override
    protected void init() {
        initFragments();
    }

    private void initFragments() {
        HotListFragment hotListFragment = new HotListFragment();
        SameCityFragment sameCityFragment = new SameCityFragment();
        NewestFragment newestFragment = new NewestFragment();
        main_fragments = new ArrayList<>();
        main_fragments.add(0, hotListFragment);
        main_fragments.add(1, newestFragment);
        main_fragments.add(2, sameCityFragment);
    }

    @Override
    protected void initData(View view) {
        initViewPager();
        initTabLayout();

    }

    private void initViewPager() {
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getChildFragmentManager(), main_fragments);
        pagerAdapter.setTabTitle(new String[] {
               getString(R.string.app_tips_text101),
                getString(R.string.app_tips_text102),
                getString(R.string.app_tips_text103)
        });
        viewPager.setNoScroll(false);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(pagerAdapter);
    }

    private void initTabLayout() {
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
