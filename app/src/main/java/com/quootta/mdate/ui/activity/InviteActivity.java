package com.quootta.mdate.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.ui.dialog.ShareDialog;

import butterknife.Bind;

/**
 * @Project android-live
 * @Package com.quootta.mdate.ui.activity
 * @Author zhuangzaiku
 * @Date 2018/12/11
 */
public class InviteActivity extends BaseActivity {

    @Bind(R.id.btnInvite)
    Button btnShare;
    @Bind(R.id.iv_back_title_bar)
    ImageView ivBack;
    @Bind(R.id.tv_title_bar)
    TextView tv_title_bar;

    String mThumbUrl;
    String mShareUrl;

    @Override
    protected void init() {

    }

    @Override
    protected int getRootView() {
        return R.layout.activity_invite_reward;
    }

    @Override
    protected void initData() {
        tv_title_bar.setText(getString(R.string.invite_reward));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        if(intent != null) {
            mThumbUrl = intent.getStringExtra("thumbUrl");
            mShareUrl = intent.getStringExtra("shareUrl");
        }
    }

    @Override
    protected void setListener() {
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });
    }

    //邀请
    private void share(){
        ShareDialog shareDialog = new ShareDialog(baseContext, new ShareDialog.onClickback() {
            @Override
            public void onShare(int id) {
                String str = mThumbUrl;
            }
        });
        shareDialog.show();
        shareDialog.setShare_link(mShareUrl);


    }
}
