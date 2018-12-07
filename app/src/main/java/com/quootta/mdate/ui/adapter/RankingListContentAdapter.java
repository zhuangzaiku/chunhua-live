package com.quootta.mdate.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.ui.fragment.GlamourListFragment;
import com.quootta.mdate.ui.fragment.MoneyListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryon on 2016/9/6/0006.
 * 排行榜详情适配
 */
public class RankingListContentAdapter extends FragmentPagerAdapter {
    private final int PAGE_COUNT=2;
    private String[] tableTitle=new String[]{BaseApp.getApplication().getString(R.string.app_tips_text97),BaseApp.getApplication().getString(R.string.app_tips_text98)};
    private List<Fragment> fragmentTab=new ArrayList<>();

    private MoneyListFragment moneyListFragment;
    private GlamourListFragment glamourListFragment;




    public RankingListContentAdapter(FragmentManager fm) {
        super(fm);
        initFragmentTab();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentTab.get(position);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tableTitle[position];
    }

    private void initFragmentTab(){
        glamourListFragment=new GlamourListFragment();
        moneyListFragment=new MoneyListFragment();
        fragmentTab.add(glamourListFragment);
        fragmentTab.add(moneyListFragment);
    }
}
