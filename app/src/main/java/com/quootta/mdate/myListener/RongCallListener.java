package com.quootta.mdate.myListener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.SurfaceView;

import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.domain.UserDetail;
import com.quootta.mdate.engine.RyMesRequest;
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

import io.rong.callkit.BaseCallActivity;
import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallCommon;
import io.rong.calllib.RongCallSession;

/**
 * Created by Ryon on 2016/9/3.
 * email:para.ryon@foxmail.com
 */
public class RongCallListener implements BaseCallActivity.IBaseCallListener {

    private final String BALANCE_NOT_ENOUGH = "2002";
    private final int TYPE_AUDIO = 1;
    private final int ONE_SECOND = 1000;

    private RequestQueue requestQueue;
    private int currentType;
    private UserDetail targetUserDetail;
    private int targetVideoPrice;
    private int targetAudioPrice;
    private Timer timer;
    private Timer mtime;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String RyMes;
    private static final  int BALANCE=999999999;

    private Timer screenTime;
    public RongCallListener(UserDetail targetUserDetail, Context context) {
        this.targetUserDetail = targetUserDetail;
        requestQueue = BaseApp.getRequestQueue();

        targetAudioPrice = Integer.parseInt(targetUserDetail.audio_pay);
        targetVideoPrice = Integer.parseInt(targetUserDetail.video_pay);

        pref=context.getSharedPreferences("iscall",0);
        editor=pref.edit();

        //动态注册广播接收器 没有关闭！！！
        context.registerReceiver(mCloseTime,new IntentFilter("com.quootta.mdate.mCloseTimer"));

        RyMes="执行构造 ";
        requestMes(RyMes);
    }

    /**
     * 电话已拨出。
     * 主叫端拨出电话后，通过回调 onCallOutgoing 通知当前 call 的详细信息。
     *
     * @param callProfile call 会话信息。
     * @param localVideo  本地 camera 信息。
     */
    @Override
    public void onCallOutgoing(RongCallSession callProfile, SurfaceView localVideo) {
        LogUtil.d("onCallOutgoing", "data init");
        RyMes=RyMes+"--> 电话拨出 ";
        requestMes(RyMes);
        if( isUserCallingOut(callProfile) ) {
            requestCallCount(callProfile, "dialing", 0);
            RyMes=RyMes+"--> 用户为主叫方且电话拨出 ";

            requestMes(RyMes);

        }

    }

    /**
     * 已建立通话。
     * 通话接通时，通过回调 onCallConnected 通知当前 call 的详细信息。
     *
     * @param callProfile call 会话信息。
     * @param localVideo  本地 camera 信息。
     */
    @Override
    public void onCallConnected(final RongCallSession callProfile, final SurfaceView localVideo) {


        LogUtil.i("RongCallListener","建立通话");


        if (isUserCallingOut(callProfile)) {

            //用户为主叫方才判断状态

            // 用来判断通话状态
            editor.putBoolean("isnormal",true);
            //设置为false 不能拨打电话 只有正常退出才能拨打
            editor.putBoolean("close",false);
            Log.i("tag","现在是不是正常退出 rongcall测试"+pref.getBoolean("close",true));

            editor.apply();


            final double activeTime = System.currentTimeMillis();
            currentType = callProfile.getMediaType().getValue();
            final int price = currentType == TYPE_AUDIO ?
                    targetAudioPrice : targetVideoPrice;
//            releaseTimer();

            RyMes=RyMes+"--> 用户为主叫方且建立通话 ";
            requestMes(RyMes);


            if (timer!=null){
                LogUtil.i("tag","timer不为null");
            }else {
                LogUtil.i("tag","timer为null");
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        int sec = (int) (System.currentTimeMillis() - activeTime) / 1000;
                        int min = (int) sec/60 ;
                        int amount = (int) (min + 1) * price;//本次通话的费用



                        // LogUtil.d("onCallConnected", "min：" + min + "\nsec：" + sec + "\namount：" + amount);
                        LogUtil.i("RongCallListener","min:"+min+ "\nsec：" + sec + "\namount：" + amount);
                        requestAVPay(callProfile, amount, sec, false);
                        int curGold = BaseApp.getGoldCount();//拥有的金币

                        BaseApp.setGoldCount(curGold + "");



                        //扣除通话费用后剩余的费用 安全起见 通话费用多扣除一分钟
                        int banlnace= BaseApp.getGoldCount()-(amount+price);
                        BaseApp.setBalnace(banlnace);
                        LogUtil.i("tag","banlance ---->扣除通话时间后剩下的金钱-通话费用扣除一分钟-->>"+ BaseApp.getBalnace());

//
                        Log.i("tag","通话计时--"+"amout-->"+amount+"--price---"+price+"----baseapp.getgoldcount--->"+BaseApp.getGoldCount());


                        if (amount+price>BaseApp.getGoldCount()){
                            //挂断通话
                            mtime=new Timer();
                            LogUtil.i("tag","进入内部");
                            mtime.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    LogUtil.i("tag","一分钟自动挂断");
//                                    Intent intent = new Intent("io.rong.imkit.HANGUP_BROADCAST");
//                                    BaseApp.getApplication().sendBroadcast(intent);

                                    Log.i("tag","---callid--->"+callProfile.getCallId()+"----calluserid--->"+callProfile.getCallerUserId());
                                    //挂断电话
                                    RongCallClient.getInstance().hangUpCall(callProfile.getCallId());

                                    mtime.cancel();
                                }
                            },58*ONE_SECOND);

                            //关闭计时器线程
                            timer.cancel();
                        }

