package com.quootta.mdate.plugin;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.domain.UserDetail;
import com.quootta.mdate.engine.invite.UserDetailRequest;
import com.quootta.mdate.engine.media.BalanceRequest;
import com.quootta.mdate.myListener.RongCallListener;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.utils.GsonUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.android.volley.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

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
import io.rong.common.RLog;
import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.utilities.PermissionCheckUtil;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Discussion;

/**
 * Created by Ryon on 2017/1/17/0017.
 */

public class MyVideoPlugin implements IPluginModule {
    private static final String TAG = "VideoPlugin";
    private ArrayList<String> allMembers;
    private Context context;
    private boolean targetEableVideo;
    private Conversation.ConversationType conversationType;
    private String targetId;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private RongExtension rongExtension;
    private RequestQueue requestQueue;
    private static final  int BALANCE=999999999;
    private static final int RESU_OK=11;
    @Override
    public Drawable obtainDrawable(Context context) {
        return context.getResources().getDrawable(io.rong.callkit.R.drawable.rc_ic_video_selector);
    }

    @Override
    public String obtainTitle(Context context) {
        return context.getString(io.rong.callkit.R.string.rc_voip_video);
    }

    @Override
    public void onClick(Fragment currentFragment, final RongExtension extension) {

        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
        if (!PermissionCheckUtil.requestPermissions(currentFragment, permissions)) {
            return;
        }
        this.rongExtension=extension;
        context = currentFragment.getActivity().getApplicationContext();
        conversationType = extension.getConversationType();
        targetId = extension.getTargetId();
        requestQueue = BaseApp.getRequestQueue();
        pref=context.getSharedPreferences("iscall",0);
        editor=pref.edit();

        Log.i("tag","现在是不是正常退出 videocall测试"+pref.getBoolean("close",true));
        if (pref.getBoolean("close",true)){
            //刷新余额 并进行单击处理
            RefrechBalance(currentFragment);
        }else {
            long time=pref.getLong("time",0);

            //等待时长
            if(System.currentTimeMillis()>=time+90000){
                editor.putBoolean("close",true);
                editor.apply();
                BaseApp.setBalnace(BALANCE);

                //刷新余额 并进行单击处理
                RefrechBalance(currentFragment);
            }else {
                long surplusTime= (time+90000-System.currentTimeMillis())/1000;
//                ToastUtil.showToast("您的网络不太稳定请"+surplusTime+"秒后重试");
                ToastUtil.showToast(currentFragment.getString(R.string.app_tips_text4,surplusTime+""));
            }
        }

    }

