package com.quootta.mdate.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.domain.Album;
import com.quootta.mdate.myInterface.OnItemClickListener;
import com.quootta.mdate.utils.LogUtil;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ryon on 2016/3/11.
 * email:para.ryon@foxmail.com
 */
public class RvAlbumAdapter extends RecyclerView.Adapter<AlbumViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<Album> list;
    private ImageLoader imageLoader;
    private OnItemClickListener onItemClickListener;

    public RvAlbumAdapter(Context context, List<Album> list) {
        Album nullAlbum = new Album();
        this.list = list;
        this.list.add(0, nullAlbum);//载入一个空album为上传按钮占位
        this.context = context;
        inflater = LayoutInflater.from(context);
        imageLoader = BaseApp.getImageLoader();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LogUtil.d("RvAlbumAdapter", "onCreateViewHolder run");

        View view = inflater.inflate(R.layout.item_album, parent, false);
        AlbumViewHolder albumViewHolder = new AlbumViewHolder(view);

        return albumViewHolder;
    }

    @Override
    public void onBindViewHolder(final AlbumViewHolder holder, final int position) {
        if (position == 0) {
            holder.uploadPhoto.setVisibility(View.VISIBLE);
            holder.photoImage.setVisibility(View.GONE);
        }else {
            holder.uploadPhoto.setVisibility(View.GONE);
            holder.photoImage.setVisibility(View.VISIBLE);
            holder.photoImage.setDefaultImageResId(R.mipmap.test);
            holder.photoImage.setErrorImageResId(R.mipmap.test);

            switch (list.get(position).res_type) {
                case "image":
                    holder.playToken.setVisibility(View.GONE);
                    holder.photoImage.setImageUrl(LocalUrl.getPicUrl(list.get(position).res_id), imageLoader);
                    break;
                case "video":
                    holder.playToken.setVisibility(View.VISIBLE);
                    holder.photoImage.setImageUrl(LocalUrl.getVideoPicUrl(list.get(position).res_id), imageLoader);
                    break;
                default:
                    break;
            }

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onItemLongClick(holder.itemView, position);
                    return true;
                }
            });
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
        return list.size();
    }
}

class AlbumViewHolder extends RecyclerView.ViewHolder{

    @Bind(R.id.niv_album_item)NetworkImageView photoImage;
    @Bind(R.id.rl_upload_photo)RelativeLayout uploadPhoto;
    @Bind(R.id.iv_play_album)ImageView playToken;

    public AlbumViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
