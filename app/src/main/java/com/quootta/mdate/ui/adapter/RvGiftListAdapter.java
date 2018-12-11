package com.quootta.mdate.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.domain.GiftListData;
import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Ryon on 2016/11/3/0003.
 */
public class RvGiftListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


//    private GiftListData GiftList;
    private Context context;
    private ImageLoader imageLoader;

    private ArrayList<GiftListData.DataBean.GiftsBean> mList=new ArrayList<>();

    private int selectedPos=-1;
    private onGetGiftPosition onGetGiftPosition;
   public interface onGetGiftPosition{
       void onGiftPosition(int position);
   }

    public RvGiftListAdapter(Context context, GiftListData giftList, onGetGiftPosition onGetGiftPosition) {
        this.context = context;
//        this.GiftList = giftList;
        this.mList.addAll(giftList.getData().getGifts());
        imageLoader = BaseApp.getImageLoader();

        this.onGetGiftPosition=onGetGiftPosition;

        for (int i=0;i<mList.size();i++){
            if (mList.get(i).isSelect){
                selectedPos=i;
            }
        }

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_gift_list, parent, false);
        GiftListViewHolder holder = new GiftListViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        imageLoader.get(
                LocalUrl.getPicUrl(mList.get(position).getCover()),
                imageLoader.getImageListener(((GiftListViewHolder)holder).giftImage, R.mipmap.test, R.mipmap.test));


        ((GiftListViewHolder)holder).giftGoldNum.setText(mList.get(position).getCost()+"");
        ((GiftListViewHolder)holder).giftCharm.setText(mList.get(position).getName()+"");

        if (mList.get(position).isSelect){
            //((GiftListViewHolder) holder).itemGift.setBackgroundColor(Color.parseColor("#88DFDCDC"));
            ((GiftListViewHolder) holder).giftImage.setBackgroundResource(R.mipmap.shade);
            ((GiftListViewHolder) holder).giftCharm.setTextColor(Color.rgb(223,171,15));
            ((GiftListViewHolder) holder).giftGold.setTextColor(Color.rgb(239,132,245));
        }else {
            ((GiftListViewHolder) holder).giftImage.setBackgroundColor(Color.parseColor("#00000000"));
            ((GiftListViewHolder) holder).giftCharm.setTextColor(context.getResources().getColor(R.color.color_494949));
            ((GiftListViewHolder) holder).giftGold.setTextColor(Color.rgb(255,255,255));
        }



        //实现单选
        ((GiftListViewHolder) holder).itemGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPos!=position ){

                    if (selectedPos!=-1){
                        mList.get(selectedPos).isSelect=false;
                        notifyItemChanged(selectedPos);
                    }
                    selectedPos=position;
                    //通过接口获取到position
                    onGetGiftPosition.onGiftPosition(position);

                    mList.get(selectedPos).isSelect=true;
                    notifyItemChanged(selectedPos);
                }
            }
        });

    }




    @Override
    public int getItemCount() {
        return mList.size();
    }


    class GiftListViewHolder extends RecyclerView.ViewHolder {


        ImageView giftImage;

        TextView giftGoldNum;
        RelativeLayout itemGift;
        TextView giftGold;
        TextView giftCharm;

        public GiftListViewHolder(final View itemView) {
            super(itemView);

            giftImage= (ImageView) itemView.findViewById(R.id.gift_image);
            giftGoldNum= (TextView) itemView.findViewById(R.id.gift_gold_num);
            itemGift= (RelativeLayout) itemView.findViewById(R.id.item_gift);
            giftGold= (TextView) itemView.findViewById(R.id.gift_gold);
            giftCharm= (TextView) itemView.findViewById(R.id.gift_charm);

        }
    }

}
