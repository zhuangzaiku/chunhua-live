package com.quootta.mdate.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.constant.LocalConfig;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.domain.Album;
import com.quootta.mdate.domain.AlbumList;
import com.quootta.mdate.engine.myCenter.AlbumRequest;
import com.quootta.mdate.engine.myCenter.DeleteFeedRequest;
import com.quootta.mdate.myInterface.OnItemClickListener;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.ui.adapter.RvAlbumAdapter;
import com.quootta.mdate.ui.dialog.MyProgressDialog;
import com.quootta.mdate.ui.popupWindow.DeletePopupWindow;
import com.quootta.mdate.ui.popupWindow.SelectPicPopupWindow;
import com.quootta.mdate.utils.FileUtil;
import com.quootta.mdate.utils.GsonUtil;
import com.quootta.mdate.utils.ImageUtil;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.quootta.mdate.utils.video.CompressMediaController;
import com.android.volley.RequestQueue;
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
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import okhttp3.Call;

public class AlbumActivity extends BaseActivity {

    @Bind(R.id.srl_album)SwipeRefreshLayout srl_album;
    @Bind(R.id.rv_album) RecyclerView rv_album;

    private RequestQueue requestQueue;
    private SharedPreferences cookiePreferences;
    private RvAlbumAdapter albumAdapter;
    private GridLayoutManager layoutManager;
    private AlbumList albumList;
    private SelectPicPopupWindow selectWindow;
    private DeletePopupWindow deleteWindow;
    private MyProgressDialog myProgressDialog;
    private int deletePosition;
    private int lastVisibleItem;
    private int page;
    private File tempFile;
    private Map<String, String> paramsMap;
    private static final String COOKIE_KEY = "Cookie";
    private final static int CAMERA_PERMISSION_REQUEST = 200;
    private final static int VIDEO_PERMISSION_REQUEST = 300;
    private final static int FILE_PERMISSION_REQUEST = 400;


    @Override
    protected void init() {
        albumList = (AlbumList) getIntent().getSerializableExtra("AlbumList");
        requestQueue = BaseApp.getRequestQueue();
        paramsMap = new HashMap<>();
        paramsMap.put("page", "0");
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_album;
    }

    @Override
    protected void initData() {
        initRecyclerView(albumList);
        myProgressDialog = new MyProgressDialog(baseContext);
        myProgressDialog.setCanceledOnTouchOutside(false);
        myProgressDialog.setCancelable(false);
    }

    @Override
    protected void setListener() {
        //下拉刷新时，从第0页开始请求
        srl_album.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                paramsMap.put("page", page + "");
                requestAlbum();
            }

        });

        //上拉加载时，从当前页数继续向下请求
        rv_album.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //SCROLL_STATE_DRAGGING  和   SCROLL_STATE_IDLE 两种效果自己看着来
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == albumAdapter.getItemCount()) {
                    srl_album.setRefreshing(true);
                    page++;
                    paramsMap.put("page", page + "");
                    requestAlbum();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
    }

    private void requestAlbum() {
        AlbumRequest albumRequest = new AlbumRequest(paramsMap, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                LogUtil.d("AlbumFragment", "Response: " + response);
                AlbumList list = new AlbumList();
                try {
                    list = GsonUtil.parse(response.getString("data"), AlbumList.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (page == 0){
                    LogUtil.d("AlbumFragment", "page = 0 ，下拉。");
                    albumList = list;
                    initRecyclerView(albumList);
                } else {
                    LogUtil.d("AlbumFragment", "page != 0 ，上拉。");
                    if (list.album.size() == 0) {
                        ToastUtil.showToast(getString(R.string.app_tips_text15));
                    }else {
                        albumList.album.addAll(list.album);
                        albumAdapter.notifyDataSetChanged();
                    }
                }
                srl_album.setRefreshing(false);

            }
        });
        requestQueue.add(albumRequest);
    }

    private void initRecyclerView(final AlbumList albumList) {
        albumAdapter = new RvAlbumAdapter(baseContext,albumList.album);
        rv_album.setAdapter(albumAdapter);

        layoutManager = new GridLayoutManager(baseContext,3);
        rv_album.setLayoutManager(layoutManager);

        albumAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Album album = albumList.album.get(position);
                if (position == 0) {//gridview中的第一个item作为上传图片button
                    showUploadWindow();
                } else {
                    Intent intent = new Intent(AlbumActivity.this, AlbumDetailActivity.class);
                    intent.putExtra("currentPosition", position);
                    intent.putExtra("albumList", albumList);
                    startActivity(intent);
                }

            }

            @Override
            public void onItemLongClick(View view, int position) {
                deletePosition = position;
                deleteWindow = new DeletePopupWindow(baseContext, itemsOnClick);
                deleteWindow.showAtLocation(findViewById(R.id.ll_album_layout),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
    }

    public void showUploadWindow() {
        selectWindow = new SelectPicPopupWindow(baseContext, itemsOnClick);
        selectWindow.showAtLocation(findViewById(R.id.ll_album_layout),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.takePhotoBtn:// 拍照
                    selectWindow.dismiss();
                    PermissionGen.with(AlbumActivity.this)
                            .addRequestCode(CAMERA_PERMISSION_REQUEST)
                            .permissions(
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ).request();
                    break;
                case R.id.pickPhotoBtn:// 相册选择图片
                    selectWindow.dismiss();
                    //配置功能
                    FunctionConfig functionConfig = new FunctionConfig.Builder()
                            .setMutiSelectMaxSize(5)
                            .setEnableEdit(true)
                            .setEnableCrop(true)
                            .setCropSquare(true)
                            .build();
                    GalleryFinal.openGalleryMuti(LocalConfig.REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);
                    break;
                case R.id.pickVideoBtn:
                    selectWindow.dismiss();
                    PermissionGen.with(AlbumActivity.this)
                            .addRequestCode(VIDEO_PERMISSION_REQUEST)
                            .permissions(
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.RECORD_AUDIO,
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ).request();
                    break;
                case R.id.btn_delete_dialog://删除所选照片
                    deleteWindow.dismiss();
                    requestDeleteFeed();
                    break;
                case R.id.btn_cancel_delete_dialog://取消
                    deleteWindow.dismiss();
                    break;
                case R.id.cancelBtn://取消
                    selectWindow.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    @PermissionSuccess(requestCode = CAMERA_PERMISSION_REQUEST)
    private void openCamera(){
        GalleryFinal.openCamera(LocalConfig.REQUEST_CODE_CAMARA, mOnHanlderResultCallback);
    }

    @PermissionSuccess(requestCode = VIDEO_PERMISSION_REQUEST)
    private void openVideo(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        startActivityForResult(intent, LocalConfig.REQUEST_CODE_GALLERY_VIDEO);
    }

    @PermissionFail(requestCode = VIDEO_PERMISSION_REQUEST)
    private void onPermissionFail() {
        ToastUtil.showToast(getString(R.string.app_tips_text16));
    }


    private void requestDeleteFeed() {
        Map<String, String> deleteParamsMap = new HashMap<>();
        deleteParamsMap.put("feed_id", albumList.album.get(deletePosition)._id);
        DeleteFeedRequest deleteFeedRequest = new DeleteFeedRequest(deleteParamsMap, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                LogUtil.d("AlbumFragment","Response: " +response);
                Toast.makeText(baseContext, getString(R.string.app_tips_text17), Toast.LENGTH_SHORT).show();
                albumList.album.remove(deletePosition);
                albumAdapter.notifyDataSetChanged();
            }
        });
        requestQueue.add(deleteFeedRequest);
    }

    //上传多图
    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
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
}
