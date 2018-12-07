package com.quootta.mdate.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.base.BaseFragment;
import com.quootta.mdate.constant.LocalConfig;
import com.quootta.mdate.domain.BannerList;
import com.quootta.mdate.domain.HotList;
import com.quootta.mdate.domain.InfoDetail;
import com.quootta.mdate.domain.UserList;
import com.quootta.mdate.engine.invite.UserListRequest;
import com.quootta.mdate.engine.media.GroupCallRequest;
import com.quootta.mdate.engine.myCenter.BannerRequest;
import com.quootta.mdate.engine.myCenter.FavoriteRequest;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.ui.activity.ChargeActivity;
import com.quootta.mdate.ui.activity.MainActivity;
import com.quootta.mdate.ui.activity.PersonalDetailsActivity;
import com.quootta.mdate.ui.activity.VipActivity;
import com.quootta.mdate.ui.adapter.RvHotListAdapter;
import com.quootta.mdate.ui.dialog.GroupCallDialog;
import com.quootta.mdate.ui.dialog.MyMessageDialog;
import com.quootta.mdate.ui.dialog.MyProgressDialog;
import com.quootta.mdate.utils.ActivityUtil;
import com.quootta.mdate.utils.GsonUtil;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.android.volley.RequestQueue;
import com.melnykov.fab.FloatingActionButton;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

import static com.quootta.mdate.ui.activity.MainActivity.meFragment;


public class HotListFragment extends BaseFragment {

    @Bind(R.id.srl_discover)
    SwipeRefreshLayout srlDiscover ;
    @Bind(R.id.rv_hot)
    RecyclerView rvHot;
    @Bind(R.id.fab_group_send)
    FloatingActionButton fabSend;
    @Bind(R.id.hotLoading)
    ImageView hotLoading;

    private final static int ADD = 0;
    private final static int REMOVE = 1;
    public final int CHARGE_VIP = 3;
    private final int RESULT_SUCCESS = 4;
    private Context baseContext;
    private RequestQueue requestQueue;
    private SharedPreferences sp;
    private RvHotListAdapter hotListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private int lastVisibleItem;
    public UserList userList;
    private BannerList bannerList;
    //private RankingListData rankingListData;
    private List<List<String>> rankingImgs;
    private List<HotList.DataBean.SceneBean> sceneList;
    private Map<String,String> paramsMap;
    private int page = 0;
    private int currentRequestType;
    private MainActivity currentMainActivity;
    private boolean isFirstRequestBanner = true;
    MyProgressDialog myProgressDialog;
    private final String TAG = "HotListFragment";
    @Override
    protected void init() {
        currentMainActivity  = (MainActivity) getActivity();
        requestQueue = BaseApp.getRequestQueue();


    }

    @Override
    protected int getRootView() {
        return R.layout.fragment_hot_list;
    }

    @Override
    protected void initData(View view) {
        baseContext = view.getContext();
        sp = baseContext.getSharedPreferences("login", BaseActivity.MODE_PRIVATE);
        paramsMap = new HashMap();
        paramsMap.put("page", page + "");

        myProgressDialog=new MyProgressDialog(baseContext);

        //加载bannner
        requestBannerData();

        hotLoading.setVisibility(View.VISIBLE);

        ObjectAnimator animator=ObjectAnimator.ofFloat(hotLoading,"rotation",0f,360f);
        animator.setDuration(1000);
        animator.setRepeatCount(-1);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.start();

//        doRefresh();

    }



