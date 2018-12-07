package com.quootta.mdate.engine.account;

import com.quootta.mdate.base.StringForJsonRequest;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.utils.LogUtil;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kky on 2016/4/18.
 */
public class LogoutRequest extends StringForJsonRequest {
    public LogoutRequest(Response.Listener<JSONObject> listener) {
        super(Method.GET, LocalUrl.GET_LOGOUT, new HashMap<String, String>(), listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.e("LogoutRequest", "Error: "+ error);
            }
        });
    }
}
