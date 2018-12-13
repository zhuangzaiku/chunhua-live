package com.quootta.mdate.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.domain.UserDetail;
import com.quootta.mdate.engine.invite.UserDetailRequest;
import com.quootta.mdate.engine.media.BalanceRequest;
import com.quootta.mdate.engine.myCenter.BlackListRequest;
import com.quootta.mdate.engine.myCenter.FavoriteRequest;
import com.quootta.mdate.myInterface.OnItemSelectedListener;
import com.quootta.mdate.myListener.RongCallListener;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.ui.adapter.AlbumPagerAdapter;
import com.quootta.mdate.ui.adapter.RvGalleryAdapter;
import com.quootta.mdate.ui.dialog.MyAlertDialog;
import com.quootta.mdate.ui.dialog.MyProgressDialog;
import com.quootta.mdate.utils.ActivityUtil;
import com.quootta.mdate.utils.GsonUtil;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import io.rong.callkit.BaseCallActivity;
import io.rong.callkit.RongCallAction;
import io.rong.callkit.RongCallKit;
import io.rong.callkit.RongVoIPIntent;
import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallSession;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;


public class PersonalDetailsActivity extends BaseActivity implements ViewPager.OnPageChangeListener {


    @Bind(R.id.tv_title_bar)
    TextView tv_title_bar;
    @Bind(R.id.tv_status_title_bar)
    TextView tv_status;
    @Bind(R.id.tv_right_title_bar)
    TextView iv_more;
    @Bind(R.id.ll_personal_details_activity)
    LinearLayout ll_body;
    @Bind(R.id.tv_age_personal_details_activity)
    TextView tv_age;
    @Bind(R.id.tv_constellation_personal_details_activity)
    TextView tv_constellation;
    //    @Bind(R.id.tv_distance_personal_details_activity) TextView tv_distance;
    @Bind(R.id.tv_personal_sign_personal_details_activity)
    TextView tv_sign;
    @Bind(R.id.iv_sesame_credit_personal_details)
    ImageView ivSesame;
    @Bind(R.id.iv_camera_credit_personal_details)
    ImageView ivCamera;
    @Bind(R.id.tv_video_price)
    TextView tvVideoPrice;
    @Bind(R.id.tv_audio_price)
    TextView tvAudioPrice;
    @Bind(R.id.tv_connection_rate)
    TextView tvConn;
    @Bind(R.id.tv_height_personal_details_activity)
    TextView tv_height;
    @Bind(R.id.tv_weight_personal_details_activity)
    TextView tv_weight;
    @Bind(R.id.tv_job_personal_details_activity)
    TextView tv_job;
    @Bind(R.id.tv_liked_date_personal_details_activity)
    TextView tv_liked_date;
    @Bind(R.id.tv_liked_sex_personal_details_activity)
    TextView tv_liked_opposite_sex;
    @Bind(R.id.tv_expertise_personal_details_activity)
    TextView tvExpertise;
    @Bind(R.id.tv_chat_personal_details_activity)
    TextView tvLikedChat;
    @Bind(R.id.vp_detail_personal_details_activity)
    ViewPager vp_detail;
    @Bind(R.id.iv_back_title_bar)
    ImageView iv_back;
    @Bind(R.id.rv_gallery_personal_details_activity)
    RecyclerView rv_gallery;
    @Bind(R.id.ll_operate_personal_details_activity)
    RelativeLayout ll_operate;
    @Bind(R.id.btn_video_chat_personal_details_activity)
    ImageView btnVideoChat;
    @Bind(R.id.btn_voice_chat_personal_details_activity)
    ImageView btnVoiceChat;
    @Bind(R.id.btn_private_chat_personal_details_activity)
    ImageView btnPrivateChat;
    //    @Bind(R.id.tv_personal_sign_personal_details_activity)
//    TextView tvPersonalSignPersonalDetailsActivity;
    @Bind(R.id.receiver_pic_one)
    ImageView receiverPicOne;
    @Bind(R.id.receiver_pic_two)
    ImageView receiverPicTwo;
    @Bind(R.id.receiver_text_two)
    TextView receiverTextTwo;
    @Bind(R.id.receiver_pic_three)
    ImageView receiverPicThree;
    @Bind(R.id.receiver_text_three)
    TextView receiverTextThree;
    @Bind(R.id.receiver_pic_four)
    ImageView receiverPicFour;
    @Bind(R.id.receiver_text_four)
    TextView receiverTextFour;
    @Bind(R.id.send_pic_one)
    ImageView sendPicOne;
    @Bind(R.id.send_text_one)
    TextView sendTextOne;
    @Bind(R.id.send_pic_two)
    ImageView sendPicTwo;
    @Bind(R.id.send_text_two)
    TextView sendTextTwo;
    @Bind(R.id.send_pic_three)
    ImageView sendPicThree;
    @Bind(R.id.send_text_three)
    TextView sendTextThree;
    @Bind(R.id.send_pic_four)
    ImageView sendPicFour;
    @Bind(R.id.send_text_four)
    TextView sendTextFour;
    @Bind(R.id.send_gift)
    LinearLayout sendGift;
    @Bind(R.id.receive_gift)
    LinearLayout receivegift;
    @Bind(R.id.receiver_text_one)
    TextView receiverTextOne;


