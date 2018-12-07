package com.quootta.mdate.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.domain.MyInviteList;
import com.quootta.mdate.myInterface.OnItemClickListener;
import com.quootta.mdate.ui.view.CircleImageView;
import com.quootta.mdate.utils.TimeUtil;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.rong.imkit.RongIM;

/**
 * Created by Ryon on 2016/2/22.
 */
public class RvMyInviteListAdapter extends RecyclerView.Adapter<MyInviteListHolder> {

    private Context context;
    private ImageLoader imageLoader;
    private List<MyInviteList.dates> dates;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;

    public RvMyInviteListAdapter(Context context, List<MyInviteList.dates> dates) {
        this.context = context;
        this.dates = dates;
        inflater = LayoutInflater.from(context);
        imageLoader = BaseApp.getImageLoader();
    }

    @Override
    public MyInviteListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        
        View view = inflater.inflate(R.layout.item_my_invite, parent, false);
        MyInviteListHolder myInviteListHolder = new MyInviteListHolder(view);
        
        return myInviteListHolder;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public void onBindViewHolder(final MyInviteListHolder holder, final int position) {
        holder.itemView.setTag(position);
        imageLoader.get(
                LocalUrl.getPicUrl(dates.get(position).to.avatar),
                imageLoader.getImageListener(holder.iv_head, R.mipmap.test, R.mipmap.test));
        holder.tv_name.setText(dates.get(position).to.nick_name);
        holder.tv_time.setText(TimeUtil.stamp2Date(dates.get(position).create_time));
        switch (dates.get(position).status) {
            case "undo"://未处理
                holder.tv_status.setText(context.getString(R.string.status_doing));
                holder.tv_status.setTextColor(context.getResources().getColor(R.color.myPink));
                holder.btn_enter_chat.setClickable(false);
                holder.btn_enter_chat.setBackgroundResource(R.drawable.shape_button_unclickable);
                break;
            case "accept"://已接受
                holder.tv_status.setText(context.getString(R.string.status_accept));
                holder.tv_status.setTextColor(context.getResources().getColor(R.color.themePink));
                holder.btn_enter_chat.setBackgroundResource(R.drawable.shape_button);
                holder.btn_enter_chat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (RongIM.getInstance() != null)
                            RongIM.getInstance().startPrivateChat(context, dates.get(position).to._id, dates.get(position).to.nick_name);
                    }
                });
                break;
            case "reject"://已拒绝
                holder.tv_status.setText(context.getString(R.string.status_reject));
                holder.tv_status.setTextColor(context.getResources().getColor(R.color.myGray));
                holder.btn_enter_chat.setClickable(false);
                holder.btn_enter_chat.setBackgroundResource(R.drawable.shape_button_unclickable);
                break;
            case "cancel"://已撤回
                holder.tv_status.setText(context.getString(R.string.status_cancel));
                holder.tv_status.setTextColor(context.getResources().getColor(R.color.myGray));
                holder.btn_enter_chat.setClickable(false);
                holder.btn_enter_chat.setBackgroundResource(R.drawable.shape_button_unclickable);
                break;
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(holder.itemView, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }
}

class MyInviteListHolder extends RecyclerView.ViewHolder{

    @Bind(R.id.iv_head_my_invite_item)CircleImageView iv_head;
    @Bind(R.id.tv_name_item_my_invite)TextView tv_name;
    @Bind(R.id.tv_time_item_my_invite)TextView tv_time;
    @Bind(R.id.tv_status_item_my_invite)TextView tv_status;
    @Bind(R.id.btn_enter_chat_item_my_invite)Button btn_enter_chat;

    public MyInviteListHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

    }
}
