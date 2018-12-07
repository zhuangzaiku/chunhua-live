package com.quootta.mdate.engine.myCenter;

import com.quootta.mdate.base.StringForJsonRequest;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.utils.LogUtil;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by kky on 2016/4/18.
 */
public class BillRechargeCheckRequest extends StringForJsonRequest {
    public BillRechargeCheckRequest(Map<String, String> paramsMap, Response.Listener<JSONObject> listener) {
        super(Method.GET, LocalUrl.GET_BILL_RECHARGE_CHECK, paramsMap, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.e("BillRechargeCheckRequest", "Error:" + error);
            }
        });
    }
}
