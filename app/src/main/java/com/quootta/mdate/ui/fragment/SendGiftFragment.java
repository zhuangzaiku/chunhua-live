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
import com.quootta.mdate.engine.gift.GiftListSend;
import com.quootta.mdate.ui.adapter.RvOnSelfGiftList;
import com.quootta.mdate.ui.adapter.RvSendGiftAdapter;
import com.quootta.mdate.utils.GsonUtil;
import com.quootta.mdate.utils.LogUtil;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Ryon on 2016/11/15/0015.
 */
public class SendGiftFragment extends BaseFragment {
    @Bind(R.id.send_gift_recy)
    RecyclerView sendGiftRecy;

    private  RequestQueue requestQueue;
    private List<UserDetail> userDetails=new ArrayList<>();
    private Bundle bundle;
    private UserDetail userDetail;
    @Override
    protected int getRootView() {
        return R.layout.fragment_send_gift;
    }

    @Override
    protected void init() {
        requestQueue= BaseApp.getRequestQueue();

        //接收传递过来的礼物信息
         bundle=this.getArguments();

    }


    @Override
    protected void initData(View view) {

        if (bundle!=null){
            //获取他人的礼物列表
            userDetails= (List<UserDetail>) bundle.getSerializable("sendgift");
            Log.i("tag","sendfragment接收到的只---》"+userDetails.get(0).gift_send.size());

            GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),3);
            gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            sendGiftRecy.setLayoutManager(gridLayoutManager);
            RvSendGiftAdapter rvSendGiftAdapter=new RvSendGiftAdapter(getContext(),userDetails);
            sendGiftRecy.setAdapter(rvSendGiftAdapter);

        }else {
            ongetGiftList();
        }

    }

    @Override
    protected void setListener() {

    }


    //获取自己的礼物列表
    private void ongetGiftList(){
        GiftListSend giftListSend=new GiftListSend(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtil.d("tag","sendgiftFragemtn-------->"+response);
                Gift_List giftList = GsonUtil.parse(response, Gift_List.class);


                if (giftList!=null){
                    //添加适配器

                   GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),3);
                    gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    sendGiftRecy.setLayoutManager(gridLayoutManager);

                    RvOnSelfGiftList onselfAdapter=new RvOnSelfGiftList(getContext(),giftList);
                    sendGiftRecy.setAdapter(onselfAdapter);
                }

            }
        });

        requestQueue.add(giftListSend);
    }


}
