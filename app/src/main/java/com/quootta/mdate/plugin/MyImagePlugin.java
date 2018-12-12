package com.quootta.mdate.plugin;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;

import com.quootta.mdate.R;

import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.plugin.ImagePlugin;
import io.rong.imkit.plugin.image.PictureSelectorActivity;
import io.rong.imkit.utilities.PermissionCheckUtil;
import io.rong.imlib.model.Conversation;


/**
 * Created by Ryon on 2017/1/16/0016.
 */

public class MyImagePlugin extends ImagePlugin {

    private Conversation.ConversationType conversationType;
    private String targetId;
    /**
     * 设置图标
     * @param context
     * @return
     */
    @Override
    public Drawable obtainDrawable(Context context) {
        return context.getResources().getDrawable(R.drawable.rc_ext_plugin_image_selector);
    }

    /**
     * 设置图标下的文字
     * @param context
     * @return
     */
    @Override
    public String obtainTitle(Context context) {
        return "";
    }


}
