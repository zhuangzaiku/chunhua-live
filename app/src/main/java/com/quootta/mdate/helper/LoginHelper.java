package com.quootta.mdate.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.domain.MyData;
import com.quootta.mdate.domain.UserChatInfoList;
import com.quootta.mdate.engine.account.LoginRequest;
import com.quootta.mdate.engine.account.LogoutRequest;
import com.quootta.mdate.engine.myCenter.OnlineRequest;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.utils.ActivityUtil;
import com.quootta.mdate.utils.GsonUtil;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.android.volley.RequestQueue;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;

/**
 * 登录Helper
 *
 * Created by kky on 2016/4/6.
 */
public class LoginHelper {

    private Context baseContext;
    private RequestQueue requestQueue;
    private MyData myData;
    private Map<String, String> paramsMap;//登录账号，密码，设备id
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String registrationID;
    private String city;
    private OnLoginListener onLoginListener;
    private boolean isFirstLocate = true;
    public interface OnLoginListener {
        void onLoginSuccess(UserChatInfoList userChatInfoList, String city);
        void onLoginFail();
    }


    public LoginHelper(Context context, OnLoginListener onLoginListener) {
        baseContext = context;
        this.onLoginListener = onLoginListener;
        requestQueue = BaseApp.getRequestQueue();
        pref = baseContext.getSharedPreferences("login", BaseActivity.MODE_PRIVATE);
        registrationID = JPushInterface.getRegistrationID(baseContext);
    }

    /**
     * 请求登录，初次登录时，通过此方法进入应用
     * 在成功回调里请求地理信息
     */
    public void login(Map<String, String> paramsMap) {
        LogUtil.d("LoginHelper", "login");
        this.paramsMap = paramsMap;
        this.paramsMap.put("device_id", registrationID);
        LoginRequest loginRequest = new LoginRequest(this.paramsMap,
                new VolleyListener() {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        LogUtil.d("LoginHelper", "onResponse: " + response.toString());
                        try {
                            myData = GsonUtil.parse(response.getString("data"), MyData.class);
                            saveLoginInfo();

    //                        getLocationDetail();
                            onLoginListener.onLoginSuccess(null, city);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFail(JSONObject response) {
                        super.onFail(response);
                        onLoginListener.onLoginFail();
                    }
                });
        requestQueue.add(loginRequest);
    }

    /**
     * 请求在线，非初次登录时，通过此方法进入应用
     * 在成功回调里请求地理信息
     */
    public void online() {
        LogUtil.d("time count", "online start time:" + System.currentTimeMillis());
        Map<String, String> onlineMap = new HashMap<>();
        onlineMap.put("status", "online");
        OnlineRequest onlineRequest = new OnlineRequest(onlineMap, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                LogUtil.d("LoginHelper", "requestOnline Response:" + response);
                LogUtil.d("time count", "online end time:" + System.currentTimeMillis());
              // getLocationDetail();
                onLoginListener.onLoginSuccess(null,city);
            }
        });
        requestQueue.add(onlineRequest);
    }

    /**
     * 请求地理信息
     * 在成功回调中更新地理信息
     */
//    private void getLocationDetail() {
//        LogUtil.d("LoginHelper", "getLocationDetail");
//        // TODO: 2016/7/5/0005 确认定位权限
//        AMapLocationClient locationClient = BaseApp.getLocationClient();
//        locationClient.setLocationListener(new AMapLocationListener() {
//            @Override
//            public void onLocationChanged(AMapLocation aMapLocation) {
//                if (isFirstLocate==true) {
//                    uploadLocation(aMapLocation);
//                    isFirstLocate = false;
//                }
//
//            }
//        });
//        locationClient.startLocation();
//    }
//
//    /**
//     * 更新地理信息，将经纬度信息保存在本地
//     * 在成功回调中链接融云服务器
//     * @param aMapLocation
//     */
//    private void uploadLocation(AMapLocation aMapLocation) {
//
//        LogUtil.d("LoginHelper", "uploadLocation");
////        Map<String, String> paramsMap = new HashMap<>();
////        paramsMap.put("lat", aMapLocation.getLatitude() + "");
////        paramsMap.put("lng", aMapLocation.getLongitude() + "");
//        saveLoginInfo(aMapLocation);//将登录信息保存到本地sp
//
//        city = aMapLocation.getCity();
//        LogUtil.d("LoginHelper", "city :" + aMapLocation.getCity());
//        connectRongIM();
//
//    }

    /**
     * 链接融云服务器，在结果中执行登录接口的回调
     *
     * 2016/9/7 update:将链接融云操作移到MainActivity
     */
//    private void connectRongIM() {
//        LogUtil.d("time count", "connectRongIM start time:" + System.currentTimeMillis());
//        ConnectRongHelper connectRongHelper = new ConnectRongHelper(pref.getString("ry_token", null), new ConnectRongHelper.OnConnectListener() {
//            @Override
//            public void onConnectSuccess(UserChatInfoList userChatInfoList) {
//                LogUtil.d("time count", "connectRongIM end time:" + System.currentTimeMillis());
//                onLoginListener.onLoginSuccess(userChatInfoList, city);
//            }
//
//            @Override
//            public void onConnectFail() {
//                onLoginListener.onLoginFail();
//            }
//        },baseContext);
//        connectRongHelper.connectRongIM();
//    }

    /**
     * 将登录相关信息保存到sp中
     */
    private void saveLoginInfo() {
        LogUtil.d("LoginHelper", "saveLoginInfo");
        editor = pref.edit();
        if (paramsMap != null) {
            editor.putString("mobile", paramsMap.get("mobile"));
            editor.putString("password", paramsMap.get("password"));
            editor.putString("device_id", registrationID);
            editor.putString("id",myData._id);
            editor.putString("ry_token", myData.ry_token);
        }
//        editor.putString("lat", aMapLocation.getLatitude() + "");
//        editor.putString("lng", aMapLocation.getLongitude() + "");
//        editor.apply();
        editor.apply();
    }

    /**
     * 退出登录
     */
    public void logout() {
        LogUtil.d("LoginHelper", "logout");
        //获取SharedPreferences相关
        //清除账号信息
        pref = baseContext.getSharedPreferences("login", baseContext.MODE_PRIVATE);
        editor = pref.edit();
        editor.clear();
        editor.apply();
        //清除Cookie信息
        pref = baseContext.getSharedPreferences("CookieStore", baseContext.MODE_PRIVATE);
        editor = pref.edit();
        editor.clear();
        editor.apply();
        Log.i("tag","注销------------》");
        //发送离线请求
        LogoutRequest logoutRequest = new LogoutRequest(
                new VolleyListener() {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        try {
                            RongIM.getInstance().disconnect();
                            LogUtil.d("LoginHelper", "Response:" + response.toString());
                            ToastUtil.showToast(response.getString("msg"));
                            finishActivity();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        requestQueue.add(logoutRequest);
    }

    /**
     * 退出登录后结束相关Activity
     */
    private void finishActivity() {
        ActivityUtil.finishAllActivity();
        Intent loginIntent = new Intent();
        loginIntent.setAction("com.quootta.mdate.OtherLoginActivity");
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        baseContext.startActivity(loginIntent);
    }
}
