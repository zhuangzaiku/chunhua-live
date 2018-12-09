package com.quootta.mdate.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.base.BaseRecyclerAdapter;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.domain.BannerList;
import com.quootta.mdate.domain.HotList;
import com.quootta.mdate.domain.UserList;
import com.quootta.mdate.ui.activity.BannerActivity;
import com.quootta.mdate.ui.activity.RankingActivity;
import com.quootta.mdate.ui.view.ChildViewPager;
import com.quootta.mdate.ui.view.CircleImageView;
import com.quootta.mdate.ui.view.FixedSpeedScroller;
import com.quootta.mdate.ui.view.IndicatorView;
import com.quootta.mdate.utils.LogUtil;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;


/**
 * Created by ryon on 2016/11/16.
 */

public class RvHotListAdapter extends BaseRecyclerAdapter<UserList.users, RvHotListAdapter.HotViewHolder> {

    private List<BannerList.Banner> bannerList;
    private List<List<String>> boardList;
    private LayoutInflater mInflater;
    private ImageLoader imageLoader;
    private OnLikeListener onLikeListener;
    private List<HotList.DataBean.SceneBean> sceneList;
    private int headerCurPos;
    private boolean isFirstInitBanner = true;
    private Timer Boardtimer;
    private Timer Bannertimer;
    private Context context;
    public RvHotListAdapter(Context mContext, List<UserList.users> mDatas) {
        super(mContext, mDatas);
        context = mContext;
        mInflater = LayoutInflater.from(mContext);
        imageLoader = BaseApp.getImageLoader();


    }

    public interface OnLikeListener {
        void onLike(int position, boolean isLike);
    }

    public void setOnLikeListener(OnLikeListener onLikeListener) {
        this.onLikeListener = onLikeListener;
    }

    public void setBannerList(List<BannerList.Banner> bannerList) {
        this.bannerList = bannerList;
    }

    public void setBoardList(List<List<String>> boardList) {
     //   boardList.add(2, boardList.get(0));
        this.boardList = boardList;
        if (boardList.size()==2){
            this.boardList.add(2,this.boardList.get(0));
        }
    }

    public void setUserScene(List<HotList.DataBean.SceneBean> sceneList){
        this.sceneList=sceneList;

    }


    @Override
    protected void bindHeaderData(final HotViewHolder holder) {
        LogUtil.d("initBanner","on initBanner");
        initBanner(holder);
//        initClassify(holder);
//        initBoard(holder);
    }

    //初始化用户分类
    private void initClassify(HotViewHolder holder){

        GridLayoutManager gridLayoutManager=new GridLayoutManager(context,sceneList.size());
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        holder.re_classify.setLayoutManager(gridLayoutManager);
        RvSceneAdapter rvSceneAdapter=new RvSceneAdapter(context,sceneList);
        holder.re_classify.setAdapter(rvSceneAdapter);

    }



