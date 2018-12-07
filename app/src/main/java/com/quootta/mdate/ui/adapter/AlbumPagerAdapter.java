package com.quootta.mdate.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.quootta.mdate.domain.Album;
import com.quootta.mdate.ui.view.AlbumItemView;
import com.quootta.mdate.utils.LogUtil;

import java.util.List;

/**
 * Created by Ryon on 2016/5/3/0003.
 */
public class AlbumPagerAdapter extends PagerAdapter {

    private Context context;
    private List<Album> albumList;
    private AlbumItemView.OnPicClickListener onPicClickListener;

    public AlbumPagerAdapter(Context context, List<Album> albumList) {
        this.albumList = albumList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return albumList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LogUtil.d("AlbumPagerAdapter","position: " + position);
        AlbumItemView item = new AlbumItemView(context);
        Album album = albumList.get(position);
        item.setData(album);
        if(onPicClickListener != null) {
            item.setOnPicClickListener(onPicClickListener);
        }
        container.addView(item);
        return item;
    }

    public void setOnPicClickListener(AlbumItemView.OnPicClickListener onPicClickListener) {
        this.onPicClickListener = onPicClickListener;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        LogUtil.d("AlbumPagerAdapter","on destroyItem");
        AlbumItemView view = (AlbumItemView) object;
        container.removeView(view);
    }
}
