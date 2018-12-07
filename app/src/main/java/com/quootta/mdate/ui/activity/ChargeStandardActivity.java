package com.quootta.mdate.ui.activity;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.domain.ChargeStandard;
import com.quootta.mdate.engine.myCenter.AnswerStatusRequest;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.utils.ActivityUtil;
import com.quootta.mdate.utils.GsonUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.android.volley.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import cn.qqtheme.framework.picker.OptionPicker;

public class ChargeStandardActivity extends BaseActivity {

    @Bind(R.id.tv_title_bar)
    TextView tvTitle;
    @Bind(R.id.iv_back_title_bar)
    ImageView ivBack;
    @Bind(R.id.sw_video)
    Switch swVideo;
    @Bind(R.id.tv_video_switch)
    TextView tvVideoSwitch;
    @Bind(R.id.iv_ban_audio)
    ImageView ivBanAudio;
    @Bind(R.id.sw_audio)
    Switch swAudio;
    @Bind(R.id.tv_audio_switch)
    TextView tvAudioSwitch;
    @Bind(R.id.iv_ban_video)
    ImageView ivBanVideo;

    private RequestQueue requestQueue;
    private Map<String , String> statusMap;
    private int audioPrice;
    private int videoPrice;

    private final int TYPE_VIDEO = 0;
    private final int TYPE_AUDIO = 1;

    @Override
    protected void init() {
        requestQueue = BaseApp.getRequestQueue();
        statusMap = new HashMap<String , String>();
        audioPrice = BaseApp.getAudioPrice();
        videoPrice = BaseApp.getVideoPrice();
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_charge_standard;
    }

    @Override
    protected void initData() {
        initTitle();
        initContent();
    }

    private void initContent() {

        if (BaseApp.getIsVideoEnable()){
            swVideo.setChecked(true);
            ivBanVideo.setVisibility(View.GONE);
            tvVideoSwitch.setText(videoPrice + getString(R.string.gold_per_min));
        } else {
            swVideo.setChecked(false);
            ivBanVideo.setVisibility(View.VISIBLE);
            tvVideoSwitch.setText(getString(R.string.abandon_answer));
        }

        if(BaseApp.getIsAudioEnable()) {
            swAudio.setChecked(true);
            ivBanAudio.setVisibility(View.GONE);
            tvAudioSwitch.setText(audioPrice + getString(R.string.gold_per_min));
        } else {
            swAudio.setChecked(false);
            ivBanAudio.setVisibility(View.VISIBLE);
            tvAudioSwitch.setText(getString(R.string.abandon_answer));
        }
    }

    private void initTitle() {
        tvTitle.setText(R.string.charge_standard);
        ivBack.setVisibility(View.VISIBLE);
    }

    @Override
    protected void setListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.finishActivty();
            }
        });

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
                picker = new OptionPicker(ChargeStandardActivity.this, getResources().getStringArray(R.array.video_charge));
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
                picker = new OptionPicker(ChargeStandardActivity.this, getResources().getStringArray(R.array.audio_charge));
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
                            ivBanVideo.setVisibility(View.VISIBLE);
                            tvVideoSwitch.setClickable(false);
                            tvVideoSwitch.setText(getString(R.string.abandon_answer));
                        } else {
                            //可接听
                            ivBanVideo.setVisibility(View.GONE);
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
                            ivBanAudio.setVisibility(View.VISIBLE);
                            tvAudioSwitch.setClickable(false);
                            tvAudioSwitch.setText(getString(R.string.abandon_answer));
                        } else {
                            //可接听
                            ivBanAudio.setVisibility(View.GONE);
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
}
