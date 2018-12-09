package com.quootta.mdate.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
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
import com.quootta.mdate.domain.UserList;
import com.quootta.mdate.ui.view.FixedSpeedScroller;
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
 * Created by ryon on 2016/11/20.
 */

public class RvNewestAdapter extends BaseRecyclerAdapter<UserList.users, RvNewestAdapter.NewestHolder> {

    private List<List<String>> callImgsList;
    private LayoutInflater mInflater;
    private ImageLoader imageLoader;
    private OnBannerItemClickListener onBannerItemClickListener;
    private  Timer timer;
    private static final String TAG = "RvNewestAdapter";
    private Context context;

    public interface OnBannerItemClickListener {
        void onItemClick(int position, int index);
    }

    public RvNewestAdapter(Context mContext, List<UserList.users> mDatas) {
        super(mContext, mDatas);
        context = mContext;
        mInflater = LayoutInflater.from(mContext);
        imageLoader = BaseApp.getImageLoader();
    }

    public void setBannerList (List<List<String>> callImgsList) {
        this.callImgsList = callImgsList;
        if (callImgsList.size() == 2) {
            this.callImgsList.add(2, this.callImgsList.get(0));
        }
    }

    public void setOnBannerItemClickListener(OnBannerItemClickListener onBannerItemClickListener) {
        this.onBannerItemClickListener = onBannerItemClickListener;
    }

    @Override
    protected void bindHeaderData(final NewestHolder holder) {
        BoardPagerAdapter boardPagerAdapter = new BoardPagerAdapter(context, callImgsList, null);
        boardPagerAdapter.setOnItemClickListener(new BoardPagerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, int index) {
                onBannerItemClickListener.onItemClick(position, index);

            }
        });
        //设置滑动速度
        try {
            Field field = VerticalViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(holder.vpList.getContext(),
                    new AccelerateInterpolator());
            field.set(holder.vpList, scroller);
            scroller.setmDuration(700);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        holder.vpList.setAdapter(boardPagerAdapter);

        //设置轮播任务
        if (timer==null){
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Activity activity = (Activity) context;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int pos = holder.vpList.getCurrentItem();
                            if ( pos + 1 == callImgsList.size()) {
                                holder.vpList.setCurrentItem(0);
                            } else {
                                holder.vpList.setCurrentItem(pos + 1);
                            }

                        }
                    });
                }
            }, 3*1000, 3*1000);
        }



        holder.vpList.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                int curPos = holder.vpList.getCurrentItem();
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    if (curPos + 1 ==  callImgsList.size()) {
                        holder.vpList.setCurrentItem(0, false);
                    }
                }
            }
        });

    }

    @Override
    protected void bindFooterData(NewestHolder holder) {

    }

    @Override
    protected void bindItemData(NewestHolder viewHolder, UserList.users user, int position) {
        viewHolder.ivShow.setDefaultImageResId(R.mipmap.home_show_loading);
        viewHolder.ivShow.setErrorImageResId(R.mipmap.home_show_loading);
        viewHolder.ivShow.setImageUrl(LocalUrl.getPicUrl(user.avatar), imageLoader);

        switch (user.gender) {
            case "male":
                viewHolder.ivGender.setImageResource(R.drawable.signup_boy_down);
                break;
            case "female":
                viewHolder.ivGender.setImageResource(R.drawable.signup_girl_down);
                break;
        }

        viewHolder.tvName.setText(user.nick_name);

    }

    @Override
    public NewestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new NewestHolder(mHeaderView);
        }
        if (mFooterView != null && viewType == TYPE_FOOTER) {
            return new NewestHolder(mFooterView);
        }

        View itemView = mInflater.inflate(R.layout.item_newest, parent, false);
        return new NewestHolder(itemView);
    }

    class NewestHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_show)
        NetworkImageView ivShow;
        @Bind(R.id.iv_gender_label)
        ImageView ivGender;
        @Bind(R.id.tv_name)
        TextView tvName;

        VerticalViewPager vpList;

        public NewestHolder(View itemView) {
            super(itemView);
            if (itemView == mHeaderView){
                vpList = (VerticalViewPager) itemView.findViewById(R.id.vp_list);
                return;
            }
            ButterKnife.bind(this, itemView);
        }
    }
}
