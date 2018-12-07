package com.quootta.mdate.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.base.BaseFragment;
import com.quootta.mdate.engine.account.PhoneNumberRequest;
import com.quootta.mdate.engine.account.VerificationRequest;
import com.quootta.mdate.helper.CountDownButtonHelper;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.ui.activity.SignActivity;
import com.quootta.mdate.utils.ActivityUtil;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.android.volley.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by Ryon on 2016/7/18/0018.
 */
public class SignUpFirstFragment extends BaseFragment implements View.OnClickListener{

    @Bind(R.id.iv_back_title_bar)ImageView iv_back;
    @Bind(R.id.tv_title_bar)TextView tv_title;
    @Bind(R.id.et_number_sign_up_activity)EditText et_number;
    @Bind(R.id.et_verification_sign_up_activity)EditText et_verification;
    @Bind(R.id.btn_verification_sign_up_activity)Button btn_verification;
    @Bind(R.id.btn_next_step_sign_up_activity)Button btn_next_step;

    private RequestQueue requestQueue;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context baseContext;

    @Override
    protected int getRootView() {
        return R.layout.activity_sign_up;
    }

    @Override
    protected void setListener() {
        iv_back.setOnClickListener(this);
        btn_verification.setOnClickListener(this);
//        btn_request.setOnClickListener(this);
        btn_next_step.setOnClickListener(this);

    }

    @Override
    protected void init() {
        requestQueue = BaseApp.getRequestQueue();
        pref = getActivity().getSharedPreferences("CookieStore",getActivity().MODE_PRIVATE);
        editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    @Override
    protected void initData(View view) {
        baseContext=view.getContext();
        initTitle();
    }

    private void initTitle() {
        iv_back.setVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.signin));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back_title_bar:
                ActivityUtil.finishActivty();
                break;

            case R.id.btn_verification_sign_up_activity:
                if(et_number.getText().toString().trim() != null) {
                    requestVerification();
                } else {
                    ToastUtil.showToast(getString(R.string.app_tips_text22));
                }

                break;

//            case R.id.btn_request_sign_up_activity:
//                Intent requestInvitationIntent = new Intent(SignUpFirstActivity.this, RequestInvitationActivity.class);
//                startActivity(requestInvitationIntent);
//                break;

            case R.id.btn_next_step_sign_up_activity:
                requestPhoneNumber();
                break;

        }
    }

    private void requestPhoneNumber() {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("mobile", et_number.getText().toString().trim());
        paramsMap.put("verify_code", et_verification.getText().toString().trim());
//        paramsMap.put("invite_code", et_request.getText().toString().trim());
        PhoneNumberRequest phoneNumberRequest = new PhoneNumberRequest(paramsMap,
                new VolleyListener() {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        LogUtil.d("SignUpFirstActivity", "Response:" + response);
                        try {
                            ToastUtil.showToast(response.getString("msg").toString());

//                            Intent secondIntent = new Intent(baseContext, SignUpSecondActivity.class);
//                           secondIntent.putExtra("number",et_number.getText().toString().trim());
//                            startActivity(secondIntent);
                            SingUpSecondFragment singUpSecondFragment=new SingUpSecondFragment();
                            Bundle bundle=new Bundle();
                            bundle.putString("number",et_number.getText().toString().trim());
                            singUpSecondFragment.setArguments(bundle);
                            ((SignActivity)getActivity()).changeFragment(singUpSecondFragment);

//                            ActivityUtil.finishActivty();
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
        VerificationRequest verificationRequest = new VerificationRequest(paramsMap,
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
        requestQueue.add(verificationRequest);
    }


    private void setCountDownTimer() {
        CountDownButtonHelper countDownButtonHelper = new CountDownButtonHelper(btn_verification,
                getString(R.string.get_verification_code),30,1);
        countDownButtonHelper.start();
    }

    public static SignUpFirstFragment newInstance() {

        Bundle args = new Bundle();
        SignUpFirstFragment fragment = new SignUpFirstFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
