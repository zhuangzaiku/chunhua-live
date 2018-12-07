package com.quootta.mdate.engine.myCenter;

import com.quootta.mdate.base.StringForJsonRequest;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.utils.LogUtil;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Ryon on 2016/5/16/0016.
 */
public class FavoriteRequest extends StringForJsonRequest{

    private final static int ADD = 0;
    private final static int REMOVE = 1;

    public FavoriteRequest(Map<String, String> paramsMap, int type, Response.Listener<JSONObject> listener) {
        super(Method.POST, type == ADD ? LocalUrl.POST_MY_LIKE_ADD : LocalUrl.POST_MY_LIKE_REMOVE, paramsMap, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.d("FavoriteRequest", "Error: " + error);
            }
        });
    }
}
