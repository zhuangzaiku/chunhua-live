package com.quootta.mdate.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.domain.SystemMessageList;
import com.quootta.mdate.engine.myCenter.SysMessageRequest;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.ui.activity.MainActivity;
import com.quootta.mdate.ui.activity.SystemMessageActivity;
import com.quootta.mdate.utils.GsonUtil;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import io.rong.common.RLog;
import io.rong.imkit.RongContext;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.widget.AsyncImageView;
import io.rong.imkit.widget.ProviderContainerView;
import io.rong.imkit.widget.adapter.ConversationListAdapter;
import io.rong.imkit.widget.provider.IContainerItemProvider;

import static android.content.Context.MODE_PRIVATE;

/**
 * @Project android-live
 * @Package com.quootta.mdate.ui.adapter
 * @Author zhuangzaiku
 * @Date 2018/12/13
 */
public class MyConversationListAdapter extends ConversationListAdapter {

    private Context mContext;
    LayoutInflater mInflater;


    private RequestQueue requestQueue;
    private SystemMessageList oldMessageList;
    private SystemMessageList systemMessageList;
    private String messageString;

    public MyConversationListAdapter(Context context) {
        super(context);
        mContext = context;
        this.mInflater = LayoutInflater.from(this.mContext);

        requestQueue = BaseApp.getRequestQueue();
        messageString = getObject();
        requestMessage();
    }


    @Override
    protected void bindView(View v, int position, UIConversation data) {
        super.bindView(v, position, data);

        RelativeLayout relativeLayout = (RelativeLayout) v.findViewById(R.id.rl_sys_msg);
        if (position == 0 && systemMessageList != null && systemMessageList.notice != null
                && systemMessageList.notice.size() > 0) {
            SystemMessageList.notice notice = systemMessageList.notice.get(0);
            relativeLayout.setVisibility(View.VISIBLE);
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent messageIntent = new Intent(mContext, SystemMessageActivity.class);
                    mContext.startActivity(messageIntent);
                }
            });
            TextView title = (TextView) v.findViewById(R.id.rc_sys_title);
            title.setText(notice.title);
            TextView content = (TextView) v.findViewById(R.id.rc_sys_content);
            content.setText(notice.content);

            TextView time = (TextView) v.findViewById(R.id.rc_sys_msg_time);
            time.setText(notice.create_time);

        } else {
            relativeLayout.setVisibility(View.GONE);
        }
    }

    private void requestMessage() {
        SysMessageRequest sysMessageRequest = new SysMessageRequest(new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                LogUtil.d("BillActivity", "Response:" + response);
                try {
//                    ToastUtil.showToast(response.getString("msg").toString());
                    systemMessageList = GsonUtil.parse(response.getString("data"), SystemMessageList.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (messageString != null) {
                    try {
                        oldMessageList = deSerialization(messageString);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    systemMessageList.notice.addAll(oldMessageList.notice);
//                    SystemMessageList.notice notice = systemMessageList.new notice();
//                    notice.title = "sasf";
//                    notice.content = "水电费福建省地方黄金时代放款速度快放假";
//                    notice.create_time = "1991-01-01";
//                    systemMessageList.notice.add(notice);
                }
            }
        });
        requestQueue.add(sysMessageRequest);

    }

    /**
     * 反序列化对象
     *
     * @param str
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private SystemMessageList deSerialization(String str) throws IOException,
            ClassNotFoundException {
        String redStr = java.net.URLDecoder.decode(str, "UTF-8");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                redStr.getBytes("ISO-8859-1"));
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        SystemMessageList systemMessageList = (SystemMessageList) objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return systemMessageList;
    }

    String getObject() {
        SharedPreferences sp = mContext.getSharedPreferences("systemMessageList", MODE_PRIVATE);
        return sp.getString("person", null);
    }


}
