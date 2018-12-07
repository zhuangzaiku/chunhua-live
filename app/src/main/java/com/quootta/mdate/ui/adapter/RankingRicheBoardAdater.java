package com.quootta.mdate.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.domain.RankingListData;
import com.quootta.mdate.ui.activity.PersonalDetailsActivity;
import com.quootta.mdate.ui.view.CircleImageView;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;



/**
 * Created by Ryon on 2016/9/7/0007.
 */
public class RankingRicheBoardAdater extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private Context context;
    private List<RankingListData.DataBean.ThisWeekBean.RicheBoardBean.RichesBean> ThisWeek_Riches;
    //    private List<RankingListData.DataBean.LastWeekBean.RicheBoardBean.RichesBean> LastWeek_Riche;
//    private List<RankingListData.DataBean.ThisWeekBean.CharmBoardBean.CharmsBean> ThisWeek_Charms;
//    private List<RankingListData.DataBean.LastWeekBean.CharmBoardBean.CharmsBean> LastWeek_Charms;
    private ImageLoader imageLoader;

    public RankingRicheBoardAdater(Context context, List<RankingListData.DataBean.ThisWeekBean.RicheBoardBean.RichesBean> ThisWeek_Riches) {
        this.context = context;
        this.ThisWeek_Riches = ThisWeek_Riches;
        //  beanList.addAll(ThisWeek_Riches);
        imageLoader = BaseApp.getImageLoader();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == 1) {
            //采用头部布局
            View topView = LayoutInflater.from(context).inflate(R.layout.item_rankinglisttop, parent, false);
            TopItemViewHolder holde = new TopItemViewHolder(topView);
            return holde;
        } else if (viewType==2){
            //item布局
            View view = LayoutInflater.from(context).inflate(R.layout.item_rankinglist, parent, false);
            RichBoardViewHolder holder = new RichBoardViewHolder(view);
            return holder;
        }else {

            return new TopItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_rankinglisttop,parent,false)) ;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if (holder instanceof RichBoardViewHolder) {
            //加载底部item的数据
            position = position + 2;

            //土豪值
         //   ((RichBoardViewHolder) holder).rankingItemDiademNum.setText(ThisWeek_Riches.get(position).getRiche_score().split("\\.")[0]);
            //昵称
            ((RichBoardViewHolder) holder).rankingItemName.setText(ThisWeek_Riches.get(position).getNick_name());
            //年龄
            ((RichBoardViewHolder) holder).rankingItemAge.setText(ThisWeek_Riches.get(position).getAge() + "");

            //判断性别
            if (ThisWeek_Riches.get(position).getGender().equals("male")) {
                ((RichBoardViewHolder) holder).rankingItemSex.setImageResource(R.drawable.ranking_sexb);
            } else {
                ((RichBoardViewHolder) holder).rankingItemSex.setImageResource(R.drawable.rankingsexg);
            }
            //排名
            ((RichBoardViewHolder) holder).rankingItemNum.setText(ThisWeek_Riches.get(position).getRiche_range() + 1 + "");
            //加载图片
            imageLoader.get(LocalUrl.getPicUrl(ThisWeek_Riches.get(position).getAvatar()),
                    imageLoader.getImageListener(((RichBoardViewHolder) holder).rankingItemImage, R.mipmap.home_show_loading, R.mipmap.home_show_loading));
        } else if (holder instanceof TopItemViewHolder) {

            //第一名
            if (ThisWeek_Riches.size() >= 1) {
                //昵称
                ((TopItemViewHolder) holder).rankingName1.setText(ThisWeek_Riches.get(0).getNick_name());
                //添加年龄数据
                if (ThisWeek_Riches.get(0).getAge() == -1) {
                    ((TopItemViewHolder) holder).rankingAge1.setText("18");
                } else {
                    ((TopItemViewHolder) holder).rankingAge1.setText(ThisWeek_Riches.get(0).getAge() + "");
                }

                //判断性别
                if (ThisWeek_Riches.get(0).getGender().equals("male")) {
                    ((TopItemViewHolder) holder).rankingSex1.setImageResource(R.drawable.ranking_sexb);
                } else {
                    ((TopItemViewHolder) holder).rankingSex1.setImageResource(R.drawable.rankingsexg);
                }
                //土豪值
              //  ((TopItemViewHolder) holder).richeScore1.setText(ThisWeek_Riches.get(0).getRiche_score().split("\\.")[0]);

                //添加图片
                imageLoader.get(LocalUrl.getPicUrl(ThisWeek_Riches.get(0).getAvatar()),
                        imageLoader.getImageListener(((TopItemViewHolder) holder).rankingImage1, R.mipmap.home_show_loading, R.mipmap.home_show_loading));


                //第二名
                if (ThisWeek_Riches.size() >= 2) {

                    //添加年龄数据
                    if (ThisWeek_Riches.get(1).getAge() == -1) {
                        ((TopItemViewHolder) holder).rankingAge2.setText("18");
                    } else {
                        ((TopItemViewHolder) holder).rankingAge2.setText(ThisWeek_Riches.get(1).getAge() + "");
                    }
                    //判断性别
                    if (ThisWeek_Riches.get(1).getGender().equals("male")) {
                        ((TopItemViewHolder) holder).rankingSex2.setImageResource(R.drawable.ranking_sexb);
                    } else {
                        ((TopItemViewHolder) holder).rankingSex2.setImageResource(R.drawable.rankingsexg);
                    }
                    //昵称
                    ((TopItemViewHolder) holder).rankingName2.setText(ThisWeek_Riches.get(1).getNick_name());

                    //土豪值
                  //  ((TopItemViewHolder) holder).richeScore2.setText(ThisWeek_Riches.get(1).getRiche_score().split("\\.")[0]);

                    //添加图片
                    imageLoader.get(LocalUrl.getPicUrl(ThisWeek_Riches.get(1).getAvatar()),
                            imageLoader.getImageListener(((TopItemViewHolder) holder).rankingImage2, R.mipmap.home_show_loading, R.mipmap.home_show_loading));


                    //第三名
                    if (ThisWeek_Riches.size() >= 3) {

                        //添加年龄数据
                        if (ThisWeek_Riches.get(2).getAge() == -1) {
                            ((TopItemViewHolder) holder).rankingAge3.setText("18");
                        } else {
                            ((TopItemViewHolder) holder).rankingAge3.setText(ThisWeek_Riches.get(2).getAge() + "");
                        }

                        //判断性别
                        if (ThisWeek_Riches.get(2).getGender().equals("male")) {
                            ((TopItemViewHolder) holder).rankingSex3.setImageResource(R.drawable.ranking_sexb);
                        } else {
                            ((TopItemViewHolder) holder).rankingSex3.setImageResource(R.drawable.rankingsexg);
                        }

                        //昵称
                        ((TopItemViewHolder) holder).rankingName3.setText(ThisWeek_Riches.get(2).getNick_name());

                        //土豪值
                      //  ((TopItemViewHolder) holder).richeScore3.setText(ThisWeek_Riches.get(2).getRiche_score().split("\\.")[0]);
                        //添加图片
                        imageLoader.get(LocalUrl.getPicUrl(ThisWeek_Riches.get(2).getAvatar()),
                                imageLoader.getImageListener(((TopItemViewHolder) holder).rankingImage3, R.mipmap.home_show_loading, R.mipmap.home_show_loading));

                    }

                }
            }
        }
    }


    @Override
    public int getItemCount() {
        if (ThisWeek_Riches.size()>=100){
            return 98;//4-2
        }else {
            return ThisWeek_Riches.size() > 3 ? ThisWeek_Riches.size() - 2 : 1;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 1;
        } else {
            return 2;
        }

    }

    class TopItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ranking_image3)
        CircleImageView rankingImage3;
        @Bind(R.id.ranking_name_3)
        TextView rankingName3;
        @Bind(R.id.ranking_sex_3)
        ImageView rankingSex3;
        @Bind(R.id.ranking_age_3)
        TextView rankingAge3;
