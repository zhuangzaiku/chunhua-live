package com.quootta.mdate.engine.invite;

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
public class UserDetailRequest extends StringForJsonRequest {

    public UserDetailRequest(Map<String, String> paramsMap, Response.Listener<JSONObject> listener) {
        super(Method.GET, LocalUrl.GET_DETAIL, paramsMap, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.e("UserDetailRequest",error.toString());
            }
        });
    }
}
