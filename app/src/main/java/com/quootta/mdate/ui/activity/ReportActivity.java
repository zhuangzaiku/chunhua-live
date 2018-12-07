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
import com.quootta.mdate.engine.invite.ReportRequest;
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

public class ReportActivity extends BaseActivity {

    @Bind(R.id.iv_back_title_bar)ImageView iv_back;
    @Bind(R.id.tv_title_bar)TextView tv_title;
    @Bind(R.id.et_feed_back)EditText etReport;
    @Bind(R.id.btn_feed_back)Button btn_report;

    private String userId;
    private RequestQueue requestQueue;
    private Map<String,String> paramsMap;

    @Override
    protected void init() {
        requestQueue = BaseApp.getRequestQueue();
        paramsMap = new HashMap<>();
        userId = getIntent().getStringExtra("user_id");
        paramsMap.put("user_id",userId);
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_feed_back;
    }

    @Override
    protected void initData() {
        initTitle();
        etReport.setHint(getString(R.string.app_tips_text66));
    }

    private void initTitle() {
        iv_back.setVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.report));
    }

    @Override
    protected void setListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.finishActivty();
            }
        });
        btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etReport.getText().toString().trim().isEmpty()) {
                    paramsMap.put("content",etReport.getText().toString().trim());
                    requestReport();
                } else {
                    ToastUtil.showToast(getString(R.string.app_tips_text29));
                }
            }
        });
    }

    private void requestReport() {
        final MyProgressDialog myProgressDialog = new MyProgressDialog(baseContext);
        myProgressDialog.show();
        ReportRequest reportRequest = new ReportRequest(paramsMap, new VolleyListener() {
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
        requestQueue.add(reportRequest);
    }
}
