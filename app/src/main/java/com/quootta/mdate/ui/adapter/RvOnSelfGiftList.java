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
import com.quootta.mdate.domain.Gift_List;
import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryon on 2016/11/16/0016.
 */
public class RvOnSelfGiftList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Gift_List> giftLists=new ArrayList<>();
    private ImageLoader imageLoader;

    public RvOnSelfGiftList(Context context, Gift_List giftList){
        this.context=context;

        giftLists.add(giftList);
        imageLoader= BaseApp.getImageLoader();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_giftlist, parent, false);
        OnselfViewHolder holder = new OnselfViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        imageLoader.get(
                LocalUrl.getPicUrl(giftLists.get(0).getData().getPresent().get(position).getCover()),
                imageLoader.getImageListener(((OnselfViewHolder)holder).pic, R.mipmap.test, R.mipmap.test));
        ((OnselfViewHolder) holder).count.setText(giftLists.get(0).getData().getPresent().get(position).getCount()+"");
        ((OnselfViewHolder) holder).name.setText(giftLists.get(0).getData().getPresent().get(position).getName());
    }

    @Override
    public int getItemCount() {

        return giftLists.get(0).getData().getPresent().size();
    }

    class OnselfViewHolder extends RecyclerView.ViewHolder {
        private ImageView pic;
        private TextView name;
        private TextView count;


        public OnselfViewHolder(View itemView) {
            super(itemView);
            pic= (ImageView) itemView.findViewById(R.id.gift_list_pic);
            name= (TextView) itemView.findViewById(R.id.gift_list_name);
            count= (TextView) itemView.findViewById(R.id.gift_list_count);
        }
    }

}
