package com.quootta.mdate.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.base.BaseFragment;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.domain.AlbumList;
import com.quootta.mdate.domain.ChargeStandard;
import com.quootta.mdate.domain.InfoDetail;
import com.quootta.mdate.domain.InfoDetailNonStatic;
import com.quootta.mdate.engine.media.BalanceRequest;
import com.quootta.mdate.engine.myCenter.AnswerStatusRequest;
import com.quootta.mdate.engine.myCenter.InfoDetailRequest;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.ui.activity.BannerActivity;
import com.quootta.mdate.ui.activity.ChargeActivity;
import com.quootta.mdate.ui.activity.ChargeStandardActivity;
import com.quootta.mdate.ui.activity.FriendsListActivity;
import com.quootta.mdate.ui.activity.GiftActivity;
import com.quootta.mdate.ui.activity.InviteActivity;
import com.quootta.mdate.ui.activity.ProfileActivity;
import com.quootta.mdate.ui.activity.SettingActivity;
import com.quootta.mdate.ui.activity.VideoVerifyActivity;
import com.quootta.mdate.ui.activity.VipActivity;
import com.quootta.mdate.ui.adapter.RvGalleryAdapter;
import com.quootta.mdate.ui.dialog.MyProgressDialog;
import com.quootta.mdate.ui.dialog.ShareDialog;
import com.quootta.mdate.ui.view.CircleImageView;
import com.quootta.mdate.utils.GsonUtil;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import cn.qqtheme.framework.picker.OptionPicker;
import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallSession;


/**
 * Created by Ryon on 2016/2/14.
 * email:para.ryon@foxmail.com
 */
public class MeFragment extends BaseFragment implements View.OnClickListener{

    @Bind(R.id.iv_head_me_fragment) ImageView iv_head;
    @Bind(R.id.tv_name_me_fragment) TextView tv_name;
    @Bind(R.id.tvId) TextView tvId;
    //    @Bind(R.id.tv_connection_rate) TextView tvConn;
//    @Bind(R.id.rl_edit_profile) RelativeLayout rlEditProfile;
    //    @Bind(R.id.iv_sesame_credit_me_fragment) ImageView ivSesameCreditMeFragment;
//    @Bind(R.id.rl_sesame_credit) RelativeLayout rlSesameCredit;
//    @Bind(R.id.iv_video_verify_me_fragment) ImageView ivVideoVerifyMeFragment;
//    @Bind(R.id.rl_video_verify) RelativeLayout rlVideoVerify;
//    @Bind(R.id.rl_balance) RelativeLayout rlBalance;
    @Bind(R.id.rl_vip) RelativeLayout rlVip;
    //    @Bind(R.id.tv_balance)public TextView tvBalance;
//    @Bind(R.id.rl_charge) RelativeLayout rlCharge;
//    @Bind(R.id.rl_withdraw) RelativeLayout rlWithdraw;
//    @Bind(R.id.rl_bill) RelativeLayout rlBill;
//    @Bind(R.id.rl_gallery) RelativeLayout rlGallery;
//    @Bind(R.id.iv_upload_pic) RoundRectImageView iv_upload_pic;
//    @Bind(R.id.rv_gallery) RecyclerView rvGallery;
    @Bind(R.id.rl_friend) RelativeLayout rlFriend;
    @Bind(R.id.rl_charge_standard) RelativeLayout rlChargeStandard;
    @Bind(R.id.rl_setting) RelativeLayout rlSetting;
    @Bind(R.id.rl_gift) RelativeLayout rlGift;
    @Bind(R.id.rl_share)RelativeLayout rlShare;
    @Bind(R.id.rl_family)RelativeLayout rlFamily;
    //    @Bind(R.id.me_age) TextView meAge;
//    @Bind(R.id.me_gender) ImageView meGender;
    @Bind(R.id.me_wallet) RelativeLayout wallet;
    @Bind(R.id.rl_videoVerify) RelativeLayout videoVerify;
    @Bind(R.id.ivEditProfile) ImageView ivEditProfile;


