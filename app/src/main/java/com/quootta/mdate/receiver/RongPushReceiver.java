package com.quootta.mdate.receiver;

import android.content.Context;

import com.quootta.mdate.utils.LogUtil;

import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * Created by Ryon on 2016/6/20/0020.
 */
public class RongPushReceiver extends PushMessageReceiver {

    /**
     * 用来接收服务器发来的通知栏消息(消息到达客户端时触发)，默认return false，
     * 通知消息会以融云 SDK 的默认形式展现。
     * 如果需要自定义通知栏的展示，在这里实现自己的通知栏展现代码，同时 return true 即可。
     * @param context
     * @param pushNotificationMessage
     * @return
     */
    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage pushNotificationMessage) {
        LogUtil.d("RongPushReceiver","onNotificationMessageArrived");
        return false;
    }

    /**
     *是在用户点击了通知栏消息时触发
     * (注意:如果自定义了通知栏的展现，则不会触发 标红)，默认 return false 。
     *
     * 如果需要自定义点击通知时的跳转，return true 即可。融云 SDK 默认跳转规则如下
     * 只有⼀个联系人发来一条或者多条消息时，会通过 intent 隐式启动会话 activity，intent 的 uri 如下：
     * @param context
     * @param pushNotificationMessage
     * @return
     */
    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage pushNotificationMessage) {
        LogUtil.d("RongPushReceiver","onNotificationMessageClicked");

//        Intent intent = new Intent();
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        Uri.Builder builder = Uri.parse("rong://" + context.getPackageName()).buildUpon();
//
//        builder.appendPath("conversation")
//                .appendPath(pushNotificationMessage.getConversationType().getName())
//                .appendQueryParameter("targetId", pushNotificationMessage.getTargetId())
//                .appendQueryParameter("title", pushNotificationMessage.getTargetUserName());
//        Uri uri = builder.build();
//        intent.setData(uri);
//        context.startActivity(intent);
        return false;
    }
}
