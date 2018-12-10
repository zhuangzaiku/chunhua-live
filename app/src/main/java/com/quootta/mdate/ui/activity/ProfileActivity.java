package com.quootta.mdate.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.quootta.mdate.domain.Album;
import com.quootta.mdate.domain.AlbumList;
import com.quootta.mdate.domain.InfoDetail;
import com.quootta.mdate.engine.myCenter.AlbumRequest;
import com.quootta.mdate.engine.myCenter.DeleteFeedRequest;
import com.quootta.mdate.engine.myCenter.UpdateAvatarRequest;
import com.quootta.mdate.myInterface.OnItemSelectedListener;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.task.AddressInitTask;
import com.quootta.mdate.ui.adapter.RvGalleryAdapter;
import com.quootta.mdate.ui.dialog.MyProgressDialog;
import com.quootta.mdate.ui.popupWindow.SelectPicPopupWindow;
import com.quootta.mdate.ui.view.CircleImageView;
import com.quootta.mdate.utils.ActivityUtil;
import com.quootta.mdate.utils.FileUtil;
import com.quootta.mdate.utils.GsonUtil;
import com.quootta.mdate.utils.ImageUtil;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.TakePhotoUtils;
import com.quootta.mdate.utils.ToastUtil;
import com.quootta.mdate.utils.video.CompressMediaController;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.NumberPicker;
import cn.qqtheme.framework.picker.OptionPicker;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import okhttp3.Call;

public class ProfileActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.iv_back_title_bar)ImageView iv_back;

    @Bind(R.id.tv_title_bar)TextView tvTitle;
    @Bind(R.id.rl_profile)
    RelativeLayout rl_profile;
    @Bind(R.id.iv_head_profile)CircleImageView iv_head;
    //    @Bind(R.id.tv_ensure_title_bar)TextView tv_ensure;
//    @Bind(R.id.tv_change_head_pic_profile)TextView tv_change_head;
    @Bind(R.id.et_sign_profile)EditText et_sign;
