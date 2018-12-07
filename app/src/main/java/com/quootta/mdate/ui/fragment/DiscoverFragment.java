package com.quootta.mdate.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.base.BaseFragment;
import com.quootta.mdate.domain.BannerList;
import com.quootta.mdate.domain.InfoDetail;
import com.quootta.mdate.domain.UserList;
import com.quootta.mdate.engine.invite.UserListRequest;
import com.quootta.mdate.engine.media.GroupCallRequest;
import com.quootta.mdate.engine.myCenter.BannerRequest;
import com.quootta.mdate.engine.myCenter.FavoriteRequest;
import com.quootta.mdate.myInterface.OnItemClickListener;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.ui.activity.ChargeActivity;
import com.quootta.mdate.ui.activity.MainActivity;
import com.quootta.mdate.ui.activity.PersonalDetailsActivity;
import com.quootta.mdate.ui.activity.VipActivity;
import com.quootta.mdate.ui.adapter.RvDiscoverAdapter;
import com.quootta.mdate.ui.dialog.GroupCallDialog;
import com.quootta.mdate.utils.ActivityUtil;
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

import static android.app.job.JobScheduler.RESULT_SUCCESS;


public class DiscoverFragment extends BaseFragment {


    @Bind(R.id.srl_discover) SwipeRefreshLayout srl_discover ;
    @Bind(R.id.rl_null_discover)RelativeLayout rl_null_discover;
    @Bind(R.id.rv_discover) RecyclerView rv_discover;
    @Bind(R.id.fab_group_send) FloatingActionButton fabSend;

    public final static int POPULAR = 0;
    public final static int CITY = 1;
    public final static int NEWEST = 2;

    private final static int ADD = 0;
    private final static int REMOVE = 1;
    public final int CHARGE_VIP = 3;

    private final static int GROUP_CALL_PRIZE = 1000;

    private Context baseContext;
    private RequestQueue requestQueue;
    private SharedPreferences sp;
    private RvDiscoverAdapter discoverAdapter;
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


    /**
     * 加载UI前的预初始化
     */
    @Override
    protected void init() {
        currentMainActivity  = (MainActivity) getActivity();
        requestQueue = BaseApp.getRequestQueue();
    }

    /**
     * 设置布局
     *
     * @return
     */
    @Override
    protected int getRootView() {
        return R.layout.fragment_discover;
    }

    /**
     * 请求数据，设置UI
     *
     * @param view
     */
    @Override
    protected void initData(View view) {
        baseContext = view.getContext();

        sp = baseContext.getSharedPreferences("login", BaseActivity.MODE_PRIVATE);
        paramsMap = new HashMap();
        paramsMap.put("page", page + "");
        paramsMap.put("gender","");
        paramsMap.put("time_ago","");
        paramsMap.put("distance", "");
        paramsMap.put("lat", sp.getString("lat","") + "");
        paramsMap.put("lng", sp.getString("lng","") + "");
        requestBannerData();

    }

    private void requestBannerData() {
        String channel = ActivityUtil.getChannelString(getActivity());

        final Map<String, String> bannerMap = new HashMap<String, String>();
        bannerMap.put("name", channel);
        BannerRequest bannerRequest = new BannerRequest(bannerMap, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                LogUtil.d("requestBannerData", "response:" + response);
                try {
                    bannerList = GsonUtil.parse(response.getString("data"), BannerList.class);
//                    LogUtil.d("BannerList", "" + bannerList.banner.image_url.get(0));

                    //设置RecyclerView的布局管理
                    linearLayoutManager = new LinearLayoutManager(baseContext,LinearLayoutManager.VERTICAL,false);
                    gridLayoutManager = new GridLayoutManager(baseContext, 3);

                    rv_discover.setLayoutManager(linearLayoutManager);
                    //设置Adapter
                    discoverAdapter = new RvDiscoverAdapter(baseContext);

                    //把过期的bannner移除掉
                    int i;
                    for ( i=0;i<bannerList.banner.size();i++){

                        if (Long.parseLong(bannerList.banner.get(i).end_time) <= System.currentTimeMillis()){
                            bannerList.banner.remove(i);
                            LogUtil.i("BannerList","移除了第"+i+"个");
                        }
                    }

//
                    if (i!=0){
                        LogUtil.i("BannerList","进入适配器");
                        View header =  LayoutInflater.from(baseContext).inflate(R.layout.banner_hot, rv_discover, false);
                        discoverAdapter.setHeaderView(header);
                        discoverAdapter.setBannerList(bannerList.banner);
                    }


                    requestPopularUserList();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        requestQueue.add(bannerRequest);
    }

    /**
     * 设置监听
     */
    @Override
    protected void setListener() {
        //下拉刷新
        srl_discover.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                paramsMap.put("page", page + "");
                LogUtil.d("DiscoverFragment", "onRefresh: " + paramsMap);
                switch (currentRequestType) {
                    case POPULAR:
                        requestPopularUserList();
                        break;
                    case CITY:
                        requestUserList();
                        break;
                    case NEWEST:
                        requestNewestUserList();
                        break;
                    default:
                        requestUserList();
                        break;
                }
            }

        });

