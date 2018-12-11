package com.quootta.mdate.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.domain.RechargeCheckData;
import com.quootta.mdate.engine.media.BalanceRequest;
import com.quootta.mdate.engine.media.GroupCallRequest;
import com.quootta.mdate.engine.myCenter.BillRechargeCheckRequest;
import com.quootta.mdate.engine.myCenter.ChargeRequest;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.ui.dialog.GroupCallDialog;
import com.quootta.mdate.ui.dialog.MyProgressDialog;
import com.quootta.mdate.ui.popupWindow.ChargePopupWindow;
import com.quootta.mdate.utils.ActivityUtil;
import com.quootta.mdate.utils.GsonUtil;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.android.volley.RequestQueue;
import com.pingplusplus.android.PaymentActivity;
import com.pingplusplus.android.PingppLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

public class ChargeActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.iv_back_title_bar)ImageView iv_back;
    @Bind(R.id.tv_title_bar)TextView tv_title;
    @Bind(R.id.tv_right_title_bar)TextView withdrao_title;
    @Bind(R.id.btn_gold1_charge_activity)Button btn_gold1;
//    @Bind(R.id.btn_gold2_charge_activity)Button btn_gold2;
//    @Bind(R.id.btn_gold3_charge_activity)Button btn_gold3;
//    @Bind(R.id.btn_gold4_charge_activity)Button btn_gold4;
//    @Bind(R.id.btn_gold5_charge_activity)Button btn_gold5;
//    @Bind(R.id.btn_gold6_charge_activity)Button btn_gold6;
//    @Bind(R.id.btn_gold7_charge_activity)Button btn_gold7;
//    @Bind(R.id.btn_gold8_charge_activity)Button btn_gold8;
    @Bind(R.id.bill)LinearLayout bill;
    @Bind(R.id.bill_charge)TextView charge;
    @Bind(R.id.bill_weixin)ImageView billWeixin;
    @Bind(R.id.bill_zhifubao) ImageView billZhifu;
//    @Bind(R.id.btn_wechat_charge_activity)Button btn_wechat_charge;
//    @Bind(R.id.btn_alipay_charge_activity)Button btn_alipay_charge;

    public final static int TYPE_WECHAT = 0;
    public final static int TYPE_ALIPAY = 1;
    private static final int REQUEST_CODE_PAYMENT = 1;
    private final int RATIO_100 = 100;

    private RequestQueue requestQueue;
    private int sum = 0;
    private String billId;
    private Map<String, String> paramsMap;
    private boolean isPay=true;
    private Button selectedBtn = null;
    private ChargePopupWindow chargePopupWindow;
    //bill_type 0是微信充值 1是支付宝充值
    private int bill_type;
    @Override
    protected void init() {
        PingppLog.DEBUG = true;
        requestQueue = BaseApp.getRequestQueue();
        paramsMap = new HashMap<>();

        bill_type=0;
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_charge;
    }

    @Override
    protected void initData() {


        initTitle();
        getBanlance();
    }

    private void initTitle() {
        tv_title.setText(getString(R.string.app_tips_text24));
        withdrao_title.setText(R.string.withdraw);
        withdrao_title.setVisibility(View.VISIBLE);
        iv_back.setVisibility(View.VISIBLE);

        //提现按钮的监听事件
        withdrao_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent withdrawIntent = new Intent(baseContext, WithdrawActivity.class);
               startActivity(withdrawIntent);
            }
        });

        billWeixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bill_type=0;
                billWeixin.setImageResource(R.mipmap.pay_weixin_blue);
                billZhifu.setImageResource(R.mipmap.pay_zhifubao_gray);
            }
        });

        billZhifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bill_type=1;
                billZhifu.setImageResource(R.mipmap.pay_zhifubao_blue);
                billWeixin.setImageResource(R.mipmap.pay_weixin_gray);
            }
        });

    }

private void getBanlance(){
    final MyProgressDialog myProgressDialog = new MyProgressDialog(baseContext);
    myProgressDialog.show();
    Map<String, String> balanceMap = new HashMap<String, String>();
    BalanceRequest balanceRequest = new BalanceRequest(balanceMap, new VolleyListener() {
        @Override
        protected void onSuccess(JSONObject response) {
            try {
                JSONObject data = response.getJSONObject("data");
                String balance = data.getString("balance");
                LogUtil.d("requestBalance", balance);
                BaseApp.setGoldCount(balance);
                //  tvBalance.setText(balance + getString(R.string.coin));
                Log.i("tag","余额--》"+balance);
                charge.setText(balance);
                myProgressDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    });
    requestQueue.add(balanceRequest);

}



    @Override
    protected void setListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.finishActivty();
            }
        });
        bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent billIntent=new Intent(ChargeActivity.this,BillActivity.class);
                startActivity(billIntent);
            }
        });
