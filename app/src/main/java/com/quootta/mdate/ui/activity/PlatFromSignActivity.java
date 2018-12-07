package com.quootta.mdate.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.constant.LocalConfig;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.domain.LocationDate;
import com.quootta.mdate.domain.PlatFormSignUpData;
import com.quootta.mdate.domain.UserChatInfoList;
import com.quootta.mdate.helper.ConnectRongHelper;
import com.quootta.mdate.helper.OtherLoginHelper;
import com.quootta.mdate.ui.dialog.MyProgressDialog;
import com.quootta.mdate.ui.popupWindow.SelectPicPopupWindow;
import com.quootta.mdate.ui.view.CircleImageView;
import com.quootta.mdate.utils.ActivityUtil;
import com.quootta.mdate.utils.DBUtil;
import com.quootta.mdate.utils.FileUtil;
import com.quootta.mdate.utils.GsonUtil;
import com.quootta.mdate.utils.ImageUtil;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.quootta.mdate.utils.video.CompressMediaController;
import com.android.volley.toolbox.ImageLoader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.jpush.android.api.JPushInterface;
import cn.qqtheme.framework.picker.DatePicker;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Ryon on 2016/10/17/0017.
 */
public class PlatFromSignActivity extends BaseActivity {
    @Bind(R.id.signup_background)
    ImageView signupBackground;
    @Bind(R.id.signup_photo)
    CircleImageView signupPhoto;
    @Bind(R.id.signup_camera)
    ImageView signupCamera;
    @Bind(R.id.signup_girl_state)
    ImageView signupGirlState;
    @Bind(R.id.signup_red1)
    View signupRed1;
    @Bind(R.id.signup_girl)
    RelativeLayout signupGirl;
    @Bind(R.id.signup_boy_state)
    ImageView signupBoyState;
    @Bind(R.id.signup_red2)
    View signupRed2;
    @Bind(R.id.signup_boy)
    RelativeLayout signupBoy;
    @Bind(R.id.signup_username)
    EditText signupUsername;
    @Bind(R.id.signup_data)
    TextView signupData;
    @Bind(R.id.signup_data_click)
    RelativeLayout signupDataClick;
    @Bind(R.id.signup_register)
    RelativeLayout signupRegister;
    @Bind(R.id.platFrom_signup)
    LinearLayout platFromSignup;
    @Bind(R.id.platfrom_back)
    ImageView platfromBack;
    @Bind(R.id.Invitation_code)
    EditText Invitation_code;


    private Map<String, String> signMap;
    private String registrationID;
    private String name;
    private Boolean sex = true;
    private ImageLoader imageLoader;
    private String gender = "male";
    private String mCity = null;
    private String mProvince = null;
    private String mDistrict = null;
    private SelectPicPopupWindow menuWindow;
    private SharedPreferences cookiePreferences;
    private final static int CAMERA_PERMISSION_REQUEST = 200;
    private static final String COOKIE_KEY = "Cookie";
    private OkHttpClient mOkHttpClient;
    private String birthday = null;
    private MyProgressDialog myProgressDialog;
    private PlatFormSignUpData platFormSignData;
    private File tempFile;
    private String channel;
    private String invite = "";

