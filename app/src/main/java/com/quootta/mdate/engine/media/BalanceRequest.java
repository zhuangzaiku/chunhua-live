package com.quootta.mdate.engine.media;

import com.quootta.mdate.base.StringForJsonRequest;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Ryon on 2016/7/4/0004.
 */
public class BalanceRequest extends StringForJsonRequest {
    public BalanceRequest(Map<String, String> paramsMap, Response.Listener<JSONObject> listener) {
        super(Method.GET, LocalUrl.GET_BALANCE, paramsMap, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.e("BalanceRequest", "Error:" + error.toString());
                ToastUtil.showToast("您的网络有些问题 请稍后重试");
            }
        });
    }
}
