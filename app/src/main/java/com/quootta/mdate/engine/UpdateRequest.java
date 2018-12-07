package com.quootta.mdate.engine;

import com.quootta.mdate.base.StringForJsonRequest;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.myListener.VolleyErrorListener;
import com.android.volley.Response;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by kky on 2016/4/11.
 */
public class UpdateRequest extends StringForJsonRequest {
    public UpdateRequest(Map<String, String> paramsMap, Response.Listener<JSONObject> listener, VolleyErrorListener errorListener) {
        super(Method.GET, LocalUrl.CHECK_UPDATE, paramsMap, listener, errorListener);
    }
}
