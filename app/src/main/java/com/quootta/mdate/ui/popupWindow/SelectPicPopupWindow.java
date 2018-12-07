package com.quootta.mdate.ui.popupWindow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.quootta.mdate.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kky on 2016/3/28.
 */
public class SelectPicPopupWindow extends PopupWindow {

    @Bind(R.id.takePhotoBtn) Button takePhotoBtn;
    @Bind(R.id.pickPhotoBtn) Button pickPhotoBtn;
    @Bind(R.id.pickVideoBtn) Button pickVideoBtn;
    @Bind(R.id.cancelBtn) Button cancelBtn;
    @Bind(R.id.lookBtn) Button lookBtn;
    @Bind(R.id.deleteBtn) Button deleteBtn;
    @Bind(R.id.avatar_update_btn)Button avatar_update;
    private View mMenuView;

    @SuppressLint("InflateParams")
    public SelectPicPopupWindow(Context context, View.OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.dialog_select_pic, null);
        ButterKnife.bind(this, mMenuView);
        // 设置按钮监听
        cancelBtn.setOnClickListener(itemsOnClick);
        pickPhotoBtn.setOnClickListener(itemsOnClick);
        pickVideoBtn.setOnClickListener(itemsOnClick);
        takePhotoBtn.setOnClickListener(itemsOnClick);
        lookBtn.setOnClickListener(itemsOnClick);
        deleteBtn.setOnClickListener(itemsOnClick);
        avatar_update.setOnClickListener(itemsOnClick);

        // 设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.PopupAnimation);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

    }

    public void setVideoBtnVisible(int visibility){
        pickVideoBtn.setVisibility(visibility);
        lookBtn.setVisibility(visibility);
        deleteBtn.setVisibility(visibility);
        avatar_update.setVisibility(visibility);
    }

    public void setChooseBtnVisible(int visibility){
        takePhotoBtn.setVisibility(visibility);

    }

}