package com.quootta.mdate.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.domain.InfoDetail;
import com.quootta.mdate.engine.myCenter.ToVipRequest;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.ui.dialog.MyAlertDialog;
import com.quootta.mdate.ui.dialog.MyProgressDialog;
import com.quootta.mdate.ui.view.CircleImageView;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.TimeUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

public class VipActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.iv_back_title_bar)
    ImageView ivBack;
    @Bind(R.id.tv_title_bar)
    TextView tvTitle;
    @Bind(R.id.tv_gold)
    TextView tvGold;
    @Bind(R.id.btn_30)
    Button btn30;
    @Bind(R.id.btn_90)
    Button btn90;
    @Bind(R.id.btn_180)
    Button btn180;
    @Bind(R.id.btn_360)
    Button btn360;
    @Bind(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_label_end_time)
    TextView tvLabelEndTime;
    @Bind(R.id.tv_end_time)
    TextView tvEndTime;
    @Bind(R.id.rl_vip_area)
    RelativeLayout rlVipArea;

    private RequestQueue requestQueue;
    private Bitmap avatar;
    private int timePeriod;
    private int goldNum;

    private final int RESULT_SUCCESS = 4;

    @Override
    protected void init() {
        requestQueue = BaseApp.getRequestQueue();
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_vip;
    }

    @Override
    protected void initData() {
        initTitle();
        initContent();
        if (Boolean.parseBoolean(InfoDetail.is_vip)) {
            initVipArea();
        }
    }

    private void initContent() {
        tvGold.setText(BaseApp.getGoldCount() + "");
    }

    private void initVipArea() {
        rlVipArea.setVisibility(View.VISIBLE);
        tvName.setText(InfoDetail.nick_name);
        tvEndTime.setText(TimeUtil.stamp2Date(InfoDetail.vip_valid_time));

//        byte[] bis =  getIntent().getByteArrayExtra("avatar");
//        Bitmap bitmap = BitmapFactory.decodeByteArray(bis, 0, bis.length);
//        avatar = bitmap.copy(Bitmap.Config.ARGB_8888, true);
//        ivAvatar.setImageBitmap(avatar);
        Glide.with(baseContext).load(LocalUrl.getPicUrl(InfoDetail.avatar)).into(ivAvatar);
    }


    private void initTitle() {
        tvTitle.setText(getString(R.string.is_member));
        ivBack.setVisibility(View.VISIBLE);
    }

    @Override
    protected void setListener() {
        ivBack.setOnClickListener(this);
        btn30.setOnClickListener(this);
        btn90.setOnClickListener(this);
        btn180.setOnClickListener(this);
        btn360.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_title_bar:
                finish();
                break;
            case R.id.btn_30:
                LogUtil.d("VipActivity", "into switch");
                timePeriod = 30;
                goldNum = 9800;
                showChargeDialog();
                break;
            case R.id.btn_90:
                LogUtil.d("VipActivity", "into switch");
                timePeriod = 90;
                goldNum = 36800;
                showChargeDialog();
                break;
            case R.id.btn_180:
                LogUtil.d("VipActivity", "into switch");
                timePeriod = 180;
                goldNum = 56800;
                showChargeDialog();
                break;
            case R.id.btn_360:
                LogUtil.d("VipActivity", "into switch");
                timePeriod = 360;
                goldNum = 76800;
                showChargeDialog();
                break;
            default:
                break;
        }
    }

    private void showChargeDialog() {
        MyAlertDialog myAlertDialog = new MyAlertDialog(baseContext, new MyAlertDialog.OnPositiveAlterListener() {
            @Override
            public void onAlter() {
                requestToVip();
            }
        });
        myAlertDialog.setTitle(getString(R.string.app_tips_text44));
        myAlertDialog.setMessage(getString(R.string.app_tips_text89));
        myAlertDialog.show();

    }

    //充值会员
    private void requestToVip() {
        if (BaseApp.getGoldCount()>=goldNum){
            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("days", timePeriod + "");
            paramsMap.put("num", goldNum + "");

            final MyProgressDialog progressDialog = new MyProgressDialog(baseContext);
            progressDialog.show();
            ToVipRequest toVipRequest = new ToVipRequest(paramsMap, new VolleyListener() {
                @Override
                protected void onSuccess(JSONObject response) {
                    try {
                        ToastUtil.showLongToast(response.getString("msg"));
                        progressDialog.dismiss();
                        setResult(RESULT_SUCCESS);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            requestQueue.add(toVipRequest);
        }else {
            ToastUtil.showToast(getString(R.string.app_tips_text90));
            Intent intent=new Intent(VipActivity.this,ChargeActivity.class);
            startActivity(intent);
        }

    }


}