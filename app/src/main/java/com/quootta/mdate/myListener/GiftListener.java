package com.quootta.mdate.myListener;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.quootta.mdate.R;
import com.quootta.mdate.domain.OnOffData;
import com.quootta.mdate.engine.config.OnOffRequest;
import com.quootta.mdate.ui.popupWindow.GiftPopuWindow;
import com.quootta.mdate.utils.GsonUtil;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.android.volley.Response;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.rong.callkit.SingleCallActivity;

import static com.quootta.mdate.base.BaseApp.requestQueue;

/**
 * Created by Ryon on 2016/12/2/0002.
 * 视频送礼物监听
 */

public class GiftListener implements SingleCallActivity.onGiftClick {


    public GiftListener(){

    }

    @Override
    public void onSendGift(final Context context, final String id) {
        String version;

        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(),0);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version = packInfo.versionName;


        Map<String,String> OnOffMap=new HashMap<>();
        OnOffMap.put("android_version",version);
        //礼物开关
        final OnOffRequest onOffRequest=new OnOffRequest(OnOffMap,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtil.i("chatActivity","全局开关--》"+response);
                 OnOffData onOffData= GsonUtil.parse(response,OnOffData.class);
                if (onOffData.getData().getConfig()!=null){

                    if (onOffData.getData().getConfig().getChatlive().equals("on")){
                        //显示礼物按钮
                        Intent intent=new Intent(context, GiftPopuWindow.class);
                        intent.putExtra("uid",id);
                        context.startActivity(intent);
                    }else {
                        ToastUtil.showLongToast(context.getString(R.string.app_tips_text1));
                    }
                }

            }
        });
        requestQueue.add(onOffRequest);

    }
}
