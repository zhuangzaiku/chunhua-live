package com.quootta.mdate.engine.myCenter;

import android.util.Log;

import com.quootta.mdate.base.StringForJsonRequest;
import com.quootta.mdate.constant.LocalUrl;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Map;

public class BannerRequest extends StringForJsonRequest {

    public BannerRequest(Map<String, String> paramsMap, Response.Listener<JSONObject> listener) {
        super(Method.GET, LocalUrl.GET_BANNER, paramsMap, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("tag","bannner eooro--》"+error.getNetworkTimeMs());
                Log.i("tag","bannerrequest-->error"+error.getMessage());
            }
        });
    }


    public BannerRequest(Response.Listener<JSONObject> listener,Map<String, String> paramsMap) {
        super(Method.POST, LocalUrl.GET_SHARE, paramsMap, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }
}
