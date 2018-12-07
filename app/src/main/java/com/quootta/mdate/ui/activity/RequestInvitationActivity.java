package com.quootta.mdate.ui.activity;

import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.constant.LocalConfig;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.ui.dialog.MyProgressDialog;
import com.quootta.mdate.ui.popupWindow.SelectPicPopupWindow;
import com.quootta.mdate.utils.ImageUtil;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.android.volley.RequestQueue;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.util.List;
import java.util.concurrent.TimeoutException;

import butterknife.Bind;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import okhttp3.Call;

public class RequestInvitationActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.et_name_request_invitation_activity)EditText et_name;
    @Bind(R.id.et_number_request_invitation_activity)EditText et_number;
    @Bind(R.id.rg_sex_sign_up_activity)RadioGroup rg_gender;
    @Bind(R.id.et_job_request_invitation_activity)EditText et_job;
    @Bind(R.id.sp_income_request_invitation_activity)Spinner sp_income;
    @Bind(R.id.rl_selfie_request_invitation_activity)RelativeLayout rl_selfie;
    @Bind(R.id.iv_selfie_request_invitation_activity)ImageView iv_selfie;
    @Bind(R.id.rl_driver_request_invitation_activity)RelativeLayout rl_driver;
    @Bind(R.id.iv_driver_request_invitation_activity)ImageView iv_driver;
    @Bind(R.id.btn_submit_request_invitation_activity)Button btn_submit;
    @Bind(R.id.include_selfie)View include_selfie;
    @Bind(R.id.include_driver)View include_driver;

    private RequestQueue requestQueue;
    private SelectPicPopupWindow selectWindow;
    private MyProgressDialog myProgressDialog;
    private PostFormBuilder postFormBuilder;

    private static final int SELFIE_TYPE = 0;
    private static final int DRIVER_TYPE = 1;

    private static final int REQUEST_CODE_SELFIE_CAMARA = 0;
    private static final int REQUEST_CODE_SELFIE_GALLERY = 1;

    private static final int REQUEST_CODE_DRIVER_CAMARA = 2;
    private static final int REQUEST_CODE_DRIVER_GALLERY = 3;

    @Override
    protected void init() {
        postFormBuilder =
                OkHttpUtils
                        .post()
                        .url(LocalUrl.POST_SIGN_UP_APPLY)
                        .addHeader("User-Agent", LocalUrl.getVersionCode() + " (" + android.os.Build.MODEL + "; Android" + android.os.Build.VERSION.RELEASE + ")");
        requestQueue = BaseApp.getRequestQueue();
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_request_invition;
    }

    @Override
    protected void initData() {
        myProgressDialog = new MyProgressDialog(baseContext);
    }

    @Override
    protected void setListener() {
        rl_selfie.setOnClickListener(this);
        rl_driver.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_selfie_request_invitation_activity:
                openSelectWindow(SELFIE_TYPE);
                break;
            case R.id.rl_driver_request_invitation_activity:
                openSelectWindow(DRIVER_TYPE);
                break;
            case R.id.btn_submit_request_invitation_activity:
                requestInvitation();
                break;
        }
    }

    private void openSelectWindow(int type) {

        switch (type) {
            case SELFIE_TYPE:
                selectWindow = new SelectPicPopupWindow(baseContext, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectWindow.dismiss();
                        switch (v.getId()) {
                            case R.id.takePhotoBtn:// 拍照
                                GalleryFinal.openCamera(REQUEST_CODE_SELFIE_CAMARA, mOnHanlderResultCallback);
                                break;
                            case R.id.pickPhotoBtn:// 相册选择图片
                                GalleryFinal.openGallerySingle(REQUEST_CODE_SELFIE_GALLERY, mOnHanlderResultCallback);
                                break;
                            default:
                                break;
                        }
                    }
                });
                break;
            case DRIVER_TYPE:
                selectWindow = new SelectPicPopupWindow(baseContext, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectWindow.dismiss();
                        switch (v.getId()) {
                            case R.id.takePhotoBtn:// 拍照
                                GalleryFinal.openCamera(REQUEST_CODE_DRIVER_CAMARA, mOnHanlderResultCallback);
                                break;
                            case R.id.pickPhotoBtn:// 相册选择图片
                                GalleryFinal.openGallerySingle(REQUEST_CODE_DRIVER_GALLERY, mOnHanlderResultCallback);
                                break;
                            default:
                                break;
                        }

                    }
                });
                break;
            default:
                break;
        }
        selectWindow.showAtLocation(findViewById(R.id.ll_request_invitation_activity),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            switch (reqeustCode) {
                case REQUEST_CODE_SELFIE_CAMARA:
                    ImageUtil.savePhotoToPath(resultList, iv_selfie, LocalConfig.SELFIE_PATH);
                    include_selfie.setVisibility(View.GONE);
                    iv_selfie.setVisibility(View.VISIBLE);
                    break;

                case REQUEST_CODE_SELFIE_GALLERY:
                    ImageUtil.savePhotoToPath(resultList, iv_selfie, LocalConfig.SELFIE_PATH);
                    include_selfie.setVisibility(View.GONE);
                    iv_selfie.setVisibility(View.VISIBLE);
                    break;

                case REQUEST_CODE_DRIVER_CAMARA:
                    ImageUtil.savePhotoToPath(resultList, iv_driver, LocalConfig.DRIVER_PATH);
                    postFormBuilder
                            .addFile("drive_licence", LocalConfig.DRIVER_NAME, new File(Environment.getExternalStorageDirectory() + "/" + LocalConfig.DRIVER_PATH));
                    include_driver.setVisibility(View.GONE);
                    iv_driver.setVisibility(View.VISIBLE);
                    break;

                case REQUEST_CODE_DRIVER_GALLERY:
                    ImageUtil.savePhotoToPath(resultList, iv_driver, LocalConfig.DRIVER_PATH);
                    postFormBuilder
                            .addFile("drive_licence", LocalConfig.DRIVER_NAME, new File(Environment.getExternalStorageDirectory() + "/" + LocalConfig.DRIVER_PATH));
                    include_driver.setVisibility(View.GONE);
                    iv_driver.setVisibility(View.VISIBLE);
                    break;
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            LogUtil.d(TAG, "ErrorMsg :" + errorMsg);
            switch (requestCode) {
                case REQUEST_CODE_SELFIE_CAMARA:
                    ToastUtil.showToast(getString(R.string.app_tips_text49));
                    break;
                case REQUEST_CODE_SELFIE_GALLERY:
                    ToastUtil.showToast(getString(R.string.app_tips_text49));
                    break;
                case REQUEST_CODE_DRIVER_CAMARA:
                    ToastUtil.showToast(getString(R.string.app_tips_text49));
                    break;
                case REQUEST_CODE_DRIVER_GALLERY:
                    ToastUtil.showToast(getString(R.string.app_tips_text49));

                    break;
            }
        }
    };

    private void requestInvitation() {
        String gender = null;
        switch (rg_gender.getCheckedRadioButtonId()) {
            case R.id.rb_male_sign_up_activity:
                gender = "male";
                break;
            case R.id.rb_female_sign_up_activity:
                gender = "female";
                break;
        }

        File picFile = new File(Environment.getExternalStorageDirectory() + "/" + LocalConfig.SELFIE_PATH);

        if (et_name.getText().toString().trim().equals("")) {
            ToastUtil.showToast(getString(R.string.app_tips_text51));
        } else if (et_number.getText().toString().trim().equals("")) {
            ToastUtil.showToast(getString(R.string.app_tips_text67));
        } else if (et_job.getText().toString().trim().equals("")) {
            ToastUtil.showToast(getString(R.string.app_tips_text68));
        } else if (!picFile.exists()) {
            ToastUtil.showToast(getString(R.string.app_tips_text69));
        } else {
            myProgressDialog.show();
            postFormBuilder
                    .addFile("autodyne", LocalConfig.SELFIE_NAME, picFile)
                    .addParams("name", et_name.getText().toString().trim())
                    .addParams("gender", gender)
                    .addParams("mobile", et_number.getText().toString().trim())
                    .addParams("job", et_job.getText().toString().trim())
                    .addParams("income", (sp_income.getSelectedItemPosition() + 1) + "")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void inProgress(float progress) {
                            super.inProgress(progress);
                            super.inProgress(progress);
                            NumberFormat percentFormat = NumberFormat.getPercentInstance();
                            percentFormat.setMinimumFractionDigits(0);
                            myProgressDialog.setMessage(getString(R.string.app_tips_text19) + percentFormat.format(progress));
                        }

                        @Override
                        public void onError(Call call, Exception e) {
                            myProgressDialog.dismiss();
                            LogUtil.d("uploadPicture", "Exception e " + e.toString());
                            if (e instanceof FileNotFoundException) {
                                ToastUtil.showToast(getString(R.string.app_tips_text70));
                            } else if (e instanceof TimeoutException) {
                                ToastUtil.showToast(getString(R.string.app_tips_text71));
                            }
                        }

                        @Override
                        public void onResponse(String response) {
                            myProgressDialog.dismiss();
                            LogUtil.d("uploadPicture", response);
                            JSONObject responseJson = null;
                            try {
                                responseJson = new JSONObject(response);
                                ToastUtil.showToast(responseJson.getString("msg"));
                                if(responseJson.getString("code").equals(LocalUrl.SUCCESS_CODE)) {
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageUtil.deleteFile(LocalConfig.FILE_DIR);
    }
}
