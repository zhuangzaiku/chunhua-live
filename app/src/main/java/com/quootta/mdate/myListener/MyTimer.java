package com.quootta.mdate.myListener;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.domain.UserDetail;
import com.quootta.mdate.engine.media.AVPayRequest;
import com.quootta.mdate.engine.media.CallCountRequest;
import com.quootta.mdate.utils.LogUtil;
import com.android.volley.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.rong.calllib.RongCallSession;

/**
 * Created by Ryon on 2016/11/17/0017.
 * 因为出现没有上传用户账单问题 暂时不使用这个这个工具类
 */
public class MyTimer {
    private static MyTimer myTimer=null;
    private final int ONE_SECOND = 1000;
    private Timer timer;


    private int price;

    private RequestQueue requestQueue;
    private int currentType;
    private final int TYPE_AUDIO = 1;
    private final String BALANCE_NOT_ENOUGH = "2002";
    private int targetVideoPrice;
    private int targetAudioPrice;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private  double activeTime;
    private Timer mOneStopTime;
    public MyTimer(UserDetail targetUserDetail, Context context){

        Log.i("tag","单例初始化");
        requestQueue= BaseApp.requestQueue;

        pref=context.getSharedPreferences("iscall",0);
        editor=pref.edit();


        targetAudioPrice = Integer.parseInt(targetUserDetail.audio_pay);
        targetVideoPrice = Integer.parseInt(targetUserDetail.video_pay);
    }

//    public static MyTimer getInstance(UserDetail targetUserDetail,Context context){
//
//       if (myTimer==null){
//           Log.i("tag","为null new单例");
//           myTimer=new MyTimer(targetUserDetail,context);
//       }else {
//           Log.i("tag","不为null ");
//       }
//
//
//        return myTimer;
//    }


