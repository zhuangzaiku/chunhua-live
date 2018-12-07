package com.quootta.mdate.engine.myCenter;

import com.quootta.mdate.base.StringForJsonRequest;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by kky on 2016/3/22.
 */
public class ChargeRequest extends StringForJsonRequest {
    public ChargeRequest(Map<String, String> paramsMap, Response.Listener<JSONObject> listener) {
        super(Method.GET, LocalUrl.GET_BILL_RECHARGE, paramsMap, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.e("ChargeRequest", "Error:" + error);
                ToastUtil.showToast("网络连接超时，请退出本页面重新进入");
            }
        });
    }
}
