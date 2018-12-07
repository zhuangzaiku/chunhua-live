package com.quootta.mdate.engine.myCenter;

import com.quootta.mdate.base.StringForJsonRequest;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.utils.LogUtil;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 请求参数为空，所以在super方法中直接new一个空map即可
 */
public class InfoDetailRequest extends StringForJsonRequest {
    public InfoDetailRequest(Response.Listener<JSONObject> listener) {
        super(Method.GET, LocalUrl.GET_INFO_DETAIL, new HashMap<String, String>(), listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.e("InfoDetailRequest", "Error:" + error);
            }
        });
    }
}
