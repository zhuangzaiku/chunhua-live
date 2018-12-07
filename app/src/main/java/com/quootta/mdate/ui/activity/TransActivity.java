package com.quootta.mdate.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.ui.dialog.MyAlertDialog;
import com.quootta.mdate.utils.ActivityUtil;
import com.quootta.mdate.utils.ToastUtil;

public class TransActivity extends BaseActivity {

    private String usage;
    private String type;
    private String price;
    private MyAlertDialog myAlertDialog;
    private static final int RESU_OK=11;
    @Override
    protected void init() {
        usage = getIntent().getStringExtra("usage");
        type = getIntent().getStringExtra("type");
        price = getIntent().getStringExtra("price");

    }

    @Override
    protected int getRootView() {
        return R.layout.activity_trans;
    }

    @Override
    protected void initData() {



        if (usage != null && usage.equals("charge")) {
            buildChargeDialog();
        } else {
            buildMediaDialog();
        }
        myAlertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
//                Intent intent = new Intent("com.quootta.mdate.ChargeActivity");
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
                finish();
            }
        });
        myAlertDialog.setTitle(R.string.app_tips_text44);
        myAlertDialog.show();
    }

    private void buildChargeDialog() {
        myAlertDialog = new MyAlertDialog(baseContext, new MyAlertDialog.OnPositiveAlterListener() {
            @Override
            public void onAlter() {
                Intent intent = new Intent("com.quootta.mdate.ChargeActivity");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        myAlertDialog.setMessage(getString(R.string.app_tips_text81));
    }

    private void buildMediaDialog() {
        myAlertDialog = new MyAlertDialog(baseContext, new MyAlertDialog.OnPositiveAlterListener() {
            @Override
            public void onAlter() {
                if (BaseApp.getGoldCount() >=Integer.parseInt(price)) {
                    setResult(RESU_OK);
                    finish();
                } else {
                    ToastUtil.showLongToast(getString(R.string.app_tips_text82));
                    Intent intent = new Intent("com.quootta.mdate.ChargeActivity");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });

        myAlertDialog.setOnNegativeAlterListener(new MyAlertDialog.OnNegativeAlterListener() {
            @Override
            public void onAlter() {
                ActivityUtil.finishActivty();
            }
        });

//        myAlertDialog.setMessage("该用户的" + type + "接听价格是"
//                + price + getString(R.string.gold_per_min)
//                + "，确定要进行" + type + "聊天吗？");
        myAlertDialog.setMessage(getString(R.string.app_tips_text83,type,price + getString(R.string.gold_per_min),type));
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myAlertDialog.dismiss();
    }
}
