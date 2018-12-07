package com.quootta.mdate.plugin;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;

import com.quootta.mdate.R;
import com.quootta.mdate.ui.popupWindow.GiftPopuWindow;

import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;

/**
 * Created by Ryon on 2017/1/20/0020.
 */

public class MyGiftPlugin implements IPluginModule {
    @Override
    public Drawable obtainDrawable(Context context) {
        return context.getResources().getDrawable(R.drawable.gift_selector);
    }

    @Override
    public String obtainTitle(Context context) {
        return context.getString(R.string.gift);
    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {
        Intent intent=new Intent(fragment.getContext(), GiftPopuWindow.class);
        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("uid",rongExtension.getTargetId());
        fragment.getContext().startActivity(intent);
    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }
}