    @Bind(R.id.sw_video)
    Switch swVideo;
    @Bind(R.id.sw_audio)
    Switch swAudio;

    @Bind(R.id.tv_video_switch)
    TextView tvVideoSwitch;
    @Bind(R.id.tv_audio_switch)
    TextView tvAudioSwitch;

    private Map<String , String> statusMap;
    private int audioPrice;
    private int videoPrice;

    private final int TYPE_VIDEO = 0;
    private final int TYPE_AUDIO = 1;

    public final int CHARGE_VIP = 3;
    public final int EDIT_PROFILE = 2;
    private final int RESULT_SUCCESS = 4;
    private final int PROFILE_SUCCESS=1;
    private final String BALANCE_NOT_ENOUGH = "2002";

    private Context baseContext;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private ImageLoader.ImageContainer imageContainer;
    private Bitmap avatar;
    private AlbumList albumList;
    private SHARE_MEDIA platform = null;
    private RvGalleryAdapter galleryAdapter;
    public boolean isFirstInit = true;
    private InfoDetailNonStatic infoDetailNonStatic;
    /**
     * 加载UI前的预初始化
     */
    @Override
    protected void init() {
        requestQueue = BaseApp.getRequestQueue();
        imageLoader = BaseApp.getImageLoader();

        statusMap = new HashMap<String , String>();
        audioPrice = BaseApp.getAudioPrice();
        videoPrice = BaseApp.getVideoPrice();

    }

    /**
     * 设置布局
     *
     * @return
     */
    @Override
    protected int getRootView() {
        return R.layout.fragment_me;
    }

    /**
     * 请求数据，设置UI
     *
     * @param view
     */
    @Override
    protected void initData(View view) {
        baseContext = view.getContext();
        requestBaseInfoDetail();
        //展示图片的接口
        requestAlbum();

        initContent();
    }

    private void initContent() {

        if (BaseApp.getIsVideoEnable()){
            swVideo.setChecked(true);
            tvVideoSwitch.setText(videoPrice + getString(R.string.gold_per_min));
        } else {
            swVideo.setChecked(false);
            tvVideoSwitch.setText(getString(R.string.abandon_answer));
        }

        if(BaseApp.getIsAudioEnable()) {
            swAudio.setChecked(true);
            tvAudioSwitch.setText(audioPrice + getString(R.string.gold_per_min));
        } else {
            swAudio.setChecked(false);
            tvAudioSwitch.setText(getString(R.string.abandon_answer));
        }
    }

