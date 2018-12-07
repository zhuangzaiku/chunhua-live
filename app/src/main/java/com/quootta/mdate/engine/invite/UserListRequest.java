package com.quootta.mdate.engine.invite;

import android.util.Log;

import com.quootta.mdate.base.StringForJsonRequest;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.utils.LogUtil;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Ryon on 2016/3/7.
 * email:para.ryon@foxmail.com
 */
public class UserListRequest extends StringForJsonRequest {

    public final static int POPULAR = 0;
    public final static int CITY = 1;
    public final static int NEWEST = 2;

    public UserListRequest(Map<String, String> paramsMap, Response.Listener<JSONObject> listener) {
        super(Method.GET, LocalUrl.GET_LIST, paramsMap, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.e("UserListRequest onErrorResponse:", error.toString());
            }
        });
    }

    public UserListRequest(Map<String, String> paramsMap, int type, Response.Listener<JSONObject> listener) {
        super(Method.GET, type == POPULAR ? LocalUrl.GET_HOT : LocalUrl.GET_NEWEST, paramsMap, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("tag","热门列表错误---》"+error.getMessage());
//                byte[] htmlBodyBytes=error.networkResponse.data;
//                Log.e("tag","热门列表获取错误信息---》"+error.networkResponse.data);
//                error.printStackTrace();
            }
        });
    }
}
