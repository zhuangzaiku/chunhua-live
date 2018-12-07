package com.quootta.mdate.utils;

import android.content.Context;
import android.util.Log;

import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.domain.WxPayList;
import com.quootta.mdate.domain.WxPayRequestData;
import com.quootta.mdate.engine.pay.WxPayRequest;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ryon on 2017/2/16/0016.
 */

public class PayUtil {


    public static void WechatPay(final Context context,int num){
        final RequestQueue requestQueue= BaseApp.getRequestQueue();

        //向服务器发起请求
        final Map<String,String> WxpayMap=new HashMap<>();
        WxpayMap.put("channel",BaseApp.getChannelName(context));
        WxpayMap.put("sum",num+"");
     //   Log.i("tag","请求---");
        WxPayRequest Wxrequest=new WxPayRequest(WxpayMap, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Log.i("tag","支付response----》"+response);
                WxPayList list=new WxPayList();

                    list=GsonUtil.parse(response,WxPayList.class);

                    if (list!=null){
                        WxSendPay(context,list.getData());

                        //保存id 回调时使用
                        WxPayRequestData._id=list.getData().get_id();


                    }

            }
        });
        requestQueue.add(Wxrequest);
    }



    private static void WxSendPay(Context context,WxPayList.DataBean list){
        IWXAPI api= WXAPIFactory.createWXAPI(context,null);
        api.registerApp(BaseApp.getWXAPPID());
        PayReq request = new PayReq();

//        Log.i("tag","---发送的数据展示---"+"--appid--"+BaseApp.getWXAPPID()
//        +"--partnerid--"+BaseApp.getWXPARTNERID()+"-prepayid--"+list.getPrepay_id()
//        +"--packagevalue--"+"Sign=WXPay"+"---noncestr--"+list.getNonce_str()+"--timestamp--"+list.getTimeStamp()
//        +"-----sign---"+list.getSign()
//        );

        request.appId = BaseApp.getWXAPPID();
        request.partnerId =BaseApp.getWXPARTNERID();
        request.prepayId= list.getPrepay_id();
        request.packageValue ="Sign=WXPay";
        request.nonceStr = list.getNonce_str();
        request.timeStamp = String.valueOf(list.getTimeStamp());
        request.sign= list.getSign();

        Log.i("tag",api.sendReq(request)+"---------发送--------");

    }

}
