package com.quootta.mdate.ui.activity;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.engine.account.PasswordRequest;
import com.quootta.mdate.helper.LoginHelper;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.ui.dialog.MyAlertDialog;
import com.quootta.mdate.utils.ActivityUtil;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.SecretUtil;
import com.android.volley.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

public class PasswordResetActivity extends BaseActivity {

    @Bind(R.id.iv_back_title_bar)ImageView iv_back;
    @Bind(R.id.tv_title_bar)TextView tv_title;
    @Bind(R.id.et_old_password_reset)EditText et_old_password;
    @Bind(R.id.et_new_password_reset)EditText et_new_password;
    @Bind(R.id.et_confirm_password_reset)EditText et_confirm_new_password;
    @Bind(R.id.btn_ensure_password_reset)Button btn_ensure;

    private RequestQueue requestQueue;
    private LoginHelper loginHelper;
    private String oldPwd,newPwd;
    private Map<String, String> paramsMap;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void init() {
        loginHelper = new LoginHelper(baseContext, null);
        requestQueue = BaseApp.getRequestQueue();
        paramsMap = new HashMap<>();
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_password_reset;
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
                oldPwd = et_old_password.getText().toString();
                newPwd = et_new_password.getText().toString();
                if (newPwd.equals(et_confirm_new_password.getText().toString())) {
                    paramsMap.put("password_origin", SecretUtil.bytesToMD5(oldPwd));
                    paramsMap.put("password", SecretUtil.bytesToMD5(newPwd));
                    MyAlertDialog myAlertDialog = new MyAlertDialog(baseContext, new MyAlertDialog.OnPositiveAlterListener() {
                        @Override
                        public void onAlter() {
                            requestResetPassword();
                        }
                    });
                    myAlertDialog.setTitle(getString(R.string.app_tips_text40));
                    myAlertDialog.setMessage(getString(R.string.app_tips_text41));
                    myAlertDialog.show();
                } else {
                    Toast.makeText(baseContext, getString(R.string.app_tips_text42), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void requestResetPassword() {
        PasswordRequest passwordRequest = new PasswordRequest(paramsMap,
                new VolleyListener() {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        LogUtil.d("PasswordResetActivity","Response:" + response);
                        try {
                            Toast.makeText(baseContext,response.getString("msg"), Toast.LENGTH_SHORT).show();
                            loginHelper.logout();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
        requestQueue.add(passwordRequest);
    }

    private void initTitle() {
        iv_back.setVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.account_security));
    }
}