//    @Bind(R.id.te_sign_profile)TextView te_sign;

    //    @Bind(R.id.et_sex_profile)EditText et_sex;
    @Bind(R.id.et_nick_name_profile)EditText et_nick_name;
    //    @Bind(R.id.te_nick_name_profile)TextView te_nick_name;
    @Bind(R.id.ll_birthday_profile)LinearLayout ll_birthday;
    @Bind(R.id.et_birthday_profile)EditText et_birthday;
    @Bind(R.id.ll_city_profile)LinearLayout ll_city;
    @Bind(R.id.et_city_profile)EditText et_city;
    @Bind(R.id.ll_height_profile)LinearLayout ll_height;
    @Bind(R.id.et_height_profile)EditText et_height;
    @Bind(R.id.ll_weight_profile)LinearLayout ll_weight;
    @Bind(R.id.et_weight_profile)EditText et_weight;
    @Bind(R.id.et_hobby_profile)EditText et_hobby;
    @Bind(R.id.et_job_profile)EditText et_job;
    @Bind(R.id.et_request_profile)EditText et_request;
    @Bind(R.id.et_date_way_profile)EditText et_date_way;
    @Bind(R.id.et_advantage_profile)EditText etExpertise;
    @Bind(R.id.et_interests)EditText etInterests;
    @Bind(R.id.rv_gallery)RecyclerView rvGallery;
    @Bind(R.id.rl_gallery)RelativeLayout rlGallery;
    @Bind(R.id.btnProfileSave)Button btnProfileSave;

    private String avatar;
    private RequestQueue requestQueue;
    private SharedPreferences cookiePreferences;
    private PostFormBuilder profileBuilder;
    private ImageLoader imageLoader;
    private MyProgressDialog myProgressDialog;
    private SelectPicPopupWindow menuWindow;
    private SelectPicPopupWindow selectWindow;
    private File tempFile;

    private final int TYPE_WEIGHT = 0;
    private final int TYPE_HEIGHT = 1;

    private final int RESULT_SUCCESS = 1;
    private final static int CAMERA_PERMISSION_REQUEST = 200;
    private final static int VIDEO_PERMISSION_REQUEST = 300;
    private final static int FILE_PERMISSION_REQUEST = 400;

    private static final String COOKIE_KEY = "Cookie";
    private Map<String, String> paramsMap;
    private AlbumList albumList;
    private RvGalleryAdapter galleryAdapter;
    private int deletePosition;

    @Override
    protected void init() {
        cookiePreferences = baseContext.getSharedPreferences("CookieStore", BaseActivity.MODE_PRIVATE);
        requestQueue = BaseApp.getRequestQueue();
        imageLoader = BaseApp.getImageLoader();
        myProgressDialog = new MyProgressDialog(baseContext);
//        byte[] bis =  getIntent().getByteArrayExtra("avatar");
//        Bitmap bitmap = BitmapFactory.decodeByteArray(bis, 0, bis.length);
//        avatar = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        avatar = LocalUrl.getPicUrl(InfoDetail.avatar);
        profileBuilder = OkHttpUtils
                .post()
                .url(LocalUrl.POST_INFO_EDIT)
                .addHeader("User-Agent", LocalUrl.getVersionCode()+" (" + android.os.Build.MODEL + "; Android" + android.os.Build.VERSION.RELEASE + ")")
                .addHeader(COOKIE_KEY, cookiePreferences.getString(COOKIE_KEY, ""));
        LogUtil.d("ProfileActivity",cookiePreferences.getString(COOKIE_KEY, ""));

        paramsMap = new HashMap<>();
        paramsMap.put("page", "0");
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_profile;
    }

    @Override
    protected void initData() {
        initTitle();
        initContent();
        //展示图片的接口
        requestAlbum();
    }

    private void requestAlbum() {

        //展示图片的接口
        AlbumRequest albumRequest = new AlbumRequest(paramsMap, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                LogUtil.d("AlbumFragment", "Response: " + response);
                Log.i("tag","图片response---->"+response);
                albumList = new AlbumList();
                try {
                    albumList = GsonUtil.parse(response.getString("data"),AlbumList.class);
                    initRecyclerView(albumList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        requestQueue.add(albumRequest);
    }

    //显示相册的适配器
    private void initRecyclerView(final AlbumList albumList) {
        if (albumList.album.size() >7) {
            List<Album> subList = albumList.album.subList(0, 7);

            galleryAdapter = new RvGalleryAdapter(baseContext, subList,0);
        } else {
            galleryAdapter = new RvGalleryAdapter(baseContext, albumList.album,0);
        }
        galleryAdapter.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onSelected(Drawable drawable, int position) {
//                Intent gallery = new Intent(baseContext, AlbumActivity.class);
//                gallery.putExtra("AlbumList", albumList);
//                startActivity(gallery);
//                selectWindow = new SelectPicPopupWindow(context, AlbumOnClick);
//                selectWindow.showAtLocation(findViewById(R.id.ll_profile_activity),
//                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                deletePosition = position;
                showAlbum();


            }
        });
        rvGallery.setAdapter(galleryAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(baseContext);
        linearLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);

        rvGallery.setLayoutManager(linearLayoutManager);
    }




    private void initTitle() {
        if(avatar != null) {
            rl_profile.setBackgroundColor(getResources().getColor(R.color.myWhite));
        }

        tvTitle.setText(R.string.edit_profile);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        Drawable drawable1 = getResources().getDrawable(R.mipmap.signature);
//        drawable1.setBounds(0, 0, 50, 50);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
//        et_sign.setCompoundDrawables(drawable1, null, null, null);//只放左边
    }

    private void initContent() {

//        if(infoDetail.avatar != null) {
//            imageLoader.get(
//                    LocalURL.getPicUrl(infoDetail.avatar),
//                    imageLoader.getImageListener(iv_head, R.mipmap.home_show_loading, R.mipmap.home_show_loading));
//        }
        Glide.with(baseContext).load(avatar).into(iv_head);

//        iv_head.setImageBitmap(avatar);

        et_sign.setText(InfoDetail.personal_desc);
//        te_sign.setText(InfoDetail.personal_desc);
//        te_nick_name.setText(InfoDetail.nick_name);
        et_nick_name.setText(InfoDetail.nick_name);
        et_birthday.setText(InfoDetail.birthday);
        et_city.setText(InfoDetail.city);
        if(!InfoDetail.height.equals("-1"))
            et_height.setText(InfoDetail.height);
        if(!InfoDetail.weight.equals("-1"))
            et_weight.setText(InfoDetail.weight);
        et_hobby.setText(InfoDetail.hobby);
        et_job.setText(InfoDetail.job);
        et_request.setText(InfoDetail.request);
        et_date_way.setText(InfoDetail.date_way);
        etExpertise.setText(InfoDetail.expertise);
        etInterests.setText(InfoDetail.chat);
    }

    @Override
    protected void setListener() {
//        iv_back.setOnClickListener(this);
//        tv_ensure.setOnClickListener(this);
        iv_head.setOnClickListener(this);
        btnProfileSave.setOnClickListener(this);
//        tv_change_head.setOnClickListener(this);
        et_birthday.setOnClickListener(this);
        et_city.setOnClickListener(this);
        et_height.setOnClickListener(this);
        et_weight.setOnClickListener(this);
        rlGallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.iv_back_title_bar:
//                ActivityUtil.finishActivty();
//                break;
//            case R.id.tv_ensure_title_bar:
//                requestProfileUpdate();
//                break;
//            case R.id.tv_change_head_pic_profile:
            case R.id.iv_head_profile:
                menuWindow = new SelectPicPopupWindow(baseContext, itemsOnClick);
                menuWindow.setVideoBtnVisible(View.GONE);
                menuWindow.showAtLocation(findViewById(R.id.ll_profile_activity),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            //相册展示
//            case R.id.rl_gallery:
//                Intent gallery = new Intent(baseContext, AlbumActivity.class);
//                gallery.putExtra("AlbumList", albumList);
//                startActivity(gallery);
//                break;
            case R.id.et_birthday_profile:
                onYearMonthPicker();
                break;
            case R.id.et_city_profile:
                new AddressInitTask(this, true, new AddressInitTask.OnCitySelectListener() {
                    @Override
                    public void onSelect(String province, String city, String county) {
                        String cityInfo = province + " " + city + " " + county;
                        et_city.setText(cityInfo);
                        profileBuilder.addParams("city",cityInfo);
                    }
                }).execute("北京市", "北京市");
                break;
            case R.id.et_height_profile:
                onNumberPicker(TYPE_HEIGHT);
                break;
            case R.id.et_weight_profile:
                onNumberPicker(TYPE_WEIGHT);
                break;
            case R.id.btnProfileSave:
                requestProfileUpdate();
                break;
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
                    PermissionGen.with(ProfileActivity.this)
                            .addRequestCode(CAMERA_PERMISSION_REQUEST)
                            .permissions(
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ).request();
                    break;
                // 相册选择图片
                case R.id.pickPhotoBtn:
                    GalleryFinal.openGallerySingle(LocalConfig.REQUEST_CODE_GALLERY, mOnHanlderResultCallback);
                    break;
                default:
                    break;
            }
        }
    };


    @PermissionSuccess(requestCode = CAMERA_PERMISSION_REQUEST)
    private void openCamera(){
        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.M){
            //7.0打开相机
            TakePhotoUtils.openCamera(ProfileActivity.this,LocalConfig.AVATAR_PATH);
        }else {

            GalleryFinal.openCamera(LocalConfig.REQUEST_CODE_CAMARA, mOnHanlderResultCallback);
        }

    }

    @PermissionFail(requestCode = CAMERA_PERMISSION_REQUEST)
    private void onPermissionFail() {
        ToastUtil.showToast(getString(R.string.app_tips_text16));
    }


    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                ImageUtil.savePhotoToPath(resultList, iv_head, LocalConfig.AVATAR_PATH);
                profileBuilder.addFile("avatar", LocalConfig.AVATAR_NAME, new File(Environment.getExternalStorageDirectory() + "/" + LocalConfig.AVATAR_PATH));
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            ToastUtil.showToast(getString(R.string.app_tips_text49));
        }
    };

    public void onYearMonthPicker() {
        DatePicker picker = new DatePicker(this, DatePicker.YEAR_MONTH_DAY);
        picker.setRange(1919, 2015);
        picker.setSelectedItem(1990, 6, 4);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                String birthday = year + "-" + month + "-" +day;
                et_birthday.setText(birthday);
                profileBuilder.addParams("birthday",birthday);
            }
        });
        picker.show();
    }

    public void onNumberPicker(final int type) {
        NumberPicker picker = new NumberPicker(this);
        picker.setOffset(2);//偏移量
        switch (type) {
            case TYPE_WEIGHT:
                picker.setRange(30, 110);//数字范围
                picker.setSelectedItem(50);
                picker.setLabel("kg");
                break;
            case TYPE_HEIGHT:
                picker.setRange(140, 229);//数字范围
                picker.setSelectedItem(170);
                picker.setLabel("cm");
                break;
        }
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
                if (type == TYPE_HEIGHT) {
                    profileBuilder.addParams("height", option);
                    et_height.setText(option);
                } else {
                    profileBuilder.addParams("weight", option);
                    et_weight.setText(option);
                }
            }
        });
        picker.show();
    }

    private void requestProfileUpdate() {

        myProgressDialog.show();
        profileBuilder
                .addParams("personal_desc", et_sign.getText().toString().trim())
                .addParams("nick_name", et_nick_name.getText().toString().trim())
                .addParams("hobby", et_hobby.getText().toString().trim())
                .addParams("job", et_job.getText().toString().trim())
                .addParams("request",et_request.getText().toString().trim())
                .addParams("date_way",et_date_way.getText().toString().trim())
                .addParams("expertise",etExpertise.getText().toString().trim())
                .addParams("chat",etInterests.getText().toString().trim())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        ImageUtil.deleteFile(LocalConfig.FILE_DIR);
                        LogUtil.d("postSignUpInfo", "Exception e--> " + e.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        myProgressDialog.dismiss();
                        LogUtil.d("postSignUpInfo", "profileactivity---->"+response);
                        ImageUtil.deleteFile(LocalConfig.FILE_DIR);
                        JSONObject responseJson;
                        try {
                            responseJson = new JSONObject(response);
                            ToastUtil.showToast(responseJson.getString("msg"));
                            if (responseJson.getString("code").equals(LocalUrl.SUCCESS_CODE)) {
                                setResult(RESULT_SUCCESS);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_submit:
                requestProfileUpdate();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageUtil.deleteFile(LocalConfig.FILE_DIR);
    }





    //相册适配器的一系列


    //弹出照片选择器
    public void showAlbum(){
        selectWindow = new SelectPicPopupWindow(baseContext, AlbumOnClick);
        selectWindow.setChooseBtnVisible(View.GONE);
        selectWindow.showAtLocation(findViewById(R.id.ll_profile_activity),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);


    }


    //为弹出窗口实现监听类
    private View.OnClickListener AlbumOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.takePhotoBtn:// 拍照
                    selectWindow.dismiss();
                    if (albumList.album.size()<7){

                        PermissionGen.with(ProfileActivity.this)
                                .addRequestCode(CAMERA_PERMISSION_REQUEST)
                                .permissions(
                                        Manifest.permission.CAMERA,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                ).request();
                    }else {
                        ToastUtil.showToast(getString(R.string.app_tips_text57));
                    }
                    break;
                case R.id.pickPhotoBtn:// 相册选择图片
                    selectWindow.dismiss();
                    if (albumList.album.size()<7){

                        //配置功能
                        FunctionConfig functionConfig = new FunctionConfig.Builder()
                                .setMutiSelectMaxSize(1)
                                .setEnableEdit(true)
                                .setEnableCrop(true)
                                .setCropSquare(true)
                                .build();
                        GalleryFinal.openGalleryMuti(LocalConfig.REQUEST_CODE_GALLERY, functionConfig,mOnAlbumCallBack );
                    }else {
                        ToastUtil.showToast(getString(R.string.app_tips_text57));
                    }
                    break;
                case R.id.pickVideoBtn:
                    selectWindow.dismiss();
                    if (albumList.album.size()<7){
                        PermissionGen.with(ProfileActivity.this)
                                .addRequestCode(VIDEO_PERMISSION_REQUEST)
                                .permissions(
                                        Manifest.permission.CAMERA,
                                        Manifest.permission.RECORD_AUDIO,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                ).request();
                    }else {
                        ToastUtil.showToast(getString(R.string.app_tips_text57));
                    }
                    break;
                //删除
                case R.id.deleteBtn:
                    selectWindow.dismiss();
                    if (deletePosition>=albumList.album.size()){
                        ToastUtil.showToast(getString(R.string.app_tips_text58));
                    }else {
                        requestDeleteFeed();
                    }
                    break;
                case R.id.lookBtn:
                    selectWindow.dismiss();
                    if (deletePosition>=albumList.album.size()){
                        ToastUtil.showToast(getString(R.string.app_tips_text59));
                    }else {
                        Intent intent = new Intent(ProfileActivity.this, AlbumDetailActivity.class);
                        Log.i("tag","当前点击的deleteposition-->"+deletePosition);
                        intent.putExtra("currentPosition", deletePosition);
                        intent.putExtra("albumList", albumList);
                        startActivity(intent);
                    }

                    break;
                //设为封面
                case R.id.avatar_update_btn:
                    selectWindow.dismiss();
                    if (deletePosition>=albumList.album.size()){
                        ToastUtil.showToast(getString(R.string.app_tips_text60));
                    }else {
                        requestAvatarUpdate();
                    }

                    break;
//                case R.id.btn_delete_dialog://删除所选照片
//                    deleteWindow.dismiss();
//                    requestDeleteFeed();
//                    break;
//                case R.id.btn_cancel_delete_dialog://取消
//                    deleteWindow.dismiss();
//                    break;
                case R.id.cancelBtn://取消
                    selectWindow.dismiss();
                    break;
                default:
                    break;
            }
        }
    };


    //设为封面
    private void requestAvatarUpdate(){
        Map<String,String> UpdateAvatarMap=new HashMap<>();
        UpdateAvatarMap.put("res_id",albumList.album.get(deletePosition).res_id);
        UpdateAvatarRequest updateAvatarRequest=new UpdateAvatarRequest(UpdateAvatarMap, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                ToastUtil.showLongToast(getString(R.string.app_tips_text61));
                //重新从服务器获取数据
                requestAlbum();
                //刷新相册
                //   galleryAdapter.notifyDataSetChanged();
            }

            @Override
            protected void onFail(JSONObject response) {
                super.onFail(response);
                ToastUtil.showLongToast(getString(R.string.app_tips_text62));
            }
        }
        );
        requestQueue.add(updateAvatarRequest);
    }


    //删除图片
    private void requestDeleteFeed() {
        Map<String, String> deleteParamsMap = new HashMap<>();
        deleteParamsMap.put("feed_id", albumList.album.get(deletePosition)._id);
        DeleteFeedRequest deleteFeedRequest = new DeleteFeedRequest(deleteParamsMap, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                LogUtil.d("AlbumFragment","Response: " +response);
                Toast.makeText(baseContext, getString(R.string.app_tips_text63), Toast.LENGTH_SHORT).show();
                albumList.album.remove(deletePosition);
                //galleryAdapter.notifyDataSetChanged();

                //适配器重新加载数据
                if (albumList.album.size() >7) {
                    List<Album> subList = albumList.album.subList(0, 7);

                    galleryAdapter = new RvGalleryAdapter(baseContext, subList,0);
                } else {
                    galleryAdapter = new RvGalleryAdapter(baseContext, albumList.album,0);
                }
                rvGallery.setAdapter(galleryAdapter);

                GridLayoutManager gridLayoutManager = new GridLayoutManager(baseContext,4);

                rvGallery.setLayoutManager(gridLayoutManager);
                //重新设置监听
                galleryAdapter.setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onSelected(Drawable drawable, int position) {
                        deletePosition = position;
                        showAlbum();

                    }
                });

            }
        });
        requestQueue.add(deleteFeedRequest);
    }



    //上传多图
    private GalleryFinal.OnHanlderResultCallback mOnAlbumCallBack = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                if (myProgressDialog==null){
                    myProgressDialog=new MyProgressDialog(baseContext);
                }
                myProgressDialog.show();
                cookiePreferences = baseContext.getSharedPreferences("CookieStore", BaseActivity.MODE_PRIVATE);
                PostFormBuilder postFormBuilder = OkHttpUtils
                        .post()
                        .url(LocalUrl.POST_FEED_CREATE)
                        .addHeader("User-Agent", LocalUrl.getVersionCode() + " (" + android.os.Build.MODEL + "; Android" + android.os.Build.VERSION.RELEASE + ")")
                        .addHeader(COOKIE_KEY,cookiePreferences.getString(COOKIE_KEY, ""));
                String fileName;
                Bitmap bitmap;
                int i = 0;
                for(PhotoInfo photoInfo : resultList) {
                    //对图片进行压缩
                    bitmap = ImageUtil.getImage(photoInfo.getPhotoPath());
                    fileName = photoInfo.getPhotoPath().substring(photoInfo.getPhotoPath().lastIndexOf("/") + 1);
                    ImageUtil.saveBitmap2file(bitmap, Environment.getExternalStorageDirectory() + "/" + fileName + i );
                    //将压缩后的图片上传
                    postFormBuilder.addFile("photo", fileName + i, new File(Environment.getExternalStorageDirectory() + "/" + fileName + i));
                }
                postFormBuilder.build()
                        .execute(mStringCallback);
                LogUtil.d("AlbumFragment", "onHanlderSuccess ");
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            ToastUtil.showToast(errorMsg);
        }
    };

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
            paramsMap.put("page","0");
            requestAlbum();
            myProgressDialog.dismiss();
            LogUtil.d("uploadPicture", response);

        }
    };

    /**
     * 上传视频
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==Activity.RESULT_OK){
            //7.0相机拍摄完返回的数据
            if (requestCode==1006){

                String path=Environment.getExternalStorageDirectory() + "/" + LocalConfig.AVATAR_PATH;
                //压缩图片
                Bitmap bitmap = ImageUtil.getImage(path);
                String fileName = path.substring(path.lastIndexOf("/") + 1);
                ImageUtil.saveBitmap2file(bitmap, Environment.getExternalStorageDirectory() + "/" + fileName+"1");
                //把图片放到头像上
                iv_head.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/" + fileName+"1"));
                //上传头像到服务器
                profileBuilder.addFile("avatar", LocalConfig.AVATAR_NAME, new File(Environment.getExternalStorageDirectory() + "/" + fileName+"1"));
            }
        }


        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();



            if (requestCode == LocalConfig.REQUEST_CODE_GALLERY_VIDEO) {
                LogUtil.d("AlbumFragment", "first if");
                if (uri != null) {
                    LogUtil.d("AlbumFragment", "uri：" + uri.toString());
                    if (myProgressDialog==null){
                        myProgressDialog=new MyProgressDialog(baseContext);
                    }
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
                            new ProfileActivity.VideoCompressor().execute();
                        }
                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                }

            }

        }

    }


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



    @PermissionSuccess(requestCode = VIDEO_PERMISSION_REQUEST)
    private void openVideo(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        startActivityForResult(intent, LocalConfig.REQUEST_CODE_GALLERY_VIDEO);
    }

    @PermissionFail(requestCode = VIDEO_PERMISSION_REQUEST)
    private void onVideoPermissionFail() {
        ToastUtil.showToast(getString(R.string.app_tips_text16));
    }


}
