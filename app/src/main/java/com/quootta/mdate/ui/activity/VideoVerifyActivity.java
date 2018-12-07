package com.quootta.mdate.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.constant.LocalConfig;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.ui.dialog.MyAlertDialog;
import com.quootta.mdate.ui.dialog.MyProgressDialog;
import com.quootta.mdate.utils.ActivityUtil;
import com.quootta.mdate.utils.ImageUtil;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.NumberFormat;

import butterknife.Bind;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import okhttp3.Call;

public class VideoVerifyActivity extends BaseActivity {

    @Bind(R.id.iv_back_title_bar)
    ImageView iv_back;
    @Bind(R.id.tv_title_bar)
    TextView tv_title;
    @Bind(R.id.rl_video_verify_camera)
    RelativeLayout rlVideoVerifyCamera;

    private static final String COOKIE_KEY = "Cookie";
    private final static int CAMERA_PERMISSION_REQUEST = 200;
    private final static int REQUEST_RECORD = 100;
    private MyProgressDialog myProgressDialog;
    private String type="";
    private String _id="";
    private final int RESULT_SUCCESS = 4;
    @Override
    protected void init() {
        myProgressDialog = new MyProgressDialog(baseContext);
        myProgressDialog.setCanceledOnTouchOutside(false);
        myProgressDialog.setCancelable(false);

        Intent intent=getIntent();
        type=intent.getStringExtra("type");
        _id=intent.getStringExtra("_id");
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_video_verify;
    }

    @Override
    protected void initData() {
        initTitleBar();
    }

    private void initTitleBar() {
        iv_back.setVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.video_verify));
    }

    @Override
    protected void setListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.finishActivty();
            }
        });

        rlVideoVerifyCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionGen.with(VideoVerifyActivity.this)
                        .addRequestCode(CAMERA_PERMISSION_REQUEST)
                        .permissions(
                                Manifest.permission.CAMERA,
                                Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ).request();
            }
        });
    }

    @PermissionSuccess(requestCode = CAMERA_PERMISSION_REQUEST)
    private void openRecorderView() {
        Intent recordIntent = new Intent(VideoVerifyActivity.this, RecordVideoActivity.class);
        startActivityForResult(recordIntent, REQUEST_RECORD);
    }

    @PermissionFail(requestCode = CAMERA_PERMISSION_REQUEST)
    private void onPermissionFail() {
        ToastUtil.showToast(getString(R.string.app_tips_text86));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_RECORD:
                if(resultCode == RESULT_OK) {
                    final String url;
                    MyAlertDialog myAlertDialog;
                    if (type!=null && type.equals("scene")){
                        //分类的视频上传

                            myAlertDialog = new MyAlertDialog(baseContext, new MyAlertDialog.OnPositiveAlterListener() {
                                @Override
                                public void onAlter() {
                                    myProgressDialog.show();
                                    // 成功
                                    SharedPreferences cookiePreferences = baseContext.getSharedPreferences("CookieStore", BaseActivity.MODE_PRIVATE);
                                    LogUtil.d("AlbumFragment", "before request init");
                                    OkHttpUtils
                                            .post()
//                                    .url("http://192.168.1.189/")
                                            .url(LocalUrl.POST_SCENE_APPLY)
                                            .addHeader("User-Agent", LocalUrl.getVersionCode() + " (" + android.os.Build.MODEL + "; Android" + android.os.Build.VERSION.RELEASE + ")")
                                            .addHeader(COOKIE_KEY, cookiePreferences.getString(COOKIE_KEY, ""))
                                            .addFile("video", LocalConfig.VIDEO_NAME, new File(data.getStringExtra("path")))
                                            .addParams("scene_id", _id)
                                            .build()
                                            .execute(mStringCallback);
                                }
                            });
                        }else {
                             //个人视频认证
                                myAlertDialog = new MyAlertDialog(baseContext, new MyAlertDialog.OnPositiveAlterListener() {
                                    @Override
                                    public void onAlter() {
                                        myProgressDialog.show();
                                        // 成功
                                        SharedPreferences cookiePreferences = baseContext.getSharedPreferences("CookieStore", BaseActivity.MODE_PRIVATE);
                                        LogUtil.d("AlbumFragment", "before request init");
                                        OkHttpUtils
                                                .post()
        //                                    .url("http://192.168.1.189/")
                                                .url(LocalUrl.POST_VERIFY_VIDEO)
                                                .addHeader("User-Agent", LocalUrl.getVersionCode() + " (" + android.os.Build.MODEL + "; Android" + android.os.Build.VERSION.RELEASE + ")")
                                                .addHeader(COOKIE_KEY, cookiePreferences.getString(COOKIE_KEY, ""))
                                                .addFile("video", LocalConfig.VIDEO_NAME, new File(data.getStringExtra("path")))

                                                .build()
                                                .execute(mStringCallback);
                                    }
                                });

                        }


                    myAlertDialog.setTitle(getString(R.string.app_tips_text46));
                    myAlertDialog.setMessage(getString(R.string.app_tips_text87));
                    myAlertDialog.show();
                } else {
                    // 失败
                    ToastUtil.showToast(getString(R.string.app_tips_text88));
                    ImageUtil.deleteFile(LocalConfig.FILE_DIR + LocalConfig.FILE_COMPRESSED_VIDEOS_DIR);
                }
                break;
        }
    }

    private StringCallback mStringCallback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e) {
            ImageUtil.deleteFile(LocalConfig.FILE_DIR + LocalConfig.FILE_COMPRESSED_VIDEOS_DIR);
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
            ImageUtil.deleteFile(LocalConfig.FILE_DIR + LocalConfig.FILE_COMPRESSED_VIDEOS_DIR);
            setResult(RESULT_SUCCESS);

            myProgressDialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(response);
                ToastUtil.showToast(jsonObject.getString("msg"));
                ActivityUtil.finishActivty();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            LogUtil.d("VideoVerifyActivity", response);
        }
    };
}