    //初始化广告条
    private void initBanner(final HotViewHolder holder) {
        //初始化Banner和点击事件
        BannerPagerAdapter bannerPagerAdapter = new BannerPagerAdapter(context, bannerList, new BannerPagerAdapter.OnPicClickListener() {
            @Override
            public void onClick(int position) {
                String str=LocalUrl.getPicUrl(bannerList.get(position).thumbnail);
                Intent bannerIntent = new Intent(context, BannerActivity.class);
                bannerIntent.putExtra("id",bannerList.get(position).id);
                Log.i("tag","rvHotListAdapter--url--->"+bannerList.get(position).action_url);
                bannerIntent.putExtra("url",bannerList.get(position).action_url);
                bannerIntent.putExtra("title",bannerList.get(position).title);
                bannerIntent.putExtra("content",bannerList.get(position).content);
                bannerIntent.putExtra("share_url",bannerList.get(position).share_url);
                bannerIntent.putExtra("thumbnail",str);
                context.startActivity(bannerIntent);
            }
        });
        holder.vpBanner.setAdapter(bannerPagerAdapter);

        if (bannerList.size() > 1) {
            holder.indicatorView.setViewPager(holder.vpBanner);
        } else {
            holder.indicatorView.setVisibility(View.GONE);
        }

        //设置轮播任务
        if (Bannertimer==null){
            Bannertimer = new Timer();
            Bannertimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Activity activity = (Activity) context;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int pos = holder.vpBanner.getCurrentItem();
                            if ( pos + 1 == bannerList.size()) {
                                holder.vpBanner.setCurrentItem(0);
                            } else {
                                holder.vpBanner.setCurrentItem(pos + 1);
                            }

                        }
                    });
                }
            }, 3*1000, 3*1000);
        }


    }

    //初始化排行榜
    private void initBoard(final HotViewHolder holder) {
        BoardPagerAdapter boardPagerAdapter = new BoardPagerAdapter(context, boardList, new BoardPagerAdapter.OnBoardClickListener() {
            @Override

            public void onClick(int position) {
                Intent intent = new Intent(context, RankingActivity.class);
                intent.putExtra("page",position);

                context.startActivity(intent);
            }
        });

        //设置滑动速度
        try {
            Field field = VerticalViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(holder.vpBoard.getContext(),
                    new AccelerateInterpolator());
            field.set(holder.vpBoard, scroller);
            scroller.setmDuration(700);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        holder.vpBoard.setAdapter(boardPagerAdapter);

        //设置轮播任务
        if (Boardtimer==null){
            Boardtimer = new Timer();
            Boardtimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Activity activity = (Activity) context;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int pos = holder.vpBoard.getCurrentItem();
//                            if ( pos + 1 == boardList.size()) {
//                                holder.vpBoard.setCurrentItem(0);
//                            } else {
//                                holder.vpBoard.setCurrentItem(pos + 1);
//                            }

                            holder.vpBoard.setCurrentItem(pos + 1);
                        }
                    });
                }
            }, 3*1000, 3*1000);
        }

        holder.vpBoard.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                int curPos = holder.vpBoard.getCurrentItem();
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    if (curPos + 1 ==  boardList.size()) {
                        holder.vpBoard.setCurrentItem(0, false);
                    }
                }
            }
        });

    }

    @Override
    protected void bindFooterData(HotViewHolder holder) {

    }

    @Override
    protected void bindItemData(HotViewHolder viewHolder, final UserList.users user, final int position) {


        imageLoader.get(
                LocalUrl.getPicUrl(user.avatar),
                imageLoader.getImageListener(viewHolder.ivHeadPic, R.mipmap.home_show_loading, R.mipmap.home_show_loading));

        viewHolder.ivShow.setDefaultImageResId(R.mipmap.home_show_loading);
        viewHolder.ivShow.setErrorImageResId(R.mipmap.home_show_loading);
        viewHolder.ivShow.setImageUrl(LocalUrl.getPicUrl(user.cover_img), imageLoader);

        viewHolder.tvName.setText(user.nick_name);
        viewHolder.tvAge.setText(user.age);

        switch (user.gender) {
            case "male":
                viewHolder.ivGender.setImageResource(R.mipmap.ic_male);
                break;
            case "female":
                viewHolder.ivGender.setImageResource(R.mipmap.ic_female);
                break;
        }

        //viewHolder.tvSign.setText(user.personal_desc);

        String[] cityArray = user.city.split(" ");
        if (cityArray.length<2){
            viewHolder.tvCity.setText("火星");
        }else {
            viewHolder.tvCity.setText(cityArray[1]);
        }
        final boolean isLike = Boolean.parseBoolean(user.is_favorite);
        viewHolder.ivFavorite.setSelected(isLike);
        viewHolder.ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.is_favorite = Boolean.toString(!isLike);//取反
                notifyDataSetChanged();
                if (onLikeListener != null){
                    onLikeListener.onLike(position, !isLike);
                }
            }
        });
    }

    @Override
    public HotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new HotViewHolder(mHeaderView);
        }
        if (mFooterView != null && viewType == TYPE_FOOTER) {
            return new HotViewHolder(mFooterView);
        }

        View itemView = mInflater.inflate(R.layout.item_hot, parent, false);

        return new HotViewHolder(itemView);
    }

    class HotViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_show)
        NetworkImageView ivShow;
        @Bind(R.id.iv_head_pic)
        CircleImageView ivHeadPic;
        @Bind(R.id.tv_city)
        TextView tvCity;
//        @Bind(R.id.tv_sign)
//        TextView tvSign;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_age)
        TextView tvAge;
        @Bind(R.id.iv_gender_label)
        ImageView ivGender;
        @Bind(R.id.iv_favorite)
        ImageView ivFavorite;

        //Banner相关控件，手动引入
        ChildViewPager vpBanner;
        IndicatorView indicatorView;
        VerticalViewPager vpBoard;

//        LinearLayout lovesinging;
//        LinearLayout seeklove;
//        LinearLayout singer;
//        LinearLayout truth;
        RecyclerView re_classify;
        public HotViewHolder(View itemView) {
            super(itemView);
            if (itemView == mHeaderView){
                vpBanner = (ChildViewPager) itemView.findViewById(R.id.vp_banner);
                indicatorView = (IndicatorView) itemView.findViewById(R.id.id_indicator);
                vpBoard = (VerticalViewPager) itemView.findViewById(R.id.vp_board);

                //分类
                re_classify= (RecyclerView) itemView.findViewById(R.id.re_classify);
//                lovesinging= (LinearLayout) itemView.findViewById(R.id.classify_lovesinging);
//                seeklove= (LinearLayout) itemView.findViewById(R.id.classify_seeklove);
//                singer= (LinearLayout) itemView.findViewById(R.id.classify_singer);
//                truth= (LinearLayout) itemView.findViewById(R.id.classify_truth);
                return;
            }
            ButterKnife.bind(this, itemView);
        }
    }
}
