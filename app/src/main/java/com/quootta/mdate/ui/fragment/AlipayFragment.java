package com.quootta.mdate.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.base.BaseFragment;
import com.quootta.mdate.engine.myCenter.WithdrawRequest;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.utils.ActivityUtil;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.SecretUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.android.volley.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

public class AlipayFragment extends BaseFragment {

    @Bind(R.id.et_amount_alipay_fragment)EditText et_amount;
    @Bind(R.id.et_account_alipay_fragment)EditText et_account;
    @Bind(R.id.et_payee_name_alipay_fragment)EditText et_payee_name;
    @Bind(R.id.et_password_alipay_fragment)EditText et_password;
    @Bind(R.id.btn_ensure_alipay_fragment)Button btn_ensure;

    private Context baseContext;
    private RequestQueue requestQueue;
    private Map<String, String> paramsMap;
    private int goldAmount;

    //更新邀约列表和金额相关常量
    public static final int INVITE_ME = 0;
    public static final int MY_INVITE = 1;
    public static final int GOLD_AMOUNT = 2;

    @Override
    protected int getRootView() {
        return R.layout.fragment_alipay;
    }

    @Override
    protected void setListener() {
        btn_ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_amount.getText().toString().trim().equals("")){
                    Toast.makeText(baseContext, "请输入提现金额", Toast.LENGTH_SHORT).show();
                } else if(et_account.getText().toString().trim().equals("")) {
                    Toast.makeText(baseContext, "请输入支付宝账号", Toast.LENGTH_SHORT).show();
                } else if(et_payee_name.getText().toString().trim().equals("")) {
                    Toast.makeText(baseContext, "请输入收款人姓名", Toast.LENGTH_SHORT).show();
                } else if(et_password.getText().toString().trim().equals("")) {
                    Toast.makeText(baseContext, "请输入密码", Toast.LENGTH_SHORT).show();
                } else {
                    int sumInt = Integer.parseInt(et_amount.getText().toString().trim());
                    if (sumInt >= 100) {
                        paramsMap.put("sum",sumInt + "");
                        paramsMap.put("password", SecretUtil.bytesToMD5(et_password.getText().toString().trim()));
                        paramsMap.put("alipay_account",et_account.getText().toString().trim());
                        paramsMap.put("alipay_user",et_payee_name.getText().toString().trim());
                        requestWithdraw(paramsMap);
                    } else {
                        ToastUtil.showToast("提现金额不能少于100元");
                    }

                }
            }
        });
    }

    private void requestWithdraw(Map<String, String> paramsMap) {
        WithdrawRequest withdrawRequest = new WithdrawRequest(paramsMap,
                new VolleyListener() {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        LogUtil.d("AlipayFragment", "Response:" + response);
                        try {
                            Toast.makeText(baseContext,response.getString("msg"),Toast.LENGTH_SHORT).show();
                            requestRefresh();
                            ActivityUtil.finishActivty();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
        requestQueue.add(withdrawRequest);
    }

    private void requestRefresh() {
        Intent freshIntent = new Intent("com.quootta.mdate.REFRESH_VIEW");
        freshIntent.putExtra("type", GOLD_AMOUNT);
        baseContext.sendBroadcast(freshIntent);
        LogUtil.d("AlipayFragment", "onRequestRefresh");
    }

    @Override
    protected void init() {

        requestQueue = BaseApp.getRequestQueue();
        paramsMap = new HashMap<>();
        paramsMap.put("type","alipay");
    }

    @Override
    protected void initData(View view) {
        baseContext = view.getContext();
        goldAmount = BaseApp.getGoldCount();
        int cash = goldAmount / 100 ;

        if (cash > 5000) {
            et_amount.setHint(getString(R.string.withdraw_amount_hint) + 5000 + getString(R.string.yuan));
        } else {
            et_amount.setHint(getString(R.string.withdraw_amount_hint) + cash + getString(R.string.yuan));
        }
    }

}
