package com.quootta.mdate.ui.fragment;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.base.BaseFragment;
import com.quootta.mdate.domain.RankingListData;
import com.quootta.mdate.engine.account.RankingListRequest;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.ui.adapter.RankingRicheBoardAdater;
import com.quootta.mdate.ui.adapter.RvLastRicheAdapter;
import com.quootta.mdate.ui.adapter.RvNullRankingAdapter;
import com.quootta.mdate.ui.dialog.MyProgressDialog;
import com.quootta.mdate.utils.GsonUtil;
import com.quootta.mdate.utils.LogUtil;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by Ryon on 2016/9/6/0006.
 * 排行榜 土豪榜
 */
public class MoneyListFragment extends BaseFragment {


    @Bind(R.id.ranking_image_pull)
    ImageView rankingImagePull;
    @Bind(R.id.ranking_changeTime)
    TextView rankingChangeTime;
    @Bind(R.id.ranking_open_popu)
    RelativeLayout rankingOpenPopu;
    @Bind(R.id.ranking_myranking)
    TextView rankingMyranking;
    @Bind(R.id.ranking_my_num)
    TextView rankingMyNum;
    @Bind(R.id.ranking_my_num2)
    TextView rankingMyNum2;
    @Bind(R.id.listRecy)
    RecyclerView listRecy;
    private Button popbt1;
    private ImageLoader imageLoader;
    private Button popbt2;
    private RankingListData rankingListData;
    private int ChangeTime = 0;
    private RankingRicheBoardAdater rankingRicheBoardAdater;
    private RvLastRicheAdapter rvLastRicheAdapter;
    private RvNullRankingAdapter rvNullRankingAdapter;
    private int list_size = 0;
    private int lastVisibleItem;
    //本周土豪榜
    private RankingListData.DataBean.ThisWeekBean.RicheBoardBean this_Week;

    private List<RankingListData> ranking_List;
    private List<RankingListData.DataBean.ThisWeekBean.RicheBoardBean.RichesBean> this_WeekList_Rich;
    private List<RankingListData.DataBean.LastWeekBean.RicheBoardBean.RichesBean> last_WeekList_Rich;
    //上周
    private RankingListData.DataBean.LastWeekBean.RicheBoardBean last_Week;


    private RequestQueue requestQueue;
    private Context baseContext;
    private PopupWindow popupWindow;
    private LinearLayoutManager linelayoutManager;
    private MyProgressDialog myProgressDialog;
    @Override
    protected int getRootView() {
        return R.layout.fragment_rankinglistcontent;
    }



    @Override
    protected void init() {
        requestQueue = BaseApp.getRequestQueue();
        imageLoader = BaseApp.getImageLoader();

    }


