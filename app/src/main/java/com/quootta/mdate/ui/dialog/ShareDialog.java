package com.quootta.mdate.ui.dialog;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.domain.InfoDetail;
import com.quootta.mdate.utils.ToastUtil;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by Ryon on 2016/7/22/0022.
 */
public class ShareDialog extends Dialog{
    private onClickback callback;
    TextView Invitation_code;
    EditText Invitation_link;

    public ShareDialog(Context context, onClickback callback) {
       this(context, R.layout.dialog_share,R.style.MyDialogStyle,
               ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.callback = callback;

        Invitation_code= (TextView) findViewById(R.id.Invitation_code);
        Invitation_code.setText(InfoDetail.invite_code);

        Invitation_link = (EditText) findViewById(R.id.Invitation_link);

//        this.setOnDismissListener(new OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                dismiss();
//            }
//        });
    }

    public void setShare_link(String share_link) {
        Invitation_link.setText(share_link);
//        Invitation_link.setEnabled(false);
//        Invitation_link.selectAll();
        try {
            ClipboardManager clipboard = (ClipboardManager) getContext().getApplicationContext().getSystemService(CLIPBOARD_SERVICE);
            clipboard.setPrimaryClip(ClipData.newPlainText("分享链接", share_link));
            ToastUtil.showToast("分享链接已经复制到剪切板");
        } catch (Exception e) {
            ToastUtil.showToast("请长按链接进行复制");
        }
//        clipboard.setText(Invitation_link.getText());
    }

    public ShareDialog(final Context context, int layout, int style, int width,
                       int height) {
        super(context, style);
        setContentView(layout);
        setCanceledOnTouchOutside(true);
        // 设置属性值
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = width;
        lp.height = height;
        getWindow().setAttributes(lp);

        setListener();

    }



    private void setListener(){
        //微信好友
        findViewById(R.id.weixin_friend).setOnClickListener(
                new android.view.View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        callback.onShare(1);
                        dismiss();
                    }
                }
        );
        findViewById(R.id.weixin_group).setOnClickListener(
                new android.view.View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        callback.onShare(2);
                        dismiss();
                    }
                }
        );
        findViewById(R.id.qq_friend).setOnClickListener(
                new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onShare(3);
                dismiss();
            }
        });
        findViewById(R.id.qq_group).setOnClickListener(
                new android.view.View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callback.onShare(4);
                        dismiss();
                    }
                });
    }

    @Override
    public void show() {
        super.show();
        //设置dialog显示动画
        getWindow().setWindowAnimations(R.style.dialogWindowAnim);
        //设置显示位置为底部、
        getWindow().setGravity(Gravity.BOTTOM);

    }

    public interface onClickback {

        abstract void onShare(int id);
    }
}
