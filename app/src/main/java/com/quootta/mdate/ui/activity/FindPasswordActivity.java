package com.quootta.mdate.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.engine.account.FindpwdPhoneNumberRequest;
import com.quootta.mdate.engine.account.FindpwdVerificationRequest;
import com.quootta.mdate.helper.CountDownButtonHelper;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.utils.ActivityUtil;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.android.volley.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

public class FindPasswordActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.iv_back_title_bar)ImageView iv_back;
    @Bind(R.id.tv_title_bar)TextView tv_title;
    @Bind(R.id.et_number_find_password)EditText et_number;
    @Bind(R.id.et_verification_find_password)EditText et_verification;
    @Bind(R.id.btn_verification_find_password)Button btn_verification;
    @Bind(R.id.btn_next_step_find_password)Button btn_next_step;

    private RequestQueue requestQueue;

    @Override
    protected void init() {
        requestQueue = BaseApp.getRequestQueue();
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_find_password;
    }

    @Override
    protected void initData() {
        initTitle();
    }



    private void initTitle() {
        iv_back.setVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.app_tips_text30));
    }

    @Override
    protected void setListener() {
        iv_back.setOnClickListener(this);
        btn_verification.setOnClickListener(this);
        btn_next_step.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_title_bar:
                finish();
                break;
            case R.id.btn_verification_find_password:
                if(et_number.getText().toString().trim() != null) {
                    requestVerification();
                } else {
                    ToastUtil.showToast(getString(R.string.app_tips_text31));
                }
                break;
            case R.id.btn_next_step_find_password:
                requestPhoneNumber();
                break;
        }
    }

    private void setCountDownTimer() {
        CountDownButtonHelper countDownButtonHelper = new CountDownButtonHelper(btn_verification,
                getString(R.string.get_verification_code),30,1);
        countDownButtonHelper.start();
    }

    private void requestPhoneNumber() {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("mobile", et_number.getText().toString().trim());
        paramsMap.put("verify_code", et_verification.getText().toString().trim());
        FindpwdPhoneNumberRequest phoneNumberRequest = new FindpwdPhoneNumberRequest(paramsMap,
                new VolleyListener() {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        LogUtil.d("FindPasswordActivity", "Response:" + response);
                        try {
                            ToastUtil.showToast(response.getString("msg").toString());
                            Intent secondIntent = new Intent(FindPasswordActivity.this, FindPwdResetActivity.class);
                            startActivity(secondIntent);
                            ActivityUtil.finishActivty();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
        requestQueue.add(phoneNumberRequest);
    }

    private void requestVerification() {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("mobile", et_number.getText().toString().trim());
        FindpwdVerificationRequest verificationRequest = new FindpwdVerificationRequest(paramsMap,
                new VolleyListener() {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        LogUtil.d("FindPasswordActivity", "Response:" + response);
                        try {
                            ToastUtil.showToast(response.getString("msg").toString());
                            setCountDownTimer();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
        requestQueue.add(verificationRequest);
    }
}