    private void requestBannerData() {
        Log.i("tag","进入热门加载数据---1");
        String channel = ActivityUtil.getChannelString(getActivity());
        final Map<String, String> bannerMap = new HashMap<String, String>();
        bannerMap.put("name", channel);
        BannerRequest bannerRequest = new BannerRequest(bannerMap, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                Log.i("tag","进入热门加载数据---2");
                Log.i("tag", "bana---->response:---->" + response);
                try {
                    bannerList = GsonUtil.parse(response.getString("data"), BannerList.class);
                    Log.i("tag","---bannerlsit.size--->"+bannerList.banner.size());

                    int i;
                    for ( i=0;i<bannerList.banner.size();i++){

                        Log.i("tag","---i-->"+bannerList.banner.get(i).id);
                        //当当前时间大于结束的时间小于开始的时间时 移除banner
                        Log.i("tag","---结束时间-"+bannerList.banner.get(i).end_time+"-----开始时间-->"+bannerList.banner.get(i).begin_time+"---system->"+System.currentTimeMillis());

                        if (Long.parseLong(bannerList.banner.get(i).end_time) <= System.currentTimeMillis()||Long.parseLong(bannerList.banner.get(i).begin_time)>= System.currentTimeMillis() ){
                            bannerList.banner.remove(i);
                            Log.i("tag","移除了第"+i+"个");
                        }
                    }

                    //加载热门数据
                    doRefresh();

                    // requestRankingList();
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

            @Override
            protected void onFail(JSONObject response) {
                super.onFail(response);
                doRefresh();
            }
        });
        requestQueue.add(bannerRequest);
    }

//    private void requestRankingList() {
//        final Map<String, String> rankingMap = new HashMap<>();
//        Log.i("tag","进入热门加载数据---3");
//        RankingListRequest rankingListRequest = new RankingListRequest(rankingMap, new VolleyListener() {
//            @Override
//            protected void onSuccess(JSONObject response) {
//                LogUtil.d(TAG, "ranking res:" + response.toString());
//                Log.i("tag","进入热门加载数据---4");
//                //解析数据
//                rankingListData = GsonUtil.parse(response, RankingListData.class);
//
//                initRankingList();
//            }
//        });
//        requestQueue.add(rankingListRequest);
//    }

//    private void initRankingList() {
//        Log.i("tag","进入热门加载数据---5");
//        RankingListData.DataBean.ThisWeekBean thisWeekBean = rankingListData.getData().getThis_week();
//        RankingListData.DataBean.ThisWeekBean.CharmBoardBean charmBoardBean = thisWeekBean.getCharm_board();
//        RankingListData.DataBean.ThisWeekBean.RicheBoardBean richeBoardBean = thisWeekBean.getRiche_board();
//        List<RankingListData.DataBean.ThisWeekBean.CharmBoardBean.CharmsBean> charmsBeanList = charmBoardBean.getCharms();
//        List<RankingListData.DataBean.ThisWeekBean.RicheBoardBean.RichesBean> richesBeanList = richeBoardBean.getRiches();
//
//        rankingImgs = new ArrayList<>();
//        List<String> charmImgs = new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//            try {
//                if (charmsBeanList.get(i) == null) {
//                    charmImgs.add(i, "null");
//                    continue;
//                }
//            } catch (IndexOutOfBoundsException e) {
//                charmImgs.add(i, "null");
//                continue;
//            }
//            charmImgs.add(i, charmsBeanList.get(i).getAvatar());
//            Log.i("tag","--------->"+charmsBeanList.get(i).getNick_name());
//        }
//
//        rankingImgs.add(0,charmImgs);
//
//        List<String> richesImgs = new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//            try {
//                if (richesBeanList.get(i) == null) {
//                    richesImgs.add(i, "null");
//                    continue;
//                }
//            } catch (IndexOutOfBoundsException e) {
//                richesImgs.add(i, "null");
//                continue;
//            }
//            richesImgs.add(i, richesBeanList.get(i).getAvatar());
//        }
//        rankingImgs.add(1, richesImgs);
//
//        doRefresh();
//    }

