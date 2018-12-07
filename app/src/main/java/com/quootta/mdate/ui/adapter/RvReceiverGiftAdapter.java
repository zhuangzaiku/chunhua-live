package com.quootta.mdate.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.domain.UserDetail;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

/**
 * Created by Ryon on 2016/11/16/0016.
 */
public class RvReceiverGiftAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<UserDetail> userDetails;
    private ImageLoader imageLoader;

    public RvReceiverGiftAdapter(Context context,List<UserDetail> userDetails){
        this.context=context;
        this.userDetails=userDetails;
        imageLoader= BaseApp.getImageLoader();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_giftlist, parent, false);
        ReceiverGiftViewHolder holder = new ReceiverGiftViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        imageLoader.get(
                LocalUrl.getPicUrl(userDetails.get(0).gift_receive.get(position).getCover()),
                imageLoader.getImageListener(((ReceiverGiftViewHolder)holder).pic, R.mipmap.test, R.mipmap.test));
        ((ReceiverGiftViewHolder) holder).count.setText(userDetails.get(0).gift_receive.get(position).getCount()+"");
        ((ReceiverGiftViewHolder) holder).name.setText(userDetails.get(0).gift_receive.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return userDetails.get(0).gift_receive.size();
    }


    class ReceiverGiftViewHolder extends RecyclerView.ViewHolder {
        private ImageView pic;
        private TextView name;
        private TextView count;


        public ReceiverGiftViewHolder(View itemView) {
            super(itemView);
            pic= (ImageView) itemView.findViewById(R.id.gift_list_pic);
            name= (TextView) itemView.findViewById(R.id.gift_list_name);
            count= (TextView) itemView.findViewById(R.id.gift_list_count);
        }
    }
}
