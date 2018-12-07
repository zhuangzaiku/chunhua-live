package com.quootta.mdate.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.utils.ActivityUtil;

import butterknife.Bind;

public class AboutUsActivity extends BaseActivity {
    @Bind(R.id.iv_back_title_bar)ImageView iv_back;
    @Bind(R.id.tv_title_bar)TextView tv_title;

    @Override
    protected void init() {

    }

    @Override
    protected int getRootView() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void initData() {
        initTitle();
    }

    private void initTitle() {
        iv_back.setVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.about_us));
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
}
