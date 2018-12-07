package com.quootta.mdate.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.domain.AccountSecurityData;
import com.quootta.mdate.engine.security.AccountSecurityRequest;
import com.quootta.mdate.engine.security.BindPlatformRequest;
import com.quootta.mdate.engine.security.UnBindRequest;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.utils.GsonUtil;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.android.volley.RequestQueue;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by Ryon on 2016/9/22/0022.
 */
public class AccountSecurityActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.account_phone_num)
    TextView accountPhoneNum;
    @Bind(R.id.account_phone_text)
    TextView accountPhoneText;
    @Bind(R.id.account_phone_src)
    ImageView accountPhoneSrc;
    @Bind(R.id.account_phone_phonebind)
    TextView accountPhonePhonebind;
    @Bind(R.id.account_phone_textbind)
    TextView accountPhoneTextbind;
    @Bind(R.id.account_phone_bind)
    RelativeLayout accountPhoneBind;
    @Bind(R.id.account_phone)
    RelativeLayout accountPhone;
    @Bind(R.id.account_alter_username)
    RelativeLayout accountAlterUsername;
    @Bind(R.id.account_qq_src)
    ImageView accountQqSrc;
    @Bind(R.id.account_qq_textbind)
    TextView accountQqTextbind;
    @Bind(R.id.account_qq_bind)
    RelativeLayout accountQqBind;
    @Bind(R.id.account_qq)
    RelativeLayout accountQq;
    @Bind(R.id.account_wechat_src)
    ImageView accountWechatSrc;
    @Bind(R.id.account_wechat_textbind)
    TextView accountWechatTextbind;
    @Bind(R.id.account_wechat_bind)
    RelativeLayout accountWechatBind;
    @Bind(R.id.account_wechat)
    RelativeLayout accountWechat;
    @Bind(R.id.account_weibo_src)
    ImageView accountWeiboSrc;
    @Bind(R.id.account_weibo_textbind)
    TextView accountWeiboTextbind;
    @Bind(R.id.account_weibo_bind)
    RelativeLayout accountWeiboBind;
    @Bind(R.id.account_weibo)
    RelativeLayout accountWeibo;
    @Bind(R.id.account_text)
    TextView accountText;
    @Bind(R.id.account_back)
    ImageView accountBack;
    @Bind(R.id.account_title)
    TextView accountTitle;


    private RequestQueue requestQueue;
    private AccountSecurityData securityData;
    private boolean qq_Bind=false;
    private boolean weibo_Bind=false;
    private boolean wechat_Bind=false;
    private  Map<String,String> unBindMap;
    private   boolean bindstatus=true;
    private UMShareAPI umShareAPI;
    private Map<String,String> platformMap;

    @Override
    protected void init() {
        requestQueue = BaseApp.getRequestQueue();

        onReturnAccountSecurty();

//        umShareAPI= UMShareAPI.get(this);

        platformMap=new HashMap<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        onReturnAccountSecurty();
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_account_security;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
        accountPhoneBind.setOnClickListener(this);
        accountQqBind.setOnClickListener(this);
        accountWeiboBind.setOnClickListener(this);
        accountWechatBind.setOnClickListener(this);
        accountAlterUsername.setOnClickListener(this);
        accountBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.account_phone_bind:
                Intent bindIntent=new Intent(baseContext,BindPhoneActivity.class);
                startActivity(bindIntent);
                break;
            case R.id.account_qq_bind:
                if (qq_Bind){
                    //解绑
                    onUnBind("qq");
                }else {
                    //绑定qq
                    platformMap.put("platform","qq");
                    onBind(SHARE_MEDIA.QQ,platformMap);
                }

                break;
            case R.id.account_wechat_bind:
                if (wechat_Bind){
                    //解绑
                    onUnBind("wechat");
                }else {
                    //绑定wechat
                    platformMap.put("platform","wechat");
                    onBind(SHARE_MEDIA.WEIXIN,platformMap);
                }
                break;
            case R.id.account_weibo_bind:
                if (weibo_Bind){
                    //解绑
                    onUnBind("weibo");
                }else {
                    //绑定weibo
                    platformMap.put("platform","weibo");
                    onBind(SHARE_MEDIA.SINA,platformMap);
                }
                break;
            case R.id.account_back:
                finish();
                break;
            case R.id.account_alter_username:
                Intent Alterintent=new Intent(baseContext,AlterUserPasActivity.class);
                startActivity(Alterintent);
                break;
        }
    }

    //解绑手机
    private void onUnBind(final String platform){
        unBindMap=new HashMap<>();
        unBindMap.put("platform",platform);

        UnBindRequest unbind=new UnBindRequest(unBindMap, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {

                ToastUtil.showToast(getString(R.string.app_tips_text13));

                onReturnAccountSecurty();
            }
        });
        requestQueue.add(unbind);

    }

    //绑定第三方

    //友盟登录授权
    private void onBind(final SHARE_MEDIA platform, final Map<String,String> platformMap){

        //申请授权
        umShareAPI.doOauthVerify(this, platform, new UMAuthListener() {

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                Toast.makeText( getApplicationContext(), "授权成功", Toast.LENGTH_SHORT).show();

                String openid=map.get("openid");
                if (openid==null){
                    openid=map.get("uid");
                }
                platformMap.put("openid",openid);
//                //     获取用户信息
                getUserInfo(platform,platformMap);

            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                Toast.makeText( getApplicationContext(), "授权失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                Toast.makeText( getApplicationContext(), "取消授权", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getUserInfo(final SHARE_MEDIA platform, final Map<String,String> platformMap){
        umShareAPI.getPlatformInfo(AccountSecurityActivity.this, platform, new UMAuthListener() {

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, final Map<String, String> map) {



                String nick_name=map.get("nickname");
                if (nick_name==null){
                    nick_name=map.get("screen_name");
                }
                platformMap.put("nick_name",nick_name);

                //  绑定第三方平台
                BindPlatformRequest bindPlat=new BindPlatformRequest(platformMap, new VolleyListener() {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        ToastUtil.showToast("绑定成功");

//                        bindstatus=true;
//                        if (platform.equals("wechat")){
//                            onWechatBind(bindstatus);
//                        }else  if (platform.equals("qq")){
//
//                            onQqBind(bindstatus);
//                        } else if (platform.equals("weibo")) {
//
//                            onWeiBoBind(bindstatus);
//                        }
                        onReturnAccountSecurty();
                    }
                });
                requestQueue.add(bindPlat);

            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                LogUtil.i("tag", "获取用户信息失败onerrot");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                LogUtil.i("tag", "取消获取用户信息");
            }
        });



    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        umShareAPI.onActivityResult(requestCode,resultCode,data);
    }




    //账户安全的回调 判断绑定了哪个接口
    public void onReturnAccountSecurty() {
        final AccountSecurityRequest securityRequest = new AccountSecurityRequest(new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                securityData = GsonUtil.parse(response, AccountSecurityData.class);

                //对安全界面进行初始化
                accountText.setText("您正在使用" + securityData.getData().getLogin_type() + "账号登陆");

                if (securityData.getData().isWeibo_bind()) {
                    weibo_Bind=true;
                    onWeiBoBind(weibo_Bind);
                } else {
                    weibo_Bind=false;
                    onWeiBoBind(weibo_Bind);
                }
                if (securityData.getData().isQq_bind()) {
                    qq_Bind=true;
                    onQqBind(qq_Bind);
                } else {
                    qq_Bind=false;
                    onQqBind(qq_Bind);
                }
                if (securityData.getData().isWechat_bind()) {
                    wechat_Bind=true;
                    onWechatBind(wechat_Bind);
                } else {
                    wechat_Bind=false;
                    onWechatBind(wechat_Bind);
                }

                if (securityData.getData().isMobile_bind()){
                    onMobileBind(true,securityData.getData().getMobile());
                }else {
                    onMobileBind(false,"");
                }

            }
        });
        requestQueue.add(securityRequest);
    }


    private void onMobileBind(boolean b,String mobile){
        if (b){
            accountPhoneNum.setVisibility(View.VISIBLE);
            accountPhoneText.setVisibility(View.VISIBLE);
            accountAlterUsername.setVisibility(View.VISIBLE);
            accountPhoneText.setText(getString(R.string.app_tips_text14)+mobile);

            accountPhoneBind.setVisibility(View.INVISIBLE);
            accountPhoneSrc.setVisibility(View.INVISIBLE);
            accountPhonePhonebind.setVisibility(View.INVISIBLE);

        }else {
            accountPhoneNum.setVisibility(View.GONE);
            accountPhoneText.setVisibility(View.GONE);
            accountAlterUsername.setVisibility(View.GONE);

            accountPhoneBind.setVisibility(View.VISIBLE);
            accountPhoneSrc.setVisibility(View.VISIBLE);
            accountPhonePhonebind.setVisibility(View.VISIBLE);
        }
    }

    private void onWeiBoBind(boolean b) {
        if (b) {
            accountWeiboTextbind.setText("解绑");
            accountWeiboSrc.setImageResource(R.drawable.security_weibo_bind);
        } else {
            accountWeiboTextbind.setText("绑定");
            accountWeiboSrc.setImageResource(R.drawable.security_weibo_unbind);
        }
    }

    private void onWechatBind(boolean b) {
        if (b) {
            accountWechatTextbind.setText("解绑");
            accountWechatSrc.setImageResource(R.drawable.security_weixin_bind);
        } else {
            accountWechatTextbind.setText("绑定");
            accountWechatSrc.setImageResource(R.drawable.security_weixin_unbind);
        }
    }

    private void onQqBind(boolean b) {
        if (b) {
            accountQqTextbind.setText("解绑");
            accountQqSrc.setImageResource(R.drawable.security_qq_bind);
        } else {
            accountQqTextbind.setText("绑定");
            accountQqSrc.setImageResource(R.drawable.security_qq_unbind);
        }
    }



}
