package com.quootta.mdate.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseApp;

import java.util.List;

/**
 * Created by Ryon on 2016/12/16/0016.
 */

public class FmFriendAdapter extends FragmentPagerAdapter {
    private String[] tableTitle=new String[]{BaseApp.getApplication().getString(R.string.app_tips_text93),BaseApp.getApplication().getString(R.string.app_tips_text94)};
    private List<Fragment> fragmentList;
    public FmFriendAdapter(FragmentManager fm, List<Fragment> fragmentList) {
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
