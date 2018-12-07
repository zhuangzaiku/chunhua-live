package com.quootta.mdate.engine.gift;

import com.quootta.mdate.base.StringForJsonRequest;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.utils.LogUtil;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Ryon on 2016/11/11/0011.
 */
public class GiftReceive extends StringForJsonRequest {
    public GiftReceive( Response.Listener<JSONObject> listener) {
        super(Method.GET, LocalUrl.GET_LIST_RECEIVE, new HashMap<String, String>(), listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.e("GiftReceive","Error:" + error.toString());
            }
        });
    }
}
