package com.quootta.mdate.engine.chat;

import com.quootta.mdate.base.StringForJsonRequest;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.utils.LogUtil;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Ryon on 2016/3/9.
 * email:para.ryon@foxmail.com
 */
public class InviteOperateRequest extends StringForJsonRequest {
    public InviteOperateRequest(Map<String, String> paramsMap, Response.Listener<JSONObject> listener) {
        super(Method.POST, LocalUrl.POST_INVITE_OPERATE, paramsMap, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.e("InviteOperateRequest", "Error:" + error);
            }
        });
    }
}
