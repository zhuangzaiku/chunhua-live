package com.quootta.mdate.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.helper.LoginHelper;
import com.quootta.mdate.helper.OtherLoginHelper;
import com.quootta.mdate.ui.dialog.MyProgressDialog;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Ryon on 2016/9/13/0013.
 */
public class OtherLoginActivity extends BaseActivity {

    @Bind(R.id.home_login_qq)
    LinearLayout homeLoginQq;
    @Bind(R.id.home_login_weixin)
    LinearLayout homeLoginWeixin;
    @Bind(R.id.home_login_weibo)
    LinearLayout homeLoginWeibo;
    @Bind(R.id.home_phonelogin)
    TextView homePhonelogin;
    @Bind(R.id.home_phoneregister)
    TextView homePhoneregister;
    @Bind(R.id.home_login_phone)
    LinearLayout homeLoginPhone;
    @Bind(R.id.home_user_deal)
    TextView homeUserDeal;
    @Bind(R.id.home_login_deal)
    LinearLayout homeLoginDeal;

    private MyProgressDialog myProgressDialog;
    private SharedPreferences pref;
    private Map<String,String> otherLoginMap;
    private Map<String,String> signUpMap;
    private OtherLoginHelper otherLoaginHelper;

    private LoginHelper loginHelper;

    @Override
    protected void init() {



        otherLoginMap=new HashMap<>();
        signUpMap=new HashMap<>();

        pref=getSharedPreferences("login",MODE_PRIVATE);

        myProgressDialog=new MyProgressDialog(baseContext);
        myProgressDialog.setCancelable(false);

    }

    @Override
    protected int getRootView() {
        return R.layout.activity_other_login;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }



    @OnClick({R.id.home_login_qq, R.id.home_login_weixin, R.id.home_login_weibo, R.id.home_phonelogin, R.id.home_phoneregister, R.id.home_login_phone, R.id.home_user_deal, R.id.home_login_deal})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_login_qq:
                otherLoginMap.clear();
                otherLoginMap.put("platform","qq");
                break;
            case R.id.home_login_weixin:
                otherLoginMap.clear();
                otherLoginMap.put("platform","wechat");
                break;
            case R.id.home_login_weibo:
                otherLoginMap.clear();
                otherLoginMap.put("platform","weibo");
                break;
            case R.id.home_phonelogin:
                Intent intent=new Intent(this,LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.home_phoneregister:
                Intent gisterIntent=new Intent(this,SignActivity.class);
                startActivity(gisterIntent);
                break;
            case R.id.home_user_deal:
                Intent userIntent=new Intent(this,UserDealActivity.class);
                startActivity(userIntent);
                break;
            case R.id.home_login_deal:
                break;
        }
    }



}
