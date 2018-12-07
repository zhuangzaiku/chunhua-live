package com.quootta.mdate.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.domain.GiftList;
import com.quootta.mdate.myInterface.OnItemSelectedListener;
import com.android.volley.toolbox.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ryon on 2016/3/8.
 * email:para.ryon@foxmail.com
 */
public class RvGiftGridAdapter extends RecyclerView.Adapter<GiftViewHolder> {

    private LayoutInflater inflater;
    private Context mContext;
    private GiftList giftList;
    private ImageLoader imageLoader;
    private OnItemSelectedListener onItemSelectedListener;


    public RvGiftGridAdapter(Context mContext, GiftList giftList) {
        this.mContext = mContext;
        this.giftList = giftList;
        inflater = LayoutInflater.from(mContext);
        imageLoader = BaseApp.getImageLoader();
    }


    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    @Override
    public GiftViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_gift, parent, false);
        GiftViewHolder giftViewHolder = new GiftViewHolder(view);
        return giftViewHolder;
    }

    @Override
    public void onBindViewHolder(final GiftViewHolder holder, final int position) {
        imageLoader.get(
                LocalUrl.getPicUrl(giftList.gifts.get(position).cover),
                imageLoader.getImageListener(holder.image, R.mipmap.test, R.mipmap.test));
        holder.image.setBackgroundResource(giftList.gifts.get(position).isSelected?
                R.drawable.shape_gift_item:0);
//        holder.name.setText(giftList.gifts.get(position).name);
        holder.gold.setText(giftList.gifts.get(position).cost +mContext.getString(R.string.coin));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //将上一个被选中的礼物设为未选中
                giftList.gifts.get(giftList.currentGift).select();
                //将当前被点击礼物设为选中
                //1.改变背景
                holder.image.setBackgroundResource(R.drawable.shape_gift_item);
                //2.改变记录的礼物位置为当前礼物位置
                giftList.currentGift = holder.getLayoutPosition();
                //3.将当前礼物设为选中
                giftList.gifts.get(position).select();
                //4.将当前礼物的drawable传回到activity中供设置
                Drawable drawable = holder.image.getDrawable();
                onItemSelectedListener.onSelected(drawable,position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return giftList.gifts.size();
    }
}

class GiftViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.iv_gift_item)ImageView image;
//    @Bind(R.id.tv_gift_item)TextView name;
    @Bind(R.id.tv_gold_gift_item)TextView gold;

    public GiftViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