    public void onCallGoing(RongCallSession callproFile){

        requestCallCount(callproFile,"dialing",0);
    }

//建立通话时执行 打开计时器
    public void  openTime(final RongCallSession callProfile){



        //设置为false 不能拨打电话 只有正常退出才能拨打
        editor.putBoolean("close",false);
        //用来判断通话状态
        editor.putBoolean("isnormal",true);

        editor.apply();

        if (timer!=null){

            LogUtil.i("tag","timer不为null ");
        }else {
            LogUtil.i("tag","timer为null ");
            //通话开始时间
            activeTime=System.currentTimeMillis();
            //通话类型
            currentType = callProfile.getMediaType().getValue();
            price = currentType == TYPE_AUDIO ?
                    targetAudioPrice : targetVideoPrice;




            timer=new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    int sec = (int) (System.currentTimeMillis() - activeTime) / 1000;
                    int min = (int) sec/60 ;
                    int amount = (int) (min + 1) * price;//本次通话的费用

                    LogUtil.i("tag","min:"+min+ "\nsec：" + sec + "\namount：" + amount);

                    //上传通话费用
                    requestAVPay(callProfile, amount, sec, false);

                    //扣除通话费用后剩余的费用 安全起见 通话费用多扣除一分钟
                   int banlnace= BaseApp.getGoldCount()-(amount+price);
                    BaseApp.setBalnace(banlnace);
                    LogUtil.i("tag","banlance ---->扣除通话时间后剩下的金钱-通话费用扣除一分钟-->>"+ BaseApp.getBalnace());

//
//                    if (amount> BaseApp.getGoldCount()){
//                        //价格不足以支持本次消费
//                        Intent intent = new Intent("io.rong.imkit.HANGUP_BROADCAST");
//
//                        BaseApp.getApplication().sendBroadcast(intent);
//                        LogUtil.i("tag","价格不足以支持本次消费");
//                    }else
                    if (amount+price> BaseApp.getGoldCount()){
                        //挂断通话
                        mOneStopTime = new Timer();
                        LogUtil.i("tag", "价格不足以支持下一分钟的消费");
                        mOneStopTime.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                //一分钟内中断通话
                                Intent intent = new Intent("io.rong.imkit.HANGUP_BROADCAST");
                                BaseApp.getApplication().sendBroadcast(intent);

                                if (mOneStopTime!=null){
                                    mOneStopTime.cancel();
                                    mOneStopTime=null;
                                }
                                //关闭计时器
                                stopTime();
                            }
                        }, 58 * ONE_SECOND);

                    }



                }
            }, ONE_SECOND, 60 * ONE_SECOND);
        }


    }


    //通话结束执行方法
    public void onCallDisConn(RongCallSession callProfile){

        //结束计时器
        stopTime();
        //??????????????????????????为什么用系统给的时间
        int sec = (int) (System.currentTimeMillis() - callProfile.getActiveTime()) / 1000;
        Log.i("tag","-----sec--->"+sec);
        int min = (int) sec / 60;
        int price = currentType == TYPE_AUDIO ?
                            targetAudioPrice : targetVideoPrice;
        int num = (int) (min + 1) * price;

        LogUtil.d("onCallDisconnected", "sec:" + sec + "num" + num);

        //上传通话信息统计数据
        requestCallCount(callProfile, "active", sec);
        //上传通话费用
        requestAVPay(callProfile, num, sec, true);

    }



    public void stopTime(){
        if (timer != null) {
            LogUtil.i("tag","单例z--->>>关闭计时器");
            timer.cancel();
            timer = null;

            //用来判断通话状态
            editor.putBoolean("isnormal",false);
            editor.apply();
        }
    }


    /**
     * 上传通话费用
     * @param callProfile
     * @param num
     */
    private void requestAVPay(final RongCallSession callProfile, final int num, final int sec, final boolean isFinish) {
        LogUtil.d("AVPayRequest", "num" + num);
        Map<String, String> payMap = new HashMap<String, String>();
        payMap.put("user_id", callProfile.getTargetId());//拨打对象的ID
        payMap.put("type",
                callProfile.getMediaType().getValue() == 1 ? "audio" : "video");//通话类型
        payMap.put("num", num + "");//总费用
        payMap.put("sec", sec + "");//通话秒数
        payMap.put("is_finish", isFinish + "");//是否正常结束通话

        LogUtil.i("tag","上传通话费用"+payMap.get("num"));

        LogUtil.i("tag","is_finish---->"+isFinish+"");

        //记录每次上传通话费用的时间
        editor.putLong("time", System.currentTimeMillis());
        editor.apply();

        AVPayRequest avPayRequest = new AVPayRequest(payMap, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                LogUtil.d("initAVInterface AVPayRequest", "response:" + response);
                if (isFinish) {
                    try {

                        //正常结束通话 设置为true 可以继续通话
                        editor.putBoolean("close",true);
                        editor.apply();

                        LogUtil.i("tag","上传通话费用成功--->"+response);
                        JSONObject jsonObject = response.getJSONObject("data");
                        String balance = jsonObject.getString("balance");


                        //关闭计时器
                        stopTime();

                        LogUtil.d("initAVInterface AVPayRequest", "balance:" + balance);
                        BaseApp.setGoldCount(balance);//修改本地金币余额


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            protected void onFail(JSONObject response) {
                try {
                    if(response.getString("code").equals(BALANCE_NOT_ENOUGH)){
                        LogUtil.d("initAVInterface AVPayRequest", "onFail:BALANCE_NOT_ENOUGH");
                        if(BaseApp.getGoldCount() <= num) {
                            LogUtil.i("tag","进入fail");
                            requestAVPay(callProfile, num, sec, true);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        requestQueue.add(avPayRequest);
    }

    /**
     * 上传通话信息统计数据
     * @param callProfile
     * @param status
     * @param sec
     */
    public void requestCallCount(RongCallSession callProfile, String status, int sec) {
        Map<String, String> callMap = new HashMap<String, String>();
        callMap.put("to_user_id", callProfile.getTargetId());
        callMap.put("type",
                callProfile.getMediaType().getValue() == TYPE_AUDIO ? "audio" : "video");
        callMap.put("status", status);
        if (status.equals("active")) {
            LogUtil.d("initAVInterface CallCountRequest", "active");
            sec = (int) (System.currentTimeMillis() - callProfile.getActiveTime()) / 1000;
            callMap.put("sec", sec + "");
        }

        CallCountRequest callCountRequest = new CallCountRequest(callMap, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                LogUtil.d("initAVInterface CallCountRequest", "response:" + response);
                LogUtil.i("tag","上传通话信息统计数据"+response);
            }
        });
        requestQueue.add(callCountRequest);
    }



}