    @Override
    protected void initData(View view) {
        baseContext = view.getContext();
        myProgressDialog=new MyProgressDialog(baseContext);

        //创建下半部分item适配器
        this_WeekList_Rich = new ArrayList<>();
        last_WeekList_Rich=new ArrayList<>();

        rankingRicheBoardAdater=new RankingRicheBoardAdater(baseContext,this_WeekList_Rich);
        rvLastRicheAdapter=new RvLastRicheAdapter(baseContext,last_WeekList_Rich);
        rvNullRankingAdapter=new RvNullRankingAdapter(baseContext);

        //创建recyclerview的管理对象
        linelayoutManager = new LinearLayoutManager(baseContext);
        listRecy.setLayoutManager(linelayoutManager);

      //  if (this_WeekList_Rich!=null && this_WeekList_Rich.size()!=0){
            listRecy.setAdapter(rankingRicheBoardAdater);
//        }else {
//            listRecy.setAdapter(rvNullRankingAdapter);
//        }




        //设置popwindow下拉的布局
        View popView = LayoutInflater.from(baseContext).inflate(R.layout.fragment_rankingpopuwindow, null);
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        popbt1 = (Button) popView.findViewById(R.id.popbt1);
        popbt2 = (Button) popView.findViewById(R.id.popbt2);

        //打开popupWindow
        rankingOpenPopu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
                    popupWindow.showAsDropDown(v);

                }
            }
        });

        ReturnRankingData(ChangeTime);
        //单击本周排行
        popbt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeTime = 0;
                list_size=0;
                rankingChangeTime.setText(getString(R.string.app_tips_text104));

                this_WeekList_Rich.clear();

                listRecy.setAdapter(rankingRicheBoardAdater);
                //请求土豪榜排行数据
                ReturnRankingData(ChangeTime);
                popupWindow.dismiss();
            }
        });
        //单击上周排行
        popbt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeTime = 1;
                list_size=0;
                rankingChangeTime.setText(getString(R.string.app_tips_text105));
                last_WeekList_Rich.clear();

               // listRecy.setAdapter(rvLastRicheAdapter);
                listRecy.setAdapter(rvLastRicheAdapter);
                //请求土豪榜排行数据
                ReturnRankingData(ChangeTime);
                popupWindow.dismiss();
            }
        });
    }


    @Override
    protected void setListener() {
        //上拉加载
        listRecy.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && (lastVisibleItem + 1 == rankingRicheBoardAdater.getItemCount() || lastVisibleItem+1==rvLastRicheAdapter.getItemCount())) {
                    myProgressDialog.show();
                    list_size++;
                    ReturnRankingData(ChangeTime);
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linelayoutManager.findLastVisibleItemPosition();
            }
        });
    }



    public void ReturnRankingData(final int changeTime) {

        Map<String, String> RankMap = new HashMap<String, String>();
        RankMap.put("page",list_size+"");
        rankingListData = new RankingListData();

        //本周bean类地址
        this_Week = new RankingListData.DataBean.ThisWeekBean.RicheBoardBean();
        //上周bean类地址
        last_Week = new RankingListData.DataBean.LastWeekBean.RicheBoardBean();

        final RankingListRequest rankingListRequest = new RankingListRequest(RankMap, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                LogUtil.d("requestRankListDate", "response:" + response);
                myProgressDialog.dismiss();
                //解析数据
                rankingListData = GsonUtil.parse(response, RankingListData.class);


                rankingMyNum.setText(getString(R.string.app_tips_text109));

                EventBus.getDefault().post(rankingListData);


                switch (changeTime) {
                    //本周
                    case 0:
                        this_Week=rankingListData.getData().getThis_week().getRiche_board();

                        //把解析后的数据存到集合中
                        this_WeekList_Rich.addAll(this_Week.getRiches());
                        if (this_WeekList_Rich==null || this_WeekList_Rich.size()<=0){
                            last_WeekList_Rich.clear();
                            listRecy.setAdapter(rvNullRankingAdapter);

                        }
                        //数据有变化更新
                        rankingRicheBoardAdater.notifyDataSetChanged();

                        //排行 排名 土豪值
                       if (rankingListData != null) {
                            //设置我的排名

                           if (this_Week.getRange() != null) {

                                Double d=(Double)this_Week.getRange();
                               int i=d.intValue();
                                rankingMyranking.setText(i+1+"");
                            } else {
                                rankingMyranking.setText("--");
                            }

                            //设置我的土豪值
                            if (this_Week.getScore() != null) {
                                rankingMyNum2.setText(this_Week.getScore().toString().split("\\.")[0]);

                            } else {
                                rankingMyNum2.setText("--");
                            }
                        }

                        break;
                   //上周
                    case 1:
                        last_Week=rankingListData.getData().getLast_week().getRiche_board();
                    //把解析后的数据存到集合中
                       last_WeekList_Rich.addAll(last_Week.getRiches());
                        if (last_WeekList_Rich==null || last_WeekList_Rich.size()<=0){
                            this_WeekList_Rich.clear();
                            listRecy.setAdapter(rvNullRankingAdapter);
                        }
                    //数据有变化更新
                      rvLastRicheAdapter.notifyDataSetChanged();


                    //排行 排名 土豪值
                    if (rankingListData != null) {
                        //设置我的排名

                        if (last_Week.getRange() != null) {

                            Double d=(Double)last_Week.getRange();
                            int i=d.intValue();
                            rankingMyranking.setText(i+1+"");
                        } else {
                            rankingMyranking.setText("--");
                        }

                        //设置我的土豪值
                        if (last_Week.getScore() != null) {
                            rankingMyNum2.setText(last_Week.getScore().toString().split("\\.")[0]);

                        } else {
                            rankingMyNum2.setText("--");
                        }
                    }
                        break;

                }


            }
        });

        requestQueue.add(rankingListRequest);
    }


}
