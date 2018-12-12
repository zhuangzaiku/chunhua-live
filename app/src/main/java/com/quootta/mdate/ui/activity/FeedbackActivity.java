package com.quootta.mdate.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.engine.setting.FeedbackRequest;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.ui.dialog.MyProgressDialog;
import com.quootta.mdate.utils.ActivityUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.android.volley.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import io.rong.imkit.RongIM;

public class FeedbackActivity extends BaseActivity {

    @Bind(R.id.iv_back_title_bar)ImageView iv_back;
    @Bind(R.id.tv_title_bar)TextView tv_title;
    @Bind(R.id.et_feed_back)EditText et_feedback;
    @Bind(R.id.btn_feed_back)Button btn_feedback;
    @Bind(R.id.bt_sousuo) Button sou;
    @Bind(R.id.ed_sousuo) EditText ed_sou;
    @Bind(R.id.tv_right_title_bar) TextView user_service;
    private RequestQueue requestQueue;
    private Map<String,String> paramsMap;

    @Override
    protected void init() {
        requestQueue = BaseApp.getRequestQueue();
        paramsMap = new HashMap<>();


    }

    @Override
    protected int getRootView() {
        return R.layout.activity_feed_back;
    }

    @Override
    protected void initData() {
        initTitle();
//        if (RongIM.getInstance() != null ){
//            RongIM.getInstance().startPrivateChat(baseContext,
//                    "1965","");
//        }
    }

    private void initTitle() {
        iv_back.setVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.feedback));
        user_service.setVisibility(View.VISIBLE);
        user_service.setText(getString(R.string.app_tips_text27));

    }

    @Override
    protected void setListener() {

        user_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RongIM.getInstance() != null ){
                    RongIM.getInstance().startPrivateChat(baseContext,
                    "7","7");
        }
            }
        });

        sou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (RongIM.getInstance() != null && ed_sou.getText() != null){
                    RongIM.getInstance().startPrivateChat(baseContext,
                            ed_sou.getText().toString(),ed_sou.getText().toString());
                }


            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.finishActivty();
            }
        });
        btn_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_feedback.getText().toString().equals(getString(R.string.app_tips_text28))){
                    ed_sou.setVisibility(View.VISIBLE);
                    sou.setVisibility(View.VISIBLE);
                }else {

                    if (et_feedback.getText().toString().trim()!=null) {
                        paramsMap.put("content",et_feedback.getText().toString().trim());
                        requestFeedback();
                    } else {
                        ToastUtil.showToast(getString(R.string.app_tips_text29));
                    }
                }

            }
        });
    }

    private void requestFeedback() {
        final MyProgressDialog myProgressDialog = new MyProgressDialog(baseContext);
        myProgressDialog.show();
        FeedbackRequest feedbackRequest = new FeedbackRequest(paramsMap, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                try {
                    myProgressDialog.dismiss();
                    Toast.makeText(baseContext,response.getString("msg"), Toast.LENGTH_SHORT).show();
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        requestQueue.add(feedbackRequest);
    }
}
