package com.quootta.mdate.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.base.BaseFragment;
import com.quootta.mdate.constant.LocalConfig;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.domain.MyData;
import com.quootta.mdate.domain.UserChatInfoList;
import com.quootta.mdate.helper.ConnectRongHelper;
import com.quootta.mdate.task.AddressInitTask;
import com.quootta.mdate.ui.activity.MainActivity;
import com.quootta.mdate.ui.dialog.MyProgressDialog;
import com.quootta.mdate.ui.popupWindow.SelectPicPopupWindow;
import com.quootta.mdate.utils.ActivityUtil;
import com.quootta.mdate.utils.FileUtil;
import com.quootta.mdate.utils.GsonUtil;
import com.quootta.mdate.utils.ImageUtil;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.quootta.mdate.utils.video.CompressMediaController;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.NumberFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.jpush.android.api.JPushInterface;
import cn.qqtheme.framework.picker.DatePicker;
import kr.co.namee.permissiongen.PermissionGen;
import okhttp3.Call;

/**
 * Created by Ryon on 2016/7/18/0018.
 */
public class SignUpLastFragment extends BaseFragment implements View.OnClickListener {
    @Bind(R.id.iv_back_title_bar)ImageView iv_back;
    @Bind(R.id.tv_title_bar)TextView tv_title;
    @Bind(R.id.iv_head_sign_up_activity)ImageView iv_head;
    @Bind(R.id.et_name_sign_up_activity)EditText et_name;
    @Bind(R.id.rg_sex_sign_up_activity)RadioGroup rg_sex;
    @Bind(R.id.ll_city_sign_up_activity)LinearLayout ll_city;
    @Bind(R.id.tv_city_sign_up_activity)TextView tv_city;
    @Bind(R.id.ll_birthday_sign_up_activity)LinearLayout ll_birthday;
    @Bind(R.id.tv_birthday_sign_up_activity)TextView tv_birthday;
    @Bind(R.id.btn_ensure_sign_up_activity)Button btn_ensure;
    @Bind(R.id.tv_Invitation_code_activity)EditText Invitation_code;

    private SelectPicPopupWindow menuWindow;
    private SharedPreferences cookiePreferences;
    private MyProgressDialog myProgressDialog;
    private String gender = null;
    private String cityInfo = null;
    private String mCity = null;
    private String birthday = null;
    private MyData myData;
    private String mobile;
    private String pwd;
    private File tempFile;

    private static final String COOKIE_KEY = "Cookie";
    private final static int CAMERA_PERMISSION_REQUEST = 200;

    private Context baseContext;

    private String channel;
    private String invite="";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mobile = getArguments().getString("number");
        LogUtil.d("gg","mobile"+mobile);
        pwd = getArguments().getString("pwd");
        LogUtil.d("gg","pwd"+pwd);

