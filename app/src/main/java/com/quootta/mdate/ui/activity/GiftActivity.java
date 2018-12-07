package com.quootta.mdate.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.domain.UserDetail;
import com.quootta.mdate.ui.adapter.GiftListAdapter;
import com.quootta.mdate.ui.fragment.ReceiverGiftFragment;
import com.quootta.mdate.ui.fragment.SendGiftFragment;
import com.quootta.mdate.utils.ActivityUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Ryon on 2016/11/15/0015.
 */
public class GiftActivity extends BaseActivity {
    @Bind(R.id.gift_vp)
    ViewPager giftVp;
    @Bind(R.id.iv_back_title_bar)
    ImageView back;
    @Bind(R.id.tv_title_bar)
    TextView title_bar;
    @Bind(R.id.gift_tab)
    TabLayout gift_tab;

    private int page;
    private List<UserDetail> List_user;

    private SendGiftFragment sendGiftFragment;
    private ReceiverGiftFragment receiverGiftFragment;
    private List<Fragment> mFragmentList=new ArrayList<>();
    @Override
    protected void init() {


        Intent intent=getIntent();
        page=intent.getIntExtra("page",0);

        List_user=new ArrayList<>();
        List_user= (List<UserDetail>) intent.getSerializableExtra("Userlist");


    }

    @Override
    protected int getRootView() {
        return R.layout.activity_gift;
    }

    @Override
    protected void initData() {

        receiverGiftFragment=new ReceiverGiftFragment();
        sendGiftFragment=new SendGiftFragment();

        //传递给fragment
        if (List_user!=null && List_user.size()!=0) {

            Bundle bundle=new Bundle();
            bundle.putSerializable("sendgift", (Serializable) List_user);
            sendGiftFragment.setArguments(bundle);
            receiverGiftFragment.setArguments(bundle);
        }
        mFragmentList.add(receiverGiftFragment);
        mFragmentList.add(sendGiftFragment);




        back.setVisibility(View.VISIBLE);

        title_bar.setText(getString(R.string.app_tips_text33));


        //礼物fragment适配器
        GiftListAdapter giftlist=new GiftListAdapter(getSupportFragmentManager(),mFragmentList);
        giftVp.setAdapter(giftlist);
        gift_tab.setupWithViewPager(giftVp);
        gift_tab.setTabMode(TabLayout.MODE_FIXED);

        giftVp.setCurrentItem(page);
    }

    @Override
    protected void setListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.finishActivty();
            }
        });

    }


}
