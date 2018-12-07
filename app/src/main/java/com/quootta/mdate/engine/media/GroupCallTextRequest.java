package com.quootta.mdate.engine.media;

import com.quootta.mdate.base.StringForJsonRequest;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.utils.LogUtil;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Ryon on 2016/11/30/0030.
 */

public class GroupCallTextRequest extends StringForJsonRequest {
    public GroupCallTextRequest(Response.Listener<JSONObject> listener) {
        super(Method.GET, LocalUrl.GET_GROUP_CALL_TEXT,new HashMap<String, String>(),listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.e("GroupCallTextRequest", "Error:" + error.toString());
            }
        });
    }
}