        //获取渠道值
        ApplicationInfo appInfo = null;
        try {
            appInfo = getContext().getPackageManager()
                    .getApplicationInfo(getContext().getPackageName(),
                            PackageManager.GET_META_DATA);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        channel=appInfo.metaData.getString("UMENG_CHANNEL");
        Log.i("tag","渠道名称------------------》》》》"+channel);



        //申请调用相机的权限
        PermissionGen.with(getActivity())
                .addRequestCode(CAMERA_PERMISSION_REQUEST)
                .permissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).request();
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_sign_up_last;
    }

    @Override
    protected void setListener() {
        iv_back.setOnClickListener(this);
        iv_head.setOnClickListener(this);
        btn_ensure.setOnClickListener(this);
        ll_city.setOnClickListener(this);
        ll_birthday.setOnClickListener(this);


        rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_male_sign_up_activity:
                        gender = "male";
                        break;
                    case R.id.rb_female_sign_up_activity:
                        gender = "female";
                        break;
                }
            }
        });
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initData(View view) {
        baseContext=view.getContext();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back_title_bar:
                ActivityUtil.finishActivty();
                break;
            case R.id.iv_head_sign_up_activity:
                menuWindow = new SelectPicPopupWindow(baseContext, itemsOnClick);
                menuWindow.setVideoBtnVisible(View.GONE);
                menuWindow.showAtLocation(getActivity().findViewById(R.id.ll_last_sign_up_activity),
                        Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.ll_city_sign_up_activity:
                new AddressInitTask(getActivity(), true, new AddressInitTask.OnCitySelectListener() {
                    @Override
                    public void onSelect(String province, String city, String county) {
                        mCity = city;
                        cityInfo = province + " " + city + " " + county;
                        tv_city.setText(cityInfo);
                    }
                }).execute("北京市", "北京市");
                break;
            case R.id.btn_ensure_sign_up_activity:
                myProgressDialog = new MyProgressDialog(baseContext);
                postSignUpInfo();
                break;
            case R.id.ll_birthday_sign_up_activity:
                onYearMonthPicker();
                break;
        }

    }

    private void onYearMonthPicker() {
        DatePicker picker = new DatePicker(getActivity(), DatePicker.YEAR_MONTH_DAY);
        picker.setRange(1919, 2015);
        picker.setSelectedItem(1990, 6, 4);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                birthday = year + "-" + month + "-" +day;
                tv_birthday.setText(birthday);
            }
        });
        picker.show();
    }

    private void postSignUpInfo() {
        cookiePreferences = baseContext.getSharedPreferences("CookieStore", BaseActivity.MODE_PRIVATE);
        Log.d("postSignUpInfo", "COOKIE_KEY----->" + cookiePreferences.getString(COOKIE_KEY, ""));


        if (Invitation_code.getText()!=null && Invitation_code.getText().toString().length()>0){
            Pattern pattern=Pattern.compile("[0-9]*");
            Matcher isNum=pattern.matcher(Invitation_code.getText().toString());
            Log.i("tag","邀请码为--------》"+isNum);
            if (isNum.matches()){
                invite=Invitation_code.getText().toString();
                Log.i("tag","邀请码为--》"+invite);
            }else {
                invite="";
            }

        }


        File picFile = new File(Environment.getExternalStorageDirectory() + File.separator + LocalConfig.AVATAR_PATH);

        if (et_name.getText().toString().trim().equals("")) {
            ToastUtil.showToast(getString(R.string.app_tips_text51));
        } else if (gender == null) {
            ToastUtil.showToast(getString(R.string.app_tips_text110));
        } else if (cityInfo == null) {
            ToastUtil.showToast(getString(R.string.app_tips_text111));
        } else if (birthday == null) {
            ToastUtil.showToast(getString(R.string.app_tips_text112));
        } else if (!picFile.exists()) {
            ToastUtil.showToast(getString(R.string.app_tips_text113));
        } else {
            myProgressDialog.show();
            OkHttpUtils
                    .post()
                    .url(LocalUrl.POST_SIGN_UP_INFO)
                    .addHeader("User-Agent", LocalUrl.getVersionCode()+" (" + android.os.Build.MODEL + "; Android" + android.os.Build.VERSION.RELEASE + ")")
                    .addHeader(COOKIE_KEY, cookiePreferences.getString(COOKIE_KEY, ""))
                    .addFile("photo", LocalConfig.AVATAR_NAME, picFile)
                    .addParams("gender", gender)
                    .addParams("nick_name", et_name.getText().toString().trim())
                    .addParams("birthday", birthday)
                    .addParams("city", cityInfo)
                    .addParams("device_id", JPushInterface.getRegistrationID(baseContext))
                    .addParams("channel",channel)
                    .addParams("invite_code",invite)
                    .addParams("UDID", BaseApp.getDeviceId())
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            LogUtil.d("SignUpLastActivity", "onError:" + e.toString());
                            myProgressDialog.dismiss();

                        }

                        @Override
                        public void onResponse(String response) {
                            ImageUtil.deleteFile(LocalConfig.AVATAR_PATH);
                            LogUtil.d("postSignUpInfo","signupLastFragment-->"+ response);
                            JSONObject responseJson;
                            try {
                                responseJson = new JSONObject(response);
                                myData = GsonUtil.parse(responseJson.getString("data"), MyData.class);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            ConnectRongHelper connectRongHelper = new ConnectRongHelper(myData.ry_token, new ConnectRongHelper.OnConnectListener() {
                                @Override
                                public void onConnectSuccess(UserChatInfoList userChatInfoList) {

                                    saveLoginInfo();

                                    Intent loginIntent = new Intent(getContext(), MainActivity.class);
                                    if (userChatInfoList != null) {
                                        loginIntent.putExtra("userChatInfoList", userChatInfoList);
                                    }
                                    loginIntent.putExtra("city", mCity);

                                    myProgressDialog.dismiss();
                                    ActivityUtil.finishAllActivity();
                                    startActivity(loginIntent);
                                }

                                @Override
                                public void onConnectFail() {
                                    LogUtil.d("SignUpLastActivity", "onConnectFail");

                                    myProgressDialog.dismiss();
                                }
                            },baseContext);
                            connectRongHelper.connectRongIM();
                        }
                    });
        }
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                // 拍照
                case R.id.takePhotoBtn:
