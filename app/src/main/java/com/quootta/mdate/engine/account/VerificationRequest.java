package com.quootta.mdate.engine.account;

import com.quootta.mdate.base.StringForJsonRequest;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.utils.LogUtil;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by kky on 2016/3/24.
 */
public class VerificationRequest extends StringForJsonRequest {
    public VerificationRequest(Map<String, String> paramsMap, Response.Listener<JSONObject> listener) {
        super(Method.POST, LocalUrl.POST_SIGN_UP_SMS, paramsMap, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.e("VerificationRequest", "Error:"+error);
            }
        });
    }
}
