package com.quootta.mdate.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.domain.InviteDetail;
import com.quootta.mdate.engine.chat.InviteDetailRequest;
import com.quootta.mdate.engine.chat.InviteOperateRequest;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.utils.ActivityUtil;
import com.quootta.mdate.utils.GsonUtil;
import com.quootta.mdate.utils.LogUtil;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by Ryon on 2016/2/24.
 * email:para.ryon@foxmail.com
 */

public class InviteDetailActivity extends BaseActivity {

    @Bind(R.id.iv_back_title_bar) ImageView iv_back;
    @Bind(R.id.iv_head_invite_detail_activity) ImageView iv_head;
    @Bind(R.id.iv_gift_invite_detail_activity) ImageView iv_gift;
    @Bind(R.id.tv_title_bar) TextView iv_title;
    @Bind(R.id.iv_show_invite_detail_activity) ImageView iv_show;
    @Bind(R.id.tv_name_invite_detail_activity) TextView tv_name;
    @Bind(R.id.tv_measurements_invite_detail_activity) TextView tv_measurements;
    @Bind(R.id.tv_distance_invite_detail_activity) TextView tv_distance;
    @Bind(R.id.tv_msg_invite_detail_activity) TextView tv_msg;
//    @Bind(R.id.tv_gift_name_invite_detail_activity) TextView tv_gift_name;
    @Bind(R.id.tv_gift_gold_invite_detail_activity) TextView tv_gift_gold;
    @Bind(R.id.btn_withdraw_invite_detail_activity) Button btn_withdraw;
    @Bind(R.id.btn_accept_invite_detail_activity) Button btn_accept;
    @Bind(R.id.btn_refuse_invite_detail_activity) Button btn_refuse;
    @Bind(R.id.ll_invite_me_invite_detail_activity) LinearLayout ll_invite_me;
    @Bind(R.id.ll_my_invite_invite_detail_activity) LinearLayout ll_my_invite;

    private static final int INVITE_ME = 0;
    private static final int MY_INVITE = 1;


    private String inviteId;
    private int inviteType;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private InviteDetail inviteDetail;

    @Override
    protected void init() {
        inviteId = getIntent().getExtras().getString("invite_id");
        inviteType = getIntent().getExtras().getInt("invite_type");
        requestQueue = BaseApp.getRequestQueue();
        imageLoader = BaseApp.getImageLoader();
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_invite_detail;
    }

    @Override
    protected void initData() {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("invite_id", inviteId);
        requestInviteDetail(paramsMap);
    }

    private void initTitle(InviteDetail inviteDetail) {
        iv_title.setText(inviteDetail.user.nick_name);
        iv_back = (ImageView) findViewById(R.id.iv_back_title_bar);
        iv_back.setVisibility(View.VISIBLE);
    }

    private void requestInviteDetail(Map<String, String> paramsMap) {
        InviteDetailRequest inviteDetailRequest = new InviteDetailRequest(paramsMap,
                new VolleyListener() {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        try {
                            inviteDetail = GsonUtil.parse(response.getString("data"), InviteDetail.class);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        LogUtil.d("InviteDetailActivity", "response:" + response);
                        initTitle(inviteDetail);
                        initView(inviteDetail);

                    }
                });
        requestQueue.add(inviteDetailRequest);
    }

    private void initView(InviteDetail inviteDetail) {
        ll_my_invite.setVisibility(View.VISIBLE);
        switch (inviteDetail.status){
            case "undo":
                switch (inviteType) {
                    case INVITE_ME:
                        ll_invite_me.setVisibility(View.VISIBLE);
                        ll_my_invite.setVisibility(View.GONE);
                        break;
                    case MY_INVITE:
                        break;
                }
                break;
            case "accept"://已接受
                btn_withdraw.setClickable(false);
                btn_withdraw.setBackgroundResource(R.drawable.shape_button_unclickable);
                btn_withdraw.setText(getString(R.string.status_accept));
                break;
            case "reject"://已拒绝
                btn_withdraw.setClickable(false);
                btn_withdraw.setBackgroundResource(R.drawable.shape_button_unclickable);
                btn_withdraw.setText(getString(R.string.status_reject));
                break;
            case "cancel"://已撤回
                btn_withdraw.setClickable(false);
                btn_withdraw.setBackgroundResource(R.drawable.shape_button_unclickable);
                btn_withdraw.setText(getString(R.string.status_cancel));
                break;
        }
        imageLoader.get(
                LocalUrl.getPicUrl(inviteDetail.user.avatar),
                imageLoader.getImageListener(iv_head, R.mipmap.test, R.mipmap.test));
        if (inviteDetail.gift != null){
            imageLoader.get(
                    LocalUrl.getPicUrl(inviteDetail.gift.cover),
                    imageLoader.getImageListener(iv_gift, R.mipmap.test, R.mipmap.test));
//            tv_gift_name.setText(inviteDetail.gift.name);
            tv_gift_gold.setText(inviteDetail.gift.cost+getString(R.string.app_tips_text35));
        }
        imageLoader.get(
                LocalUrl.getPicUrl(inviteDetail.user.cover_img),
                imageLoader.getImageListener(iv_show, R.mipmap.test, R.mipmap.test));

        tv_name.setText(inviteDetail.user.nick_name);
        tv_measurements.setText(inviteDetail.user.age+getString(R.string.app_tips_text36)+inviteDetail.user.height+ "cm/" + inviteDetail.user.weight+"kg");
        tv_distance.setText(inviteDetail.user.city);
        tv_msg.setText(inviteDetail.date_msg);
    }

    @Override
    protected void setListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.finishActivty();
            }
        });

        iv_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InviteDetailActivity.this, PersonalDetailsActivity.class);
                intent.putExtra("user_id",inviteDetail.user._id);
                startActivity(intent);
            }
        });

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String ,String > paramsMap = new HashMap<String, String>();
                paramsMap.put("type", "accept");
                requestInviteOperate(paramsMap);
            }
        });

        btn_refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String ,String > paramsMap = new HashMap<String, String>();
                paramsMap.put("type", "reject");
                requestInviteOperate(paramsMap);
            }
        });

        btn_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String ,String > paramsMap = new HashMap<String, String>();
                paramsMap.put("type", "cancel");
                requestInviteOperate(paramsMap);
            }
        });
    }

    private void requestInviteOperate(final Map<String, String> paramsMap) {
        paramsMap.put("invite_id", inviteId);
        InviteOperateRequest inviteOperateRequest = new InviteOperateRequest(paramsMap,
                new VolleyListener() {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        try {
                            LogUtil.d("InviteDetailActivity","response:"+response);
                            Intent freshIntent = new Intent("com.quootta.mdate.REFRESH_VIEW");
                            if(paramsMap.get("type").equals("accept")|| paramsMap.get("type").equals("reject")){
                                freshIntent.putExtra("type",0);
                            } else {
                                freshIntent.putExtra("type",1);
                            }
                            LogUtil.d("InviteDetailActivity","sendBroadcast!");
                            sendBroadcast(freshIntent);
                            finish();
                            Toast.makeText(baseContext,response.getString("msg"),Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
        requestQueue.add(inviteOperateRequest);
    }
}
