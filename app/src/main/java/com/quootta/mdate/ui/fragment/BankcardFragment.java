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
import com.android.volley.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

public class BankcardFragment extends BaseFragment {

    @Bind(R.id.et_amount_bankcard_fragment)EditText et_amount;
    @Bind(R.id.et_owner_name_bankcard_fragment)EditText et_owner_name;
    @Bind(R.id.et_card_number_bankcard_fragment)EditText et_card_number;
    @Bind(R.id.et_bank_name_bankcard_fragment)EditText et_bank_name;
    @Bind(R.id.et_bank_province_bankcard_fragment)EditText et_bank_province;
    @Bind(R.id.et_point_bankcard_fragment)EditText et_point;
    @Bind(R.id.et_password_bankcard_fragment)EditText et_password;
    @Bind(R.id.btn_ensure_bankcard_fragment)Button et_ensure;

    private Context baseContext;
    private RequestQueue requestQueue;
    private Map<String,String> paramsMap;
    private int goldAmount;

    //更新邀约列表和金额相关常量
    public static final int INVITE_ME = 0;
    public static final int MY_INVITE = 1;
    public static final int GOLD_AMOUNT = 2;

    @Override
    protected int getRootView() {
        return R.layout.fragment_bankcard;
    }

    @Override
    protected void setListener() {
        et_ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_amount.getText().toString().trim().equals("")){
                    Toast.makeText(baseContext, "请输入提现金额", Toast.LENGTH_SHORT).show();
                } else if(et_owner_name.getText().toString().trim().equals("")) {
                    Toast.makeText(baseContext, "请输入开卡姓名", Toast.LENGTH_SHORT).show();
                } else if(et_card_number.getText().toString().trim().equals("")) {
                    Toast.makeText(baseContext, "请输入银行卡卡号", Toast.LENGTH_SHORT).show();
                } else if(et_bank_name.getText().toString().trim().equals("")) {
                    Toast.makeText(baseContext, "请输入开户银行名称", Toast.LENGTH_SHORT).show();
//                }else if(et_bank_province.getText().toString().trim().equals("")) {
//                    Toast.makeText(baseContext, "请输入银行开户省市", Toast.LENGTH_SHORT).show();
//                } else if(et_point.getText().toString().trim().equals("")) {
//                    Toast.makeText(baseContext, "请输入开户银行支行", Toast.LENGTH_SHORT).show();
                } else if(et_password.getText().toString().trim().equals("")) {
                    Toast.makeText(baseContext, "请输入密码", Toast.LENGTH_SHORT).show();
                }  else {
                    int sumInt = Integer.parseInt(et_amount.getText().toString().trim());
                    paramsMap.put("sum",sumInt + "");
                    paramsMap.put("password", SecretUtil.bytesToMD5(et_password.getText().toString().trim()));
                    paramsMap.put("card_bank",et_bank_name.getText().toString().trim());
                    paramsMap.put("card_city",et_bank_province.getText().toString().trim());
                    paramsMap.put("card_branch",et_point.getText().toString().trim());
                    paramsMap.put("card_account",et_card_number.getText().toString().trim());
                    paramsMap.put("card_user", et_owner_name.getText().toString().trim());
                    requestWithdraw(paramsMap);
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
        LogUtil.d("BankcardFragment", "onRequestRefresh");
    }

    @Override
    protected void init() {

        requestQueue = BaseApp.getRequestQueue();
        paramsMap = new HashMap<>();
        paramsMap.put("type","card");

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
