package com.quootta.mdate.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.engine.account.FindpwdResetRequest;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.utils.ActivityUtil;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.SecretUtil;
import com.android.volley.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

public class FindPwdResetActivity extends BaseActivity {

    @Bind(R.id.iv_back_title_bar)ImageView iv_back;
    @Bind(R.id.tv_title_bar)TextView tv_title;
    @Bind(R.id.et_new_find_password_reset)EditText et_new_password;
    @Bind(R.id.et_confirm_find_password_reset)EditText et_confirm_new_password;
    @Bind(R.id.btn_ensure_find_password_reset)Button btn_ensure;

    private RequestQueue requestQueue;
    private String newPwd;
    private Map<String, String> paramsMap;

    @Override
    protected void init() {
        requestQueue = BaseApp.getRequestQueue();
        paramsMap = new HashMap<>();

    }

    @Override
    protected int getRootView() {
        return R.layout.activity_find_pwd_reset;
    }

    @Override
    protected void initData() {
        initTitle();
    }

    @Override
    protected void setListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.finishActivty();
            }
        });

        btn_ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPwd = et_new_password.getText().toString();
                if (newPwd.equals(et_confirm_new_password.getText().toString())) {
                    paramsMap.put("password", SecretUtil.bytesToMD5(newPwd));
                    requestResetPassword();
                } else {
                    Toast.makeText(baseContext, getString(R.string.app_tips_text32), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void requestResetPassword() {
        FindpwdResetRequest passwordRequest = new FindpwdResetRequest(paramsMap,
                new VolleyListener() {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        LogUtil.d("PasswordResetActivity", "Response:" + response);
                        try {
                            Toast.makeText(baseContext,response.getString("msg"), Toast.LENGTH_SHORT).show();
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
        requestQueue.add(passwordRequest);
    }

    private void initTitle() {
        iv_back.setVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.app_tips_text30));
    }
}
