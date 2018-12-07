package com.quootta.mdate.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.domain.UserList;
import com.quootta.mdate.ui.view.CircleImageView;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ryon on 2016/5/16/0016.
 */
public class RvFriendsAdapter extends RecyclerView.Adapter<RvFavoriteHolder> {

    private LayoutInflater inflater;
    private List<UserList.users> userList;
    private Context baseContext;
    private ImageLoader imageLoader;
    private OnLongClickListener onLongClickListener;
    private OnItemClickListener onItemClickListener;

    public RvFriendsAdapter(Context baseContext, List<UserList.users> userList) {
        this.baseContext = baseContext;
        this.userList = userList;
        imageLoader = BaseApp.getImageLoader();
        this.inflater = LayoutInflater.from(baseContext);
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnLongClickListener {
        void onLongClick(int position);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @Override
    public RvFavoriteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_favorite_list, parent, false);
        RvFavoriteHolder rvFavoriteHolder = new RvFavoriteHolder(view);
        return rvFavoriteHolder;
    }

    @Override
    public void onBindViewHolder(RvFavoriteHolder holder, final int position) {
        imageLoader.get(LocalUrl.getPicUrl(userList.get(position).avatar),
                imageLoader.getImageListener(holder.iv_head, R.mipmap.home_show_loading, R.mipmap.home_show_loading));
        holder.tv_name.setText(userList.get(position).nick_name);
        holder.tv_sign.setText(userList.get(position).personal_desc);

        holder.tv_age.setText(userList.get(position).age);

        String city = userList.get(position).city;
        Log.i("tag","-------->"+city);
        if (city == "" || city.isEmpty()) {

            holder.tv_address.setText(R.string.mars);
        } else {
//            String lastChar = city.substring(city.length() - 1, city.length());
//            Log.i("tag","-------->"+lastChar);
//            if (lastChar.equals("市")) {
//                holder.tv_address.setText(city.substring(0, city.length() - 1));
//            }
            String[] cityArray = city.split(" ");
            if (cityArray.length<2){
                holder.tv_address.setText(baseContext.getString(R.string.app_tips_text50));
            }else {
                holder.tv_address.setText(cityArray[1]);
            }

        }

        if (userList.get(position).gender!=null){
            if (userList.get(position).gender.equals("male")){
                holder.tv_gender.setImageResource(R.drawable.ranking_sexb);
            }else {
                holder.tv_gender.setImageResource(R.drawable.rankingsexg);

            }
        }


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onLongClickListener != null) {
                    onLongClickListener.onLongClick(position);
                }
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}

class RvFavoriteHolder extends RecyclerView.ViewHolder {


    @Bind(R.id.iv_head_item_favorite)
    CircleImageView iv_head;
    @Bind(R.id.tv_name_item_favorite)
    TextView tv_name;
    @Bind(R.id.tv_gender_item_favorite)
    ImageView tv_gender;
    @Bind(R.id.tv_age_item_favorite)
    TextView tv_age;
    @Bind(R.id.tv_address_item_favorite)
    TextView tv_address;
    @Bind(R.id.tv_sign_item_favorite)
    TextView tv_sign;


    public RvFavoriteHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
