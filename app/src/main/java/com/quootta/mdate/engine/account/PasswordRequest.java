package com.quootta.mdate.engine.account;

import com.quootta.mdate.base.StringForJsonRequest;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.utils.LogUtil;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by kky on 2016/3/28.
 */
public class PasswordRequest extends StringForJsonRequest {
    public PasswordRequest(Map<String, String> paramsMap, Response.Listener<JSONObject> listener) {
        super(Method.POST, LocalUrl.POST_PASSWORD_RESET, paramsMap, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.d("PasswordRequest", "Error:" + error);
            }
        });
    }
}
