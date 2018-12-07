package com.quootta.mdate.ui.adapter;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.quootta.mdate.base.BaseFragment;

import java.util.List;

/**
 * Created by Ryon on 2016/2/22.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {

    private List<BaseFragment> fragment;
    private String[] tabTitle;

    public MyPagerAdapter(FragmentManager fm, List fragmetList) {
        super(fm);
        this.fragment = fragmetList;
    }

    public void setTabTitle(String[] tabTitle) {
        this.tabTitle = tabTitle;
    }

    @Override
    public Fragment getItem(int position) {
        return fragment.get(position);
    }

    @Override
    public int getCount() {
        return fragment.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (tabTitle == null) {
            return super.getPageTitle(position);
        } else {
            return tabTitle[position];
        }
    }
}