    @Override
    protected void init() {
        signMap = new HashMap<>();
        EventBus.getDefault().register(this);

        imageLoader = BaseApp.getImageLoader();

        myProgressDialog = new MyProgressDialog(baseContext);

        mOkHttpClient = new OkHttpClient();
        //设备唯一标识符
        registrationID = JPushInterface.getRegistrationID(baseContext);

        //获取渠道值
        ApplicationInfo appInfo = null;
        try {
            appInfo = this.getPackageManager()
                    .getApplicationInfo(getPackageName(),
                            PackageManager.GET_META_DATA);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        channel = appInfo.metaData.getString("UMENG_CHANNEL");
        // Log.i("tag","渠道名称------------------》》》》"+channel);

//        //确认定位权限
//        AMapLocationClient locationClient = BaseApp.getLocationClient();
//        locationClient.setLocationListener(new AMapLocationListener() {
//            @Override
//            public void onLocationChanged(AMapLocation aMapLocation) {
//                //省
//                mProvince = aMapLocation.getProvince();
//                //市
//                mCity = aMapLocation.getCity();
//                //区
//                mDistrict = aMapLocation.getDistrict();
//
//            }
//        });
//        locationClient.startLocation();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMes(Map<String, String> signMap) {

        this.signMap = signMap;
        this.signMap.put("device_id", registrationID);


        // 根据传过来的数据在界面添加默认资料
        if (signMap.get("platform").equals("wechat")) {
            signupUsername.setText(signMap.get("screen_name"));

            name = signMap.get("screen_name");
            //  imageUrl=signMap.get("headimgurl");

            if (signMap.get("gender").equals("1")) {
                sex = false;
                judgeSex(sex);
            } else {
                sex = true;
                judgeSex(sex);
            }

        } else {

            signupUsername.setText(signMap.get("screen_name"));
            //  imageUrl=signMap.get("profile_image_url");

            name = signMap.get("screen_name");
            //性别
            if (signMap.get("gender").equals("男") || signMap.get("gender").equals("m")) {
                sex = false;
                judgeSex(sex);
            } else {
                sex = true;
                judgeSex(sex);
            }

        }

        //将从第三方取下来的图片存储到头像和背景中
        imageLoader.get(signMap.get("profile_image_url"),
                imageLoader.getImageListener(signupPhoto, R.mipmap.home_show_loading, R.mipmap.home_show_loading));

//        imageLoader.get(signMap.get("profile_image_url"),
//                imageLoader.getImageListener(signupBackground, R.mipmap.home_show_loading, R.mipmap.home_show_loading));

        //把从第三方读取的文件存储到本地
        ImageUtil.SaveurlToBitMap(signMap.get("profile_image_url"));
        //出生日期
        Calendar c = Calendar.getInstance();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        c.set(Calendar.YEAR, c.get(Calendar.YEAR) - 20);
        Date date = c.getTime();
        String time = format.format(date);
        signupData.setText(time);
    }


    @Override
    protected int getRootView() {
        return R.layout.activity_plarform_signup;
    }

    @Override
    protected void initData() {
        //  回退键单击事件
        platfromBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlatFromSignActivity.this.finish();
            }
        });

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void judgeSex(Boolean sex) {
        //判断男女 sex true为女
        if (sex) {
            signupGirlState.setImageResource(R.drawable.signup_girl_down);
            signupBoyState.setImageResource(R.drawable.signup_boy_up);
            signupRed1.setVisibility(View.VISIBLE);
            signupRed2.setVisibility(View.INVISIBLE);
        } else {
            signupGirlState.setImageResource(R.drawable.signup_girl_up);
            signupBoyState.setImageResource(R.drawable.signup_boy_down);
            signupRed1.setVisibility(View.INVISIBLE);
            signupRed2.setVisibility(View.VISIBLE);
        }
    }

    @Nullable
    @OnClick({R.id.signup_photo, R.id.signup_camera, R.id.signup_girl, R.id.signup_boy, R.id.signup_data_click, R.id.signup_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signup_photo:
            case R.id.signup_camera:
                menuWindow = new SelectPicPopupWindow(baseContext, itemsOnClick);
                menuWindow.setVideoBtnVisible(view.GONE);
                menuWindow.showAtLocation(PlatFromSignActivity.this.findViewById(R.id.platFrom_signup),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                break;
            case R.id.signup_girl:
                sex = true;
                judgeSex(sex);
                break;
            case R.id.signup_boy:
                sex = false;
                judgeSex(sex);
                break;
            case R.id.signup_data_click:
                onYearMonthPicker();
                break;
            case R.id.signup_register:
                if (sex) {
                    gender = "female";
                } else {
                    gender = "male";
                }

                //上传数据
                if (signMap != null && signMap.size() > 0) {
                    myProgressDialog.show();
                    postPlatFormSignUpInfo();
                } else {
//                    ToastUtil.showToast("您的网络现在不太稳定 请稍等一下");
                    ToastUtil.showToast(getString(R.string.app_tips_text47));
                }

                break;
        }
    }


    private void onYearMonthPicker() {
        DatePicker picker = new DatePicker(PlatFromSignActivity.this, DatePicker.YEAR_MONTH_DAY);
        picker.setRange(1919, 2015);
        picker.setSelectedItem(1990, 6, 4);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                birthday = year + "-" + month + "-" + day;
                signupData.setText(birthday);
            }
        });
        picker.show();
    }


    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                // 拍照
                case R.id.takePhotoBtn:
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_PERMISSION_REQUEST);
                    break;
                // 相册选择图片
                case R.id.pickPhotoBtn:
                    GalleryFinal.openGallerySingle(LocalConfig.REQUEST_CODE_GALLERY, mOnHanlderResultCallback);
                    break;
                //上传视频：
                case R.id.pickVideoBtn:
                    ToastUtil.showToast(getString(R.string.app_tips_text48));


                default:
                    break;
            }
        }
    };
    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            ImageUtil.savePhotoToPath(resultList, signupPhoto, LocalConfig.AVATAR_PATH);
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            ToastUtil.showToast(getString(R.string.app_tips_text49));
        }
    };

    //上传数据
    private void postPlatFormSignUpInfo() {
        if (Invitation_code.getText() != null && Invitation_code.getText().toString().length() > 0) {
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(Invitation_code.getText().toString());
            Log.i("tag", "邀请码为--------》" + isNum);
            if (isNum.matches()) {
                invite = Invitation_code.getText().toString();
                Log.i("tag", "邀请码为--》" + invite);
            } else {
                invite = "";
            }

        }

        if (mProvince == null || mProvince.equals("")) {
            mProvince = getString(R.string.app_tips_text50);
        }
        if (mCity == null || mCity.equals("")) {
            mCity = getString(R.string.app_tips_text50);
        }
        if (mDistrict == null || mDistrict.equals("")) {
            mDistrict = getString(R.string.app_tips_text50);
        }

        cookiePreferences = baseContext.getSharedPreferences("CookieStore", BaseActivity.MODE_PRIVATE);


        File picFile = new File(Environment.getExternalStorageDirectory() + File.separator + LocalConfig.AVATAR_PATH);


        if (signupUsername.getText().toString().trim().equals("")) {
            ToastUtil.showToast(getString(R.string.app_tips_text51));
            myProgressDialog.dismiss();
        } else if (signupData.getText().toString().trim().equals("")) {
            ToastUtil.showToast(getString(R.string.app_tips_text52));
            myProgressDialog.dismiss();
        } else if (!picFile.exists()) {
            ToastUtil.showToast(getString(R.string.app_tips_text53));
            myProgressDialog.dismiss();
        } else {


            RequestBody fileBody = RequestBody.create(MediaType.parse(LocalConfig.AVATAR_NAME), picFile);
            MultipartBody fileBuilder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addPart(Headers.of(
                            "Content-Disposition",
                            "form-data; name=\"" + "photo" + "\"; filename=\"" + LocalConfig.AVATAR_NAME +
                                    "\""), fileBody)
                    .addPart(Headers.of(
                            "Content-Disposition",
                            "form-data; name=\"openid\""),
                            RequestBody.create(null, signMap.get("uid")))
                    .addPart(Headers.of(
                            "Content-Disposition",
                            "form-data; name=\"access_token\""),
                            RequestBody.create(null, signMap.get("access_token")))
                    .addPart(Headers.of(
                            "Content-Disposition",
                            "form-data; name=\"device_id\""),
                            RequestBody.create(null, signMap.get("device_id")))
                    .addPart(Headers.of(
                            "Content-Disposition",
                            "form-data; name=\"platform\""),
                            RequestBody.create(null, signMap.get("platform")))
                    .addPart(Headers.of(
                            "Content-Disposition",
                            "form-data; name=\"nickname\""),
                            RequestBody.create(null, name))
                    .addPart(Headers.of(
                            "Content-Disposition",
                            "form-data; name=\"username\""),
                            RequestBody.create(null, signupUsername.getText().toString()))
                    .addPart(Headers.of(
                            "Content-Disposition",
                            "form-data; name=\"gender\""),
                            RequestBody.create(null, gender))
                    .addPart(Headers.of(
                            "Content-Disposition",
                            "form-data; name=\"birth\""),
                            RequestBody.create(null, signupData.getText().toString()))
                    .addPart(Headers.of(
                            "Content-Disposition",
                            "form-data; name=\"city\""),
                            RequestBody.create(null, mProvince + " " + mCity + " " + mDistrict))
                    .addPart(Headers.of(
                            "Content-Disposition",
                            "form-data; name=\"channel\""),
                            RequestBody.create(null, channel))
                    .addPart(Headers.of(
                            "Content-Disposition",
                            "form-data; name=\"invite_code\""),
                            RequestBody.create(null, invite))
                    .addPart(Headers.of(
                            "Content-Disposition",
                            "form-data; name=\"UDID\""),
                            RequestBody.create(null, BaseApp.getDeviceId()))

                    .build();
            //RequestBody body=builder.build();

            Request req = new Request.Builder()
                    .url(LocalUrl.POST_PLATFORM_SIGN_UP)
                    .addHeader("User-Agent", LocalUrl.getVersionCode() + " (" + Build.MODEL + "; Android" + Build.VERSION.RELEASE + ")")
                    .post(fileBuilder)
                    .build();
            //进行网络连接
            mOkHttpClient.newCall(req).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //异步线程更新Ui
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showToast(getString(R.string.app_tips_text54));
                            myProgressDialog.dismiss();
                        }
                    });


                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
