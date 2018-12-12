package com.quootta.mdate.ui.fragment;

import android.content.Context;

import com.quootta.mdate.ui.adapter.MyConversationListAdapter;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.widget.adapter.ConversationListAdapter;

/**
 * @Project android-live
 * @Package com.quootta.mdate.ui.fragment
 * @Author zhuangzaiku
 * @Date 2018/12/13
 */
public class MyConversationListFragment extends ConversationListFragment {

    @Override
    public ConversationListAdapter onResolveAdapter(Context context) {
        return new MyConversationListAdapter(context);
    }
}
