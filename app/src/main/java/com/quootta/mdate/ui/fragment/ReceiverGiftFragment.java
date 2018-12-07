package com.quootta.mdate.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.base.BaseFragment;
import com.quootta.mdate.domain.Gift_List;
import com.quootta.mdate.domain.UserDetail;
import com.quootta.mdate.engine.gift.GiftReceive;
import com.quootta.mdate.ui.adapter.RvOnSelfGiftList;
import com.quootta.mdate.ui.adapter.RvReceiverGiftAdapter;
import com.quootta.mdate.utils.GsonUtil;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Ryon on 2016/11/15/0015.
 */
public class ReceiverGiftFragment extends BaseFragment {
    @Bind(R.id.receiver_gift_recy)
    RecyclerView receiverGiftRecy;

    private List<UserDetail> userDetails=new ArrayList<>();
    private RequestQueue requestQueue;
    private Bundle bundle;
    private UserDetail userDetail;
    @Override
    protected int getRootView() {
        return R.layout.fragment_receiver_gift;
    }



    @Override
    protected void init() {
        requestQueue= BaseApp.getRequestQueue();

        bundle=this.getArguments();

    }


    @Override
    protected void initData(View view) {

        if (bundle!=null){
            //获取他人的礼物列表
            userDetails= (List<UserDetail>) bundle.getSerializable("sendgift");


            GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),3);
            gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            receiverGiftRecy.setLayoutManager(gridLayoutManager);
            RvReceiverGiftAdapter rvSendGiftAdapter=new RvReceiverGiftAdapter(getContext(),userDetails);
            receiverGiftRecy.setAdapter(rvSendGiftAdapter);

        }else {
            ongetGiftList();
        }

    }


    //获取本人的礼物列表
    private void ongetGiftList(){


        GiftReceive giftReceive=new GiftReceive(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("tag","receivergiftfragment--->"+response);

                   Gift_List giftList = GsonUtil.parse(response, Gift_List.class);


                if (giftList!=null){
                    //添加适配器

                    GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),3);
                    gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    receiverGiftRecy.setLayoutManager(gridLayoutManager);

                    RvOnSelfGiftList onselfAdapter=new RvOnSelfGiftList(getContext(),giftList);
                    receiverGiftRecy.setAdapter(onselfAdapter);
                }

            }
        });
        requestQueue.add(giftReceive);
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}