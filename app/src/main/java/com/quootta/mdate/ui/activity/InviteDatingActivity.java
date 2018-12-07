package com.quootta.mdate.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.domain.GiftList;
import com.quootta.mdate.engine.invite.GiftListRequest;
import com.quootta.mdate.engine.invite.InviteRequest;
import com.quootta.mdate.myInterface.OnItemSelectedListener;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.ui.adapter.RvGiftGridAdapter;
import com.quootta.mdate.ui.view.FullyGridLayoutManager;
import com.quootta.mdate.utils.ActivityUtil;
import com.quootta.mdate.utils.GsonUtil;
import com.quootta.mdate.utils.LogUtil;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;


public class InviteDatingActivity extends BaseActivity {

    @Bind(R.id.rv_gift_list_invite_dating_activity) RecyclerView rv_gift;
    @Bind(R.id.tv_title_bar) TextView tv_title;
    @Bind(R.id.iv_back_title_bar) ImageView iv_back;
    @Bind(R.id.iv_current_gift_invite_dating_activity) ImageView iv_gift;
    @Bind(R.id.tv_gold_invite_dating_activity) TextView tv_gift_cost;

    @Bind(R.id.et_invite_dating_activity) EditText et_invite;
    @Bind(R.id.btn_invite_dating_activity) Button btn_invite;

    public final static int MY_INVITE = 1;
    public final static int GOLD_AMOUNT = 2;

    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private String userId;
    private GiftList giftList;
    private RvGiftGridAdapter rvGiftGridAdapter;

    @Override
    protected void init() {
        userId = getIntent().getExtras().getString("userId");
        requestQueue = BaseApp.getRequestQueue();
        imageLoader = BaseApp.getImageLoader();
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_invite_dating;
    }

    @Override
    protected void initData() {
        initTitleBar();
        giftRequest();
    }

    private void initTitleBar() {
        tv_title.setText(R.string.start_invite_title);
        iv_back.setVisibility(View.VISIBLE);
    }

    private void giftRequest() {

        Map<String,String> map=new HashMap<>();
        GiftListRequest giftListRequest = new GiftListRequest(map,new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                LogUtil.d("InviteDatingActivity", "onResponse:" + response.toString());
                try {
                    giftList = GsonUtil.parse(response.getString("data"),GiftList.class);
                    imageLoader.get(
                            LocalUrl.getPicUrl(giftList.gifts.get(0).cover),
                            imageLoader.getImageListener(iv_gift, R.mipmap.test, R.mipmap.test));
                    tv_gift_cost.setText(giftList.gifts.get(0).cost + baseContext.getString(R.string.coin));
                    giftList.currentGift = 0;
                    giftList.gifts.get(0).select();
                    initRecycleView(giftList.gifts);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        requestQueue.add(giftListRequest);
    }

    private void initRecycleView(List<GiftList.gifts> gifts) {
        if(gifts==null){
            LogUtil.d("InviteDatingActivity", "Gift is null");
        } else {
            LogUtil.d("InviteDatingActivity", "Gift is not null:"+gifts.get(0).isSelected+gifts.get(0).cost);
        }

        rvGiftGridAdapter = new RvGiftGridAdapter(baseContext,giftList);
        rv_gift.setAdapter(rvGiftGridAdapter);

//        rv_gift.setLayoutManager(new GridLayoutManager(baseContext, 3));
        rv_gift.setLayoutManager(new FullyGridLayoutManager(baseContext, 3));


        rvGiftGridAdapter.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onSelected(Drawable drawable,int position) {
                iv_gift.setImageDrawable(drawable);
                tv_gift_cost.setText(giftList.gifts.get(position).cost + baseContext.getString(R.string.coin));

            }
        });
    }

    @Override
    protected void setListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.finishActivty();
            }
        });

        btn_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(giftList.currentGift == -1){
                    Toast.makeText(baseContext,getString(R.string.app_tips_text34),Toast.LENGTH_SHORT).show();
                }else {
                    inviteRequest();
                }

            }
        });
    }

    private void inviteRequest() {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("user_id",userId);
        if(et_invite.getText().toString().trim().equals("")) {
            paramsMap.put("date_msg", getString(R.string.hint_invite));
        }else {
            paramsMap.put("date_msg",et_invite.getText().toString().trim());
        }
        paramsMap.put("gift_id",giftList.gifts.get(giftList.currentGift)._id);
        paramsMap.put("num","1");

        InviteRequest inviteRequest = new InviteRequest(paramsMap,
                new VolleyListener() {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        LogUtil.d("InviteDatingActivity", "response:"+response.toString());
                        try {
                            LogUtil.d("InviteDatingActivity", "code:"+response.getString("code"));
                            Toast.makeText(baseContext,response.getString("msg"),Toast.LENGTH_SHORT).show();
                            Intent freshIntent = new Intent("com.quootta.mdate.REFRESH_VIEW");
                            freshIntent.putExtra("type", MY_INVITE);
                            sendBroadcast(freshIntent);
                            freshIntent.putExtra("type", GOLD_AMOUNT);
                            sendBroadcast(freshIntent);
                            ActivityUtil.finishActivty();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFail(JSONObject response) {
                        super.onFail(response);
                        LogUtil.d("InviteDatingActivity", "onFail response:" + response.toString());
                        try {
                            if (response.getString("code").equals("2002")) {
                                Intent chargeIntent = new Intent(InviteDatingActivity.this, ChargeActivity.class);
                                startActivity(chargeIntent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        requestQueue.add(inviteRequest);
    }
}