    @Bind(R.id.tvNickname)
    TextView tvNickname;
    @Bind(R.id.tvId)
    TextView tvId;
    @Bind(R.id.tvAge)
    TextView tvAge;
    @Bind(R.id.ivGender)
    ImageView ivGender;

    private final static int ADD = 0;
    private final static int REMOVE = 1;


    private RequestQueue requestQueue;
    private AlbumPagerAdapter albumPagerAdapter;
    private RvGalleryAdapter galleryAdapter;
    private String userId;
    private UserDetail userDetail;
    private ImageLoader imageLoader;
    private MyProgressDialog myProgressDialog;
    private Boolean isUnlock;
    private SharedPreferences preferences;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private List<UserDetail> ListUser;
    private static final  int BALANCE=999999999;
    @Override
    protected void init() {
        userId = getIntent().getExtras().getString("user_id");

        pref = baseContext.getSharedPreferences("login", BaseActivity.MODE_PRIVATE);

        preferences = baseContext.getSharedPreferences("iscall", 0);
        editor = preferences.edit();

        requestQueue = BaseApp.getRequestQueue();
        imageLoader = BaseApp.getImageLoader();


    }

    @Override
    protected int getRootView() {
        return R.layout.activity_personal_details;
    }

    @Override
    protected void initData() {
        Map<String, String> paramsMap = new HashMap<>();
        ListUser=new ArrayList<>();
        paramsMap.put("user_id", userId);
        detailRequest(paramsMap);

    }


    private void detailRequest(Map<String, String> paramsMap) {
        ll_body.setVisibility(View.GONE);
        myProgressDialog = new MyProgressDialog(baseContext);
        myProgressDialog.show();
        final UserDetailRequest userDetailRequest = new UserDetailRequest(paramsMap,
                new VolleyListener() {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        LogUtil.d("PersonalDetailsActivity", response.toString());
                        try {
                            userDetail = GsonUtil.parse(response.getString("data"), UserDetail.class);

                            ListUser.add(userDetail);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        initTitleBar();
                        initPicture();
                        initDetails();
                        inintGift();

                        ll_body.setVisibility(View.VISIBLE);
                        if (userId.equals(pref.getString("id", ""))) {
                            Log.e("gc", userId);
                            ll_operate.setVisibility(View.GONE);
                        }

                    }
                });
        requestQueue.add(userDetailRequest);
    }

