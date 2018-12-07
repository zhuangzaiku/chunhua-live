package com.quootta.mdate.ui.popupWindow;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.domain.GiftListData;
import com.quootta.mdate.engine.gift.GiftSendRequest;
import com.quootta.mdate.engine.invite.GiftListRequest;
import com.quootta.mdate.engine.media.BalanceRequest;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.ui.activity.ChargeActivity;
import com.quootta.mdate.ui.adapter.RvGiftListAdapter;
import com.quootta.mdate.ui.message.RongGiftMessage;
import com.quootta.mdate.utils.GsonUtil;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by Ryon on 2016/11/10/0010.
 */
public class GiftPopuWindow extends Activity {

    @Bind(R.id.gift_recycler)
    RecyclerView giftRecycler;
//    @Bind(R.id.gold_recharge)
//    TextView goldRecharge;
//    @Bind(R.id.gold_limit)
//    TextView goldLimit;
//    @Bind(R.id.test)
//    TextView test;
    @Bind(R.id.give_gift)
    Button giveGift;



    private GiftListData giftList;

    private String uid;
    private String charm;
    private String cover;
    private int cost;
    private boolean isClick;
    private String gift_id;
    private RequestQueue requestQueue;
    private static final  int BALANCE=999999999;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String giftName;
    private boolean isnormal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_pop_gift);
        ButterKnife.bind(this);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        requestQueue = BaseApp.getRequestQueue();

        preferences =GiftPopuWindow.this.getSharedPreferences("iscall", 0);
        editor = preferences.edit();



        //获取用户id
        Intent intent=getIntent();
        uid=intent.getStringExtra("uid");

        //发送礼物按钮设为不可用
        giveGift.setEnabled(false);
        //刷新余额 成功 发送按钮设为可用
        RefrechBalance();


        LogUtil.i("tag","余额为-oncr--》"+BaseApp.getGoldCount());
        LogUtil.i("tag","banlance -oncr--->扣除通话时间后剩下的金钱---》》"+BaseApp.getBalnace());

//        test.setText("真实余额"+BaseApp.getGoldCount());
//
//        //如果balnace不等于99999999 说明是在通话中  使用balnace的值显示余额
//        if (BaseApp.getBalnace()!=BALANCE){
//            //金币余额  settext必须是string类型的
//            goldLimit.setText(BaseApp.getBalnace()+"");
//        }else {
//            goldLimit.setText(BaseApp.getGoldCount()+"");
//        }


        isClick=false;

        final Map<String, String> giftListMap = new HashMap<>();
        giftListMap.put("type", "chatlive");
        //获取礼物列表
        GiftListRequest giftListRequest = new GiftListRequest(giftListMap, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {

                giftList = GsonUtil.parse(response, GiftListData.class);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(GiftPopuWindow.this,4);

                giftRecycler.setLayoutManager(gridLayoutManager);

                giftRecycler.setAdapter(new RvGiftListAdapter(GiftPopuWindow.this, giftList, new RvGiftListAdapter.onGetGiftPosition() {
                    @Override
                    public void onGiftPosition(int position) {

                        charm= giftList.getData().getGifts().get(position).getCharm()+"";
                        cost=giftList.getData().getGifts().get(position).getCost();
                        cover=giftList.getData().getGifts().get(position).getCover();
                        giftName=giftList.getData().getGifts().get(position).getName();

                        gift_id=giftList.getData().getGifts().get(position).get_id();

                        //判断是否可以单击送出礼物
                        isClick=true;


                    }
                }));


            }
        });
        requestQueue.add(giftListRequest);



    }


    @Override
    protected void onStart() {
        super.onStart();

        giveGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //判断是否正常退出
                if (preferences.getBoolean("close",true)){
                    isnormal=true;

                }else{
                    //判断是否在通话过程中
                    if (preferences.getBoolean("isnormal",true)){
                        Log.i("tag","在通话过程中 还没有退出");
                        //在通话过程中
                        isnormal=true;
                    }else {
                        //没有在通话过程中
                        Log.i("tag","没有在通话过程中 且错误退出");
                        isnormal=false;
                    }
                }



                    //判断是否特殊用户
                    if (!BaseApp.getIsSpecial()){

                        //判断是否选择了一个礼物
                        if (isClick){

                            LogUtil.i("tag","余额为--click-》"+BaseApp.getGoldCount());
                            //判断是否正常挂断
                            if (isnormal) {
                                //判断余额是否足够支付礼物
                                if (BaseApp.getGoldCount() >= cost) {


                                    //剩余的金币是否足够支付
                                    if (BaseApp.getBalnace() >= cost) {
                                        if (BaseApp.getBalnace() != BALANCE) {

                                            BaseApp.setBalnace(BaseApp.getBalnace() - cost);

                                            LogUtil.i("tag", "banlance --click--单击后余额不为999999--->扣除通话时间后剩下的金钱---》》" + BaseApp.getBalnace());
                                        }

                                        LogUtil.i("tag", "banlance --click-->扣除通话时间后剩下的金钱--->>" + BaseApp.getBalnace());
                                        //赠送按钮设为不可用
                                        giveGift.setEnabled(false);
                                        //给服务器发送赠送礼物请求
                                        sendMessage(requestQueue);
                                    } else {
                                        ToastUtil.showLongToast(getString(R.string.app_tips_text115));
                                    }
                                } else {
                                    ToastUtil.showLongToast(getString(R.string.app_tips_text116));

                                    Intent chargeIntent=new Intent(GiftPopuWindow.this, ChargeActivity.class);
                                    GiftPopuWindow.this.startActivity(chargeIntent);
                                    GiftPopuWindow.this.finish();

                                }

                            }else {
                                long time=preferences.getLong("time",0);

                                //等待时长
                                if(System.currentTimeMillis()>=time+90000){
                                    editor.putBoolean("close",true);
                                    editor.apply();
                                    BaseApp.setBalnace(BALANCE);
                                    isnormal=true;
                                }else {
                                    long surplusTime= (time+90000-System.currentTimeMillis())/1000;
                                    ToastUtil.showToast(getString(R.string.app_tips_text4,surplusTime+""));
                                }
                            }

                        }else {
                            ToastUtil.showLongToast(getString(R.string.app_tips_text117));
                        }


                    }else {
                        ToastUtil.showToast(getString(R.string.app_tips_text118));
                    }

            }
        });


