package com.quootta.mdate.ui.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.domain.Album;
import com.quootta.mdate.ui.activity.VideoActivity;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ryon on 2016/5/3/0003.
 */
public class AlbumItemView extends FrameLayout {

    @Bind(R.id.niv_picture_activity) NetworkImageView imageView;
    @Bind(R.id.iv_play_album_item)ImageView iv_play;

    private Context baseContext;
    private ImageLoader imageLoader;
    private Album album;
    private OnPicClickListener onPicClickListener;

    public AlbumItemView(Context context) {
        super(context);
        this.baseContext = context;
        setViews();
    }

    public AlbumItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.baseContext = context;
        setViews();
    }

    public interface OnPicClickListener{
        void onClick();
    }

    public void setViews() {
        imageLoader = BaseApp.getImageLoader();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.item_album_detail, null);
        ButterKnife.bind(this, view);
        addView(view);
    }

    public void setData(final Album album) {// Album
        this.album = album;
        imageView.setDefaultImageResId(R.mipmap.test);
        imageView.setErrorImageResId(R.mipmap.test);
        switch (album.res_type) {
            case "image":
                imageView.setImageUrl(LocalUrl.getPicUrl(album.res_id), imageLoader);
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onPicClickListener != null) {
                            onPicClickListener.onClick();
                        }
                    }
                });
                iv_play.setVisibility(GONE);
                imageView.setVisibility(VISIBLE);
                break;
            case "video":
                imageView.setImageUrl(LocalUrl.getVideoPicUrl(album.res_id), imageLoader);
                imageView.setVisibility(VISIBLE);
                iv_play.setVisibility(VISIBLE);
                iv_play.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(baseContext, VideoActivity.class);
                        intent.putExtra("videoUrl",album.res_id);
                        baseContext.startActivity(intent);
                    }
                });
                break;
        }
    }

    public void setOnPicClickListener(OnPicClickListener onPicClickListener) {
        this.onPicClickListener = onPicClickListener;
    }
}
