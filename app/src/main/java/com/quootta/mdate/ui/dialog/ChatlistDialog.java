package com.quootta.mdate.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.quootta.mdate.R;
import com.quootta.mdate.myInterface.OnConfirmListener;

/**
 * Created by Ryon on 2016/2/19.
 */
public class ChatlistDialog extends Dialog {

    private Context context;
    private Button btn_cancel_chat_list_dialog, btn_confirm_chat_list_dialog;
    private OnConfirmListener onConfirmListener;

    public ChatlistDialog(Context context, OnConfirmListener listener) {
        super(context, R.style.MyBaseDialog);
        this.context = context;
        this.onConfirmListener = listener;
        initView();
    }

    private void initView() {
        View view = View.inflate(context, R.layout.dialog_chat_list, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        setContentView(view, params);

//        Window window = getWindow();
//        WindowManager.LayoutParams params = window.getAttributes();
//        window.setContentView(R.layout.dialog_chat_list);
//        params.gravity = Gravity.CENTER;
//        window.setAttributes(params);
        btn_cancel_chat_list_dialog = (Button) view.findViewById(R.id.btn_cancel_chat_list_dialog);
        btn_confirm_chat_list_dialog = (Button) view.findViewById(R.id.btn_confirm_chat_list_dialog);
        setCanceledOnTouchOutside(true);

        setListener();
    }

    private void setListener() {
        btn_cancel_chat_list_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btn_confirm_chat_list_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConfirmListener.onConfirm();
                dismiss();
            }
        });
    }


}
