package com.quootta.mdate.ui.fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.base.BaseFragment;
import com.quootta.mdate.engine.account.SignPasswordRequest;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.ui.activity.SignActivity;
import com.quootta.mdate.utils.ActivityUtil;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.SecretUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.android.volley.RequestQueue;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by Ryon on 2016/7/18/0018.
 */
public class SingUpSecondFragment extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.iv_back_title_bar)ImageView iv_back;
    @Bind(R.id.tv_title_bar)TextView tv_title;
    @Bind(R.id.et_password_sign_up_activity)EditText et_password;
    @Bind(R.id.et_confirm_password_sign_up_activity)EditText et_confirm_password;
    @Bind(R.id.btn_next_step_second_sign_up_activity)Button btn_second_next_step;

    private RequestQueue requestQueue;
    private String number;

    private Context baseContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        number = getArguments().getString("number");
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_sign_up_second;
    }

    @Override
    protected void setListener() {
        iv_back.setOnClickListener(this);
        btn_second_next_step.setOnClickListener(this);
    }

    @Override
    protected void init() {
        requestQueue = BaseApp.getRequestQueue();
//        number = getActivity().getIntent().getStringExtra("number");


    }

    @Override
    protected void initData(View view) {
        baseContext=view.getContext();
        initTitle();
    }

    private void initTitle() {
        iv_back.setVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.signin));
    }

    private void requestPassword() {
        Map<String, String> paramsMap = new HashMap<>();
        final String password = et_password.getText().toString().trim();
        String passwordConfirm = et_confirm_password.getText().toString().trim();
        if(password.equals("")){
            ToastUtil.showToast(getString(R.string.app_tips_text114));
        }
        if(passwordConfirm.equals("")){
            ToastUtil.showToast(getString(R.string.app_tips_text114));
        }
        if (password.equals(passwordConfirm) && !password.isEmpty() && !passwordConfirm.isEmpty() ) {
            paramsMap.put("password", SecretUtil.bytesToMD5(password));
            SignPasswordRequest signPasswordRequest = new SignPasswordRequest(paramsMap,
                    new VolleyListener() {
                        @Override
                        protected void onSuccess(JSONObject response) {
                            LogUtil.d("SignUpSecondActivity", "Response:" + response);
                            try {
                                ToastUtil.showToast(response.getString("msg").toString());
//                                Intent secondIntent = new Intent(baseContext, SignUpLastActivity.class);
//                                secondIntent.putExtra("number", number);
//                                secondIntent.putExtra("pwd", SecretUtil.bytesToMD5(password));
//                                startActivity(secondIntent);
                                SignUpLastFragment lastFragment=new SignUpLastFragment();
                                Bundle bundle=new Bundle();
                                bundle.putString("number", number);
                                bundle.putString("pwd", SecretUtil.bytesToMD5(password));
                                lastFragment.setArguments(bundle);

                                ((SignActivity)getActivity()).changeFragment(lastFragment);
//                                ActivityUtil.finishActivty();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            requestQueue.add(signPasswordRequest);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back_title_bar:
                ActivityUtil.finishActivty();
                break;
            case R.id.btn_next_step_second_sign_up_activity:
                requestPassword();
                break;
        }
    }

    public static SingUpSecondFragment newInstance() {
        
        Bundle args = new Bundle();
        
        SingUpSecondFragment fragment = new SingUpSecondFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
