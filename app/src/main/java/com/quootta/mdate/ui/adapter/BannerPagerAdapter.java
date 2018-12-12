package com.quootta.mdate.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.domain.BannerList;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.quootta.mdate.ui.view.RoundRectNetworkImageView;

import java.util.List;
public class BannerPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater mInflater;
    private ImageLoader imageLoader;
    private List<BannerList.Banner> bannerList;
    private OnPicClickListener onPicClickListener;

    public BannerPagerAdapter(Context context, List<BannerList.Banner> bannerList, OnPicClickListener onPicClickListener) {
        this.context = context;
        this.bannerList = bannerList;
        this.onPicClickListener = onPicClickListener;
        mInflater = LayoutInflater.from(context);
        imageLoader = BaseApp.getImageLoader();
    }

    public interface OnPicClickListener{
        void onClick(int position);
    }

    @Override
    public int getCount() {
        return bannerList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        RoundRectNetworkImageView imageView = (RoundRectNetworkImageView) mInflater.inflate(R.layout.item_banner, container, false);
        imageView.setDefaultImageResId(R.mipmap.home_show_loading);
        imageView.setErrorImageResId(R.mipmap.home_show_loading);
        imageView.setImageUrl(LocalUrl.getPicUrl(bannerList.get(position).image_url), imageLoader);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPicClickListener.onClick(position);
            }
        });
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((NetworkImageView) object);
    }
}
