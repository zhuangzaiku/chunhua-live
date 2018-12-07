package com.quootta.mdate.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.domain.SystemMessageList;
import com.quootta.mdate.utils.TimeUtil;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ryon on 2016/5/17/0017.
 */
public class RvSystemMessageAdapter extends RecyclerView.Adapter<SystemMessageHolder>{

    private LayoutInflater inflater;
    private Context mContext;
    private ImageLoader imageLoader;
    private List<SystemMessageList.notice> noticeList;
    private OnItemClickListener onItemClickListener;

    public RvSystemMessageAdapter(Context mContext, List<SystemMessageList.notice> noticeList) {
        this.mContext = mContext;
        this.noticeList = noticeList;
        imageLoader = BaseApp.getImageLoader();
        inflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onClick(int position);
    }

    @Override
    public SystemMessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_system_message, parent, false);
        SystemMessageHolder systemMessageHolder = new SystemMessageHolder(view);
        return systemMessageHolder;
    }

    @Override
    public void onBindViewHolder(SystemMessageHolder holder, final int position) {
        holder.tv_name.setText(noticeList.get(position).title);
        holder.tv_content.setText(noticeList.get(position).content);
        holder.tv_time.setText(TimeUtil.stamp2Date(noticeList.get(position).create_time));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( onItemClickListener != null) {
                    onItemClickListener.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }
}

class SystemMessageHolder extends RecyclerView.ViewHolder{

//    @Bind(R.id.iv_head_system_message_item)ImageView iv_head;
    @Bind(R.id.tv_name_system_message_item)TextView tv_name;
    @Bind(R.id.tv_content_system_message_item)TextView tv_content;
    @Bind(R.id.tv_time_system_message_item)TextView tv_time;


    public SystemMessageHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
