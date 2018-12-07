package com.quootta.mdate.myListener;

import android.util.Log;

import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.domain.UserDetail;
import com.quootta.mdate.engine.invite.UserDetailRequest;
import com.quootta.mdate.ui.message.RongGiftMessage;
import com.quootta.mdate.utils.GsonUtil;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.android.volley.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.rong.calllib.RongCallClient;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;


/**
 * Created by Ryon on 2016/12/5/0005.
 * 接受用户发出的消息
 */

public class RongReceiveMessageListener implements RongIMClient.OnReceiveMessageListener {


    @Override
    public boolean onReceived(Message message, int i) {

        MessageContent content=message.getContent();

        Log.i("tag","message中的信息--->"+message.getContent().getJSONUserInfo());


        if (content instanceof RongGiftMessage){
            //直接强转成礼物消息类型
            final RongGiftMessage rongGiftMessage= (RongGiftMessage) content;


            //判断通话状态
           if (RongCallClient.getInstance().getCallSession()!=null){


               RequestQueue requestQueue=BaseApp.getRequestQueue();
               Map paramsMap=new HashMap();
               paramsMap.put("user_id",message.getSenderUserId());
               UserDetailRequest userDetailRequest = new UserDetailRequest(paramsMap,
                       new VolleyListener() {
                           @Override
                           protected void onSuccess(JSONObject response) {
                               LogUtil.d("PersonalDetailsActivity", response.toString());
                               try {
                                   UserDetail userDetail = GsonUtil.parse(response.getString("data"), UserDetail.class);

                                   if (rongGiftMessage!=null){

                                       ToastUtil.showGiftToast(BaseApp.getApplication(),userDetail.nick_name,rongGiftMessage.getGiftName(),userDetail.avatar,rongGiftMessage.getImageName());
                                   }

                               } catch (JSONException e) {
                                   e.printStackTrace();
                               }

                           }
                       });
               requestQueue.add(userDetailRequest);

            } else {

                LogUtil.i("tag","没有在通话中---");
            }

        }

        return false;
    }
}
