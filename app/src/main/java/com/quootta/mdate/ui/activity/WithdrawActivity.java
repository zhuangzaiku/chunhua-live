package com.quootta.mdate.ui.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.base.BaseFragment;
import com.quootta.mdate.domain.AccountSecurityData;
import com.quootta.mdate.engine.security.AccountSecurityRequest;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.ui.adapter.MyPagerAdapter;
import com.quootta.mdate.ui.fragment.AlipayFragment;
import com.quootta.mdate.ui.fragment.BankcardFragment;
import com.quootta.mdate.ui.view.MyViewPager;
import com.quootta.mdate.utils.ActivityUtil;
import com.quootta.mdate.utils.GsonUtil;
import com.android.volley.RequestQueue;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class WithdrawActivity extends BaseActivity {

    @Bind(R.id.iv_back_title_bar)ImageView iv_back;
    @Bind(R.id.tv_title_bar)TextView tv_title;
//    @Bind(R.id.rg_withdraw) RadioGroup rg_withdraw;
//    @Bind(R.id.rb_bank_withdraw) RadioButton rb_bank;
//    @Bind(R.id.rb_alipay_withdraw) RadioButton rb_alipay;
    @Bind(R.id.vp_withdraw) MyViewPager vp_withdraw;

//    @Bind(R.id.rb_alipay_withdraw)
    Button rb_alipay;
    private List<BaseFragment> withdraw_fragments;
    private BankcardFragment bankcardFragment;
    private AlipayFragment alipayFragment;
    private AccountSecurityData securityData;
    private RequestQueue requestQueue;
    @Override
    protected void init() {
        requestQueue= BaseApp.getRequestQueue();

        IsBindPhone();
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_withdraw;
    }

    @Override
    protected void initData() {
        initTitle();
        initViewPager();
    }

    private void IsBindPhone() {
        AccountSecurityRequest securityRequest = new AccountSecurityRequest(new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                securityData = GsonUtil.parse(response, AccountSecurityData.class);

                if (!securityData.getData().isMobile_bind()){


                    AlertDialog.Builder builder = new AlertDialog.Builder(WithdrawActivity.this);
                    builder.setMessage(getString(R.string.app_tips_text91));
                    builder.setCancelable(false);
                    builder.setPositiveButton(getString(R.string.app_tips_text92), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            WithdrawActivity.this.finish();
                        }
                    });

                    builder.create().show();
                }


            }
        });
        requestQueue.add(securityRequest);
    }


    private void initViewPager() {
//        alipayFragment = new AlipayFragment();
        bankcardFragment = new BankcardFragment();

       // vp_withdraw.setNoScroll(false);
        withdraw_fragments = new ArrayList<>();
//        withdraw_fragments.add(alipayFragment);
        withdraw_fragments.add(bankcardFragment);

       // vp_withdraw.setOffscreenPageLimit(2);
        vp_withdraw.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), withdraw_fragments));
    }

    private void initTitle() {
        tv_title.setText(getString(R.string.withdraw));
        iv_back.setVisibility(View.VISIBLE);
    }

    @Override
    protected void setListener() {

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.finishActivty();
            }
        });

//        rg_withdraw.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                int position = -1;
//                switch (checkedId) {
//                    case R.id.rb_alipay_withdraw:
//                        position = 0;
//                        break;
//                    case R.id.rb_bank_withdraw:
//                        position = 1;
//                        break;
//                    default:
//                        break;
//                }
//                vp_withdraw.setCurrentItem(position, false);
//            }
//        });

//        vp_withdraw.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                switch (position) {
//                    case 0:
//                        rb_alipay.setChecked(true);
//                        break;
//                    case 1:
//                        rb_bank.setChecked(true);
//                        break;
//                    default:
//                        break;
//                }
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
    }
}
