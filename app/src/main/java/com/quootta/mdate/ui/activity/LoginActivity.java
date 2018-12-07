package com.quootta.mdate.ui.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.domain.UserChatInfoList;
import com.quootta.mdate.helper.LoginHelper;
import com.quootta.mdate.ui.dialog.MyProgressDialog;
import com.quootta.mdate.utils.ActivityUtil;
import com.quootta.mdate.utils.SecretUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;


public class LoginActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.et_number_login_activity) EditText et_phone;
    @Bind(R.id.et_password_login_activity) EditText et_password;
    @Bind(R.id.tv_forget_login_activity) TextView tv_forget_password;
    @Bind(R.id.btn_login_activity) Button btn_login;
    @Bind(R.id.btn_join_login_activity) Button btn_join;

    private SharedPreferences pref;
    private Map<String,String> paramsMap;
    private LoginHelper loginHelper;
    private MyProgressDialog myProgressDialog;
    private long mExitTime;

    @Override
    protected void init() {
        pref = getSharedPreferences("login",MODE_PRIVATE);

        myProgressDialog = new MyProgressDialog(baseContext);
        myProgressDialog.setCancelable(false);
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
        btn_login.setOnClickListener(this);
        tv_forget_password.setOnClickListener(this);
        btn_join.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login_activity:
                myProgressDialog.show();
                paramsMap = new HashMap<>();
                paramsMap.put("mobile", et_phone.getText().toString());
                paramsMap.put("password", SecretUtil.bytesToMD5(et_password.getText().toString()));
                loginHelper = new LoginHelper(baseContext, new LoginHelper.OnLoginListener() {
                    @Override
                    public void onLoginSuccess(UserChatInfoList userChatInfoList, String city) {
                        myProgressDialog.dismiss();
                        Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                        if (userChatInfoList != null) {
                            loginIntent.putExtra("userChatInfoList", userChatInfoList);
                        }
                        loginIntent.putExtra("city", city);
                        ActivityUtil.finishActivty();
                        startActivity(loginIntent);
                        finish();
                    }

                    @Override
                    public void onLoginFail() {
                        myProgressDialog.dismiss();
                    }
                });
                loginHelper.login(paramsMap);
                break;
            case R.id.tv_forget_login_activity:
                Intent pwdIntent = new Intent(LoginActivity.this, FindPasswordActivity.class);
                startActivity(pwdIntent);
                break;
            case R.id.btn_join_login_activity:
                Intent signUpIntent = new Intent(LoginActivity.this, SignActivity.class);
                startActivity(signUpIntent);
                break;
        }
    }


//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if((System.currentTimeMillis() - mExitTime) > 2000) {
//                ToastUtil.showToast("再按一次返回键退出程序");
//                mExitTime = System.currentTimeMillis();
//            } else {
//                System.exit(0);
//            }
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myProgressDialog.dismiss();
    }
}
