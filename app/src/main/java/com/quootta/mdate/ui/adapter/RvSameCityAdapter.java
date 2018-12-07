package com.quootta.mdate.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.base.BaseRecyclerAdapter;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.domain.BannerList;
import com.quootta.mdate.domain.UserList;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ryon on 2016/11/20.
 */

public class RvSameCityAdapter extends BaseRecyclerAdapter<UserList.users, RvSameCityAdapter.SameCityViewHolder> {

    private Context baseContext;
    private List<BannerList.Banner> bannerList;
    private LayoutInflater mInflater;
    private ImageLoader imageLoader;
    private View headerView = null;
    private int headerCurPos;
    private boolean isFirstInitBanner = true;

    public RvSameCityAdapter(Context mContext, List<UserList.users> mDatas) {
        super(mContext, mDatas);
        baseContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        imageLoader = BaseApp.getImageLoader();
    }

    @Override
    protected void bindHeaderData(SameCityViewHolder holder) {

    }

    @Override
    protected void bindFooterData(SameCityViewHolder holder) {

    }

    @Override
    protected void bindItemData(SameCityViewHolder viewHolder, UserList.users user, int position) {
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

        viewHolder.tvAge.setText(user.age);
        String[] cityArray = user.city.split(" ");
        if (cityArray.length < 2) {
            viewHolder.tvCity.setText(baseContext.getString(R.string.app_tips_text50));
        } else {
            viewHolder.tvCity.setText(cityArray[1]);
        }

    }

    @Override
    public SameCityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new SameCityViewHolder(mHeaderView);
        }
        if (mFooterView != null && viewType == TYPE_FOOTER) {
            return new SameCityViewHolder(mFooterView);
        }

        View itemView = mInflater.inflate(R.layout.item_newest, parent, false);
        return new SameCityViewHolder(itemView);
    }

    class SameCityViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_show)
        NetworkImageView ivShow;
        @Bind(R.id.tv_age)
        TextView tvAge;
        @Bind(R.id.iv_gender_label)
        ImageView ivGender;
        @Bind(R.id.tv_city)
        TextView tvCity;

        public SameCityViewHolder(View itemView) {
            super(itemView);

            if (itemView == headerView){
                // TODO: 2016/11/18 findViewById
                return;
            }
            ButterKnife.bind(this, itemView);
        }
    }
}
