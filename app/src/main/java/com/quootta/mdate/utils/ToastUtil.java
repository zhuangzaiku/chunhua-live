package com.quootta.mdate.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.quootta.mdate.R;
import com.quootta.mdate.constant.LocalUrl;

import static com.quootta.mdate.base.BaseApp.imageLoader;


/**
 * Created by kky on 2016/4/11.
 */
public class ToastUtil {

    private static Toast toast = null;

    public static void showToast(String msg) {
       if (toast == null) {
           toast = Toast.makeText(ActivityUtil.getCurrentActivity(), msg, Toast.LENGTH_SHORT);
       } else {
           toast.setText(msg);
       }
        toast.show();
    }

    public static void showLongToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(ActivityUtil.getCurrentActivity(), msg, Toast.LENGTH_LONG);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }
    public static void showGiftToast(Context context, String userName, String giftName, String userImage, String giftImage){
            //加载动画
            LayoutInflater inflater=LayoutInflater.from(context);
            View layout=inflater.inflate(R.layout.toast_gift,null);
            TextView username= (TextView) layout.findViewById(R.id.giftToast_user_name);
            TextView giftname= (TextView) layout.findViewById(R.id.giftToast_gift_name);
            ImageView  userimage= (ImageView) layout.findViewById(R.id.giftToast_user_image);
            ImageView giftimage= (ImageView) layout.findViewById(R.id.giftToast_gift_image);

        //礼物图片
            imageLoader.get(
                    LocalUrl.getPicUrl(giftImage),
                    imageLoader.getImageListener(giftimage, R.mipmap.test, R.mipmap.test));
        //人物头像
            imageLoader.get(
                    LocalUrl.getPicUrl(userImage),
                    imageLoader.getImageListener(userimage, R.mipmap.test, R.mipmap.test));



            username.setText(userName);
            giftname.setText(giftName);

            Toast toast=new Toast(context);
            toast.setDuration(toast.LENGTH_LONG);
            toast.setGravity(Gravity.LEFT,0,0);
            toast.setView(layout);

             toast.show();
    }

}
