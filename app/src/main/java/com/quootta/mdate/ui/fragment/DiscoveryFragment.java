package com.quootta.mdate.ui.fragment;


import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseFragment;
import com.quootta.mdate.ui.adapter.MyPagerAdapter;
import com.quootta.mdate.ui.view.MyViewPager;
import com.quootta.mdate.utils.UIUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class DiscoveryFragment extends BaseFragment {


    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.view_pager)
    MyViewPager viewPager;

    private List<BaseFragment> main_fragments;
    private List<TextView> mTabTitles = new ArrayList<>();

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
//        main_fragments.add(2, sameCityFragment);
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
                getString(R.string.app_tips_text102)
        });
        viewPager.setNoScroll(false);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void initTabLayout() {
        TextView tv = new TextView(getContext());
        tv.setText(R.string.app_tips_text101);
        tv.setTextColor(ContextCompat.getColor(getContext(), R.color.myWhite));
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(18);
        tabLayout.addTab(tabLayout.newTab().setCustomView(tv), 0, true);
        mTabTitles.add(tv);
        TextView tv2 = new TextView(getContext());
        tv2.setTextColor(ContextCompat.getColor(getContext(), R.color.myWhite));
        tv2.setText(R.string.app_tips_text102);
        tv2.setGravity(Gravity.CENTER);
        mTabTitles.add(tv2);
        tabLayout.addTab(tabLayout.newTab().setCustomView(tv2), 1);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                View view = tab.getCustomView();
                if (null != view && view instanceof TextView) {
                    ((TextView) view).setTextSize(18);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // No-op
                View view = tab.getCustomView();
                if (null != view && view instanceof TextView) {
                    ((TextView) view).setTextSize(14);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // No-op
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
