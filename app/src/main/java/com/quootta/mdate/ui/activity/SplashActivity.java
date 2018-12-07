package com.quootta.mdate.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.domain.UserChatInfoList;
import com.quootta.mdate.helper.LoginHelper;
import com.quootta.mdate.helper.OtherLoginHelper;
import com.quootta.mdate.utils.ActivityUtil;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.android.volley.RequestQueue;

import java.util.HashMap;
import java.util.Map;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

/**
 * Created by Ryon on 2016/3/2.
 * email:para.ryon@foxmail.com
 */
public class SplashActivity extends BaseActivity {

    //pp助手多增加一秒闪屏时间
   // private final int SPLASH_DISPLAY_LENGHT = 300; //延迟0.3秒
    private final int SPLASH_DISPLAY_LENGHT =1500;

    private RequestQueue requestQueue;
    private SharedPreferences pref;
    private Map<String,String> paramsMap;
    private LoginHelper loginHelper;
    private PackageInfo info;
    private ConnectivityManager connectivityManager;
//    private MyProgressDialog myProgressDialog;

    private final static int LOCATION_PERMISSION_REQUEST = 100;

    @Override
    protected void init() {
        try {
            info = getPackageManager().getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        paramsMap = new HashMap<>();
        requestQueue = BaseApp.getRequestQueue();
        pref = getSharedPreferences("login", MODE_PRIVATE);

        //6.0索要读写权限
 //       ImageUtil.verifyStoragePermissions(this);
//        myProgressDialog = new MyProgressDialog(baseContext);
//        myProgressDialog.setCancelable(false);
    }



    @Override
    protected int getRootView() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initData() {
        Log.i("tag","splashActivity--->1");
        if (isNetworkConnected(baseContext)) {
            PermissionGen.with(SplashActivity.this)
                    .addRequestCode(LOCATION_PERMISSION_REQUEST)
                    .permissions(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.CALL_PHONE
                    )
                    .request();
        } else {
            ToastUtil.showLongToast(getString(R.string.app_tips_text79));
        }
    }



    @PermissionFail(requestCode = LOCATION_PERMISSION_REQUEST)
    private void onPermissionFail() {
        ToastUtil.showToast(getString(R.string.app_tips_text80));
       // checkUpdate();
        goToRouter();
    }

    @PermissionSuccess(requestCode = LOCATION_PERMISSION_REQUEST)
    private void confirmVersion() {
        //checkUpdate();
        goToRouter();
    }

//   private void checkUpdate() {
//        Log.i("tag","splashActivity--->2");
////        myProgressDialog.show();
//        Map<String,String> updateParams = new HashMap<>();
//        updateParams.put("platform","android");
//        updateParams.put("version",info.versionName);
//        UpdateRequest versionRequest = new UpdateRequest(updateParams,
//                new VolleyListener() {
//                    @Override
//                    protected void onSuccess(JSONObject response) {
//                        Log.i("tag","splashActivity--->3");
//                        LogUtil.d("SplashActivity","Response: " + response.toString());
//                        try {
//                            UpdateNotify updateNotify = GsonUtil.parse(response.getString("data"), UpdateNotify.class);
//                            if (updateNotify.update.equals("true")) {
//                                buildUpdateDialog(updateNotify);
//                            }else {
//                                goToRouter();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new VolleyErrorListener() {
//                    @Override
//                    protected void onFail(VolleyError error) {
////                        myProgressDialog.dismiss();
//                        Log.i("tag","更新错误---》"+error.getMessage());
//                       // byte[] htmlBodyBytes=error.networkResponse.data;;
////                        Log.e("tag","更新获取错误信息---》"+error.networkResponse.data);
////                        error.printStackTrace();
//                        buildExitDialog();
//                    }
//                });
//        requestQueue.add(versionRequest);
//    }

//    private void buildExitDialog() {
//        MyAlertDialog exitDialog = new MyAlertDialog(baseContext, new MyAlertDialog.OnPositiveAlterListener() {
//            @Override
//            public void onAlter() {
//                ActivityUtil.appExit(baseContext);
//            }
//        });
//        exitDialog.setTitle("提示");
//        exitDialog.setMessage("网络状况不佳，请确认后退出重试");
//        exitDialog.setCancelable(false);
//        exitDialog.show();
//    }
//
//    private void buildUpdateDialog(UpdateNotify updateNotify) {
//        MyAlertDialog updateDialog = new MyAlertDialog(baseContext, new MyAlertDialog.OnPositiveAlterListener() {
//            @Override
//            public void onAlter() {
//                Intent updateIntent = new Intent(Intent.ACTION_VIEW);
//                updateIntent.setData(Uri.parse(LocalUrl.DOWNLOAD_URL));
//                startActivity(updateIntent);
//                ActivityUtil.appExit(baseContext);
//            }
//        });

//        updateDialog.setOnNegativeAlterListener(new MyAlertDialog.OnNegativeAlterListener() {
//            @Override
//            public void onAlter() {
//                goToRouter();
//            }
//        });
//        updateDialog.setTitle(getString(R.string.update_title) + "(" + updateNotify.version + ")");
//        updateDialog.setMessage("\n" + updateNotify.info);
//        updateDialog.setCancelable(false);
//        updateDialog.show();
//    }

    private void goToRouter() {
        Log.i("tag","splashActivity--->4");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pref.getString("mobile", null) == null) { //没有登录记录，跳转到引导页
//                    myProgressDialog.dismiss();
                    LogUtil.d("SplashActivity", "pref == null");
                    Log.i("tag","splashActivity--->5");
                    goToLogin();
                    LogUtil.i("tag", "跳转到引导页--");
                } else { //有登录记录，自动登录
                    LogUtil.d("SplashActivity", "pref != null");
                    Log.i("tag","splashActivity--第二次登录->5");
                    paramsMap = new HashMap<>();
                    paramsMap.put("mobile", pref.getString("mobile", null));
                    paramsMap.put("password", pref.getString("password", null));

                    if (pref.getString("password",null)==null){
                        LogUtil.i("tag", "第三方第二次登录--password为空");
                        OtherLoginHelper otherLoginHelper=new OtherLoginHelper(SplashActivity.this, new OtherLoginHelper.OnOtherLoginListener() {
                            @Override
                            public void onLoginSuccess(UserChatInfoList userChatInfoList, String city) {
                                Log.i("tag","splashActivity--->6");
                                Intent platFormIntent=new Intent(SplashActivity.this, MainActivity.class);
                                if (userChatInfoList!=null){
                                    platFormIntent.putExtra("userChatInfoList", userChatInfoList);
                                }
                                platFormIntent.putExtra("city",city);
                                //ActivityUtil.finishActivty();
                             //  SplashActivity.this.finish();
                                startActivity(platFormIntent);
                                finish();
                            }

                            @Override
                            public void onLoginFail() {
                                Log.i("tag","otherloginHelper---》fail");
                                goToLogin();
                            }
                        });
                        otherLoginHelper.onOtherLine();
                    }else {
                        LogUtil.i("tag", "手机第二次登录--"+pref.getString("password",null));
                        loginHelper = new LoginHelper(baseContext, new LoginHelper.OnLoginListener() {
                            @Override
                            public void onLoginSuccess(UserChatInfoList userChatInfoList, String city) {

//                            myProgressDialog.dismiss();
                                Intent loginIntent = new Intent(SplashActivity.this, MainActivity.class);
                                if (userChatInfoList != null) {
                                    loginIntent.putExtra("userChatInfoList", userChatInfoList);
                                }
                                loginIntent.putExtra("city", city);
                             //   ActivityUtil.finishActivty();
                                startActivity(loginIntent);
                                finish();
                            }

                            @Override
                            public void onLoginFail() {
                                LogUtil.d("SplashActivity", "onLoginFail");
//                            myProgressDialog.dismiss();
                                goToLogin();
                            }
                        });
                        loginHelper.online();
                    }
                }
            }
        }, SPLASH_DISPLAY_LENGHT);
    }

    private void goToLogin() {
        Intent guideIntent = new Intent(SplashActivity.this, OtherLoginActivity.class);
        ActivityUtil.finishActivty();
        startActivity(guideIntent);
    }


    @Override
    protected void setListener() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        myProgressDialog.dismiss();
    }

    /**
     * 判断是否有网络连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            // 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            // 获取NetworkInfo对象
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            //判断NetworkInfo对象是否为空
            if (networkInfo != null)
                return networkInfo.isAvailable();
        }
        return false;
    }
}
