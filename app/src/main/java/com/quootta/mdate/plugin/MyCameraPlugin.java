package com.quootta.mdate.plugin;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.domain.UserDetail;
import com.quootta.mdate.engine.invite.UserDetailRequest;
import com.quootta.mdate.engine.media.BalanceRequest;
import com.quootta.mdate.myListener.RongCallListener;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.ui.dialog.MyAlertDialog;
import com.quootta.mdate.utils.GsonUtil;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.rong.callkit.BaseCallActivity;
import io.rong.callkit.CallSelectMemberActivity;
import io.rong.callkit.RongCallAction;
import io.rong.callkit.RongVoIPIntent;
import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallCommon;
import io.rong.calllib.RongCallSession;
import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.RongKitIntent;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.plugin.image.PicturePreviewActivity;
import io.rong.imkit.plugin.image.PictureSelectorActivity;
import io.rong.imkit.utilities.PermissionCheckUtil;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Discussion;


/**
 * Created by Ryon on 2017/1/16/0016.
 */

public class MyCameraPlugin implements IPluginModule {

    private Conversation.ConversationType conversationType;
    private String targetId;
    private Context context;
    /**
     * 设置图标
     * @param context
     * @return
     */
    @Override
    public Drawable obtainDrawable(Context context) {
        return context.getResources().getDrawable(R.drawable.rc_ic_camera_normal);
    }

    /**
     * 设置图标下的文字
     * @param context
     * @return
     */
    @Override
    public String obtainTitle(Context context) {
        return "";
    }

    @Override
    public void onClick(Fragment currentFragment, RongExtension extension) {

        context = currentFragment.getActivity().getApplicationContext();
        conversationType = extension.getConversationType();
        targetId = extension.getTargetId();

        String[] permissions = new String[]{Manifest.permission.CAMERA};
        if (PermissionCheckUtil.requestPermissions(currentFragment, permissions)) {
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!path.exists()) {
                path.mkdirs();
            }

            String name = System.currentTimeMillis() + ".jpg";
            File file = new File(path, name);
            Uri uri = FileProvider.getUriForFile(context.getApplicationContext(), context.getApplicationContext().getPackageName() + ".FileProvider", file);
            Intent intentCamera = new Intent();
            intentCamera.putExtra("output", uri);
            intentCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            extension.startActivityForPluginResult(intentCamera, 23, this);

            this.conversationType = extension.getConversationType();
            this.targetId = extension.getTargetId();
        }

    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Intent intent = new Intent(RongKitIntent.RONG_INTENT_ACTION_PICTUREPAGERVIEW);
////        ArrayList<String> userIds = data.getStringArrayListExtra("invited");
////        userIds.add(RongIMClient.getInstance().getCurrentUserId());
//        intent.putExtra("conversationType", conversationType.getName().toLowerCase());
//        intent.putExtra("targetId", targetId);
//        intent.putExtra("callAction", RongCallAction.ACTION_OUTGOING_CALL.getName());
////        intent.putStringArrayListExtra("invitedUsers", userIds);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setPackage(context.getPackageName());
//        context.getApplicationContext().startActivity(intent);

    }


}
