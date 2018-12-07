package com.quootta.mdate.ui.activity;

import android.support.v4.view.ViewPager;
import android.util.Log;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.domain.Album;
import com.quootta.mdate.domain.AlbumList;
import com.quootta.mdate.ui.adapter.AlbumPagerAdapter;
import com.quootta.mdate.ui.view.AlbumItemView;
import com.quootta.mdate.utils.ActivityUtil;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Ryon on 2016/3/11.
 * email:para.ryon@foxmail.com
 */
public class AlbumDetailActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @Bind(R.id.vp_album)ViewPager vp_album;

    private AlbumList mAlbumList;
    private AlbumPagerAdapter albumPagerAdapter;
    private int currentPosition;

    @Override
    protected void init() {
        currentPosition = getIntent().getIntExtra("currentPosition", 1);
        Log.i("tag","查看详情中当前点击的deleteposition-->"+currentPosition);
        mAlbumList = (AlbumList) getIntent().getExtras().getSerializable("albumList");
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_picture;
    }

    @Override
    protected void initData() {
        List<Album> albumList = mAlbumList.album.subList(0, mAlbumList.album.size());
        albumPagerAdapter = new AlbumPagerAdapter(baseContext, albumList);
        albumPagerAdapter.setOnPicClickListener(new AlbumItemView.OnPicClickListener() {
            @Override
            public void onClick() {
                ActivityUtil.finishActivty();
            }
        });
        vp_album.setAdapter(albumPagerAdapter);
        vp_album.setCurrentItem(currentPosition);
        vp_album.addOnPageChangeListener(this);
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