                        curGold -= price;
                        LogUtil.i("RongCallListener","curGold减去价钱之后---->"+curGold);
                    }
                },ONE_SECOND,60*ONE_SECOND);
            }

        }
    }



    @Override
    public void onRemoteUserLeft(String userId, RongCallCommon.CallDisconnectedReason reason) {
        LogUtil.d("onRemoteUserLeft", "userId:" + userId + ",reason" + reason.getValue());
    }


    /**
     * 通话结束。
     * 通话中，对方挂断，己方挂断，或者通话过程网络异常造成的通话中断，都会回调 onCallDisconnected。
     *
     * @param callProfile call 会话信息。
     * @param reason      通话中断原因。
     */
    @Override
    public void onCallDisconnected(RongCallSession callProfile, RongCallCommon.CallDisconnectedReason reason) {
        LogUtil.i("RongCallListener","通话结束");

        if (screenTime!=null){
            screenTime.cancel();
            screenTime=null;
        }


        //通话结束 把值设置为BALANCE 999999999
        BaseApp.setBalnace(BALANCE);

        if(isUserCallingOut(callProfile) ) {   //用户为主叫方
            if (callProfile.getActiveTime() > 0) { //接通时间不为零
                LogUtil.i("RongCallListener","通话结束原因"+reason);

                RyMes=RyMes+"--> 用户为主叫方且通话结束 且通话结束原因为"+reason;
                requestMes(RyMes);

//                if (reason.toString().equals("NETWORK_ERROR")){
//                    LogUtil.i("RongCallListener","网络异常结束");
//                    // mtime.cancel();
//                    timer.cancel();
//
//
//                }else {
                    int sec = (int) (System.currentTimeMillis() - callProfile.getActiveTime()) / 1000;
                    int min = (int) sec / 60;
                    int price = currentType == TYPE_AUDIO ?
                            targetAudioPrice : targetVideoPrice;
                    int num = (int) (min + 1) * price;

                    LogUtil.d("onCallDisconnected", "sec:" + sec + "num" + num);

                    requestCallCount(callProfile, "active", sec);
                    requestAVPay(callProfile, num, sec, true);

                    //只要结束通话 关闭计时器
                    releaseTimer();
             //   }
            }
        }

    }

    @Override
    public void onError(RongCallCommon.CallErrorCode errorCode) {
        LogUtil.e("onError","errorCode:" + errorCode);
         releaseTimer();
    }

    /**
     * 上传用户通话日志信息
     */
    public void requestMes(String mes){
        Map<String,String> RyMesMap=new HashMap<>();
        RyMesMap.put("msg",mes);
        RyMesRequest ryMesRequest=new RyMesRequest(RyMesMap, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {

            }
        });
        requestQueue.add(ryMesRequest);
    }


    /**
     * 上传通话信息统计数据
     * @param callProfile
     * @param status
     * @param sec
     */
    public void requestCallCount(RongCallSession callProfile, String status, int sec) {

        RyMes=RyMes+"--> 上传通话信息统计数据 ";
        requestMes(RyMes);

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

    /**
     * 上传通话费用
     * @param callProfile
     * @param num
     */
    private void requestAVPay(final RongCallSession callProfile, final int num, final int sec, final boolean isFinish) {

        RyMes=RyMes+"--> 上传通话费用 ";
        requestMes(RyMes);

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


                        // if (balance != null) {
                        releaseTimer();
                        LogUtil.d("initAVInterface AVPayRequest", "balance:" + balance);
                        BaseApp.setGoldCount(balance);//修改本地金币余额

                        // }
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
     * 判断用户是否是主叫方
     * @param callProfile
     * @return
     */
    private boolean isUserCallingOut(RongCallSession callProfile) {
        LogUtil.i("userid","获取到的用户id--->"+callProfile.getCallerUserId()+"用户详情的用户id-->"+targetUserDetail.sid);
        return callProfile.getCallerUserId().equals(targetUserDetail.sid);
    }

    //按下home键后(mini窗口状态)结束音视频发送的广播 接收广播 关闭计时器
    BroadcastReceiver mCloseTime=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            releaseTimer();
            RyMes=RyMes+"--> 用户在缩小通话界面的情况下结束了应用程序 ";
            BaseApp.setBalnace(BALANCE);
            requestMes(RyMes);
        }
    };



    public void releaseTimer() {
        if (timer != null) {
            LogUtil.i("tag","关闭计时器");
            timer.cancel();
            timer = null;


            //用来判断通话状态
            editor.putBoolean("isnormal",false);
            editor.apply();
        }
    }
}
