package com.quootta.mdate.ui.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.domain.ChatUserInfo;
import com.quootta.mdate.domain.OnOffData;
import com.quootta.mdate.engine.config.OnOffRequest;
import com.quootta.mdate.engine.invite.ChatUserInfoRequest;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.ui.popupWindow.GiftPopuWindow;
import com.quootta.mdate.utils.GsonUtil;
import com.quootta.mdate.utils.LogUtil;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.quootta.mdate.utils.UIUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Conversation;

public class ChatActivity extends FragmentActivity {

    @Bind(R.id.iv_back_title_bar) ImageView iv_back;
    @Bind(R.id.tv_title_bar) TextView iv_title;
    @Bind(R.id.pop_gift) ImageView pop_gift;
    private ChatUserInfo chatUserInfo;
    private RequestQueue requestQueue;
    private OnOffData onOffData;
    /**
     * 目标 Id
     */
    private String mTargetId;

    /**
     * 刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId
     */
    private String mTargetIds;
    private String version;
    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ButterKnife.bind(this);
        requestQueue = BaseApp.getRequestQueue();
        Intent intent = getIntent();
        getIntentDate(intent);
        initTitle();
        setListenerToRootView();


        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(),0);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version = packInfo.versionName;



    }

    private void initTitle() {
        Map<String, String> paramsMap = new HashMap<>();
        LogUtil.d("MainActivity", "user_ids:" + mTargetId);
        paramsMap.put("user_ids", "[" + mTargetId + "]");
        final ChatUserInfoRequest chatUserInfoRequest = new ChatUserInfoRequest(paramsMap, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                LogUtil.d("MainActivity", "response:" + response);
                try {

                    chatUserInfo = GsonUtil.parse(response.getString("data"), ChatUserInfo.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (chatUserInfo.users!=null){
                    iv_title.setText(chatUserInfo.users.get(0).nick_name);
                }
            }
        });
        requestQueue.add(chatUserInfoRequest);



        Map<String,String> OnOffMap=new HashMap<>();
        OnOffMap.put("android_version",version);

        //礼物开关
            final OnOffRequest onOffRequest=new OnOffRequest(OnOffMap,new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    LogUtil.i("chatActivity","全局开关--》"+response);
                    onOffData=GsonUtil.parse(response,OnOffData.class);
                    if (onOffData.getData().getConfig()!=null){

                        if (onOffData.getData().getConfig().getGift().equals("on")){
                            //显示礼物按钮
                            pop_gift.setVisibility(View.VISIBLE);
                        }else {
                            pop_gift.setVisibility(View.INVISIBLE);
                        }
                    }

                }
        });
        requestQueue.add(onOffRequest);


        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pop_gift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChatActivity.this, GiftPopuWindow.class);
                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("uid",mTargetId);
                ChatActivity.this.startActivity(intent);
            }
        });
    }

    /**
     * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
     */
    private void getIntentDate(Intent intent) {

        mTargetId = intent.getData().getQueryParameter("targetId");
        mTargetIds = intent.getData().getQueryParameter("targetIds");
        //intent.getData().getLastPathSegment();//获得当前会话类型
        mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));

        enterFragment(mConversationType, mTargetId);
    }

    /**
     * 加载会话页面 ConversationFragment
     *
     * @param mConversationType 会话类型
     * @param mTargetId 目标 Id
     */
    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {

        ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", mTargetId).build();

        fragment.setUri(uri);
    }


    private void setListenerToRootView() {
        final View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                boolean mKeyboardUp = isKeyboardShown(rootView);
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) pop_gift.getLayoutParams();
                if (mKeyboardUp) {
                    // LogUtils.d(TAG, "键盘弹出..");
                    lp.bottomMargin = UIUtil.dip2px(200);
                } else {
                    lp.bottomMargin = UIUtil.dip2px(60);
                    //LogUtils.d(TAG, "键盘收起..");
                }
                pop_gift.setLayoutParams(lp);
            }
        });
    }

    private boolean isKeyboardShown(View rootView) {
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }
}