    private void inintGift() {


        //显示收到的礼物
        if (userDetail.gift_receive.size() > 0) {

            if (userDetail.gift_receive.size() >= 1) {
                imageLoader.get(LocalUrl.getPicUrl(userDetail.gift_receive.get(0).getCover()),
                        imageLoader.getImageListener(receiverPicOne, R.mipmap.home_show_loading, R.mipmap.home_show_loading));
                receiverTextOne.setText("x"+userDetail.gift_receive.get(0).getCount());
                receiverTextOne.setBackgroundResource(R.mipmap.ic_gift_num_bg);

            }
            if (userDetail.gift_receive.size() >=2){
                imageLoader.get(
                        LocalUrl.getPicUrl(userDetail.gift_receive.get(1).getCover()),
                        imageLoader.getImageListener(receiverPicTwo, R.mipmap.test, R.mipmap.test));


                receiverTextTwo.setText("x"+userDetail.gift_receive.get(1).getCount());
                receiverTextTwo.setBackgroundResource(R.mipmap.ic_gift_num_bg);
            }

            if (userDetail.gift_receive.size() >= 3){
                imageLoader.get(LocalUrl.getPicUrl(userDetail.gift_receive.get(2).getCover()),
                        imageLoader.getImageListener(receiverPicThree, R.mipmap.home_show_loading, R.mipmap.home_show_loading));
                receiverTextThree.setText("x"+userDetail.gift_receive.get(2).getCount());
                receiverTextThree.setBackgroundResource(R.mipmap.ic_gift_num_bg);
            }

            if (userDetail.gift_receive.size() >= 4){
                imageLoader.get(LocalUrl.getPicUrl(userDetail.gift_receive.get(3).getCover()),
                        imageLoader.getImageListener(receiverPicFour, R.mipmap.home_show_loading, R.mipmap.home_show_loading));
                receiverTextFour.setText("x"+userDetail.gift_receive.get(3).getCount());
                receiverTextFour.setBackgroundResource(R.mipmap.ic_gift_num_bg);
            }
        }

        //送出的礼物

        if (userDetail.gift_send.size()>0){
            if (userDetail.gift_send.size()>=1){
                imageLoader.get(LocalUrl.getPicUrl(userDetail.gift_send.get(0).getCover()),
                        imageLoader.getImageListener(sendPicOne, R.mipmap.home_show_loading, R.mipmap.home_show_loading));
                sendTextOne.setText("x"+userDetail.gift_send.get(0).getCount());
                sendTextOne.setBackgroundResource(R.mipmap.ic_gift_num_bg);
            }

            if (userDetail.gift_send.size()>=2){
                imageLoader.get(LocalUrl.getPicUrl(userDetail.gift_send.get(1).getCover()),
                        imageLoader.getImageListener(sendPicTwo, R.mipmap.home_show_loading, R.mipmap.home_show_loading));
                sendTextTwo.setText("x"+userDetail.gift_send.get(1).getCount());
                sendTextTwo.setBackgroundResource(R.mipmap.ic_gift_num_bg);
            }

            if (userDetail.gift_send.size()>=3){
                imageLoader.get(LocalUrl.getPicUrl(userDetail.gift_send.get(2).getCover()),
                        imageLoader.getImageListener(sendPicThree, R.mipmap.home_show_loading, R.mipmap.home_show_loading));
                sendTextThree.setText("x"+userDetail.gift_send.get(2).getCount());
                sendTextThree.setBackgroundResource(R.mipmap.ic_gift_num_bg);
            }
            if (userDetail.gift_send.size()>=4){
                imageLoader.get(LocalUrl.getPicUrl(userDetail.gift_send.get(3).getCover()),
                        imageLoader.getImageListener(sendPicFour, R.mipmap.home_show_loading, R.mipmap.home_show_loading));
                sendTextFour.setText("x"+userDetail.gift_send.get(3).getCount());
                sendTextFour.setBackgroundResource(R.mipmap.ic_gift_num_bg);
            }
        }




    }


    private void initTitleBar() {
        if (userDetail.nick_name != null) {
            tv_title_bar.setText(userDetail.nick_name);
        } else {
            tv_title_bar.setText(getString(R.string.unknown));
        }

        if (Boolean.parseBoolean(userDetail.is_online)) {
            tv_status.setText(getString(R.string.online));
        } else {
            tv_status.setText(getString(R.string.offline));
        }
        iv_more.setVisibility(View.VISIBLE);
        iv_back.setVisibility(View.VISIBLE);
    }

