package com.quootta.mdate.engine.config;

import com.quootta.mdate.base.StringForJsonRequest;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.utils.LogUtil;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Map;

public class OnOffRequest extends StringForJsonRequest {
    public OnOffRequest(Map<String,String> parseMap, Response.Listener<JSONObject> listener) {
        super(Request.Method.GET, LocalUrl.GET_SYS_CONFIG,parseMap, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.e("OnOffRequest","Error:" + error);
            }
        });
    }
}