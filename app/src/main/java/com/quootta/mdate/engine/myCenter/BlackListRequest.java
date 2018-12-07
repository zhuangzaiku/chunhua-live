package com.quootta.mdate.engine.myCenter;

import com.quootta.mdate.base.StringForJsonRequest;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.utils.LogUtil;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Ryon on 2016/7/8/0008.
 */
public class BlackListRequest extends StringForJsonRequest {

    private final static int ADD = 0;
    private final static int REMOVE = 1;

    public BlackListRequest(int type, Map<String, String> paramsMap, Response.Listener<JSONObject> listener) {
        super(Method.POST, type == ADD ? LocalUrl.POST_ADD_BLACKLIST : LocalUrl.POST_REMOVE_BLACKLIST, paramsMap, listener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        LogUtil.e("BlackListRequest", "Error:" + error.toString());
                    }
                });
    }
}