    @Override
    protected void setListener() {
        //下拉刷新
        srlDiscover.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                paramsMap.put("page", page + "");
                LogUtil.d(TAG, "onRefresh" + paramsMap);
                doRefresh();
            }
        });

        //上拉加载
        rvHot.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //SCROLL_STATE_DRAGGING  和   SCROLL_STATE_IDLE 两种效果自己看着来
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == hotListAdapter.getItemCount()) {
                    LogUtil.d(TAG, "onScrollStateChanged:" + paramsMap);
                    //  srlDiscover.setRefreshing(true);
                    myProgressDialog.show();
                    paramsMap.put("page", "" + (++page));
                    doRefresh();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    private void doRefresh() {
        Log.i("tag","进入热门加载数据---6");
        srlDiscover.setRefreshing(false);
        UserListRequest userListRequest = new UserListRequest(paramsMap, UserListRequest.POPULAR, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                myProgressDialog.dismiss();


                HotList hotList = new HotList();

                //隐藏动画
                hotLoading.setVisibility(View.GONE);
                 LogUtil.i("tag","热门加载数据------>"+response);

                hotList = GsonUtil.parse(response, HotList.class);

                freshRecyclerView(hotList);
            }
        });
        if (requestQueue == null){
            requestQueue = BaseApp.getRequestQueue();
        }
        requestQueue.add(userListRequest);

    }

    private void freshRecyclerView(HotList hotList) {
        if(page == 0) {//当前是初始化页面或者下拉刷新状态
            LogUtil.d(TAG, "freshRecyclerView:page == 0");
            userList=new UserList();
            userList.users=hotList.getData().getUsers();
            if(hotList.getData().getUsers().size() == 0) {
                LogUtil.d(TAG, "freshRecyclerView:no content");
                rvHot.setVisibility(View.GONE);
            } else {
                rvHot.setVisibility(View.VISIBLE);
                Log.i("tag","进入热门加载数据---8");
                initRecyclerView(hotList);
            }
        } else {//当前是上拉加载状态
            if (hotList.getData().getUsers().size() == 0) {
                ToastUtil.showToast(getString(R.string.app_tips_text15));
            } else {

                //移除重复出现的用户
                //  List<UserList.users> hot=hotList.getData().getUsers();

                List<UserList.users> delehot=new ArrayList<>();

                for (int i=0;i<hotList.getData().getUsers().size();i++){

                    for (int j=0;j<userList.users.size();j++){

                        if (hotList.getData().getUsers().get(i)._id.equals(userList.users.get(j)._id)){

//                            UserList.users hots=hot.get(i);
//                            hot.remove(hots);
                            //把所有需要移除的放到一个集合中
                            delehot.add(hotList.getData().getUsers().get(i));

                            Log.i("tag","hotList移除一项--");
                        }
                    }

                }


                //从hotlist中移除
                hotList.getData().getUsers().removeAll(delehot);


                userList.users.addAll(hotList.getData().getUsers());

                hotListAdapter.notifyDataSetChanged();

            }
        }
        srlDiscover.setRefreshing(false);

    }

    private void initRecyclerView(final HotList hotList) {

        //绑定Adapter
        hotListAdapter = new RvHotListAdapter(baseContext, hotList.getData().getUsers());

        //分类数据
        sceneList=new ArrayList<>();
        if (hotList.getData().getScene()!=null){

            sceneList.addAll(hotList.getData().getScene());
        }

        //为了兼容以前的适配器 排行榜数据直接放到集合中  以后改版直接重新写
        rankingImgs=new ArrayList<>();
        List<String> charmImgs = new ArrayList<>();
        List<String> richImgs=new ArrayList<>();

        //初始化排行榜三个位置
        for (int i=0;i<3;i++){
            richImgs.add(i,"null");
            charmImgs.add(i,"null");
        }

        for (int i=0;i<hotList.getData().getRiche().size();i++){
            richImgs.add(i, hotList.getData().getRiche().get(i).getAvatar());
        }
        for (int i=0;i<hotList.getData().getCharm().size();i++){
            charmImgs.add(i,hotList.getData().getCharm().get(i).getAvatar());
        }


        rankingImgs.add(0,charmImgs);
        rankingImgs.add(1,richImgs);



        gridLayoutManager = new GridLayoutManager(baseContext, 2);
        rvHot.setLayoutManager(gridLayoutManager);
        rvHot.setAdapter(hotListAdapter);

        attachFabButton();

        //传入Banner数据

        if (bannerList!=null &&  bannerList.banner.size() >0 && rankingImgs!=null
                && rankingImgs.size()>0 && sceneList!=null && sceneList.size()>0)
        {
            View header =  LayoutInflater.from(baseContext).inflate(R.layout.banner_hot, rvHot, false);
            hotListAdapter.setHeaderView(header);
            hotListAdapter.setBannerList(bannerList.banner);
            hotListAdapter.setBoardList(rankingImgs);
            //热门页面分类
            hotListAdapter.setUserScene(sceneList);
        }



        hotListAdapter.setOnLikeListener(new RvHotListAdapter.OnLikeListener() {
            @Override
            public void onLike(int position, boolean isLike) {
                if (position != 0){
                    position = position -1;
                }
                requestFavorite(hotList.getData().getUsers().get(position)._id, isLike);
            }
        });

        hotListAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (hotList.getData().getUsers().size() != 0) {
                    Intent detailIntent = new Intent(getActivity(), PersonalDetailsActivity.class);
                    detailIntent.putExtra("user_id", hotList.getData().getUsers().get(position)._id);
                    startActivity(detailIntent);
                }
            }
        });
    }

    private void attachFabButton() {
        fabSend.attachToRecyclerView(rvHot);
        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupCallDialog groupCallDialog = new GroupCallDialog(baseContext, new GroupCallDialog.OnGroupClickListener() {
                    @Override
                    public void onGoldClick(String gender) {
                        if (BaseApp.getGoldCount() >= LocalConfig.GROUP_CALL_PRIZE) {
                            Map<String, String> paramsMap = new HashMap<String, String>();
                            paramsMap.put("gender", gender);
                            paramsMap.put("num", LocalConfig.GROUP_CALL_PRIZE + "");
                            GroupCallRequest callRequest = new GroupCallRequest(paramsMap, new VolleyListener() {
                                @Override
                                protected void onSuccess(JSONObject response) {

                                    MyMessageDialog messageDialog = new MyMessageDialog(baseContext, getString(R.string.app_tips_text44), getString(R.string.app_tips_text107));
                                    messageDialog.show();

                                }

                            });
                            requestQueue.add(callRequest);
                        } else {
                            ToastUtil.showToast(getString(R.string.remain_not_enough));
                            // TODO: 2016/11/10 跳到金币充值页面
                            Intent chargeIntent = new Intent(baseContext, ChargeActivity.class);
                            baseContext.startActivity(chargeIntent);
                        }
                    }

                    @Override
                    public void onVipBtnClick(String gender) {
                        if (Boolean.parseBoolean(InfoDetail.is_vip)) {
                            Map<String, String> paramsMap = new HashMap<String, String>();
                            paramsMap.put("gender", gender);
                            paramsMap.put("num", 0 + "");
                            GroupCallRequest callRequest = new GroupCallRequest(paramsMap, new VolleyListener() {
                                @Override
                                protected void onSuccess(JSONObject response) {

                                    MyMessageDialog messageDialog = new MyMessageDialog(baseContext, getString(R.string.app_tips_text44), getString(R.string.app_tips_text107));
                                    messageDialog.show();

                                }

                            });
                            requestQueue.add(callRequest);
                        } else {
                            ToastUtil.showToast(getString(R.string.you_are_not_vip));
                            // TODO: 2016/11/10 跳到会员购买页面
                            Intent vipIntent = new Intent(baseContext, VipActivity.class);
                            startActivityForResult(vipIntent, CHARGE_VIP);
                        }
                    }
                });
                groupCallDialog.show();
            }
        });

    }

    private void requestFavorite(String id, boolean isLike) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", id);
        FavoriteRequest favoriteRequest = new FavoriteRequest(map, isLike ? ADD : REMOVE, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                try {
                    ToastUtil.showToast(response.getString("msg"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        requestQueue.add(favoriteRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("tag","hot-requestcode--->"+requestCode+"--hot--resultcode-->"+resultCode);

        switch (requestCode){
            case CHARGE_VIP:
                if (resultCode==RESULT_SUCCESS){

                    Log.i("tag","执行一次 main");
                    meFragment.requestBaseInfoDetail();

                }

                break;
        }


    }
}
