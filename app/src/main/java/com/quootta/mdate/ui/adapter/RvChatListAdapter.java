package com.quootta.mdate.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.myInterface.OnItemClickListener;
import com.quootta.mdate.ui.view.BadgeView;
import com.quootta.mdate.ui.view.CircleImageView;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ryon on 2016/2/18.
 */
public class RvChatListAdapter extends RecyclerView.Adapter<ChatListViewHolder> {

    private LayoutInflater inflater;
    private Context mContext;
    private List<Map> list;
    private OnItemClickListener itemClickListener;

    public RvChatListAdapter(Context context, List<Map> list) {
        this.mContext = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ChatListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_chat_list, parent, false);
        ChatListViewHolder chatListViewHolder = new ChatListViewHolder(view);
        return chatListViewHolder;
    }

    @Override
    public void onBindViewHolder(final ChatListViewHolder holder, final int position) {
        holder.tv_name_item_chat_list.setText(list.get(position).get("name").toString());
        holder.tv_time_item_chat_list.setText(list.get(position).get("time").toString());
        holder.tv_content_item_chat_list.setText(list.get(position).get("content").toString());
        
        BadgeView badge = new BadgeView(mContext);
        badge.setTargetView(holder.ll_head_item_chat_list);
        badge.setBadgeCount(Integer.parseInt(list.get(position).get("newMsgCount").toString()));

        //设置点击事件
        if(itemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int layoutPosition = holder.getLayoutPosition();
                    itemClickListener.onItemClick(holder.itemView, layoutPosition);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int layoutPosition = holder.getLayoutPosition();
                    itemClickListener.onItemLongClick(holder.itemView, layoutPosition);
                    return true;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class ChatListViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.ll_head_item_chat_list)LinearLayout ll_head_item_chat_list;
    @Bind(R.id.iv_head_item_chat_list)CircleImageView iv_head_item_chat_list;
    @Bind(R.id.tv_name_item_chat_list)TextView tv_name_item_chat_list;
    @Bind(R.id.tv_time_item_chat_list)TextView tv_time_item_chat_list;
    @Bind(R.id.tv_content_item_chat_list)TextView tv_content_item_chat_list;

    public ChatListViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
