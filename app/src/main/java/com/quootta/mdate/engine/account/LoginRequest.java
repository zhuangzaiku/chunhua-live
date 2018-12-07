package com.quootta.mdate.engine.account;

import com.quootta.mdate.base.StringForJsonRequest;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.utils.LogUtil;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Map;


/**
 * Created by Ryon on 2016/3/3.
 * email:para.ryon@foxmail.com
 */
public class LoginRequest extends StringForJsonRequest {
    public LoginRequest(Map<String, String> paramsMap, Response.Listener<JSONObject> listener) {
        super(Method.POST, LocalUrl.POST_LOGIN, paramsMap, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.e("LoginRequest onErrorResponse:", error.toString());
            }
        });
    }

}
