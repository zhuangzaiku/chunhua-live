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
import com.quootta.mdate.domain.InviteMeList;
import com.quootta.mdate.myInterface.OnItemClickListener;
import com.quootta.mdate.ui.view.CircleImageView;
import com.quootta.mdate.utils.TimeUtil;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ryon on 2016/2/19.
 */
public class RvInviteMeListAdapter extends RecyclerView.Adapter<InviteMeListHolder> {

    private Context context;
    private List<InviteMeList.dates> dates;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;
    private OnAcceptListener onAcceptListener;
    private ImageLoader imageLoader;

    public RvInviteMeListAdapter(Context context, List<InviteMeList.dates> dates) {
        this.context = context;
        this.dates = dates;
        inflater = LayoutInflater.from(context);
        imageLoader = BaseApp.getImageLoader();
    }

    public void setOnAcceptListener(OnAcceptListener onAcceptListener) {
        this.onAcceptListener = onAcceptListener;
    }

    public interface  OnAcceptListener{
        void onAccept(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }



    @Override
    public InviteMeListHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_invite_me, parent, false);
        InviteMeListHolder inviteMeListHolder = new InviteMeListHolder(view);

        return inviteMeListHolder;
    }

    @Override
    public void onBindViewHolder(final InviteMeListHolder holder, final int position) {
        imageLoader.get(
                LocalUrl.getPicUrl(dates.get(position).from.avatar),
                imageLoader.getImageListener(holder.iv_head_invite_me_item, R.mipmap.test, R.mipmap.test));
        holder.tv_name_item_invite_me.setText(dates.get(position).from.nick_name);
        holder.tv_time_item_invite_me.setText(TimeUtil.stamp2Date(dates.get(position).create_time));
        switch (dates.get(position).status){
            case "undo":
                holder.tv_status_item_invite_me.setVisibility(View.GONE);
                holder.btn_accept_item_invite_me.setVisibility(View.VISIBLE);
                holder.v_status.setBackgroundResource(R.drawable.shape_pink_view);


                holder.btn_accept_item_invite_me.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onAcceptListener.onAccept(position);
                    }
                });
                break;
            case "accept":
                holder.tv_status_item_invite_me.setVisibility(View.VISIBLE);
                holder.tv_status_item_invite_me.setTextColor(context.getResources().getColor(R.color.themePink));
                holder.btn_accept_item_invite_me.setVisibility(View.GONE);
                holder.v_status.setBackgroundResource(R.drawable.shape_yellow_view);

                holder.tv_status_item_invite_me.setText(R.string.invite_accept);
                break;
            case "reject":
                holder.tv_status_item_invite_me.setVisibility(View.VISIBLE);
                holder.tv_status_item_invite_me.setTextColor(context.getResources().getColor(R.color.myGray));
                holder.btn_accept_item_invite_me.setVisibility(View.GONE);
                holder.v_status.setBackgroundResource(R.drawable.shape_gray_view);

                holder.tv_status_item_invite_me.setText(R.string.invite_reject);
                break;
            case "cancel":
                holder.tv_status_item_invite_me.setVisibility(View.VISIBLE);
                holder.tv_status_item_invite_me.setTextColor(context.getResources().getColor(R.color.myGray));
                holder.btn_accept_item_invite_me.setVisibility(View.GONE);
                holder.v_status.setBackgroundResource(R.drawable.shape_gray_view);

                holder.tv_status_item_invite_me.setText(R.string.invite_cancel);

                break;
            default:
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

class InviteMeListHolder extends RecyclerView.ViewHolder{

    @Bind(R.id.v_status_invite_me_item)View v_status;
    @Bind(R.id.iv_head_invite_me_item)CircleImageView iv_head_invite_me_item;
    @Bind(R.id.tv_name_item_invite_me)TextView tv_name_item_invite_me;
    @Bind(R.id.tv_time_item_invite_me)TextView tv_time_item_invite_me;
    @Bind(R.id.btn_accept_item_invite_me)Button btn_accept_item_invite_me;
    @Bind(R.id.tv_status_item_invite_me)TextView tv_status_item_invite_me;

    public InviteMeListHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
