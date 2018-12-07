package com.quootta.mdate.engine.security;

import com.quootta.mdate.base.StringForJsonRequest;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.utils.LogUtil;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Ryon on 2016/9/22/0022.
 */
public class AccountSecurityRequest extends StringForJsonRequest {

    public AccountSecurityRequest(Response.Listener<JSONObject> listener) {
        super(Method.GET, LocalUrl.GET_ACCOUNT_SECURITY, new HashMap<String, String>(), listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.e("AccountSecurityRequest", "Error:" + error);
            }
        });
    }
}