        //上拉加载
        rv_discover.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == discoverAdapter.getItemCount()) {

                    LogUtil.d("DiscoverFragment", "onScrollStateChanged: " + paramsMap);
                    srl_discover.setRefreshing(true);
                    paramsMap.put("page", "" + (++page));
                    switch (currentRequestType) {
                        case POPULAR:
                            LogUtil.d("DiscoverFragment", "onScrollStateChanged: POPULAR");
                            requestPopularUserList();
                            break;
                        case CITY:
                            LogUtil.d("DiscoverFragment", "onScrollStateChanged: CITY");
                            requestUserList();
                            break;
                        case NEWEST:
                            LogUtil.d("DiscoverFragment", "onScrollStateChanged: NEWEST");
                            requestNewestUserList();
                            break;
                        default:
                            LogUtil.d("DiscoverFragment", "onScrollStateChanged: default");
                            requestUserList();
                            break;
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    public void requestUserList() {
        setCurrentRequestType(CITY);
        final Map<String, String> tempMap = new HashMap<>(paramsMap);
//        tempMap.put("page",paramsMap.get("page"));
//        tempMap.put("gender",paramsMap.get("gender"));
//        tempMap.put("age_l",paramsMap.get("age_l"));
//        tempMap.put("age_h", paramsMap.get("age_h"));
//        tempMap.put("time_ago",paramsMap.get("time_ago"));
//        tempMap.put("distance", paramsMap.get("distance"));

        LogUtil.d("DiscoverFragment", "tempMap outside:" + tempMap);
        UserListRequest userListRequest = new UserListRequest(tempMap, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                LogUtil.d("DiscoverFragment", "tempMap inside:" + tempMap);
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

    public void requestPopularUserList() {
        setCurrentRequestType(POPULAR);
        final Map<String, String> tempMap = new HashMap<>();
        tempMap.put("page", paramsMap.get("page"));
        UserListRequest popUserListRequest = new UserListRequest( tempMap, POPULAR, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
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
        requestQueue.add(popUserListRequest);
    }

    public void requestNewestUserList() {
        setCurrentRequestType(NEWEST);
        final Map<String, String> tempMap = new HashMap<>();
        tempMap.put("page",paramsMap.get("page"));
        UserListRequest popUserListRequest = new UserListRequest(tempMap, NEWEST, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
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
        requestQueue.add(popUserListRequest);
    }

    private void freshRecyclerView(UserList list) {
        if(page == 0) {//下拉刷新
            LogUtil.d("DiscoverFragment", "freshRecyclerView:page == 0");
            userList = list;
            if(userList.users.size() == 0) {
                LogUtil.d("DiscoverFragment", "freshRecyclerView:no content");
                rv_discover.setVisibility(View.GONE);
                rl_null_discover.setVisibility(View.VISIBLE);
            } else {
                rl_null_discover.setVisibility(View.GONE);
                rv_discover.setVisibility(View.VISIBLE);
                initRecyclerView();
            }
        } else {//上拉加载
            if (list.users.size() == 0) {
                ToastUtil.showToast(getString(R.string.app_tips_text15));
            } else {
                userList.users.addAll(list.users);
                discoverAdapter.notifyDataSetChanged();
            }
        }
        srl_discover.setRefreshing(false);
    }

    private void initRecyclerView() {
        //绑定Adapter
        discoverAdapter.setUsersList(userList.users);
//        discoverAdapter = new RvDiscoverAdapter(baseContext, userList.users);
        rv_discover.setAdapter(discoverAdapter);
        attachFabButton();


        //设置点击事件
        discoverAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (userList.users.size() != 0) {
                    Intent detailIntent = new Intent(getActivity(), PersonalDetailsActivity.class);
                    detailIntent.putExtra("user_id", userList.users.get(position)._id);
                    startActivity(detailIntent);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        discoverAdapter.setOnLikeListener(new RvDiscoverAdapter.onLikeListener() {
            @Override
            public void onLike(int position, boolean isLike) {
                requestFavorite(userList.users.get(position)._id, isLike);
            }
        });
    }

    private void attachFabButton() {
        fabSend.attachToRecyclerView(rv_discover);
        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupCallDialog groupCallDialog = new GroupCallDialog(baseContext, new GroupCallDialog.OnGroupClickListener() {
                    @Override
                    public void onGoldClick(String gender) {
                        if (BaseApp.getGoldCount() >= GROUP_CALL_PRIZE) {
                            Map<String, String> paramsMap = new HashMap<String, String>();
                            paramsMap.put("gender", gender);
                            paramsMap.put("num", GROUP_CALL_PRIZE + "");
                            GroupCallRequest callRequest = new GroupCallRequest(paramsMap, new VolleyListener() {
                                @Override
                                protected void onSuccess(JSONObject response) {

                                      //  ToastUtil.showLongToast(response.getString("msg"));
                                        showMessageDialog(getString(R.string.app_tips_text107), baseContext);

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

                                       // ToastUtil.showLongToast(response.getString("msg"));
                                        showMessageDialog(getString(R.string.app_tips_text107), baseContext);

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

    private void showMessageDialog(String msg, Context baseContext) {
        AlertDialog.Builder builder = new AlertDialog.Builder(baseContext);
        builder.setTitle(R.string.app_tips_text46);
        builder.setMessage(msg);
        builder.setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
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

    public void setParamsMap(Map<String, String> paramsMap) {
        this.paramsMap = paramsMap;
        page = 0;
        this.paramsMap.put("page", page + "");
        requestUserList();
    }

    public void setPage(int page) {
        this.page = page;
        if (paramsMap == null) {
            paramsMap = new HashMap<String, String>();
        }
        paramsMap.put("page", page + "");
    }

    public void setCurrentRequestType(int currentRequestType) {
        this.currentRequestType = currentRequestType;
    }

    public void setRefreshing(){
        LogUtil.d("MainActivity", "setRefreshing");
        if (srl_discover != null)
            srl_discover.setRefreshing(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHARGE_VIP:
                if (resultCode == RESULT_SUCCESS){

                        currentMainActivity.meFragment.requestBaseInfoDetail();

                }
                break;
            default:
                break;
        }
    }
}
