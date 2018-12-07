package com.quootta.mdate.ui.fragment;

import android.net.Uri;
import android.view.View;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseFragment;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * Created by Ryon on 2016/2/18.
 */
public class ChatsListFragment extends BaseFragment {

    @Override
    protected int getRootView() {
        return R.layout.fragment_chats_list;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initData(View view) {
        initChatList();
    }

    private void initChatList() {
        ConversationListFragment fragment = (ConversationListFragment) getChildFragmentManager().findFragmentById(R.id.fm_chat_list_fragment);
        Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//讨论组
                .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//公共服务号
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//系统
                .build();

        fragment.setUri(uri);
    }


    @Override
    protected void setListener() {

    }
}
