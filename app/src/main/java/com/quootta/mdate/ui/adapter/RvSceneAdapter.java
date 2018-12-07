package com.quootta.mdate.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.domain.HotList;
import com.quootta.mdate.ui.activity.SceneActivity;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.quootta.mdate.base.BaseApp.imageLoader;

/**
 * Created by Ryon on 2016/12/29/0029.
 */

public class RvSceneAdapter extends RecyclerView.Adapter<RvSceneAdapter.SceneViewHolder> {



    private Context context;
    private List<HotList.DataBean.SceneBean> sceneList;
    public RvSceneAdapter(Context context, List<HotList.DataBean.SceneBean> sceneList) {
        this.context = context;
        this.sceneList=sceneList;
        //倒序排序
        Collections.reverse(this.sceneList);


    }

    @Override
    public SceneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_scene, parent, false);
        SceneViewHolder SceneViewHolder = new SceneViewHolder(view);
        return SceneViewHolder;
    }

    @Override
    public void onBindViewHolder(SceneViewHolder holder, final int position) {

        imageLoader.get(
                LocalUrl.getPicUrl(sceneList.get(position).getCover()),
                imageLoader.getImageListener(holder.SceneItemPic, R.mipmap.home_show_loading, R.mipmap.home_show_loading));

        holder.SceneItemTv.setText(sceneList.get(position).getName());

        holder.Scene_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, SceneActivity.class);
                intent.putExtra("id",sceneList.get(position).get_id());
                intent.putExtra("name",sceneList.get(position).getName());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return sceneList.size();
    }


    class SceneViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.scene_item_pic)
        ImageView SceneItemPic;
        @Bind(R.id.scene_item_tv)
        TextView SceneItemTv;
        @Bind(R.id.scene_item)
        LinearLayout Scene_item;
        public SceneViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
