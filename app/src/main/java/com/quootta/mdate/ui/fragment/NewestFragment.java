package com.quootta.mdate.ui.fragment;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.base.BaseFragment;
import com.quootta.mdate.constant.LocalConfig;
import com.quootta.mdate.domain.CallList;
import com.quootta.mdate.domain.InfoDetail;
import com.quootta.mdate.domain.UserList;
import com.quootta.mdate.engine.invite.CallListRequest;
import com.quootta.mdate.engine.invite.UserListRequest;
import com.quootta.mdate.engine.media.GroupCallRequest;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.ui.activity.ChargeActivity;
import com.quootta.mdate.ui.activity.MainActivity;
import com.quootta.mdate.ui.activity.PersonalDetailsActivity;
import com.quootta.mdate.ui.activity.VipActivity;
import com.quootta.mdate.ui.adapter.RvNewestAdapter;
import com.quootta.mdate.ui.dialog.GroupCallDialog;
import com.quootta.mdate.ui.dialog.MyMessageDialog;
import com.quootta.mdate.utils.GsonUtil;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.android.volley.RequestQueue;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

public class NewestFragment extends BaseFragment {


    @Bind(R.id.rv_newest)
    RecyclerView rvNewest;
    @Bind(R.id.srl_discover)
    SwipeRefreshLayout srlDiscover;
    @Bind(R.id.fab_group_send)
    FloatingActionButton fabSend;
    @Bind(R.id.newLoading)
    ImageView newloading;

    private final static int ADD = 0;
    private final static int REMOVE = 1;
    public final int CHARGE_VIP = 3;

    private Context baseContext;
    private RequestQueue requestQueue;
    private SharedPreferences sp;
    private RvNewestAdapter newestAdapter;
    private GridLayoutManager gridLayoutManager;
    private int lastVisibleItem;
    public UserList userList;
//    private List<List<CallListBean>> callBeanList;
    private CallList callList;
    private List<List<String>> callImgsList;
    private Map<String,String> paramsMap;
    private int page = 0;
    private int currentRequestType;
    private MainActivity currentMainActivity;
    private boolean isFirstRequestBanner = true;

    private final String TAG = "NewestFragment";

    private boolean isVisible=false;
    private boolean isLoadData=false;
    private boolean isViewInit=false;
    private boolean isRefreshing=false;

    @Override
    protected void init() {



        currentMainActivity  = (MainActivity) getActivity();
        requestQueue = BaseApp.getRequestQueue();

    }

    @Override
    protected int getRootView() {
        return R.layout.fragment_newest;
    }

    @Override
    protected void initData(View view) {
        baseContext = view.getContext();
        sp = baseContext.getSharedPreferences("login", BaseActivity.MODE_PRIVATE);
        paramsMap = new HashMap();
        paramsMap.put("page", page + "");

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

            //加载数据
            requestBannerData();
            isLoadData=true;
        }
    }

    private void requestBannerData() {

        newloading.setVisibility(View.VISIBLE);
        ObjectAnimator animator=ObjectAnimator.ofFloat(newloading,"rotation",0f,360f);
        animator.setDuration(1000);
        animator.setRepeatCount(-1);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.start();


        Map<String, String> map = new HashMap<>();
        CallListRequest callListRequest = new CallListRequest(map, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                LogUtil.d(TAG, "res:" + response.toString());
                try {
                    callList = GsonUtil.parse(response.getJSONObject("data").getString("calls"), CallList.class);
                    initBannerData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        requestQueue.add(callListRequest);
    }

    private void initBannerData() {
        callImgsList = new ArrayList<>();
        List<String> fromList = new ArrayList<>();


        List<String> toList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            try {
                if (callList.to.get(i) == null) {
                    toList.add(i, "null");
                    continue;
                }
            } catch (IndexOutOfBoundsException e) {
                toList.add(i, "null");
                continue;
            }
            toList.add(i, callList.to.get(i).avatar);
        }
        callImgsList.add(0, toList);


        for (int i = 0; i < 3; i++) {
            try {
                if (callList.from.get(i) == null) {
                    fromList.add(i, "null");
                    continue;
                }
            } catch (IndexOutOfBoundsException e) {
                fromList.add(i, "null");
                continue;
            }
            fromList.add(i, callList.from.get(i).avatar);
        }
        callImgsList.add(1,fromList);



        doRefresh();

    }

    @Override
    protected void setListener() {

        //下拉刷新
        srlDiscover.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isRefreshing) {
                    srlDiscover.setRefreshing(false);
                    return;
                }

                page = 0;
                paramsMap.put("page", page + "");
                LogUtil.d(TAG, "onRefresh" + paramsMap);
                doRefresh();
            }
        });

        //上拉加载
        rvNewest.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (isRefreshing) {
                    srlDiscover.setRefreshing(false);
                    return;
                }

                //SCROLL_STATE_DRAGGING  和   SCROLL_STATE_IDLE 两种效果自己看着来
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == newestAdapter.getItemCount()) {
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


        //取消转圈
        srlDiscover.setRefreshing(false);
        isRefreshing = true;

        UserListRequest userListRequest = new UserListRequest(paramsMap, UserListRequest.NEWEST, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {

                //隐藏动画
                newloading.setVisibility(View.GONE);

                LogUtil.d(TAG, "tempMap inside:" + paramsMap);
                LogUtil.d(TAG, "onSuccess:" + response.toString());
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
                rvNewest.setVisibility(View.GONE);
            } else {
                rvNewest.setVisibility(View.VISIBLE);
                initRecyclerView(list);
            }
        } else {//当前是上拉加载状态
            if (list.users.size() == 0) {
                ToastUtil.showToast(getString(R.string.app_tips_text15));
            } else {
                userList.users.addAll(list.users);
                newestAdapter.notifyDataSetChanged();
            }
        }
        srlDiscover.setRefreshing(false);
        isRefreshing = false;
    }

    private void initRecyclerView(UserList list) {
        //绑定Adapter
        newestAdapter = new RvNewestAdapter(baseContext, userList.users);
        gridLayoutManager = new GridLayoutManager(baseContext, 3);
        rvNewest.setLayoutManager(gridLayoutManager);
        rvNewest.setAdapter(newestAdapter);

        attachFabButton();

        //传入Banner数据

        if (callImgsList!=null && callImgsList.size() >0 ) {
            View header =  LayoutInflater.from(baseContext).inflate(R.layout.banner_newest, rvNewest, false);
            newestAdapter.setHeaderView(header);
            newestAdapter.setBannerList(callImgsList);
            newestAdapter.setOnBannerItemClickListener(new RvNewestAdapter.OnBannerItemClickListener() {
                @Override
                public void onItemClick(int position, int index) {
                    Intent intent = new Intent(baseContext, PersonalDetailsActivity.class);
                    switch (position) {
                        case 0:
                            intent.putExtra("user_id", callList.to.get(index)._id);
                            break;
                        case 1:
                            intent.putExtra("user_id", callList.from.get(index)._id);
                            break;
                    }
                    startActivity(intent);
                }
            });
        }

        newestAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        fabSend.attachToRecyclerView(rvNewest);
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
