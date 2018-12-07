package com.quootta.mdate.engine.security;

import com.quootta.mdate.base.StringForJsonRequest;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.utils.LogUtil;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Ryon on 2016/9/23/0023.
 */
public class BindPhoneSmsRequest extends StringForJsonRequest {
    public BindPhoneSmsRequest(Map<String, String> paramsMap, Response.Listener<JSONObject> listener) {
        super(Method.POST, LocalUrl.POST_BIND_PHONE_SMS, paramsMap, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.e("BindPhoneSmsRequest", "Error:"+error);
            }
        });
    }
}
