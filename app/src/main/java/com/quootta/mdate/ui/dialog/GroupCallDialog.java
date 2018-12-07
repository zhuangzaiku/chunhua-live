package com.quootta.mdate.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.domain.GroupTextData;
import com.quootta.mdate.domain.InfoDetail;
import com.quootta.mdate.engine.media.GroupCallTextRequest;
import com.quootta.mdate.utils.GsonUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by Ryon on 2016/7/26/0026.
 */
public class GroupCallDialog extends Dialog {

    @Bind(R.id.boy_btn)
    Button btnBoy;
    @Bind(R.id.girl_btn)
    Button btnGirl;
    @Bind(R.id.img_btn)
    ImageButton btnImg;
    @Bind(R.id.cash_btn)
    Button btnCash;
    @Bind(R.id.free_btn)
    Button btnFree;
    @Bind(R.id.call_free_btn)
    Button callFreeBtn;
    @Bind(R.id.firstText)
    TextView firstText;
    @Bind(R.id.call_gorup_text)
    TextView gourp_text;

    private Context baseContext;
    private OnGroupClickListener groupClickListener;
    private onFirstClickListener onFirstClickListener;
    private onCashBtnListener onCashBtnListener;

    private String gender = null;
    private RequestQueue requestQueue;
    private GroupTextData groupTextData;

    public GroupCallDialog(Context context, OnGroupClickListener groupClickListener) {
        this(context, R.layout.dialog_group_call, R.style.MyDialogStyle,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.groupClickListener = groupClickListener;
        this.baseContext = context;


        btnFree.setVisibility(View.VISIBLE);
        if (Boolean.parseBoolean(InfoDetail.is_vip)) {
            btnCash.setVisibility(View.GONE);
        }else {
            btnCash.setVisibility(View.VISIBLE);
        }
        callFreeBtn.setVisibility(View.GONE);
        firstText.setVisibility(View.GONE);

    }

    public GroupCallDialog(Context context,onFirstClickListener onFirstClickListener,onCashBtnListener onCashBtnListener){
        this(context, R.layout.dialog_group_call, R.style.MyDialogStyle,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        this.baseContext=context;

        btnFree.setVisibility(View.GONE);
        btnCash.setVisibility(View.GONE);

        callFreeBtn.setVisibility(View.VISIBLE);
        firstText.setVisibility(View.VISIBLE);
        this.onFirstClickListener=onFirstClickListener;
        this.onCashBtnListener=onCashBtnListener;
    }


    public GroupCallDialog(final Context context, int layout, int style, int width, int height) {
        super(context, style);
        setContentView(layout);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        // 设置属性值
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = width;
        lp.height = height;
        getWindow().setAttributes(lp);

        requestQueue= BaseApp.getRequestQueue();

        btnFree.setEnabled(true);
        btnCash.setEnabled(true);
        callFreeBtn.setEnabled(true);

        onGetGroupText();

        setListener();
    }


    @Override
    public void show() {
        super.show();
    }

    private void  onGetGroupText(){
        GroupCallTextRequest groupCallTextRequest=new GroupCallTextRequest(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("tag","response--->"+response);
               groupTextData= GsonUtil.parse(response,GroupTextData.class);

                Log.i("tag","用户详情的性别---》"+InfoDetail.gender);

                if (InfoDetail.gender.equals("female")){
                    gourp_text.setText(groupTextData.getData().getText().getMale());

                    btnBoy.setBackgroundColor(Color.parseColor("#6abffb"));
                    btnBoy.setTextColor(Color.parseColor("#ffffff"));
                    btnGirl.setTextColor(Color.parseColor("#444444"));
                    btnGirl.setBackgroundResource(R.drawable.bg_borde_rounded);
                    gender = "male";
                }else {
                    gourp_text.setText(groupTextData.getData().getText().getFemale());

                    btnGirl.setBackgroundColor(Color.parseColor("#ff8aff"));
                    btnGirl.setTextColor(Color.parseColor("#ffffff"));
                    btnBoy.setTextColor(Color.parseColor("#444444"));
                    btnBoy.setBackgroundResource(R.drawable.bg_borde_rounded);
                    gender = "female";
                }

            }
        });
        requestQueue.add(groupCallTextRequest);
    }


    private void setListener() {

        btnImg.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onCashBtnListener != null){
                            onCashBtnListener.onfinish();
                        }
                        dismiss();
                    }
                }
        );

        btnBoy.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (groupTextData != null) {
                            gourp_text.setText(groupTextData.getData().getText().getMale());
                            btnBoy.setBackgroundColor(Color.parseColor("#6abffb"));
                            btnBoy.setTextColor(Color.parseColor("#ffffff"));
                            btnGirl.setTextColor(Color.parseColor("#444444"));
                            btnGirl.setBackgroundResource(R.drawable.bg_borde_rounded);
                            gender = "male";
                        }
                    }
                }
        );

        btnGirl.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (groupTextData != null) {
                            gourp_text.setText(groupTextData.getData().getText().getFemale());
                            btnGirl.setBackgroundColor(Color.parseColor("#ff8aff"));
                            btnGirl.setTextColor(Color.parseColor("#ffffff"));
                            btnBoy.setTextColor(Color.parseColor("#444444"));
                            btnBoy.setBackgroundResource(R.drawable.bg_borde_rounded);
                            gender = "female";
                        }
                    }
                }
        );

        //点击充值免费群呼
        callFreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gender==null){
                    ToastUtil.showToast(baseContext.getString(R.string.app_tips_text100));
                }else {
                    callFreeBtn.setEnabled(false);
                    onFirstClickListener.onFirstClick(gender);
                }
            }
        });


        btnCash.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (gender == null) {
                            ToastUtil.showToast(baseContext.getString(R.string.app_tips_text100));
                        } else {
                            btnCash.setEnabled(false);
                            groupClickListener.onGoldClick(gender);
                            dismiss();
                        }
                    }
                }
        );

        btnFree.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(gender==null) {
                            ToastUtil.showToast(baseContext.getString(R.string.app_tips_text100));
                        } else {
                            btnFree.setEnabled(false);
                            groupClickListener.onVipBtnClick(gender);
                            dismiss();
                        }
                    }
                }
        );


    }


    public interface OnGroupClickListener {
        void onGoldClick(String gender);
        void onVipBtnClick(String gender);
    }

    public interface onFirstClickListener{
        void onFirstClick(String gender);
    }

    public interface onCashBtnListener{
        void onfinish();
    }

}