    private void initPicture() {

        albumPagerAdapter = new AlbumPagerAdapter(baseContext, userDetail.album);
        vp_detail.setAdapter(albumPagerAdapter);
        vp_detail.addOnPageChangeListener(this);
        vp_detail.setCurrentItem(0);

        galleryAdapter = new RvGalleryAdapter(baseContext, userDetail.album,1);
        rv_gallery.setAdapter(galleryAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(baseContext,
                LinearLayoutManager.HORIZONTAL, false);
        rv_gallery.setLayoutManager(linearLayoutManager);

        galleryAdapter.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onSelected(Drawable drawable, int position) {
                vp_detail.setCurrentItem(position);
            }
        });
    }

    private void initDetails() {
        tvAge.setText(userDetail.age);
        tvNickname.setText(getResources().getString(R.string.nickname, userDetail.nick_name));
        tvId.setText(getResources().getString(R.string.id, userDetail._id));
        if(TextUtils.isEmpty(userDetail.gender)) {
            ivGender.setImageResource(R.mipmap.ic_female);
        } else {
            ivGender.setImageResource(userDetail.gender.equals("female") ? R.mipmap.ic_female : R.mipmap.ic_male);
        }
        tv_constellation.setText(userDetail.constellation);

//        double distance = Double.parseDouble(userDetail.distance);
//        if (distance == -1){
//            tv_distance.setText(getString(R.string.unknown));
//        }else if (distance > 1) {
//            tv_distance.setText(((int)distance) + baseContext.getString(R.string.km));
//        } else {
//            distance *= 1000;
//            tv_distance.setText(((int)distance) + baseContext.getString(R.string.m));
//        }

//        tv_distance.setText(userDetail.distance);

        tv_sign.setText(userDetail.personal_desc);
        if (Boolean.parseBoolean(userDetail.is_verified_zhima)) {
            ivSesame.setImageResource(R.mipmap.credit_sesame_personal);
        } else {
            ivSesame.setImageResource(R.mipmap.credit_sesame_personal_uncheck);
        }

        if (Boolean.parseBoolean(userDetail.is_verified_video)) {
            ivCamera.setImageResource(R.mipmap.camera_selected);
        } else {
            ivCamera.setImageResource(R.mipmap.camera);
        }

        tvVideoPrice.setText(userDetail.video_pay + getString(R.string.gold_per_min));
        tvAudioPrice.setText(userDetail.audio_pay + getString(R.string.gold_per_min));

        int conn = (int) Double.parseDouble(userDetail.connect_rate);
        tvConn.setText(conn + "%");

        if (!userDetail.height.equals("-1"))
            tv_height.setText(userDetail.height + "cm");
        if (!userDetail.weight.equals("-1"))
            tv_weight.setText(userDetail.weight + "kg");
        tv_job.setText(userDetail.job);
        tv_liked_opposite_sex.setText(userDetail.request);
        tv_liked_date.setText(userDetail.date_way);
        tvExpertise.setText(userDetail.expertise);
        tvLikedChat.setText(userDetail.chat);
        myProgressDialog.dismiss();
    }

    @Override
    protected void setListener() {

        sendGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SendIntent=new Intent(PersonalDetailsActivity.this,GiftActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("Userlist", (Serializable) ListUser);
                SendIntent.putExtras(bundle);
                SendIntent.putExtra("page",1);
                startActivity(SendIntent);
            }
        });

        receivegift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ReceiverIntent=new Intent(PersonalDetailsActivity.this,GiftActivity.class);

                Bundle bundle=new Bundle();
                bundle.putSerializable("Userlist", (Serializable) ListUser);
                ReceiverIntent.putExtras(bundle);
                ReceiverIntent.putExtra("page",0);
                startActivity(ReceiverIntent);
            }
        });

        btnVideoChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //判断是否可以拨打电话 默认可以
                if (preferences.getBoolean("close", true)) {

                    int num=2;
                    RefrechBalance(num);


                } else {

                    long time = preferences.getLong("time", 0);


                    //等待时长
                    if (System.currentTimeMillis() >= time + 90000) {
                        editor.putBoolean("close", true);
                        editor.apply();

                        BaseApp.setBalnace(BALANCE);
                    } else {
                        long surplusTime = (time + 90000 - System.currentTimeMillis()) / 1000;

//                        ToastUtil.showToast("您的网络不太稳定请" + surplusTime + "秒后重试");
                        ToastUtil.showToast(getString(R.string.app_tips_text4,surplusTime+""));
                    }
                }


            }
        });

        btnVoiceChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //判断是否可以拨打电话 默认可以
                if (preferences.getBoolean("close", true)) {
                    int num=1;
                    //刷新余额 然后进行通话判断
                    RefrechBalance(num);

            } else {
                long time = preferences.getLong("time", 0);


                //等待时长
                if (System.currentTimeMillis() >= time + 90000) {
                    editor.putBoolean("close", true);

                    editor.apply();

                    BaseApp.setBalnace(BALANCE);

                } else {
                    long surplusTime = (time + 90000 - System.currentTimeMillis()) / 1000;
//                    ToastUtil.showToast("您的网络不太稳定请" + surplusTime + "秒后重试");
                    ToastUtil.showToast(getString(R.string.app_tips_text4,surplusTime+""));
                }
            }

            }
        });

        btnPrivateChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动会话界面
                if (RongIM.getInstance() != null && userDetail!=null)
                    RongIM.getInstance().startPrivateChat(baseContext,
                            userDetail._id, userDetail.nick_name);
            }
        });

        iv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showItemsDialog();
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.finishActivty();
            }
        });
    }

    private void OnVideo() {
        //判断用户是否是特殊账号
        if (!BaseApp.getIsSpecial()) {

            if (Boolean.parseBoolean(userDetail.video_enable)) {
                final RongCallSession profile = RongCallClient.getInstance().getCallSession();
                if (profile != null && profile.getActiveTime() > 0) {
                    Intent resumeIntent = new Intent("io.rong.imkit.WINDOW_RESUME");
                    sendBroadcast(resumeIntent);
                } else {
                    MyAlertDialog myAlertDialog = new MyAlertDialog(baseContext, new MyAlertDialog.OnPositiveAlterListener() {
                        @Override
                        public void onAlter() {
                            if (BaseApp.getGoldCount() >= Integer.parseInt(userDetail.video_pay)) {
                                BaseCallActivity.setBaseCallListener(new RongCallListener(userDetail, PersonalDetailsActivity.this));//绑定通话监听

                                // BaseCallActivity.setBaseCallListener(new RongCallListener(userDetail,PersonalDetailsActivity.this));
//                                RongCallKit.startSingleCall(baseContext,
//                                        userDetail._id,
//                                        RongCallKit.CallMediaType.CALL_MEDIA_TYPE_VIDEO);
                                //拨打电话
                                if (profile != null && profile.getActiveTime() > 0) {
                                    Toast.makeText(baseContext, getString(io.rong.callkit.R.string.rc_voip_call_start_fail), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                                if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
                                    Toast.makeText(baseContext, getString(io.rong.callkit.R.string.rc_voip_call_network_error), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Intent intent = new Intent(RongVoIPIntent.RONG_INTENT_ACTION_VOIP_SINGLEVIDEO);
                                intent.putExtra("conversationType", Conversation.ConversationType.PRIVATE.getName().toLowerCase());
                                intent.putExtra("targetId", userDetail._id);
                                intent.putExtra("callAction", RongCallAction.ACTION_OUTGOING_CALL.getName());
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setPackage(getPackageName());
                                getApplicationContext().startActivity(intent);

                            } else {
                                ToastUtil.showToast(getString(R.string.app_tips_text43));
                                Intent intent = new Intent("com.quootta.mdate.ChargeActivity");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                    });
                    myAlertDialog.setTitle(getString(R.string.app_tips_text44));
//                    myAlertDialog.setMessage("该用户的视频接听价格是"
//                            + userDetail.video_pay + getString(R.string.gold_per_min)
//                            + "，确定要进行视频聊天吗？");
                    myAlertDialog.setMessage(getString(R.string.app_tips_text45,userDetail.video_pay + getString(R.string.gold_per_min)));
                    myAlertDialog.show();
                }
            } else {
                ToastUtil.showToast(getString(R.string.app_tips_text8));
            }
        }else {
            ToastUtil.showToast(getString(R.string.app_tips_text7));
        }


    }

    private void onVoiceChat() {

//判断用户是否是特殊账号
        if (!BaseApp.getIsSpecial()){

            if (Boolean.parseBoolean(userDetail.audio_enable)) {
                RongCallSession profile = RongCallClient.getInstance().getCallSession();
                if (profile != null && profile.getActiveTime() > 0) {
                    Intent resumeIntent = new Intent("io.rong.imkit.WINDOW_RESUME");
                    sendBroadcast(resumeIntent);
                } else {
                    MyAlertDialog myAlertDialog = new MyAlertDialog(baseContext, new MyAlertDialog.OnPositiveAlterListener() {
                        @Override
                        public void onAlter() {
                            if (BaseApp.getGoldCount() >= Integer.parseInt(userDetail.audio_pay)) {

                                BaseCallActivity.setBaseCallListener(new RongCallListener(userDetail, PersonalDetailsActivity.this));//绑定通话监听

                                RongCallKit.startSingleCall(baseContext,
                                        userDetail._id,
                                        RongCallKit.CallMediaType.CALL_MEDIA_TYPE_AUDIO);
                            } else {
                                ToastUtil.showToast(getString(R.string.app_tips_text43));
                                Intent intent = new Intent("com.quootta.mdate.ChargeActivity");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                    });
                    myAlertDialog.setTitle(getString(R.string.app_tips_text44));
                    myAlertDialog.setMessage(getString(R.string.app_tips_text163,userDetail.audio_pay + getString(R.string.gold_per_min)));
//                    myAlertDialog.setMessage("该用户的语音接听价格是"
//                            + userDetail.audio_pay + getString(R.string.gold_per_min)
//                            + "，确定要进行语音聊天吗？");
                    myAlertDialog.show();
                }
            } else {
                ToastUtil.showToast(getString(R.string.app_tips_text6));
            }


        }else {
            ToastUtil.showToast(getString(R.string.app_tips_text7));
        }

    }

    private void showItemsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(baseContext);

        String[] items;
        final int type;

        if (Boolean.parseBoolean(userDetail.is_favorite)) {
            items = getResources().getStringArray(R.array.more_btn_item2);
            type = REMOVE;
        } else {
            items = getResources().getStringArray(R.array.more_btn_item1);
            type = ADD;
        }

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0://拉黑
                        requestBlackList();
                        break;
                    case 1://举报
                        requestReport();
                        break;
                    case 2://关注相关
                        requestLike(type);
                        break;
                    default:
                        break;
                }
            }
        });
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }





    private void requestBlackList() {
        Map<String, String> listParams = new HashMap<String, String>();
        listParams.put("user_id", userId);
        BlackListRequest blackListRequest = new BlackListRequest(ADD, listParams, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                try {
                    ToastUtil.showToast(response.getString("msg"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        requestQueue.add(blackListRequest);
    }

    private void requestReport() {
        Intent reportIntent = new Intent(this, ReportActivity.class);
        reportIntent.putExtra("user_id", userId);
        startActivity(reportIntent);
    }

    private void requestLike(int type) {
        Map<String, String> likeParams = new HashMap<String, String>();
        likeParams.put("user_id", userId);
        FavoriteRequest favoriteRequest = new FavoriteRequest(likeParams, type, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                try {
                    ToastUtil.showToast(response.getString("msg"));
                    if (Boolean.parseBoolean(userDetail.is_favorite)) {
                        userDetail.is_favorite = "false";
                    } else {
                        userDetail.is_favorite = "true";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        requestQueue.add(favoriteRequest);
    }

//    private void requestUnlock() {
//        Map<String, String> unlockParamsMap = new HashMap<>();
//        unlockParamsMap.put("user_id", userId);
//        UnlockRequest unlockRequest = new UnlockRequest(unlockParamsMap, new VolleyListener() {
//            @Override
//            protected void onSuccess(JSONObject response) {
//                try {
//                    ToastUtil.showToast(response.getString("msg"));
//                    Intent freshIntent = new Intent("com.quootta.mdate.REFRESH_VIEW");
//                    freshIntent.putExtra("type", MainActivity.GOLD_AMOUNT);
//                    sendBroadcast(freshIntent);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            protected void onFail(JSONObject response) {
//                super.onFail(response);
//                LogUtil.d("PersonalDetailsActivity", "FailResponse: " + response);
//                try {
//                    if (response.getString("code").equals("2002")) {
//                        Intent chargeIntent = new Intent(PersonalDetailsActivity.this,
//                                ChargeActivity.class);
//                        startActivity(chargeIntent);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        requestQueue.add(unlockRequest);
//    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        rv_gallery.scrollToPosition(vp_detail.getCurrentItem());
    }


    //刷新余额
    private  void RefrechBalance(final int num){

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

                if (userDetail!=null){
                    if (num==1){
                        onVoiceChat();
                    }else if (num==2){
                        OnVideo();
                    }
                }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected void onFail(JSONObject response) {
                super.onFail(response);
                ToastUtil.showLongToast(getString(R.string.app_tips_text5));
            }
        });
        requestQueue.add(balanceRequest);

    }


}