//        @Bind(R.id.ranking_diadem_3)
//        ImageView rankingDiadem3;
        @Bind(R.id.ranking_image1)
        CircleImageView rankingImage1;
        @Bind(R.id.ranking_name_1)
        TextView rankingName1;
        @Bind(R.id.ranking_sex_1)
        ImageView rankingSex1;
        @Bind(R.id.ranking_age_1)
        TextView rankingAge1;
//        @Bind(R.id.ranking_diadem_1)
//        ImageView rankingDiadem1;
//        @Bind(R.id.riche_score3)
//        TextView richeScore3;
//        @Bind(R.id.riche_score1)
//        TextView richeScore1;
//        @Bind(R.id.riche_score2)
//        TextView richeScore2;
        @Bind(R.id.ranking_one)
        LinearLayout rankingOne;
        @Bind(R.id.ranking_image2)
        CircleImageView rankingImage2;
        @Bind(R.id.ranking_name_2)
        TextView rankingName2;
        @Bind(R.id.ranking_sex_2)
        ImageView rankingSex2;
        @Bind(R.id.ranking_age_2)
        TextView rankingAge2;
//        @Bind(R.id.ranking_diadem_2)
//        ImageView rankingDiadem2;
        @Bind(R.id.ranking_three)
        LinearLayout rankingThree;
        @Bind(R.id.ranking_two)
        LinearLayout rankingTwo;
        public TopItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //单机第一个item
            rankingOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PersonalDetailsActivity.class);

                    intent.putExtra("user_id", ThisWeek_Riches.get(0).get_id());
                    context.startActivity(intent);
                }
            });
            //单击第四个item
            rankingTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PersonalDetailsActivity.class);

                    intent.putExtra("user_id", ThisWeek_Riches.get(1).get_id());
                    context.startActivity(intent);
                }
            });
            //单击第三个item
            rankingThree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PersonalDetailsActivity.class);

                    intent.putExtra("user_id", ThisWeek_Riches.get(2).get_id());
                    context.startActivity(intent);
                }
            });

        }
    }


    /**
     * 底部加载更多
     */
    class FooterViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.rcv_load_more)
        ProgressBar rcvLoadMore;

        public FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class RichBoardViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ranking_item_num)
        TextView rankingItemNum;
        @Bind(R.id.ranking_item_image)
        CircleImageView rankingItemImage;
        @Bind(R.id.ranking_item_name)
        TextView rankingItemName;
        @Bind(R.id.ranking_item_sex)
        ImageView rankingItemSex;
        @Bind(R.id.ranking_item_age)
        TextView rankingItemAge;
//        @Bind(R.id.ranking_item_diadem)
//        ImageView rankingItemDiadem;
//        @Bind(R.id.ranking_item_diadem_num)
//        TextView rankingItemDiademNum;
        @Bind(R.id.ranking_item)
        LinearLayout rankingItem;

        public RichBoardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            rankingItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PersonalDetailsActivity.class);

                    intent.putExtra("user_id", ThisWeek_Riches.get(getLayoutPosition() + 2).get_id());
                    context.startActivity(intent);
                }
            });
        }
    }
}