//                    Headers headers= req.headers();
//                 String key=  headers.get("SET_COOKIE_KEY");


                    ImageUtil.deleteFile(LocalConfig.AVATAR_PATH);

                    //  JSONObject responseJosn;
                    String res = response.body().string();

                    Log.i("tag", "response返回的值----》" + res);


                    // responseJosn = new JSONObject(String.valueOf(response));
                    platFormSignData = GsonUtil.parse(res, PlatFormSignUpData.class);

                    //在ptr中保存登录信息
                    Headers headers = response.headers();
                    String header = headers.toString();

                    LogUtil.i("tag", "headers--->" + header);
                    String cookie = "";

                    if (header.indexOf("server_token") == -1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast toast = Toast.makeText(PlatFromSignActivity.this, getString(R.string.app_tips_text55), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                        });
                        PlatFromSignActivity.this.finish();
                        Intent otherIntent = new Intent(PlatFromSignActivity.this, OtherLoginActivity.class);
                        startActivity(otherIntent);
                    } else {
                        if (header.indexOf("Set-Cookie") == -1) {
                            cookie = headers.get("set-cookie");
                        } else {
                            cookie = header.substring(header.indexOf("server_token"), header.indexOf("Set-Cookie"));
                        }
                    }


                    LogUtil.i("tag", "cookie---->" + cookie);
                    //cookie=null;
                    if (cookie != null) {
                        //  if (cookie.length() > 0) {
                        String[] splitCookie = cookie.split(";");
                        cookie = splitCookie[0];
                        SharedPreferences.Editor prefEditor = cookiePreferences.edit();
                        prefEditor.putString(COOKIE_KEY, cookie);
                        LogUtil.d("tag", "保存cookie" + cookie);
                        prefEditor.commit();
                    } else {
                        Log.d("tag", "platformsignupActivity_cookie值为空");

                    }


                    ConnectRongHelper connectRongHelper = new ConnectRongHelper(platFormSignData.getData().getRy_token(), new ConnectRongHelper.OnConnectListener() {
                        @Override
                        public void onConnectSuccess(UserChatInfoList userChatInfoList) {
                            //向友盟发送统计数据
                            Log.i("tag", "----保存数据 跳转主页面");
                            //保存登录信息
                            saveLoginInfo();
                            Intent intent = new Intent(baseContext, MainActivity.class);
                            if (userChatInfoList != null) {

                                intent.putExtra("userChatInforList", userChatInfoList);
                            }
                            intent.putExtra("city", mCity);
                            myProgressDialog.dismiss();
                            ActivityUtil.finishActivty();
                            startActivity(intent);
                        }

                        @Override
                        public void onConnectFail() {
                            myProgressDialog.dismiss();
                            Log.i("tag", "注册成功 但登录失败  重新执行一遍第三方登录逻辑");
                            // ToastUtil.showLongToast("您已经注册成功  请点击登陆~~~~~");
                            OtherLoginHelper otherLoginHelper = new OtherLoginHelper(PlatFromSignActivity.this, new OtherLoginHelper.OnOtherLoginListener() {
                                @Override
                                public void onLoginSuccess(UserChatInfoList userChatInfoList, String city) {
                                    Intent otherIntent = new Intent(PlatFromSignActivity.this, MainActivity.class);
                                    if (userChatInfoList != null) {
                                        otherIntent.putExtra("userChatInfoList", userChatInfoList);
                                    }

                                    otherIntent.putExtra("city", city);


                                    ActivityUtil.finishActivty();
                                    startActivity(otherIntent);
                                    finish();
                                }

                                @Override
                                public void onLoginFail() {
                                    ToastUtil.showLongToast(getString(R.string.app_tips_text56));
                                }
                            });
                            //   调用登录模块
                            otherLoginHelper.onOtherLine();
                        }
                    }, baseContext);
                    connectRongHelper.connectRongIM();

                }
            });
        }
    }

    //保存用户信息
    private void saveLoginInfo() {
        SharedPreferences pref = baseContext.getSharedPreferences("login", baseContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("id", platFormSignData.getData().get_id());
        editor.putString("ry_token", platFormSignData.getData().getRy_token());
        editor.putString("mobile", platFormSignData.getData().get_id());

        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (requestCode == LocalConfig.REQUEST_CODE_GALLERY_VIDEO) {
                LogUtil.d("AlbumFragment", "first if");
                if (uri != null) {
                    LogUtil.d("AlbumFragment", "uri：" + uri.toString());
                    myProgressDialog.show();
                    //获得选择视频的路径
                    Cursor cursor = baseContext.getContentResolver().query(uri, null, null, null, null, null);
                    try {
                        LogUtil.d("AlbumFragment", "cursor：" + cursor.toString());
                        if (cursor != null && cursor.moveToFirst()) {
                            LogUtil.d("AlbumFragment", "in if");
                            int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                            String size = null;
                            if (!cursor.isNull(sizeIndex)) {
                                size = cursor.getString(sizeIndex);
                            } else {
                                size = "Unknown";
                            }
                            LogUtil.d("AlbumFragment", "init file");
                            tempFile = FileUtil.saveTempFile(LocalConfig.VIDEO_TEMP_NAME, baseContext, uri);
                            LogUtil.d("AlbumFragment", "tempFile:" + tempFile.getAbsolutePath());
                            new VideoCompressor().execute();
                        }
                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                }
            } else if (requestCode == CAMERA_PERMISSION_REQUEST) {
                Bundle bundle = data.getExtras();
                // 获取相机返回的数据，并转换为Bitmap图片格式 ，这是缩略图
                Bitmap bitmap = (Bitmap) bundle.get("data");
//                saveBitmap2file(bitmap,"avatar.jpg");
                tempFile = new File(Environment.getExternalStorageDirectory() + "avatar.jpg");
                signupPhoto.setImageBitmap(bitmap);

                // post data to server
                String absPath = Environment.getExternalStorageDirectory() + "/" + LocalConfig.AVATAR_PATH;
                ImageUtil.saveBitmap2file(bitmap, absPath);
            }
        }
    }


    /**
     * 上传图片的回调
     */
    class VideoCompressor extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("VideoCompressor", "Start video compression");
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            myProgressDialog.setMessage(getString(R.string.app_tips_text20));
            Log.d("VideoCompressor", "doInBackground " + tempFile.getPath());
            return CompressMediaController.getInstance().convertVideo(tempFile.getPath());
        }

        @Override
        protected void onPostExecute(Boolean compressed) {
            super.onPostExecute(compressed);
            if (compressed) {
                Log.d("VideoCompressor", "Compression successfully!");
                cookiePreferences = baseContext.getSharedPreferences("CookieStore", BaseActivity.MODE_PRIVATE);
                LogUtil.d("AlbumFragment", "before request init");
                OkHttpUtils
                        .post()
                        .url(LocalUrl.POST_FEED_CREATE)
                        .addHeader("User-Agent", LocalUrl.getVersionCode() + " (" + Build.MODEL + "; Android" + Build.VERSION.RELEASE + ")")
                        .addHeader(COOKIE_KEY, cookiePreferences.getString(COOKIE_KEY, ""))
                        .addFile("video", LocalConfig.VIDEO_NAME, new File(Environment.getExternalStorageDirectory() + File.separator + LocalConfig.VIDEO_PATH))
                        .build()
                        .execute(mStringCallback);
                LogUtil.d("AlbumFragment", "after request init");
            }
        }
    }

    private StringCallback mStringCallback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e) {
            myProgressDialog.dismiss();
            LogUtil.d("uploadPicture", "Exception e " + e.toString());
            ToastUtil.showToast(getString(R.string.app_tips_text18));
        }

        @Override
        public void inProgress(float progress) {
            super.inProgress(progress);
            NumberFormat percentFormat = NumberFormat.getPercentInstance();
            percentFormat.setMinimumFractionDigits(0);
            myProgressDialog.setMessage(getString(R.string.app_tips_text19) + percentFormat.format(progress));
        }

        @Override
        public void onResponse(String response) {
            // paramsMap.put("page","0");
            //  requestAlbum();
            myProgressDialog.dismiss();
            LogUtil.d("uploadPicture", response);

        }
    };

    LocationManager locationManager;

    @SuppressLint("MissingPermission")
    private void getLocation() {
        // 获取位置管理服务
        if (locationManager == null) {
            String serviceName = Context.LOCATION_SERVICE;
            locationManager = (LocationManager) this.getSystemService(serviceName);
        }
        // 查找到服务信息
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗
//        String provider = locationManager.getBestProvider(criteria, true); // 获取GPS信息
        String provider = locationManager.NETWORK_PROVIDER;
        Location location = locationManager.getLastKnownLocation(provider);
        if (location == null) {
            locationManager.requestLocationUpdates(provider, 5000, 1, locationListener);
        } else {
            LocationDate locationInfo = new LocationDate();
            locationInfo.setLatitude(location.getLatitude());
            locationInfo.setLongitude(location.getLongitude());
            DBUtil.setParam(DBUtil.LOCATION_DATE, locationInfo);

        }
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                LocationDate locationInfo = new LocationDate();
                locationInfo.setLatitude(location.getLatitude());
                locationInfo.setLongitude(location.getLongitude());
                DBUtil.setParam(DBUtil.LOCATION_DATE, locationInfo);


            }
            locationManager.removeUpdates(locationListener);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };
}
