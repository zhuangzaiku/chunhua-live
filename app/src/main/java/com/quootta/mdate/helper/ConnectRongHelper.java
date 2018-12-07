package com.quootta.mdate.helper;

import android.content.Context;
import android.util.Log;

import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.domain.UserChatInfoList;
import com.quootta.mdate.engine.invite.UserInfoRequest;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.utils.GsonUtil;
import com.quootta.mdate.utils.LogUtil;
import com.android.volley.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * 链接融云服务器的Helper
 * Created by Ryon on 2016/7/5/0005.
 */
public class ConnectRongHelper {

    private final String TAG = "userchat";

    private String ryToken;
    private OnConnectListener onConnectListener;
    private RequestQueue requestQueue;
    private Context context;
    public interface OnConnectListener {
        void onConnectSuccess(UserChatInfoList userChatInfoList);
        void onConnectFail();
    }

    public ConnectRongHelper(String ryToken, OnConnectListener onConnectListener, Context context) {
        this.ryToken = ryToken;
        this.onConnectListener = onConnectListener;
        this.context=context;
        requestQueue = BaseApp.getRequestQueue();
    }

    public void connectRongIM() {
        if (BaseApp.getApplication().getApplicationInfo().packageName.equals(BaseApp.getCurProcessName(BaseApp.getApplication()))) {
            LogUtil.e(TAG, "connectRongIM into if block\n" + "--ryToken:" + ryToken);
            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            RongIM.connect(ryToken, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
                 */
                @Override
                public void onTokenIncorrect() {
                    Log.e(TAG, "--onTokenIncorrect");

                    //ToastUtil.showToast("与服务器的连接过期，请重新登录");
                    //Toast.makeText(context, "与服务器的连接过期，请重新登录", Toast.LENGTH_SHORT).show();

                    //如果连接失败 跳转到登录界面 重新登录 再次获取token

//                    Intent intent=new Intent(context, OtherLoginActivity.class);
//                    context.startActivity(intent);


                    onConnectListener.onConnectFail();
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */
                @Override
                public void onSuccess(String userid) {
                    Log.e(TAG, "onSuccess");
                    //设置视频清晰度
                   // RongCallClient.getInstance().setVideoProfile(RongCallCommon.CallVideoProfile.VIDEO_PROFILE_360P);
                    getChatList(userid);
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.e(TAG, "--onError" + errorCode.getMessage());
                 //   ToastUtil.showToast("服务器开小差啦，请重新登录");
                    onConnectListener.onConnectFail();
                }
            });
        }
    }

    /**
     * 1、为融云聊天接口设置用户本人的头像、昵称等信息
     * 2、获取聊天列表中用户对应的idList
     * 3、将该idList发送给服务器，获取聊天列表中用户的头像、昵称等信息
     */
    private void getChatList(final String userid) {
        Log.e(TAG, "getChatList");
        //设置消息内附加用户信息接口
       // RongIM.getInstance().setMessageAttachedUserInfo(true);

        RongIMClient.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                List<String> userIds = new ArrayList<String>();
                Log.e(TAG,"有用户数据");
                if (conversations != null) { //如果聊天列表不为空，则先获取聊天列表信息再登录，否则直接登录
                    for (Conversation conversation : conversations) {
                        userIds.add(conversation.getTargetId());
                        LogUtil.e(TAG,"有用户数据 conversation.id"+conversation.getTargetId());
                    }
                    userIds.add(userid);
                    requestUserInfo(userIds);
                } else {
                    Log.e(TAG,"为null");
                    onConnectListener.onConnectSuccess(null);
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                onConnectListener.onConnectFail();
                Log.d("tag", "ErrorCode: " + errorCode);

               // ToastUtil.showToast("获取会话列表失败,请重试");
            }
        });
    }

    /**
     * 在进入主界面之前通过聊天列表的用户idList，向服务器请求用户InfoList
     *
     * @param userIdList
     */
    private void requestUserInfo(List<String> userIdList) {
        Log.e(TAG, "requestUserInfo");
        Map<String, String> map = new HashMap<>();
        String userIds = null;
        try {
            userIds = URLEncoder.encode(userIdList.toString(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        map.put("user_ids", userIds);
        UserInfoRequest userInfoRequest = new UserInfoRequest(map, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                LogUtil.d(TAG, "getUserInfo:" + response.toString());
                LogUtil.e(TAG,"有用户数据 conversation.id");
                UserChatInfoList userChatInfoList = new UserChatInfoList();
                try {
                    userChatInfoList = GsonUtil.parse(response.getString("data"), UserChatInfoList.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                LogUtil.d(TAG, "getChatList end time:" + System.currentTimeMillis());
                onConnectListener.onConnectSuccess(userChatInfoList);
            }

            @Override
            protected void onFail(JSONObject response) {
                super.onFail(response);
                Log.e("tag","获取对话列表错误--》"+response);
                onConnectListener.onConnectFail();
            }
        });
        requestQueue.add(userInfoRequest);
    }
}
