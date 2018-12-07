package com.quootta.mdate.engine.account;

import android.util.Log;

import com.quootta.mdate.base.StringForJsonRequest;
import com.quootta.mdate.constant.LocalUrl;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Ryon on 2016/9/6/0006.
 */
public class RankingListRequest extends StringForJsonRequest {

        public RankingListRequest(Map<String, String> paramsMap, Response.Listener<JSONObject> listener){
            super(Method.GET, LocalUrl.GET_RANKING_LIST, paramsMap, listener, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("tag","排行榜列表错误---》"+error.getMessage());

//                    Log.e("tag","排行榜列表获取错误信息---》"+error.networkResponse.data);
//                    error.printStackTrace();
                }
            });

        }
}

