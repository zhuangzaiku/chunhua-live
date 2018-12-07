package com.quootta.mdate.engine.myCenter;

import com.quootta.mdate.base.StringForJsonRequest;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.utils.LogUtil;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Ryon on 2017/1/4/0004.
 */

public class UpdateAvatarRequest extends StringForJsonRequest {
    public UpdateAvatarRequest(Map<String, String> paramsMap, Response.Listener<JSONObject> listener) {
        super(Method.POST, LocalUrl.POST_AVATAR_UPDATE, paramsMap, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.e("UpdateAvatarRequest", "Error: " + error);
            }
        });
    }
}
