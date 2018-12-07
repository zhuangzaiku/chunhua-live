package com.quootta.mdate.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.domain.OtherloginData;
import com.quootta.mdate.domain.PlatFormSignUpData;
import com.quootta.mdate.domain.UserChatInfoList;
import com.quootta.mdate.engine.account.OtherLoginRequest;
import com.quootta.mdate.engine.myCenter.OnlineRequest;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.utils.GsonUtil;
import com.quootta.mdate.utils.LogUtil;
import com.android.volley.RequestQueue;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

public class OtherLoginHelper {
    private Context baseContext;
    private Map<String, String> otherLoginMap;
    private String registrationID;
    private RequestQueue requestQueue;
    private OtherloginData loginData;
    private PlatFormSignUpData signUpData;
    private Map<String,String> signMap;
    private OnOtherLoginListener onOtherLoginListener;
    private OtherLoginRequest otherLoginRequest;
    private boolean isFirstLocate = true;
    private String city;
    private SharedPreferences pref;

    private SharedPreferences.Editor editor;
    private Map<String, String> paramsMap;

    public interface OnOtherLoginListener{
       void onLoginSuccess(UserChatInfoList userChatInfoList, String city);
     //void onLoginSuccess();
        void onLoginFail();
    }

    public OtherLoginHelper(Context context, OnOtherLoginListener onOtherLoginListener){
        baseContext=context;
        this.onOtherLoginListener=onOtherLoginListener;

        requestQueue= BaseApp.getRequestQueue();

        pref=baseContext.getSharedPreferences("login", BaseActivity.MODE_PRIVATE);

        //设备唯一标识符
        registrationID= JPushInterface.getRegistrationID(baseContext);

    }

    /**
     * 第一次登录走这里
     * @param otherLoginMap
     */
    public void otherLogin(final Map<String,String> otherLoginMap){
        this.otherLoginMap=otherLoginMap;
        this.otherLoginMap.put("device_id",registrationID);
        otherLoginRequest=new OtherLoginRequest(this.otherLoginMap, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                    Log.i("tag", "登录返回数据------>"+response.toString());

                    loginData= GsonUtil.parse(response,OtherloginData.class);
                    //判断是否注册
                    if (!loginData.getData().isFirstlogin()){
                        //不是第一次登录
                       //  getLocationDetail();
                        //保存用户信息
                        saveLoginInfo();

                        onOtherLoginListener.onLoginSuccess(null,city);
                    }else{
                        //第一次登录 注册
                        onOtherLoginListener.onLoginFail();
                    }
            }
        });

        requestQueue.add(otherLoginRequest);
    }
    /**
     * 请求在线，第三方非初次登录时，通过此方法进入应用
     */
    public void onOtherLine(){
        Map<String, String> onlineMap = new HashMap<>();
        onlineMap.put("status", "online");
        OnlineRequest onlineRequest = new OnlineRequest(onlineMap, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                LogUtil.d("LoginHelper", "requestOnline Response:" + response);
                LogUtil.d("time count", "online end time:" + System.currentTimeMillis());
                //getLocationDetail();
                onOtherLoginListener.onLoginSuccess(null,city);
            }
        });
        requestQueue.add(onlineRequest);
    }

    /**
     * 请求地理信息
     * 在成功回调中更新地理信息
     */
//    private void getLocationDetail() {
//        Log.i("tag", "--地理位置");
//
//        // TODO: 2016/7/5/0005 确认定位权限
//        AMapLocationClient locationClient = BaseApp.getLocationClient();
//        locationClient.setLocationListener(new AMapLocationListener() {
//            @Override
//            public void onLocationChanged(AMapLocation aMapLocation) {
//                if (isFirstLocate == true) {
//                    LogUtil.d("time count", "getLocationDetail end time:" + System.currentTimeMillis());
//                    uploadLocation(aMapLocation);
//                    isFirstLocate = false;
//                }
//
//            }
//        });
//        locationClient.startLocation();
//    }

    /**
     * 更新地理信息，将经纬度信息保存在本地
     * 在成功回调中链接融云服务器
     * @param aMapLocation
     */
//    private void uploadLocation(AMapLocation aMapLocation) {
//        Log.i("tag", "--经纬度");
//
//        saveLoginInfo(aMapLocation);//将登录信息保存到本地sp
//
//        city = aMapLocation.getCity();
//        LogUtil.d("LoginHelper", "city :" + aMapLocation.getCity());
//        LogUtil.d("time count", "getLocationDetail end time:" + System.currentTimeMillis());
//        connectRongIM();
//    }


    /**
     * 链接融云服务器，在结果中执行登录接口的回调
     */
//    private void connectRongIM() {
//        Log.i("tag", "------pref---"+pref.getString("ry_token", null));
//
//        ConnectRongHelper connectRongHelper = new ConnectRongHelper(pref.getString("ry_token", null), new ConnectRongHelper.OnConnectListener() {
//            @Override
//            public void onConnectSuccess(UserChatInfoList userChatInfoList) {
//
//                Log.i("tag", "ry_token--->"+pref);
//                onOtherLoginListener.onLoginSuccess(userChatInfoList, city);
//            }
//
//            @Override
//            public void onConnectFail() {
//
//            //    ToastUtil.showToast("登录过期 请重新登录");
//                Log.i("tag","融云请求错误");
//                Intent intent=new Intent(baseContext, OtherLoginActivity.class);
//                baseContext.startActivity(intent);
//            }
//        },baseContext);
//        connectRongHelper.connectRongIM();
//    }


    /**
     * 将登录相关信息保存到sp中
     */
    private void saveLoginInfo() {

        editor = pref.edit();
        if (otherLoginMap != null) {
            editor.putString("mobile",loginData.getData().getUser().get_id());
//            editor.putString("password", paramsMap.get("password"));
            editor.putString("device_id", registrationID);
            editor.putString("id",loginData.getData().getUser().get_id());
            editor.putString("ry_token",loginData.getData().getUser().getRy_token());
        }
//        editor.putString("lat", aMapLocation.getLatitude() + "");
//        editor.putString("lng", aMapLocation.getLongitude() + "");
        editor.apply();
        Log.d("time count", "saveLoginInfo end time:" + System.currentTimeMillis());
    }


//    /**
//     * 退出登录
//     */
//    public void logout() {
//        LogUtil.d("LoginHelper", "logout");
//        //获取SharedPreferences相关
//        //清除账号信息
//        pref = baseContext.getSharedPreferences("login", baseContext.MODE_PRIVATE);
//        editor = pref.edit();
//        editor.clear();
//        editor.apply();
//        //清除Cookie信息
//        pref = baseContext.getSharedPreferences("CookieStore", baseContext.MODE_PRIVATE);
//        editor = pref.edit();
//        editor.clear();
//        editor.apply();
//
//        //发送离线请求
//        LogoutRequest logoutRequest = new LogoutRequest(
//                new VolleyListener() {
//                    @Override
//                    protected void onSuccess(JSONObject response) {
//                        try {
//                            RongIM.getInstance().disconnect();
//                            LogUtil.d("LoginHelper", "Response:" + response.toString());
//                            ToastUtil.showToast(response.getString("msg"));
//                            finishActivity();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//        requestQueue.add(logoutRequest);
//    }
//
//    /**
//     * 退出登录后结束相关Activity
//     */
//    private void finishActivity() {
//        ActivityUtil.finishAllActivity();
//        Intent loginIntent = new Intent();
//        loginIntent.setAction("com.quootta.mdate.OtherLoginActivity");
//        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        baseContext.startActivity(loginIntent);
//    }
}
