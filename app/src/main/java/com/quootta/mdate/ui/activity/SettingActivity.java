package com.quootta.mdate.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.engine.myCenter.AnswerStatusRequest;
import com.quootta.mdate.engine.myCenter.VisibleRequest;
import com.quootta.mdate.helper.LoginHelper;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.ui.dialog.MyAlertDialog;
import com.quootta.mdate.utils.ActivityUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.android.volley.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.iv_back_title_bar)
    ImageView iv_back;
    @Bind(R.id.tv_title_bar)
    TextView tv_title;
    @Bind(R.id.rl_security_setting_activity)RelativeLayout rl_security;
    @Bind(R.id.rl_about_us_setting_activity)
    RelativeLayout rl_about_us;
    @Bind(R.id.rl_update_setting_activity)
    RelativeLayout rl_update;
    @Bind(R.id.txt_version_name_setting_activity)
    TextView txt_version;
    @Bind(R.id.switch_setting_activity)
    Switch aSwitch;
    @Bind(R.id.tv_switch_setting_activity)
    TextView tv_switch;
    @Bind(R.id.answer_switch)
    Switch answerSwitch;
    @Bind(R.id.tv_answer_switch)
    TextView tvAnswerSwitch;
    @Bind(R.id.rl_feed_back_setting_activity)
    RelativeLayout rl_feed_back;
    @Bind(R.id.rl_log_out_setting_activity)
    RelativeLayout rl_log_out;
    @Bind(R.id.iv_security)
    RelativeLayout ivSecurity;

    private RequestQueue requestQueue;

    @Override
    protected void init() {
        requestQueue = BaseApp.getRequestQueue();
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initData() {
        initTitle();

        if (BaseApp.getIsVisual().equals("invisible")) {
            tv_switch.setText(getString(R.string.on));
            aSwitch.setSelected(true);
            aSwitch.setChecked(true);
        } else {
            tv_switch.setText(getString(R.string.off));
            aSwitch.setSelected(false);
            aSwitch.setChecked(false);
        }

        if (BaseApp.getIsAudioEnable() && BaseApp.getIsVideoEnable()) {
            tvAnswerSwitch.setText(getString(R.string.on));
            answerSwitch.setSelected(true);
            answerSwitch.setChecked(true);
        } else if (!BaseApp.getIsAudioEnable() && !BaseApp.getIsVideoEnable()) {
            tvAnswerSwitch.setText(getString(R.string.off));
            answerSwitch.setSelected(false);
            answerSwitch.setChecked(false);
        }

        try {
            //设置版本号
            txt_version.setText(getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initTitle() {
        iv_back.setVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.setting));
    }

    @Override
    protected void setListener() {
        iv_back.setOnClickListener(this);
        rl_security.setOnClickListener(this);
        ivSecurity.setOnClickListener(this);
        rl_about_us.setOnClickListener(this);
        rl_update.setOnClickListener(this);
        rl_feed_back.setOnClickListener(this);
        rl_log_out.setOnClickListener(this);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                requestVisible(isChecked);
            }
        });

        answerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                requestAnswerStatus(isChecked);
            }
        });
    }

    private void requestAnswerStatus(final boolean isChecked) {
        Map<String, String> statusMap = new HashMap<>();
        statusMap.put("video_pay", BaseApp.getVideoPrice() + "");
        statusMap.put("audio_pay", BaseApp.getAudioPrice() + "");

        if (isChecked) {
            statusMap.put("video_enable", "true");
            statusMap.put("audio_enable", "true");
        } else {
            statusMap.put("video_enable", "false");
            statusMap.put("audio_enable", "false");
        }
        AnswerStatusRequest answerStatusRequest = new AnswerStatusRequest(statusMap, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                try {
                    ToastUtil.showToast(response.getString("msg"));
                    BaseApp.setIsVideoEnable(isChecked);
                    BaseApp.setIsAudioEnable(isChecked);
                    if (isChecked) {
                        tv_switch.setText(getString(R.string.on));
                    } else {
                        tv_switch.setText(getString(R.string.off));
                    }
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
                answerSwitch.setChecked(!isChecked);
                answerSwitch.setSelected(!isChecked);
            }
        });
        requestQueue.add(answerStatusRequest);
    }

    private void requestVisible(final boolean isChecked) {
        Map<String, String> statusMap = new HashMap<>();
        if (isChecked) {
            statusMap.put("visual_status", "invisible");
        } else {
            statusMap.put("visual_status", "visible");
        }

        VisibleRequest visibleRequest = new VisibleRequest(statusMap, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                try {
                    ToastUtil.showToast(response.getString("msg"));
                    BaseApp.setIsVisual(isChecked + "");
                    if (isChecked) {
                        tv_switch.setText(getString(R.string.on));
                    } else {
                        tv_switch.setText(getString(R.string.off));
                    }
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
                aSwitch.setChecked(!isChecked);
                aSwitch.setSelected(!isChecked);
            }
        });
        requestQueue.add(visibleRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_title_bar:
                ActivityUtil.finishActivty();
                break;
            case R.id.rl_security_setting_activity:
                Intent pwdIntent = new Intent(SettingActivity.this, PasswordResetActivity.class);
                startActivity(pwdIntent);
                break;
            case R.id.iv_security:
                 Intent securityIntent=new Intent(SettingActivity.this,AccountSecurityActivity.class);
                 startActivity(securityIntent);
                break;
            case R.id.rl_about_us_setting_activity:
                Intent aboutIntent = new Intent(SettingActivity.this, AboutUsActivity.class);
                startActivity(aboutIntent);
                break;
            case R.id.rl_update_setting_activity:
                break;
            case R.id.rl_feed_back_setting_activity:
                Intent feedbackIntent = new Intent(SettingActivity.this, FeedbackActivity.class);
                startActivity(feedbackIntent);
                break;
            case R.id.rl_log_out_setting_activity:
                MyAlertDialog myAlertDialog = new MyAlertDialog(baseContext, new MyAlertDialog.OnPositiveAlterListener() {
                    @Override
                    public void onAlter() {
                        LoginHelper loginHelper = new LoginHelper(baseContext, null);
                        loginHelper.logout();
                    }
                });
                myAlertDialog.setTitle(getString(R.string.log_out_title));
                myAlertDialog.setMessage(getString(R.string.log_out_message));
                myAlertDialog.show();
                break;
        }
    }


}
