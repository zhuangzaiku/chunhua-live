package com.quootta.mdate.engine.gift;

import com.quootta.mdate.base.StringForJsonRequest;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Ryon on 2016/11/9/0009.
 */
public class GiftSendRequest extends StringForJsonRequest {
    public GiftSendRequest(Map<String,String> giftSendMap, Response.Listener<JSONObject> listener) {
        super(Method.POST, LocalUrl.POST_GIFT_SEND_TO, giftSendMap, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.e("tag","Error:" + error.toString());
                ToastUtil.showToast("您的网络有些不稳定  请稍后重新发送礼物");
            }
        });
    }
}
