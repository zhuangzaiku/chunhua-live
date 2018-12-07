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
import com.quootta.mdate.engine.account.AlterUserPasMesRequest;
import com.quootta.mdate.engine.account.AlterUserPasRequest;
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
import butterknife.OnClick;

/**
 * Created by Ryon on 2016/9/22/0022.
 */
public class AlterUserPasActivity extends BaseActivity {

    @Bind(R.id.account_title)
    TextView accountTitle;
    @Bind(R.id.rel_title_bar)
    RelativeLayout relTitleBar;
    @Bind(R.id.account_alter_user_src)
    ImageView accountAlterUserSrc;
    @Bind(R.id.account_alter_user_text)
    EditText accountAlterUserText;
    @Bind(R.id.account_alter_get_code)
    Button accountAlterGetCode;
    @Bind(R.id.account_alter_mes_src)
    ImageView accountAlterMesSrc;
    @Bind(R.id.account_alter_mes_text)
    EditText accountAlterMesText;
    @Bind(R.id.account_alter_pas_src)
    ImageView accountAlterPasSrc;
    @Bind(R.id.account_alter_pas_text)
    EditText accountAlterPasText;
    @Bind(R.id.account_alter_bind)
    RelativeLayout accountAlterBind;
    //    @Bind(R.id.account_alter_back)
//    ImageView accountAlterBack;
//    @Bind(R.id.account_title)
//    TextView accountTitle;
//    @Bind(R.id.rel_title_bar)
//    RelativeLayout relTitleBar;
//    @Bind(R.id.account_alter_user_src)
//    ImageView accountAlterUserSrc;
//    @Bind(R.id.account_alter_user_text)
//    EditText accountAlterUserText;
//    @Bind(R.id.account_alter_get_code)
//    Button accountAlterGetCode;
//    @Bind(R.id.account_alter_mes_src)
//    ImageView accountAlterMesSrc;
//    @Bind(R.id.account_alter_mes_text)
//    EditText accountAlterMesText;
//    @Bind(R.id.account_alter_pas_src)
//    ImageView accountAlterPasSrc;
//    @Bind(R.id.account_alter_pas_text)
//    EditText accountAlterPasText;
//    @Bind(R.id.account_alter_bind)
//    RelativeLayout accountAlterBind;
    private RequestQueue requestQueue;

    @Override
    protected void init() {
        requestQueue = BaseApp.getRequestQueue();

    }

    @Override
    protected int getRootView() {
        return R.layout.activity_alte_userpas;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }


    //获取验证码
    private void requestVerification() {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("mobile", accountAlterUserText.getText().toString().trim());
        AlterUserPasMesRequest alterUserPasMesRequest = new AlterUserPasMesRequest(paramsMap,
                new VolleyListener() {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        LogUtil.d("AlterUserPasActivity", "Response:" + response);
                        try {
                            ToastUtil.showToast(response.getString("msg").toString());
                            setCountDownTimer();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                });
        requestQueue.add(alterUserPasMesRequest);
    }

    private void setCountDownTimer() {
        CountDownButtonHelper countDownButtonHelper = new CountDownButtonHelper(accountAlterGetCode,
                getString(R.string.get_verification_code), 30, 1);
        countDownButtonHelper.start();
    }


    //修改密码
    private void alterUserPas() {
        Map<String, String> alterPasMap = new HashMap<>();
        alterPasMap.put("password", SecretUtil.bytesToMD5(accountAlterPasText.getText().toString()));
        alterPasMap.put("mobile", accountAlterUserText.getText().toString());
        alterPasMap.put("verify_code", accountAlterMesText.getText().toString());
        AlterUserPasRequest alterUserPasRequest = new AlterUserPasRequest(alterPasMap, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                ToastUtil.showToast(getString(R.string.app_tips_text21));
                finish();
            }
        });
        requestQueue.add(alterUserPasRequest);
    }

    @OnClick({R.id.account_alter_back, R.id.account_alter_get_code, R.id.account_alter_bind})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.account_alter_back:
                finish();
                break;
            case R.id.account_alter_get_code:
                if (accountAlterPasText.getText().toString().trim() != null) {
                    requestVerification();
                } else {
                    ToastUtil.showToast(getString(R.string.app_tips_text22));
                }
                break;
            case R.id.account_alter_bind:
                alterUserPas();
                break;
        }
    }


}