//        btn_wechat_charge.setOnClickListener(wechatOnClickListener);
//        btn_alipay_charge.setOnClickListener(alipayOnClickListener);
        btn_gold1.setOnClickListener(this);
//        btn_gold2.setOnClickListener(this);
//        btn_gold3.setOnClickListener(this);
//        btn_gold4.setOnClickListener(this);
//        btn_gold5.setOnClickListener(this);
//        btn_gold6.setOnClickListener(this);
//        btn_gold7.setOnClickListener(this);
//        btn_gold8.setOnClickListener(this);
    }

//    View.OnClickListener wechatOnClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            if (sum == 0) {
//                ToastUtil.showToast("请先选择支付金额");
//            } else {
//                btn_wechat_charge.setOnClickListener(null);
//                btn_alipay_charge.setOnClickListener(null);
//                requestCharge(TYPE_WECHAT);
//            }
//        }
//    };
//
//    View.OnClickListener alipayOnClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            if (sum == 0) {
//                ToastUtil.showToast("请先选择支付金额");
//            } else {
//                btn_wechat_charge.setOnClickListener(null);
//                btn_alipay_charge.setOnClickListener(null);
//                requestCharge(TYPE_ALIPAY);
//            }
//        }
//    };

    @Override
    public void onClick(View v) {
        if(selectedBtn != null)
            selectedBtn.setSelected(false);
        switch (v.getId()){

            case R.id.btn_gold1_charge_activity:
                btn_gold1.setSelected(true);
                selectedBtn = btn_gold1;
                sum = 6;
                break;
//            case R.id.btn_gold2_charge_activity:
//                btn_gold2.setSelected(true);
//                selectedBtn = btn_gold2;
//                sum = 50;
//                break;
//            case R.id.btn_gold3_charge_activity:
//                btn_gold3.setSelected(true);
//                selectedBtn = btn_gold3;
//                sum = 98;
//                break;
//            case R.id.btn_gold4_charge_activity:
//                btn_gold4.setSelected(true);
//                selectedBtn = btn_gold4;
//                sum = 198;
//                break;
//            case R.id.btn_gold5_charge_activity:
//                btn_gold5.setSelected(true);
//                selectedBtn = btn_gold5;
//                sum = 588;
//                break;
//            case R.id.btn_gold6_charge_activity:
//                btn_gold6.setSelected(true);
//                selectedBtn = btn_gold6;
//                sum = 998;
//                break;
//            case R.id.btn_gold7_charge_activity:
//                btn_gold7.setSelected(true);
//                selectedBtn = btn_gold7;
//                sum = 1998;
//                break;
//            case R.id.btn_gold8_charge_activity:
//                btn_gold8.setSelected(true);
//                selectedBtn = btn_gold8;
//                sum = 4998;
//                break;
        }
        //showPopUpWindow();
        //使用isPay防止点击多次
        if (isPay){
            isPay=false;
            showVharge();
        }else {
            ToastUtil.showToast(getString(R.string.app_tips_text25));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPay=true;
    }

    private void showVharge(){

//        switch (bill_type){
//            case 0:
//                //取消掉ping++的支付  使用微信原生支付
//               // requestCharge(TYPE_WECHAT);
//                PayUtil.WechatPay(this,sum* RATIO_100);
//
//                break;
//            case 1:
//                requestCharge(TYPE_ALIPAY);
//                isPay=true;
//                break;
//        }

        Intent intent = new Intent(this,PayActivity.class);
        intent.putExtra("num",sum* RATIO_100);
        startActivityForResult(intent,10001);
    }

    private void showPopUpWindow() {
        chargePopupWindow = new ChargePopupWindow(baseContext, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_alipay:
                        requestCharge(TYPE_ALIPAY);
                        break;
                    case R.id.btn_wechat:
                        requestCharge(TYPE_WECHAT);
                        break;
                }
            }
        });
        chargePopupWindow.showAtLocation(findViewById(R.id.ll_charge_activity),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void requestCharge(int type) {
        switch (type) {
            case TYPE_WECHAT:
                paramsMap.put("type","wx");
                paramsMap.put("channel",BaseApp.getChannelName(this));
                break;
            case TYPE_ALIPAY:
                paramsMap.put("type","alipay");
                paramsMap.put("channel",BaseApp.getChannelName(this));
                break;
        }
        paramsMap.put("sum", sum * RATIO_100 + "");
        ChargeRequest chargeRequest = new ChargeRequest(paramsMap, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                LogUtil.d("ChargeActivity", "Response:" + response);
                try {
                    JSONObject dataJson = response.getJSONObject("data");
                    String charge = dataJson.getString("charge");
                    billId = dataJson.getString("_id");
                    Intent chargeIntent = new Intent(ChargeActivity.this, PaymentActivity.class);
                    chargeIntent.putExtra(PaymentActivity.EXTRA_CHARGE, charge);
                    startActivityForResult(chargeIntent, REQUEST_CODE_PAYMENT);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        requestQueue.add(chargeRequest);
    }


//    /**
//     * onActivityResult 获得支付结果，如果支付成功，服务器会收到ping++ 服务器发送的异步通知。
//     * 最终支付成功根据异步通知为准
//     */
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
////        btn_wechat_charge.setOnClickListener(wechatOnClickListener);
////        btn_alipay_charge.setOnClickListener(alipayOnClickListener);
//
//        Log.i("tag","--requestcode--->"+requestCode+"---resultcode--->"+resultCode+"---data--->"+data);
//        requestRechargeCheck(requestCode, resultCode, data);
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10001 && resultCode == 10000){
            getBanlance();
        }
    }

    private void requestRechargeCheck(final int requestCode, final int resultCode, final Intent data) {
        final Map<String, String> rechargeCheckParams = new HashMap<>();
        rechargeCheckParams.put("bill_id", billId);
        BillRechargeCheckRequest billRechargeCheckRequest = new BillRechargeCheckRequest(rechargeCheckParams, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                try {
                    LogUtil.i("BillRechargeCheckRequest",response.getString("msg"));
                    //支付页面返回处理
                    if (requestCode == REQUEST_CODE_PAYMENT) {
                        if (resultCode == Activity.RESULT_OK) {
                            String result = data.getExtras().getString("pay_result");

                            /* 处理返回值
                             * "success" - payment succeed
                             * "fail"    - payment failed
                             * "cancel"  - user canceld
                             * "invalid" - payment plugin not installed
                             */
                            switch (result) {
                                case "success":
                                    result = "支付已成功,支付结果显示可能会有延时";
                                    Intent freshIntent = new Intent("com.quootta.mdate.REFRESH_VIEW");
                                    freshIntent.putExtra("type", MainActivity.GOLD_AMOUNT);
                                    sendBroadcast(freshIntent);
                                    //刷新余额
                                    getBanlance();

                                    Log.i("tag","获得response的值"+response);
                                    RechargeCheckData rechargeCheckData= GsonUtil.parse(response,RechargeCheckData.class);
                                    //判断是否是第一次充值
                                    if (rechargeCheckData.getData().getCharge().is_alert_groupcall()){
                                        showGroupCallDialog();
                                    }else {
                                        finish();
                                    }

                                    break;
                                case "fail":
                                    result = "支付失败";
                                    break;
                                case "cancel":
                                    result = "用户取消支付";
                                    break;
                                case "invalid":
                                    result = "支付插件未安装，详情请联系客服";
                                    break;
                            }
                            LogUtil.e("ChargeActivity","ErrorMsg: " + data.getExtras().getString("error_msg"));// 错误信息
                            LogUtil.e("ChargeActivity","ExtraMsg: " + data.getExtras().getString("extra_msg"));// 错误信息

                            ToastUtil.showToast(result);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        requestQueue.add(billRechargeCheckRequest);
    }

    private void showGroupCallDialog(){
        final GroupCallDialog groupCallDialog=new GroupCallDialog(ChargeActivity.this, new GroupCallDialog.onFirstClickListener() {
            @Override
            public void onFirstClick(String gender) {
                Map<String, String> paramsMap = new HashMap<String, String>();
                paramsMap.put("gender", gender);
                paramsMap.put("num", 0 + "");
                GroupCallRequest callRequest = new GroupCallRequest(paramsMap, new VolleyListener() {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        Log.i("tag", "groupcall----response--》" + response);
//                        MyMessageDialog messageDialog = new MyMessageDialog(baseContext, "提示", "一大波帅哥美女正在向你走来~");
//                        messageDialog.show();
                        ToastUtil.showToast(getString(R.string.app_tips_text26));
                        finish();
                    }

                });
                requestQueue.add(callRequest);
            }
        }, new GroupCallDialog.onCashBtnListener() {
            @Override
            public void onfinish() {

            }
        });
        groupCallDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (chargePopupWindow != null) {
            chargePopupWindow.dismiss();
        }
    }

    //    public void showMsg(String title) {
//        String str = title;
////        if (null !=msg1 && msg1.length() != 0) {
////            str += "\n" + msg1;
////        }
////        if (null !=msg2 && msg2.length() != 0) {
////            str += "\n" + msg2;
////        }
//        AlertDialog.Builder builder = new AlertDialog.Builder(ChargeActivity.this);
//        builder.setMessage(str);
//        builder.setTitle("充值提示");
//        builder.setPositiveButton("确定", null);
//        builder.create().show();
//    }

}