    //刷新余额
    private void RefrechBalance(final Fragment fragment){
        Map<String, String> balanceMap = new HashMap<String, String>();
        BalanceRequest balanceRequest = new BalanceRequest(balanceMap, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {

                try {
                    JSONObject data = response.getJSONObject("data");
                    String balance = data.getString("balance");
                    // LogUtil.d("requestBalance", balance);
                    BaseApp.setGoldCount(balance);


                    //进行计费逻辑的处理

                    requestEnable(fragment);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected void onFail(JSONObject response) {
                super.onFail(response);

//                ToastUtil.showLongToast("无法获得您的余额 请稍后重新尝试");
                ToastUtil.showLongToast(fragment.getString(R.string.app_tips_text5));

            }
        });
        requestQueue.add(balanceRequest);
    }

    private void requestEnable(final Fragment fragment){
        RongCallSession profile = RongCallClient.getInstance().getCallSession();
        if (profile != null && profile.getActiveTime() > 0) {
            Toast.makeText(context, context.getString(io.rong.callkit.R.string.rc_voip_call_start_fail), Toast.LENGTH_SHORT).show();
            return;
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
            Toast.makeText(context, context.getString(io.rong.callkit.R.string.rc_voip_call_network_error), Toast.LENGTH_SHORT).show();
            return;
        }


        Map<String, String> idMap = new HashMap<String, String>();
        idMap.put("user_id", targetId);
        UserDetailRequest userDetailRequest = new UserDetailRequest(idMap, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                Log.i("tag","--用户详情-->"+response);
                UserDetail userDetail = null;
                try {
                    userDetail = GsonUtil.parse(response.getString("data"), UserDetail.class);

                    BaseCallActivity.setBaseCallListener(new RongCallListener(userDetail,context));//绑定通话监听

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                targetEableVideo = Boolean.parseBoolean(userDetail.video_enable);
                //判断是否是特殊账号
                if (!BaseApp.getIsSpecial()) {
                    if (targetEableVideo) {
                        Intent intent = new Intent("com.quootta.mdate.TransActivity");
                        intent.putExtra("type", fragment.getString(R.string.app_tips_text85));
                        intent.putExtra("price", userDetail.video_pay);
                        rongExtension.startActivityForPluginResult(intent, 120, MyVideoPlugin.this);
                    } else {
//                        ToastUtil.showToast("该用户暂时不接受视频聊天");
                        ToastUtil.showToast(fragment.getString(R.string.app_tips_text8));
                    }
                }else {
//                    ToastUtil.showToast("无法呼叫");
                    ToastUtil.showToast(fragment.getString(R.string.app_tips_text7));
                }
            }
        });
        requestQueue.add(userDetailRequest);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        switch (requestCode){
            case 120:
                if (resultCode==RESU_OK){
                    onVideo();
                }
                break;
            case 110:
                if (resultCode != Activity.RESULT_OK) {
                    return;
                }

                Intent intent = new Intent(RongVoIPIntent.RONG_INTENT_ACTION_VOIP_MULTIVIDEO);
                ArrayList<String> userIds = data.getStringArrayListExtra("invited");
                userIds.add(RongIMClient.getInstance().getCurrentUserId());
                intent.putExtra("conversationType", conversationType.getName().toLowerCase());
                intent.putExtra("targetId", targetId);
                intent.putExtra("callAction", RongCallAction.ACTION_OUTGOING_CALL.getName());
                intent.putStringArrayListExtra("invitedUsers", userIds);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage(context.getPackageName());
                context.getApplicationContext().startActivity(intent);
                break;
        }


    }

    private void onVideo(){
        if (conversationType.equals(Conversation.ConversationType.PRIVATE)) {
            Intent intent = new Intent(RongVoIPIntent.RONG_INTENT_ACTION_VOIP_SINGLEVIDEO);
            intent.putExtra("conversationType", conversationType.getName().toLowerCase());
            intent.putExtra("targetId", targetId);
            intent.putExtra("callAction", RongCallAction.ACTION_OUTGOING_CALL.getName());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setPackage(context.getPackageName());
            context.getApplicationContext().startActivity(intent);
        } else if (conversationType.equals(Conversation.ConversationType.DISCUSSION)) {
            RongIM.getInstance().getDiscussion(targetId, new RongIMClient.ResultCallback<Discussion>() {
                @Override
                public void onSuccess(Discussion discussion) {

                    Intent intent = new Intent(context, CallSelectMemberActivity.class);
                    allMembers = (ArrayList<String>) discussion.getMemberIdList();
                    intent.putStringArrayListExtra("allMembers", allMembers);
                    String myId = RongIMClient.getInstance().getCurrentUserId();
                    ArrayList<String> invited = new ArrayList<>();
                    invited.add(myId);
                    intent.putStringArrayListExtra("invitedMembers", invited);
                    intent.putExtra("mediaType", RongCallCommon.CallMediaType.VIDEO.getValue());
                    rongExtension.startActivityForPluginResult(intent, 110, MyVideoPlugin.this);
                }

                @Override
                public void onError(RongIMClient.ErrorCode e) {
                    RLog.d(TAG, "get discussion errorCode = " + e.getValue());
                }
            });
        } else if (conversationType.equals(Conversation.ConversationType.GROUP)) {
            Intent intent = new Intent(context, CallSelectMemberActivity.class);
            String myId = RongIMClient.getInstance().getCurrentUserId();
            ArrayList<String> invited = new ArrayList<>();
            invited.add(myId);
            intent.putStringArrayListExtra("invitedMembers", invited);
            intent.putExtra("groupId", targetId);
            intent.putExtra("mediaType", RongCallCommon.CallMediaType.VIDEO.getValue());
            rongExtension.startActivityForPluginResult(intent, 110, this);
        }
    }

}