//        goldRecharge.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(GiftPopuWindow.this, ChargeActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });

    }


    private void sendMessage(RequestQueue requestQueue) {
        Map<String,String> giftSendMap=new HashMap<String, String>();
        Log.i("tag","gift--message---->"+gift_id+"user_id------>"+uid);
        giftSendMap.put("user_id",uid);
        giftSendMap.put("gift_id",gift_id);
        giftSendMap.put("num","1");
        giftSendMap.put("msg","暂不发送留言");

        //发送礼物
        GiftSendRequest giftSendRequest=new GiftSendRequest(giftSendMap, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //发送礼物消息
                sendGiftMessage();
                LogUtil.i("tag","礼物送出 价值----》"+cost);
            }
        });


        requestQueue.add(giftSendRequest);
    }

    //发送礼物消息
    private void sendGiftMessage() {
        RongGiftMessage rongGiftMessage= RongGiftMessage.obtain(giftName,charm,cost+"",cover,getString(R.string.app_tips_text119));

        RongIM.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, uid, rongGiftMessage, null, null, new RongIMClient.SendMessageCallback() {
            @Override
            public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {


            }

            @Override
            public void onSuccess(Integer integer) {

                ToastUtil.showToast(getString(R.string.app_tips_text120));

                //刷新金币余额
                RefrechBalance();
               // GetBalance.Balance(); 刷新余额的外部方法

            }
        });

        isClick=false;
        GiftPopuWindow.this.finish();
    }


    //实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    //刷新余额
    private  void RefrechBalance(){

        RequestQueue requestQueue;
        requestQueue= BaseApp.getRequestQueue();


        Map<String, String> balanceMap = new HashMap<String, String>();
        BalanceRequest balanceRequest = new BalanceRequest(balanceMap, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {

                try {
                    JSONObject data = response.getJSONObject("data");
                    String balance = data.getString("balance");
                    // LogUtil.d("requestBalance", balance);
                    BaseApp.setGoldCount(balance);

                    LogUtil.i("tag","余额刷新成功-------->>>"+BaseApp.getGoldCount());
                    giveGift.setEnabled(true);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected void onFail(JSONObject response) {
                super.onFail(response);

                ToastUtil.showLongToast(getString(R.string.app_tips_text54));
            }
        });
        requestQueue.add(balanceRequest);

    }


}