    //更新用户信息
    public void requestBaseInfoDetail() {
        //暂停显示progressdialog
        // final MyProgressDialog myProgressDialog = new MyProgressDialog(baseContext);
        // myProgressDialog.show();

        final InfoDetailRequest infoDetailRequest = new InfoDetailRequest(
                new VolleyListener() {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        LogUtil.d("MeFragment", "Response:----->" + response);
                        try {
                            infoDetailNonStatic = GsonUtil.parse(response.getString("data"), InfoDetailNonStatic.class);
                            LogUtil.d("MeFragment", infoDetailNonStatic.toString());
                            //  Log.i("MeFragment","用户详情--》"+infoDetailNonStatic.getShare().getContent());

                            initStaticInfoDetail(infoDetailNonStatic);
                            initInfoDetail();

                            //特殊用户
                            BaseApp.setIsSpecial(infoDetailNonStatic.if_special);
                            if (infoDetailNonStatic.if_special){
                                wallet.setVisibility(View.GONE);
                                rlChargeStandard.setVisibility(View.GONE);
                                rlVip.setVisibility(View.GONE);
                            }

                            BaseApp.setGoldCount(InfoDetail.bill);
                            BaseApp.setIsVisual(InfoDetail.visual_status);
                            BaseApp.setIsAudioEnable(Boolean.parseBoolean(InfoDetail.audio_enable));
                            BaseApp.setIsVideoEnable(Boolean.parseBoolean(InfoDetail.video_enable));
                            isFirstInit = false;
                            if(InfoDetail.audio_pay != null) {
                                BaseApp.setAudioPrice(Integer.parseInt(InfoDetail.audio_pay));
                            } else {
                                BaseApp.setAudioPrice(0);
                            }
                            if(InfoDetail.video_pay != null) {
                                BaseApp.setVideoPrice(Integer.parseInt(InfoDetail.video_pay));
                            } else {
                                BaseApp.setVideoPrice(0);
                            }
                            //  myProgressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        requestQueue.add(infoDetailRequest);
    }

    private void initStaticInfoDetail(InfoDetailNonStatic infoDetailNonStatic) {
        InfoDetail._id = infoDetailNonStatic._id;
        InfoDetail.visual_status = infoDetailNonStatic.visual_status;
        InfoDetail.avatar = infoDetailNonStatic.avatar;
        InfoDetail.personal_desc = infoDetailNonStatic.personal_desc;
        InfoDetail.nick_name = infoDetailNonStatic.nick_name;
        InfoDetail.gender = infoDetailNonStatic.gender;
        InfoDetail.birthday = infoDetailNonStatic.birthday;
        InfoDetail.city = infoDetailNonStatic.city;
        InfoDetail.invite_code=infoDetailNonStatic.invite_code;
        InfoDetail.height = infoDetailNonStatic.height;
        InfoDetail.weight = infoDetailNonStatic.weight;
        InfoDetail.hobby = infoDetailNonStatic.hobby;
        InfoDetail.job = infoDetailNonStatic.job;
        InfoDetail.request = infoDetailNonStatic.request;
        InfoDetail.date_way = infoDetailNonStatic.date_way;
        InfoDetail.expertise = infoDetailNonStatic.expertise;
        InfoDetail.chat = infoDetailNonStatic.chat;
        InfoDetail.bill = infoDetailNonStatic.bill;
        InfoDetail.album_count = infoDetailNonStatic.album_count;
        InfoDetail.is_verified_video = infoDetailNonStatic.is_verified_video;
        InfoDetail.zhima_score = infoDetailNonStatic.zhima_score;
        InfoDetail.is_verified_zhima = infoDetailNonStatic.is_verified_zhima;
        InfoDetail.video_pay = infoDetailNonStatic.video_pay;
        InfoDetail.audio_pay = infoDetailNonStatic.audio_pay;
        InfoDetail.connect_rate = infoDetailNonStatic.connect_rate;
        InfoDetail.video_enable = infoDetailNonStatic.video_enable;
        InfoDetail.audio_enable = infoDetailNonStatic.audio_enable;
        InfoDetail.bRecharge = infoDetailNonStatic.bRecharge;
        InfoDetail.bWithdraw = infoDetailNonStatic.bWithdraw;
        //InfoDetail.bAvPay = infoDetailNonStatic.bAvPay;
        InfoDetail.is_vip = infoDetailNonStatic.is_vip;
        InfoDetail.vip_valid_time = infoDetailNonStatic.vip_valid_time;

        LogUtil.d("MeFragment", InfoDetail.toStaticString());
    }

    private void requestAlbum() {

        //展示图片的接口
//            AlbumRequest albumRequest = new AlbumRequest(paramsMap, new VolleyListener() {
//                @Override
//                protected void onSuccess(JSONObject response) {
//                    LogUtil.d("AlbumFragment", "Response: " + response);
//                    albumList = new AlbumList();
//                    try {
//                        albumList = GsonUtil.parse(response.getString("data"),AlbumList.class);
//                        initRecyclerView(albumList);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            requestQueue.add(albumRequest);
    }
    //显示相册的适配器
//    private void initRecyclerView(final AlbumList albumList) {
//        if (albumList.album.size() > 3) {
//            List<Album> subList = albumList.album.subList(0, 3);
//
//            galleryAdapter = new RvGalleryAdapter(baseContext, subList);
//        } else {
//            galleryAdapter = new RvGalleryAdapter(baseContext, albumList.album);
//        }
//        galleryAdapter.setOnItemSelectedListener(new OnItemSelectedListener() {
//            @Override
//            public void onSelected(Drawable drawable, int position) {
//                Intent gallery = new Intent(baseContext, AlbumActivity.class);
//                gallery.putExtra("AlbumList", albumList);
//                startActivity(gallery);
//            }
//        });
//        rvGallery.setAdapter(galleryAdapter);
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(baseContext,
//                LinearLayoutManager.HORIZONTAL, false);
//        rvGallery.setLayoutManager(linearLayoutManager);
//    }


    private void initInfoDetail() {

        //设置当前用户的userInfo 这个已经弃用 现在使用消息提供者来加载消息
//        RongIM.getInstance().setCurrentUserInfo(
//                new UserInfo(InfoDetail._id,
//                        InfoDetail.nick_name,
//                        Uri.parse(LocalUrl.getPicUrl(InfoDetail.avatar))));

        requestAvatar();

//        int conn = (int) Double.parseDouble(InfoDetail.connect_rate);
//        tvConn.setText("接通率" + conn + "%");

        LogUtil.i("MeFragment","个人资料性别--》"+InfoDetail.gender);
//        if (InfoDetail.gender.equals("female")){
//            meGender.setImageResource(R.drawable.rankingsexg);
//        }else {
//            meGender.setImageResource(R.drawable.ranking_sexb);
//        }

        tv_name.setText(InfoDetail.nick_name);
        tvId.setText(getResources().getString(R.string.user_id_str, InfoDetail._id));

        //认证 不再使用
//        if (Boolean.parseBoolean(InfoDetail.is_verified_zhima)) {
//            ivSesameCreditMeFragment.setImageResource(R.mipmap.credit_sesame_personal);
//        } else {
//            ivSesameCreditMeFragment.setImageResource(R.mipmap.credit_sesame_personal_uncheck);
//        }
//
//        if (Boolean.parseBoolean(InfoDetail.is_verified_video)) {
//            ivVideoVerifyMeFragment.setImageResource(R.mipmap.camera_selected);
//        } else {
//            ivVideoVerifyMeFragment.setImageResource(R.mipmap.camera);
//        }

        //    LogUtil.d("initAVInterface CallCountRequest", "active");
        //  tvBalance.setText(InfoDetail.bill + getString(R.string.coin));
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser){
//            ToastUtil.showToast("这个方法执行了"+BaseApp.getGoldCount()+getString(R.string.coin));
//
//            tvBalance.setText(BaseApp.getGoldCount()+getString(R.string.coin));
//        }
//    }

    private void requestAvatar() {
        ImageRequest avatarRequest = new ImageRequest(LocalUrl.getPicUrl(InfoDetail.avatar),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        LogUtil.d("MeFragment", "Bitmap size1:" + response.getByteCount());
                        iv_head.setImageBitmap(response);
                        avatar = response;
                    }
                }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        iv_head.setImageResource(R.mipmap.test);
                    }
                });
        requestQueue.add(avatarRequest);
    }

    //获取余额取消
    public void requestBalance() {
        if (!isFirstInit) {
            final MyProgressDialog myProgressDialog = new MyProgressDialog(baseContext);
            myProgressDialog.show();
            Map<String, String> balanceMap = new HashMap<String, String>();
            BalanceRequest balanceRequest = new BalanceRequest(balanceMap, new VolleyListener() {
                @Override
                protected void onSuccess(JSONObject response) {
                    try {
                        JSONObject data = response.getJSONObject("data");
                        String balance = data.getString("balance");
                        LogUtil.d("requestBalance", balance);
                        BaseApp.setGoldCount(balance);
                        //  tvBalance.setText(balance + getString(R.string.coin));
                        myProgressDialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            requestQueue.add(balanceRequest);
        }
    }

    /**
     * 设置监听
     */
    @Override
    protected void setListener() {
        ivEditProfile.setOnClickListener(this);
        iv_head.setOnClickListener(this);
//        rlSesameCredit.setOnClickListener(this);
//        rlVideoVerify.setOnClickListener(this);
//        rlBalance.setOnClickListener(this);
//        rlCharge.setOnClickListener(this);
//        rlWithdraw.setOnClickListener(this);
//        rlBill.setOnClickListener(this);
//        rlGallery.setOnClickListener(this);
//        iv_upload_pic.setOnClickListener(this);
        rlFriend.setOnClickListener(this);
        rlChargeStandard.setOnClickListener(this);
        rlVip.setOnClickListener(this);
        rlSetting.setOnClickListener(this);
        rlGift.setOnClickListener(this);
        rlShare.setOnClickListener(this);
        rlFamily.setOnClickListener(this);
        wallet.setOnClickListener(this);
        videoVerify.setOnClickListener(this);

        tvAudioSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPricePicker(TYPE_AUDIO);
            }
        });

        tvVideoSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPricePicker(TYPE_VIDEO);
            }
        });

