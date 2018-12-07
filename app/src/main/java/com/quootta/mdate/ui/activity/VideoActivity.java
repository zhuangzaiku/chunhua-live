package com.quootta.mdate.ui.activity;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.utils.MediaController;
import com.pili.pldroid.player.widget.PLVideoView;

import butterknife.Bind;

public class VideoActivity extends BaseActivity {

    @Bind(R.id.plvv_activity) PLVideoView videoView;

    private String videoPath;

    @Override
    protected void init() {
        //获取视频地址
        videoPath = getIntent().getStringExtra("videoUrl");
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_video;
    }

    @Override
    protected void initData() {
        initVideo();
    }

    private void initVideo() {
        MediaController mMediaController = new MediaController(this);
        videoView.setMediaController(mMediaController);
        videoView.setVideoPath(LocalUrl.getVideoUrl(videoPath));
    }

    @Override
    protected void setListener() {
    }
}
