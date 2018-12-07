package com.quootta.mdate.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseApp;

import java.util.List;

/**
 * Created by Ryon on 2016/11/15/0015.
 */
public class GiftListAdapter extends FragmentPagerAdapter {
    private int GIFT_SIZE=2;
    private String[] tableTitle=new String[]{BaseApp.getApplication().getString(R.string.app_tips_text95),BaseApp.getApplication().getString(R.string.app_tips_text96)};
    private List<Fragment> fragmentList;

    public GiftListAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);

        this.fragmentList=fragmentList;

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tableTitle[position];
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
