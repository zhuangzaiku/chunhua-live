package com.quootta.mdate.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.domain.Album;
import com.quootta.mdate.myInterface.OnItemSelectedListener;
import com.quootta.mdate.ui.view.RoundRectNetworkImageView;
import com.quootta.mdate.utils.ImageUtil;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ryon on 2016/2/25.
 * email:para.ryon@foxmail.com
 */
public class RvGalleryAdapter extends RecyclerView.Adapter<GalleryViewHolder> {

    private LayoutInflater inflater;
    private Context mContext;
    private List<Album> albums;
    private OnItemSelectedListener onItemSelectedListener;
    private ImageLoader imageLoader;
    private int type;

    public RvGalleryAdapter(Context mContext, List<Album> albums,int type) {
        this.mContext = mContext;
        this.albums = albums;
        this.type=type;
        inflater = LayoutInflater.from(mContext);
        imageLoader = BaseApp.getImageLoader();
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_gallery, parent, false);
        GalleryViewHolder galleryViewHolder = new GalleryViewHolder(view);
        return galleryViewHolder;
    }

    @Override
    public void onBindViewHolder(final GalleryViewHolder holder, final int position) {



        if (position>=albums.size()){

           holder.upload.setVisibility(View.VISIBLE);
        }else {

            //显示封面图标
            if (albums.get(position).is_cover_img){
                holder.cover_item.setVisibility(View.VISIBLE);
            }else {
                holder.cover_item.setVisibility(View.GONE);
            }


            switch (albums.get(position).res_type) {
                case "image":
                    holder.playToken.setVisibility(View.GONE);

                    holder.image.setErrorImageResId(R.mipmap.home_show_loading);
                    holder.image.setDefaultImageResId(R.mipmap.home_show_loading);
                    holder.image.setImageUrl(
                            LocalUrl.getPicUrl(albums.get(position).res_id), imageLoader);
                    break;
                case "video":
                    holder.playToken.setVisibility(View.VISIBLE);
                    holder.image.setErrorImageResId(R.mipmap.home_show_loading);
                    holder.image.setDefaultImageResId(R.mipmap.home_show_loading);
                    holder.image.setImageUrl(
                            LocalUrl.getVideoPicUrl(albums.get(position).res_id), imageLoader);
                    holder.image.setImageBitmap(ImageUtil.createVideoThumbnail(LocalUrl.getVideoPicUrl(albums.get(position).res_id), MediaStore.Images.Thumbnails.MINI_KIND));
                    break;
            }

        }


        //设置点击事件
        if(onItemSelectedListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Drawable drawable = holder.image.getDrawable();
                    onItemSelectedListener.onSelected(drawable, position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (type==0){
            return albums.size()+1;
        }else {
            return albums.size();
        }
    }
}

class GalleryViewHolder extends RecyclerView.ViewHolder{

    @Bind(R.id.iv_gallery_item)RoundRectNetworkImageView image;
    @Bind(R.id.iv_play_gallery)ImageView playToken;
    @Bind(R.id.iv_gallery_upload)ImageView upload;
    @Bind(R.id.iv_cover_item) ImageView cover_item;
    public GalleryViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
