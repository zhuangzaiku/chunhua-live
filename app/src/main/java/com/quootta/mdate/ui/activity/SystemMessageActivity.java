package com.quootta.mdate.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.domain.SystemMessageList;
import com.quootta.mdate.engine.myCenter.SysMessageRequest;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.ui.adapter.RvSystemMessageAdapter;
import com.quootta.mdate.utils.ActivityUtil;
import com.quootta.mdate.utils.GsonUtil;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.android.volley.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import butterknife.Bind;

public class SystemMessageActivity extends BaseActivity {

    @Bind(R.id.iv_back_title_bar)ImageView iv_back;
    @Bind(R.id.tv_title_bar)TextView tv_title;
    @Bind(R.id.rv_system_message)RecyclerView rv_message;

    private RequestQueue requestQueue;
    private RvSystemMessageAdapter systemMessageAdapter;
    private LinearLayoutManager linearLayoutManager;
    private SystemMessageList oldMessageList;
    private SystemMessageList systemMessageList;
    private String messageString;

    @Override
    protected void init() {
        requestQueue = BaseApp.getRequestQueue();
        messageString = getObject();
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_system_message;
    }

    @Override
    protected void initData() {
        initTitle();
        requestMessage();
    }

    private void requestMessage() {
        SysMessageRequest sysMessageRequest = new SysMessageRequest(new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                LogUtil.d("BillActivity", "Response:" + response);
                try {
                    ToastUtil.showToast(response.getString("msg").toString());
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
                }
                initRecyclerView();
            }
        });
        requestQueue.add(sysMessageRequest);

    }

    private void initRecyclerView() {
        systemMessageAdapter = new RvSystemMessageAdapter(baseContext,systemMessageList.notice);
        systemMessageAdapter.setOnItemClickListener(new RvSystemMessageAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                switch (systemMessageList.notice.get(position).type) {
                    case "send_gift":
                        Intent giftIntent = new Intent(SystemMessageActivity.this, InviteDetailActivity.class);
                        giftIntent.putExtra("send_gift_id",systemMessageList.notice.get(position).send_gift_id);
                        giftIntent.putExtra("invite_type", "0");
                        startActivity(giftIntent);
                        break;
                    case "invite":
                        Intent inviteIntent = new Intent(SystemMessageActivity.this, InviteDetailActivity.class);
                        inviteIntent.putExtra("invite_id",systemMessageList.notice.get(position).invite_id);
                        inviteIntent.putExtra("invite_type", "0");
                        startActivity(inviteIntent);
                        break;
                    case "url":
                        Intent urlIntent = new Intent(Intent.ACTION_VIEW);
                        urlIntent.setData(Uri.parse(systemMessageList.notice.get(position).url));
                        startActivity(urlIntent);
                        break;
                    case "msg":
                        break;
                }
            }
        });
        rv_message.setAdapter(systemMessageAdapter);

        linearLayoutManager = new LinearLayoutManager(baseContext,LinearLayoutManager.VERTICAL,false);
        rv_message.setLayoutManager(linearLayoutManager);

        try {
            saveObject(serialize(systemMessageList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initTitle() {
        iv_back.setVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.notice));
    }

    @Override
    protected void setListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.finishActivty();
            }
        });
    }

    void saveObject(String strObject) {
        SharedPreferences sp = getSharedPreferences("systemMessageList", MODE_PRIVATE);
        Editor edit = sp.edit();
        edit.putString("person", strObject);
        edit.commit();
    }

    String getObject() {
        SharedPreferences sp = getSharedPreferences("systemMessageList", MODE_PRIVATE);
        return sp.getString("person", null);
    }

    /**
     * 序列化对象
     *
     * @param systemMessageList
     * @return
     * @throws IOException
     */
    private String serialize(SystemMessageList systemMessageList) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        objectOutputStream.writeObject(systemMessageList);
        String serStr = byteArrayOutputStream.toString("ISO-8859-1");
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
        objectOutputStream.close();
        byteArrayOutputStream.close();
        Log.d("serial", "serialize str =" + serStr);
        return serStr;
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

}
