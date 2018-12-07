package com.quootta.mdate.ui.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.domain.SesameParam;
import com.quootta.mdate.engine.myCenter.SesameCheckRequest;
import com.quootta.mdate.engine.myCenter.SesameVerifyRequest;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.ui.dialog.MyProgressDialog;
import com.quootta.mdate.utils.ActivityUtil;
import com.quootta.mdate.utils.GsonUtil;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.android.volley.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

public class SesameVerifyActivity extends BaseActivity {

    @Bind(R.id.iv_back_title_bar)ImageView iv_back;
    @Bind(R.id.tv_title_bar)TextView tv_title;

    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.et_number)
    EditText etNumber;
    @Bind(R.id.btn_binding)
    Button btnBinding;

    private RequestQueue requestQueue;
    private String mName;
    private String mNumber;
    private MyProgressDialog myProgressDialog;
    private SesameParam sesameParam;
    private Map<String, String> paramsMap;

    @Override
    protected void init() {
        paramsMap = new HashMap<>();
        requestQueue = BaseApp.getRequestQueue();
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_sesame_verify;
    }

    @Override
    protected void initData() {
        initTitle();
    }

    private void initTitle() {
        tv_title.setText(getString(R.string.sesame_verify));
        iv_back.setVisibility(View.VISIBLE);
    }

    @Override
    protected void setListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.finishActivty();
            }
        });

        btnBinding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mName = etName.getText().toString().trim();
                mNumber = etNumber.getText().toString().trim();
                if (mName == null) {
                    ToastUtil.showToast("请输入姓名");
                } else if (mNumber == null) {
                    ToastUtil.showToast("请输入身份证号");
                } else {
                    requestSesameVerify();
                }
            }
        });
    }

    private void requestSesameVerify() {
        final Map<String, String> requestMap = new HashMap<>();
        try {
            requestMap.put("name", URLEncoder.encode(mName, "UTF-8"));
            requestMap.put("card_number", URLEncoder.encode(mNumber, "UTF-8"));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        myProgressDialog = new MyProgressDialog(baseContext);
        myProgressDialog.show();
        SesameVerifyRequest sesameVerifyRequest = new SesameVerifyRequest(requestMap, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                try {
                    sesameParam = GsonUtil.parse(response.getString("data"), SesameParam.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    //请求授权
//                    CreditAuthHelper.creditAuth(SesameVerifyActivity.this, LocalUrl.SESAME_ID, sesameParam.params, sesameParam.sign, new HashMap<String, String>(), new ICreditListener() {
//                        @Override
//                        public void onComplete(Bundle result) {
//                            myProgressDialog.dismiss();
//                            //toast message
//                            ToastUtil.showToast("授权成功");
//                            //从result中获取params参数,然后解析params数据,可以获取open_id。
//                            if (result != null) {
//                                Set<String> keys = result.keySet();
//                                for (String key : keys) {
//                                    Log.d(TAG, key + " = " + result.getString(key));
//                                    paramsMap.put(key, result.getString(key));
//                                }
//                                requestSesameCheck();
//                            }
//                        }
//
//                        @Override
//                        public void onError(Bundle result) {
//                            myProgressDialog.dismiss();
//                            //toast message
//                            ToastUtil.showToast("授权错误");
//                            Log.d(TAG, "DemoPresenterImpl.doCreditAuthRequest.onError.");
//                        }
//
//                        @Override
//                        public void onCancel() {
//                            myProgressDialog.dismiss();
//                            //toast message
//                            ToastUtil.showToast("授权失败");
//                            Log.d(TAG, "DemoPresenterImpl.doCreditAuthRequest.onCancel.");
//                        }
//                    });
                } catch (Exception e) {
                    Log.e(TAG, "DemoPresenterImpl.doCreditAuthRequest.exception=" + e.toString());
                }
            }
        });

        requestQueue.add(sesameVerifyRequest);
    }

    private void requestSesameCheck() {
        SesameCheckRequest sesameCheckRequest = new SesameCheckRequest(paramsMap, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                LogUtil.d(TAG, "response:" + response.toString());
            }
        });
        requestQueue.add(sesameCheckRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "DemoActivity.onActivityResult");
        //onActivityResult callback
    }
}
