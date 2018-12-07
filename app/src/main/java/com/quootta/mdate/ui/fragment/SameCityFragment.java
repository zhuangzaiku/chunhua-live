package com.quootta.mdate.ui.fragment;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.base.BaseFragment;
import com.quootta.mdate.constant.LocalConfig;
import com.quootta.mdate.domain.BannerList;
import com.quootta.mdate.domain.InfoDetail;
import com.quootta.mdate.domain.LocationDate;
import com.quootta.mdate.domain.UserList;
import com.quootta.mdate.engine.invite.UserListRequest;
import com.quootta.mdate.engine.media.GroupCallRequest;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.ui.activity.ChargeActivity;
import com.quootta.mdate.ui.activity.MainActivity;
import com.quootta.mdate.ui.activity.PersonalDetailsActivity;
import com.quootta.mdate.ui.activity.VipActivity;
import com.quootta.mdate.ui.adapter.RvSameCityAdapter;
import com.quootta.mdate.ui.dialog.GroupCallDialog;
import com.quootta.mdate.ui.dialog.MyMessageDialog;
import com.quootta.mdate.utils.DBUtil;
import com.quootta.mdate.utils.GsonUtil;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.android.volley.RequestQueue;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * A simple {@link Fragment} subclass.
 */
public class SameCityFragment extends BaseFragment {


    @Bind(R.id.rv_same_city)
    RecyclerView rvSameCity;
    @Bind(R.id.srl_discover)
    SwipeRefreshLayout srlDiscover;
    @Bind(R.id.fab_group_send)
    FloatingActionButton fabSend;
//    @Bind(R.id.SameCity_loadView)
//    LoadingView loadingView;
    @Bind(R.id.cityLoading)
    ImageView cityloading;


    private final static int ADD = 0;
    private final static int REMOVE = 1;
    public final int CHARGE_VIP = 3;

    private Context baseContext;
    private RequestQueue requestQueue;
    private SharedPreferences sp;
    private RvSameCityAdapter sameCityAdapter;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private int lastVisibleItem;
    public UserList userList;
    private BannerList bannerList;
    private Map<String,String> paramsMap;
    private int page = 0;
    private int currentRequestType;
    private MainActivity currentMainActivity;
    private boolean isFirstRequestBanner = true;

    private final String TAG = "SameCityFragment";
    private double latitude;//纬度
    private double longitude;//经度
    private boolean isVisible=false;
    private boolean isLoadData=false;
    private boolean isViewInit=false;
    @Override
    protected void init() {
        currentMainActivity  = (MainActivity) getActivity();
        requestQueue = BaseApp.getRequestQueue();


        LocationDate locationInfo = (LocationDate) DBUtil.getBeanFromPrefences(DBUtil.LOCATION_DATE);
        if (locationInfo != null){
            latitude= locationInfo.getLatitude();
            longitude= locationInfo.getLongitude();
        }else {
            latitude= 0;
            longitude= 0;
        }


    }

    @Override
    protected int getRootView() {
        return R.layout.fragment_same_city;
    }

    @Override
    protected void initData(View view) {
        baseContext = view.getContext();
        sp = baseContext.getSharedPreferences("login", BaseActivity.MODE_PRIVATE);
        paramsMap = new HashMap();
        paramsMap.put("page", page + "");

        //首先oncreate 再懒加载
        isViewInit=true;
    }

    //懒加载
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible=isVisibleToUser;
        if (getUserVisibleHint()){
            preLoadData(false);

        }
    }
    private void preLoadData(boolean forceLoad){
        if (isViewInit && isVisible && (!isLoadData || forceLoad)){
            //显示loadingview动画
//            loadingView.setVisibility(View.VISIBLE);
//            loadingView.setLoadingText("Loading...");

            cityloading.setVisibility(View.VISIBLE);
            ObjectAnimator animator=ObjectAnimator.ofFloat(cityloading,"rotation",0f,360f);
            animator.setDuration(1000);
            animator.setRepeatCount(-1);
            animator.setRepeatMode(ValueAnimator.RESTART);
            animator.start();

            requestBannerData();
            isLoadData=true;
        }
    }


    private void requestBannerData() {
        doRefresh();
    }

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
        rvSameCity.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //SCROLL_STATE_DRAGGING  和   SCROLL_STATE_IDLE 两种效果自己看着来
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == sameCityAdapter.getItemCount()) {
                    LogUtil.d(TAG, "onScrollStateChanged:" + paramsMap);
                    srlDiscover.setRefreshing(true);
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
        srlDiscover.setRefreshing(false);

        final Map<String, String> tempMap = new HashMap<>(paramsMap);
//        LogUtil.d(TAG, "tempMap outside:" + tempMap);
        tempMap.put("lat",latitude+"");
        tempMap.put("lng",longitude+"");

        UserListRequest userListRequest = new UserListRequest(tempMap, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {

                cityloading.setVisibility(View.GONE);

                LogUtil.i(TAG, "tempMap inside:" + paramsMap);
                LogUtil.i(TAG, "onSuccess:" + response.toString());
                UserList list = new UserList();
                try {
                    list = GsonUtil.parse(response.getString("data"), UserList.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                freshRecyclerView(list);
            }
        });
        if (requestQueue == null){
            requestQueue = BaseApp.getRequestQueue();
        }
        requestQueue.add(userListRequest);

    }

    private void freshRecyclerView(UserList list) {
        if(page == 0) {//当前是初始化页面或者下拉刷新状态
            LogUtil.d(TAG, "freshRecyclerView:page == 0");
            userList = list;
            if(userList.users.size() == 0) {
                LogUtil.d(TAG, "freshRecyclerView:no content");
                rvSameCity.setVisibility(View.GONE);
            } else {
                rvSameCity.setVisibility(View.VISIBLE);
                initRecyclerView(list);
            }
        } else {//当前是上拉加载状态
            if (list.users.size() == 0) {
                ToastUtil.showToast("没有更多数据啦");
            } else {
                userList.users.addAll(list.users);
                sameCityAdapter.notifyDataSetChanged();
            }
        }
        srlDiscover.setRefreshing(false);

    }

    private void initRecyclerView(UserList list) {
        //绑定Adapter
        sameCityAdapter = new RvSameCityAdapter(baseContext, userList.users);
        gridLayoutManager = new GridLayoutManager(baseContext, 3);
        rvSameCity.setLayoutManager(gridLayoutManager);
        rvSameCity.setAdapter(sameCityAdapter);

        attachFabButton();

        sameCityAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (userList.users.size() != 0) {
                    Intent detailIntent = new Intent(getActivity(), PersonalDetailsActivity.class);
                    detailIntent.putExtra("user_id", userList.users.get(position)._id);
                    startActivity(detailIntent);
                }
            }
        });
    }

    private void attachFabButton() {
        fabSend.attachToRecyclerView(rvSameCity);
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
}
