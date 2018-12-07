package com.quootta.mdate.engine.myCenter;

import com.quootta.mdate.base.StringForJsonRequest;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.utils.LogUtil;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Ryon on 2016/5/17/0017.
 */
public class SysMessageRequest extends StringForJsonRequest {
    public SysMessageRequest(Response.Listener<JSONObject> listener) {
        super(Method.GET, LocalUrl.GET_NOTICE, new HashMap<String, String>(), listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.d("SysMessageRequest", "Error:" + error);
            }
        });
    }
}
