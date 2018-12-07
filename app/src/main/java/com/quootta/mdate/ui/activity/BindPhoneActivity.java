package com.quootta.mdate.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.engine.security.BindPhoneRequest;
import com.quootta.mdate.engine.security.BindPhoneSmsRequest;
import com.quootta.mdate.helper.CountDownButtonHelper;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.SecretUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.android.volley.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by Ryon on 2016/9/22/0022.
 */
public class BindPhoneActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.account_bind_phone_text)
    EditText accountBindPhoneText;
    @Bind(R.id.account_bind_phone_authcode)
    Button accountBindPhoneAuthcode;
    @Bind(R.id.account_bind_mes_text)
    EditText accountBindMesText;
    @Bind(R.id.account_bind_phone)
    RelativeLayout accountBindPhone;
    @Bind(R.id.phone_bind_back)
    ImageView phoneBindBack;
    @Bind(R.id.phone_bind_title)
    TextView phoneBindTitle;
    @Bind(R.id.account_bind_pas_text)
    EditText accountBindPasText;
    private RequestQueue requestQueue;

    @Override
    protected void init() {
        requestQueue = BaseApp.getRequestQueue();
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_bind_phone;
    }

    @Override
    protected void initData() {


    }

    @Override
    protected void setListener() {
        phoneBindBack.setOnClickListener(this);
        accountBindPhoneAuthcode.setOnClickListener(this);
        accountBindPhone.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.phone_bind_back:
                finish();
                break;
            case R.id.account_bind_phone_authcode:
                if (accountBindPhoneText.getText().toString().trim() != null) {
                    requestVerification();
                } else {
                    ToastUtil.showToast(getString(R.string.app_tips_text22));
                }
                break;
            case R.id.account_bind_phone:
                    onBindPhone();
                    break;
        }
    }

    //绑定
    private void onBindPhone(){
        Map<String,String> bindMap=new HashMap<>();
        bindMap.put("mobile",accountBindPhoneText.getText().toString());
        bindMap.put("verify_code",accountBindMesText.getText().toString());
        bindMap.put("password", SecretUtil.bytesToMD5(accountBindPasText.getText().toString()));
        BindPhoneRequest bindPhoneRequest=new BindPhoneRequest(bindMap, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {

                ToastUtil.showToast(getString(R.string.app_tips_text23));
                finish();
            }
        });
        requestQueue.add(bindPhoneRequest);
    }


    //获取验证码
    private void requestVerification() {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("mobile", accountBindPhoneText.getText().toString().trim());
        BindPhoneSmsRequest bindPhoneSmsRequest = new BindPhoneSmsRequest(paramsMap,
                new VolleyListener() {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        LogUtil.d("SignUpFirstActivity", "Response:" + response);
                        try {
                            ToastUtil.showToast(response.getString("msg").toString());
                            setCountDownTimer();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                });
        requestQueue.add(bindPhoneSmsRequest);
    }

    private void setCountDownTimer() {
        CountDownButtonHelper countDownButtonHelper = new CountDownButtonHelper(accountBindPhoneAuthcode,
                getString(R.string.get_verification_code), 30, 1);
        countDownButtonHelper.start();
    }


}
