package com.quootta.mdate.ui.fragment;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseFragment;
import com.quootta.mdate.ui.adapter.MyPagerAdapter;
import com.quootta.mdate.ui.view.MyViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


public class ChatsFragment extends BaseFragment {

    @Bind(R.id.rg_chats) RadioGroup rg_chats;

    @Bind(R.id.rb_chat_chats_fragment) RadioButton rb_chat_chats;

    @Bind(R.id.rb_invite_me_chats_fragment) RadioButton rb_invite_me_chats;

    @Bind(R.id.rb_my_invite_chats_fragment) RadioButton rb_my_invite_chats;

    @Bind(R.id.vp_chats_fragment) MyViewPager vp_chats;

    private Context baseContext;
    public ChatsListFragment chatsListFragment;
    public InviteMeFragment inviteMeFragment;
    public MyInviteFragment myInviteFragment;
    private MyPagerAdapter myPagerAdapter;
    private List<BaseFragment> chats_fragments;
    private FragmentManager fm;


    /**
     * 加载UI前的预初始化
     */
    @Override
    protected void init() {

    }

    /**
     * 设置布局
     *
     * @return
     */
    @Override
    protected int getRootView() {
        return R.layout.fragment_chats;
    }


    /**
     * 请求数据，设置UI
     *
     * @param view
     */
    @Override
    protected void initData(View view) {
        baseContext = view.getContext();
        initViewPager(view);
        initTab(view);
    }

    /**
     * 初始化Tab栏
     * @param view
     */
    private void initTab(View view) {

    }

    private void initViewPager(View view) {
        vp_chats.setNoScroll(false);
        chats_fragments = new ArrayList<>();
        chatsListFragment = new ChatsListFragment();
        inviteMeFragment = new InviteMeFragment();
        myInviteFragment = new MyInviteFragment();

        chats_fragments.add(0, chatsListFragment);
        chats_fragments.add(1, inviteMeFragment);
        chats_fragments.add(2, myInviteFragment);

        vp_chats.setOffscreenPageLimit(3);
        vp_chats.setAdapter(new MyPagerAdapter(getChildFragmentManager(), chats_fragments));

    }

    /**
     * 设置监听
     */
    @Override
    protected void setListener() {
        rg_chats.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int position = -1;
                switch (checkedId) {
                    case R.id.rb_chat_chats_fragment:
                        position = 0;
                        break;

                    case R.id.rb_invite_me_chats_fragment:
                        position = 1;
                        break;

                    case R.id.rb_my_invite_chats_fragment:
                        position = 2;
                        break;

                    default:
                        break;
                }
                vp_chats.setCurrentItem(position, false);
            }
        });

        vp_chats.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        rb_chat_chats.setChecked(true);
                        break;
                    case 1:
                        rb_invite_me_chats.setChecked(true);
                        break;
                    case 2:
                        rb_my_invite_chats.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}