//                    if (Build.VERSION.SDK_INT>Build.VERSION_CODES.M){
//                        //7.0打开相机
//                        Log.i("tag","hxz7.0");
//                        TakePhotoUtils.openCamera(,LocalConfig.AVATAR_PATH);
//                    }else {
//                        Log.i("tag","低版本");
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, CAMERA_PERMISSION_REQUEST);
//                    }
                    break;
                // 相册选择图片
                case R.id.pickPhotoBtn:
                    GalleryFinal.openGallerySingle(LocalConfig.REQUEST_CODE_GALLERY, mOnHanlderResultCallback);
                    break;
                //上传视频：
                case R.id.pickVideoBtn:
                    PermissionGen.with(getActivity())
                            .addRequestCode(LocalConfig.REQUEST_CODE_GALLERY_VIDEO)
                            .permissions(
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ).request();
                    Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
                    intent1.setType("video/*");
                    startActivityForResult(intent1, LocalConfig.REQUEST_CODE_GALLERY_VIDEO);
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CAMERA_PERMISSION_REQUEST && resultCode==getActivity().RESULT_OK) {
//
//
//        }
        Log.i("tag","------------>"+requestCode+resultCode);
//        if (resultCode==Activity.RESULT_OK){
//            //7.0相机拍摄完返回的数据
//            if (requestCode==1006){
//                Log.i("tag","------------>执行7.0");
//                String path=Environment.getExternalStorageDirectory() + "/" + LocalConfig.AVATAR_PATH;
//                //压缩图片
//                Bitmap bitmap = ImageUtil.getImage(path);
//                ImageUtil.saveBitmap2file(bitmap, Environment.getExternalStorageDirectory()+"avatar.jpg");
//                //把图片放到头像上
//                iv_head.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"avatar.jpg"));
//            }
//        }

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
            }else if(requestCode == CAMERA_PERMISSION_REQUEST ){
                Bundle bundle = data.getExtras();
                // 获取相机返回的数据，并转换为Bitmap图片格式 ，这是缩略图
                Bitmap bitmap = (Bitmap) bundle.get("data");
//                saveBitmap2file(bitmap,"avatar.jpg");
                tempFile = new File(Environment.getExternalStorageDirectory()+"avatar.jpg");
                iv_head.setImageBitmap(bitmap);

                // post data to server
                String absPath = Environment.getExternalStorageDirectory() + "/" +  LocalConfig.AVATAR_PATH;
                ImageUtil.saveBitmap2file(bitmap, absPath);
            }
        }
    }



    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            ImageUtil.savePhotoToPath(resultList, iv_head, LocalConfig.AVATAR_PATH);
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            ToastUtil.showToast(getString(R.string.app_tips_text49));
        }
    };


    private void saveLoginInfo() {
        SharedPreferences pref = baseContext.getSharedPreferences("login", baseContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("id",myData._id);
        editor.putString("ry_token", myData.ry_token);
        editor.putString("mobile", mobile);
        editor.putString("password", pwd);
        editor.apply();
    }

    /**
     * 上传图片的回调
     */
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
            if(compressed){
                Log.d("VideoCompressor","Compression successfully!");
                cookiePreferences = baseContext.getSharedPreferences("CookieStore", BaseActivity.MODE_PRIVATE);
                LogUtil.d("AlbumFragment", "before request init");
                OkHttpUtils
                        .post()
                        .url(LocalUrl.POST_FEED_CREATE)
                        .addHeader("User-Agent", LocalUrl.getVersionCode() + " (" + android.os.Build.MODEL + "; Android" + android.os.Build.VERSION.RELEASE + ")")
                        .addHeader(COOKIE_KEY, cookiePreferences.getString(COOKIE_KEY, ""))
                        .addFile("video", LocalConfig.VIDEO_NAME, new File(Environment.getExternalStorageDirectory() + File.separator + LocalConfig.VIDEO_PATH))
                        .build()
                        .execute(mStringCallback);
                LogUtil.d("AlbumFragment", "after request init");
            }
        }
    }
}