        swAudio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                statusMap.put("video_pay", BaseApp.getVideoPrice() + "");
                statusMap.put("audio_pay", BaseApp.getAudioPrice() + "");
                statusMap.put("video_enable", BaseApp.getIsVideoEnable() + "");
                statusMap.put("audio_enable", isChecked + "");
                requestAnswerStatus(TYPE_AUDIO, statusMap, null);
            }
        });

        swVideo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                statusMap.put("video_pay", BaseApp.getVideoPrice() + "");
                statusMap.put("audio_pay", BaseApp.getAudioPrice() + "");
                statusMap.put("video_enable", isChecked + "");
                statusMap.put("audio_enable", BaseApp.getIsAudioEnable() + "");
                requestAnswerStatus(TYPE_VIDEO, statusMap, null);
            }
        });

        if (BaseApp.getIsAudioEnable()) {
            tvAudioSwitch.setClickable(true);
        } else {
            tvAudioSwitch.setClickable(false);
        }

        if (BaseApp.getIsVideoEnable()) {
            tvVideoSwitch.setClickable(true);
        } else {
            tvVideoSwitch.setClickable(false);
        }

    }

    private void onPricePicker(int type) {
        OptionPicker picker = null;
        switch (type) {
            case TYPE_VIDEO:
                picker = new OptionPicker(getActivity(), getResources().getStringArray(R.array.video_charge));
                picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(String option) {
                        videoPrice = Integer.parseInt(option.replace(getString(R.string.gold_per_min), ""));
                        statusMap.put("video_pay", videoPrice + "");
                        statusMap.put("audio_pay", BaseApp.getAudioPrice() + "");
                        statusMap.put("video_enable", BaseApp.getIsVideoEnable() + "");
                        statusMap.put("audio_enable", BaseApp.getIsAudioEnable() + "");

                        requestAnswerStatus(TYPE_VIDEO, statusMap, option);
                    }
                });
                break;
            case TYPE_AUDIO:
                picker = new OptionPicker(getActivity(), getResources().getStringArray(R.array.audio_charge));
                picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(String option) {
                        audioPrice = Integer.parseInt(option.replace(getString(R.string.gold_per_min), ""));
                        statusMap.put("video_pay", BaseApp.getVideoPrice() + "");
                        statusMap.put("audio_pay", audioPrice + "");
                        statusMap.put("video_enable", swVideo.isChecked() + "");
                        statusMap.put("audio_enable", swAudio.isChecked() + "");

                        requestAnswerStatus(TYPE_AUDIO, statusMap, option);
                    }
                });
                break;
            default:
                break;
        }
        picker.setOffset(2);
        picker.setTextSize(20);
        picker.show();
    }

    private void requestAnswerStatus(final int type, final Map<String , String> statusMap, final String option) {
        AnswerStatusRequest answerStatusRequest = new AnswerStatusRequest(statusMap, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                ChargeStandard chargeStandard = null;
                try {
                    chargeStandard = GsonUtil.parse(response.getString("data"), ChargeStandard.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                switch (type) {
                    case TYPE_VIDEO:
                        //成功后同步本地属性值
                        BaseApp.setIsVideoEnable(Boolean.parseBoolean(chargeStandard.video_enable));
                        BaseApp.setVideoPrice(Integer.parseInt(chargeStandard.video_pay));

                        //根据接听状态设置点击事件和图片显示状态
                        if(!BaseApp.getIsVideoEnable()) {
                            //不可接听
                            tvVideoSwitch.setClickable(false);
                            tvVideoSwitch.setText(getString(R.string.abandon_answer));
                        } else {
                            //可接听
                            tvVideoSwitch.setClickable(true);

                            tvVideoSwitch.setText(chargeStandard.video_pay + getString(R.string.gold_per_min));
                        }

                        break;
                    case TYPE_AUDIO:
                        //成功后同步本地属性值
                        BaseApp.setIsAudioEnable(Boolean.parseBoolean(chargeStandard.audio_enable));
                        BaseApp.setAudioPrice(Integer.parseInt(chargeStandard.audio_pay));

                        //根据接听状态设置点击事件和图片显示状态
                        if(!BaseApp.getIsAudioEnable()) {
                            //不可接听
                            tvAudioSwitch.setClickable(false);
                            tvAudioSwitch.setText(getString(R.string.abandon_answer));
                        } else {
                            //可接听
                            tvAudioSwitch.setClickable(true);

                            tvAudioSwitch.setText(chargeStandard.audio_pay + getString(R.string.gold_per_min));
                        }
                        break;
                }

                try {
                    ToastUtil.showToast(response.getString("msg"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected void onFail(JSONObject response) {
                super.onFail(response);
                try {
                    ToastUtil.showToast(response.getString("msg"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                videoPrice = BaseApp.getVideoPrice();
                swVideo.setChecked(BaseApp.getIsVideoEnable());
                swVideo.setSelected(BaseApp.getIsVideoEnable());

                audioPrice = BaseApp.getAudioPrice();
                swAudio.setChecked(BaseApp.getIsVideoEnable());
                swAudio.setSelected(BaseApp.getIsVideoEnable());
            }
        });
        requestQueue.add(answerStatusRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivEditProfile:
            case R.id.iv_head_me_fragment:
//                new ProfileTask().execute();
//                new ImageCompressTask(baseContext, avatar, new ImageCompressTask.OnCompressFinishListener() {
//                    @Override
//                    public void onFinish(byte[] bytes) {
//                        LogUtil.d("MeFragment", "into profile on finish");
//                        Intent profileIntent = new Intent(baseContext, ProfileActivity.class);
//                        profileIntent.putExtra("avatar",bytes);
//                        startActivityForResult(profileIntent, EDIT_PROFILE);
//                    }
//                }).execute();
                Intent profileIntent = new Intent(baseContext, ProfileActivity.class);
                startActivityForResult(profileIntent, EDIT_PROFILE);
                break;
//            case R.id.rl_sesame_credit:
//                if (Boolean.parseBoolean(InfoDetail.is_verified_zhima)) {
//                    ToastUtil.showToast(baseContext.getString(R.string.is_sesame));
//                } else {
//                    Intent sesameIntent = new Intent(baseContext, SesameVerifyActivity.class);
//                    startActivityForResult(sesameIntent, EDIT_PROFILE);
//                }
//
//                break;
//            case R.id.rl_video_verify:
//
//                if (Boolean.parseBoolean(InfoDetail.is_verified_video)) {
//                    ToastUtil.showToast(baseContext.getString(R.string.is_video));
//                } else {
//                    Intent videoIntent = new Intent(baseContext, VideoVerifyActivity.class);
//                    startActivityForResult(videoIntent, EDIT_PROFILE);
//                }
//                break;
            //充值
//            case R.id.rl_charge:
//                Intent chargeIntent = new Intent(baseContext, ChargeActivity.class);
//                startActivity(chargeIntent);
//                break;
            //提现
//            case R.id.rl_withdraw:
//                Intent withdrawIntent = new Intent(baseContext, WithdrawActivity.class);
//                startActivity(withdrawIntent);
//                break;
            //账单
//            case R.id.rl_bill:
//                Intent billIntent = new Intent(baseContext, BillActivity.class);
//                startActivity(billIntent);
//                break;
            //跳转到家族页面
            case R.id.rl_family:
                if (infoDetailNonStatic!=null){
                    final Intent bannerIntent = new Intent(baseContext, BannerActivity.class);
                    bannerIntent.putExtra("id",InfoDetail._id);
                    bannerIntent.putExtra("url",infoDetailNonStatic.getShare().getIndex_url());
                    bannerIntent.putExtra("title",infoDetailNonStatic.getShare().getTitle());
                    bannerIntent.putExtra("content",infoDetailNonStatic.getShare().getContent());
                    bannerIntent.putExtra("share_url",infoDetailNonStatic.getShare().getShare_url());
                    bannerIntent.putExtra("thumbnail",infoDetailNonStatic.getShare().getThumbnail());
                    baseContext.startActivity(bannerIntent);
                }

                break;
            case R.id.rl_share:
                if (infoDetailNonStatic!=null){
//                    Meshare();
                    Intent intent = new Intent(baseContext, InviteActivity.class);
                    intent.putExtra("thumbUrl", infoDetailNonStatic.getShare().getThumbnail());
                    intent.putExtra("shareUrl", infoDetailNonStatic.getShare().getShare_url());
                    startActivity(intent);
                }
                break;
            case R.id.me_wallet:
                Intent walletIntent=new Intent(baseContext, ChargeActivity.class);
                startActivity(walletIntent);
                break;
            case R.id.rl_gift:
                Intent giftIntent=new Intent(baseContext, GiftActivity.class);
                startActivity(giftIntent);
                break;
            case R.id.rl_videoVerify:
                if (Boolean.parseBoolean(InfoDetail.is_verified_video)) {
                    ToastUtil.showToast(baseContext.getString(R.string.is_video));
                } else {
                    Intent videoIntent = new Intent(baseContext, VideoVerifyActivity.class);
                    startActivityForResult(videoIntent, EDIT_PROFILE);
                }
                break;
            //case R.id.iv_upload_pic:
            //相册展示
//            case R.id.rl_gallery:
//                Intent gallery = new Intent(baseContext, AlbumActivity.class);
//                gallery.putExtra("AlbumList", albumList);
//                startActivity(gallery);
//                break;
            case R.id.rl_friend:
                Intent friendsList = new Intent(baseContext, FriendsListActivity.class);
                startActivity(friendsList);
                break;
            case R.id.rl_charge_standard:
                RongCallSession profile = RongCallClient.getInstance().getCallSession();
                if(profile != null && profile.getActiveTime() > 0) {
                    ToastUtil.showToast(getString(R.string.app_tips_text108));
                } else {
                    Intent chargeStandard = new Intent(baseContext, ChargeStandardActivity.class);
                    startActivity(chargeStandard);
                }
                break;
            case R.id.rl_vip:
                LogUtil.d("MeFragment", "rl_vip");
//                new ImageCompressTask(baseContext, avatar, new ImageCompressTask.OnCompressFinishListener() {
//                    @Override
//                    public void onFinish(byte[] bytes) {
//                        LogUtil.d("MeFragment", "into vip on finish");
//                        Intent vipIntent = new Intent(baseContext, VipActivity.class);
//                        vipIntent.putExtra("avatar",bytes);
//                        startActivityForResult(vipIntent, CHARGE_VIP);
////                        startActivity(vipIntent);
//                    }
//                }).execute();
                Intent vipIntent = new Intent(baseContext, VipActivity.class);
                startActivityForResult(vipIntent, CHARGE_VIP);
                break;
            case R.id.rl_setting:
                Intent settingIntent = new Intent(baseContext, SettingActivity.class);
                startActivity(settingIntent);
                break;
        }
    }
    //邀请
    private void Meshare(){
        ShareDialog shareDialog = new ShareDialog(baseContext, new ShareDialog.onClickback() {
            @Override
            public void onShare(int id) {
                String str = infoDetailNonStatic.getShare().getThumbnail();
            }
        });
        shareDialog.show();
        shareDialog.setShare_link(infoDetailNonStatic.getShare().getShare_url());

//        new ShareDialog(baseContext, new ShareDialog.onClickback() {
//            @Override
//            public void onShare(int id) {
//                String str=infoDetailNonStatic.getShare().getThumbnail();
//                UMImage image=new UMImage(context,str);
//                switch (id){
//
//                    case 1:
//                        new ShareAction((Activity) baseContext).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
//                                .withMedia(image)
//                                .withTitle(infoDetailNonStatic.getShare().getTitle())
//                                .withTargetUrl(infoDetailNonStatic.getShare().getShare_url())
//                                .withText(infoDetailNonStatic.getShare().getContent())
////                                .setCallback(umShareListener)
//                                .share();
//
//                        break;
//                    case 2:
//                        new ShareAction((Activity) baseContext).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
//                                .withMedia(image)
//                                .withTitle(infoDetailNonStatic.getShare().getTitle())
//                                .withText(infoDetailNonStatic.getShare().getContent())
//                                .withTargetUrl(infoDetailNonStatic.getShare().getShare_url())
//                                .setCallback(umShareListener)
//                                .share();
//                        break;
//                    case 3:
//                        new ShareAction((Activity) baseContext).setPlatform(SHARE_MEDIA.QQ)
//                                .withMedia(image)
//                                .withTitle(infoDetailNonStatic.getShare().getTitle())
//                                .withText(infoDetailNonStatic.getShare().getContent())
//                                .withTargetUrl(infoDetailNonStatic.getShare().getShare_url())
//                                .setCallback(umShareListener)
//                                .share();
//                        break;
//                    case 4:
//                        new ShareAction((Activity) baseContext).setPlatform(SHARE_MEDIA.QZONE)
//                                .withMedia(image)
//                                .withTitle(infoDetailNonStatic.getShare().getTitle())
//                                .withText(infoDetailNonStatic.getShare().getContent())
//                                .withTargetUrl(infoDetailNonStatic.getShare().getShare_url())
//                                .setCallback(umShareListener)
//                                .share();
//                        break;
//                }
//
//            }
//        }).show();

    }

    private UMShareListener umShareListener=new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA share_media) {
            ToastUtil.showLongToast("分享成功");

        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            ToastUtil.showLongToast("分享失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            ToastUtil.showLongToast("分享取消");
        }
    };


    class ProfileTask extends AsyncTask <Void, Integer, byte[]> {

        MyProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new MyProgressDialog(baseContext);
            progressDialog.show();
        }

        /**
         * 压缩头像
         * @param params
         * @return
         */
        @Override
        protected byte[] doInBackground(Void... params) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            avatar.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            return baos.toByteArray();
        }

        /**
         * 压缩完成后进入个人资料页
         * @param bytes
         */
        @Override
        protected void onPostExecute(byte[] bytes) {
            Intent profileIntent = new Intent(baseContext, ProfileActivity.class);
            profileIntent.putExtra("avatar",bytes);
            startActivityForResult(profileIntent, EDIT_PROFILE);
            progressDialog.dismiss();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("tag","requestcode mefragmentg-->"+requestCode+"   requestcoe resultcode--->"+resultCode+"---data--->"+data);
        switch (requestCode) {
            case EDIT_PROFILE:

                if (resultCode == PROFILE_SUCCESS){
                    Log.i("tag","执行一次 edit");
                    requestBaseInfoDetail();
                }

                break;
            case CHARGE_VIP:
                if (resultCode == RESULT_SUCCESS){
                    Log.i("tag","执行一次 vip");
                    requestBaseInfoDetail();
                }
                break;
            default:
                break;
        }
    }
}