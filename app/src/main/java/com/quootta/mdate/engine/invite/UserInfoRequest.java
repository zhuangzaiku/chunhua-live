package com.quootta.mdate.engine.invite;

import android.util.Log;

import com.quootta.mdate.base.StringForJsonRequest;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.utils.ToastUtil;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Ryon on 2016/5/6/0006.
 */
public class UserInfoRequest extends StringForJsonRequest{

    public UserInfoRequest(Map<String, String> paramsMap, Response.Listener<JSONObject> listener) {
        super(Method.GET, LocalUrl.GET_BATCH, paramsMap, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("UserInfoRequest", "Error: " + error);
                ToastUtil.showLongToast("现在网络有些问题  请重新登录");
            }
        });
    }
}
