package com.quootta.mdate.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.domain.BannerList;
import com.quootta.mdate.domain.UserList;
import com.quootta.mdate.myInterface.OnItemClickListener;
import com.quootta.mdate.ui.activity.BannerActivity;
import com.quootta.mdate.ui.view.ChildViewPager;
import com.quootta.mdate.ui.view.CircleImageView;
import com.quootta.mdate.ui.view.IndicatorView;
import com.quootta.mdate.utils.LogUtil;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by Ryon on 2016/3/10.
 * email:para.ryon@foxmail.com
 */
public class RvDiscoverAdapter extends RecyclerView.Adapter<RvDiscoverAdapter.DiscoverViewHolder>{

    private Context context;
    private List<UserList.users> usersList;
    private List<BannerList.Banner> bannerList;
    private LayoutInflater mInflater;
    private ImageLoader imageLoader;
    private OnItemClickListener onItemClickListener;
    private onLikeListener onLikeListener;
    private View headerView = null;
    private int headerCurPos;
    private boolean isFirstInitBanner = true;

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;


    public RvDiscoverAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        imageLoader = BaseApp.getImageLoader();
    }

    public RvDiscoverAdapter(Context context, List<UserList.users> usersList) {
        this.context = context;
        this.usersList = usersList;
        mInflater = LayoutInflater.from(context);
        imageLoader = BaseApp.getImageLoader();
    }

    public void setUsersList(List<UserList.users> usersList) {
        this.usersList = usersList;
    }

    public interface onLikeListener {
        void onLike(int position, boolean isLike);
    }

    public void setOnLikeListener(onLikeListener onLikeListener) {
        this.onLikeListener = onLikeListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setHeaderView(View headerView) {
        this.headerView = headerView;
    }

    public View getHeaderView() {
        return headerView;
    }

//    public void setBannerList(BannerList bannerList) {
//        this.bannerList =  bannerList;
//    }


    public void setBannerList(List<BannerList.Banner> bannerList) {
        this.bannerList = bannerList;
    }

    @Override
    public int getItemViewType(int position) {
        if (headerView == null) {
            return TYPE_NORMAL;
        } else if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public DiscoverViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (headerView != null && viewType == TYPE_HEADER)
            return new DiscoverViewHolder(headerView);

        View view = mInflater.inflate(R.layout.item_discover, parent, false);
        return new DiscoverViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DiscoverViewHolder holder, final int position) {
        if(getItemViewType(position) == TYPE_HEADER) {
            if(isFirstInitBanner) {
                initBanner(holder);
                isFirstInitBanner = false;
            }
            return;
        }

        final int pos = getRealPosition(holder);

        final UserList.users user = usersList.get(pos);

        initView(holder, user, pos);
    }

    private void initBanner(final DiscoverViewHolder holder) {
        LogUtil.d("initBanner","on initBanner");
        //初始化Banner和点击事件
        BannerPagerAdapter bannerPagerAdapter = new BannerPagerAdapter(context, bannerList, new BannerPagerAdapter.OnPicClickListener() {
            @Override
            public void onClick(int position) {
                Intent bannerIntent = new Intent(context, BannerActivity.class);
                bannerIntent.putExtra("id",bannerList.get(position).id);
                bannerIntent.putExtra("url",bannerList.get(position).action_url);
                bannerIntent.putExtra("title",bannerList.get(position).title);
                bannerIntent.putExtra("content",bannerList.get(position).content);
                bannerIntent.putExtra("share_url",bannerList.get(position).share_url);
                bannerIntent.putExtra("thumbnail",bannerList.get(position).thumbnail);
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
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
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

    private void initView(final DiscoverViewHolder holder, final UserList.users user, final int position) {
        imageLoader.get(
                LocalUrl.getPicUrl(user.avatar),
                imageLoader.getImageListener(holder.iv_head, R.mipmap.home_show_loading, R.mipmap.home_show_loading));
//        imageLoader.get(
//                LocalURL.getPicUrl(user.cover_img),
//                imageLoader.getImageListener(holder.iv_show, R.mipmap.home_show_loading, R.mipmap.home_show_loading));

        holder.iv_show.setDefaultImageResId(R.mipmap.home_show_loading);
        holder.iv_show.setErrorImageResId(R.mipmap.home_show_loading);
        holder.iv_show.setImageUrl(LocalUrl.getPicUrl(user.cover_img), imageLoader);

        holder.tv_name.setText(user.nick_name);
        holder.tv_age.setText(user.age +context.getString(R.string.year));
        String[] cityArray = user.city.split(" ");
        if (cityArray.length<2){
            holder.tvCity.setText(context.getString(R.string.app_tips_text50));
        }else {
            holder.tvCity.setText(cityArray[1]);
        }
        int conn = (int) Double.parseDouble(user.connect_rate);
//        holder.tvConnRate.setText("接通率" + conn + "%");
        holder.tvConnRate.setText(context.getString(R.string.app_tips_text99,conn+"%"));
        holder.tvVideoPrice.setText(user.video_pay + context.getString(R.string.gold_per_min));
        holder.tvAudioPrice.setText(user.audio_pay + context.getString(R.string.gold_per_min));
        final boolean isLike = Boolean.parseBoolean(user.is_favorite);
        holder.iv_like.setSelected(isLike);
        holder.iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.is_favorite = Boolean.toString(!isLike);//取反
                notifyDataSetChanged();
                if (onLikeListener != null){
                    onLikeListener.onLike(position, !isLike);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(holder.itemView, position);
            }
        });

    }

    private int getRealPosition(DiscoverViewHolder holder) {
        int position = holder.getLayoutPosition();

        return headerView == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {
        return headerView == null ? usersList.size() : usersList.size() + 1;
    }

    class DiscoverViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.iv_head_item_discover)CircleImageView iv_head;
        @Bind(R.id.niv_show_item_discover)NetworkImageView iv_show;
        @Bind(R.id.tv_name_item_discover)TextView tv_name;
        @Bind(R.id.tv_age_item_discover)TextView tv_age;
        @Bind(R.id.tv_city_item_discover)TextView tvCity;
        @Bind(R.id.tv_connection_rate)TextView tvConnRate;
        @Bind(R.id.tv_video_price)TextView tvVideoPrice;
        @Bind(R.id.tv_audio_price)TextView tvAudioPrice;
        @Bind(R.id.iv_like_item_discover)ImageView iv_like;

        ChildViewPager vpBanner;
        IndicatorView indicatorView;

        public DiscoverViewHolder(View itemView) {
            super(itemView);
            if (itemView == headerView){
                vpBanner = (ChildViewPager) itemView.findViewById(R.id.vp_banner);
                indicatorView = (IndicatorView) itemView.findViewById(R.id.id_indicator);
                return;
            }
            ButterKnife.bind(this, itemView);
        }
    }
}