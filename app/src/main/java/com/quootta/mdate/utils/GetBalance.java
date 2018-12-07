package com.quootta.mdate.utils;

import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.engine.media.BalanceRequest;
import com.quootta.mdate.myListener.VolleyListener;
import com.android.volley.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ryon on 2016/11/9/0009.
 */
public class GetBalance {

    public static void Balance(){
        RequestQueue requestQueue;
        requestQueue= BaseApp.getRequestQueue();

        Map<String, String> balanceMap = new HashMap<String, String>();
        BalanceRequest balanceRequest = new BalanceRequest(balanceMap, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");
                    String balance = data.getString("balance");
                    LogUtil.d("requestBalance", balance);
                    BaseApp.setGoldCount(balance);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        requestQueue.add(balanceRequest);
    }

}
