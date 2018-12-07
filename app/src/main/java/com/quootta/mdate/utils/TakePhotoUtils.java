package com.quootta.mdate.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.quootta.mdate.base.BaseApp;

import java.io.File;


/**
 * Created by Ryon on 2017/2/10/0010.
 * 调用相机的工具类
 */

public class TakePhotoUtils {
    public static void openCamera(Activity activity,String path){
        File file=new File(Environment.getExternalStorageDirectory(),"/"+path);
        if (!file.getParentFile().exists())file.getParentFile().mkdirs();
        Uri imageUri = FileProvider.getUriForFile(BaseApp.getApplication(), "com.quootta.mdate.FileProvider", file);//通过FileProvider创建一个content类型的Uri
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
        activity.startActivityForResult(intent,1006);
    }
}
