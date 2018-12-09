package com.quootta.mdate.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.domain.FavoriteList;
import com.quootta.mdate.domain.UserList;
import com.quootta.mdate.engine.myCenter.FriendsListRequest;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.ui.adapter.FmFriendAdapter;
import com.quootta.mdate.ui.adapter.RvFriendsAdapter;
import com.quootta.mdate.ui.dialog.MyProgressDialog;
import com.quootta.mdate.ui.fragment.FollowMeFragment;
import com.quootta.mdate.ui.fragment.MeFollowFragment;
import com.quootta.mdate.utils.ActivityUtil;
import com.quootta.mdate.utils.GsonUtil;
import com.quootta.mdate.utils.LogUtil;
import com.android.volley.RequestQueue;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

public class FriendsListActivity extends BaseActivity {

    @Bind(R.id.tvTitle)TextView tv_title;
    @Bind(R.id.imgBack)ImageView iv_back;
//    @Bind(R.id.srl_favorite)SwipeRefreshLayout srl_favorite;
//    @Bind(R.id.rl_favorite_list) RecyclerView rv_favorite_list;


    private final static int ADD = 0;
    private final static int REMOVE = 1;
    @Bind(R.id.friend_tab)
    TabLayout friendTab;
    @Bind(R.id.friend_vp)
    ViewPager friendVp;

    private RequestQueue requestQueue;
    private MyProgressDialog myProgressDialog;
    private RvFriendsAdapter rvFriendsAdapter;
    private Map<String, String> paramsMap;
    private UserList userList;
    private LinearLayoutManager linearLayoutManager;
    private int lastVisibleItem;
    private int page;


    private FollowMeFragment followMeFragment;
    private MeFollowFragment meFollowFragment;
    private List<Fragment> followFragmentList;

    @Override
    protected void init() {
//        page = 0;
        paramsMap = new HashMap<>();
//        paramsMap.put("page",page + "");
//        myProgressDialog = new MyProgressDialog(baseContext);
        requestQueue = BaseApp.getRequestQueue();

        followFragmentList=new ArrayList<>();
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_favorite_list;
    }

    @Override
    protected void initData() {
//        myProgressDialog.show();
       requestFavoriteList();

        initTitleBar();

        meFollowFragment = new MeFollowFragment();
        followMeFragment = new FollowMeFragment();

        followFragmentList.add(meFollowFragment);
        followFragmentList.add(followMeFragment);

        FmFriendAdapter fmFriendAdapter = new FmFriendAdapter(getSupportFragmentManager(), followFragmentList);
        friendVp.setAdapter(fmFriendAdapter);
        friendTab.setupWithViewPager(friendVp);
        friendTab.setTabMode(TabLayout.MODE_FIXED);

    }


    private void requestFavoriteList() {
       FriendsListRequest favoriteListRequest = new FriendsListRequest(paramsMap, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                FavoriteList list = new FavoriteList();
                LogUtil.d("FriendsListActivity", "response:" + response.toString());

                    list = GsonUtil.parse(response, FavoriteList.class);

                if (list !=null){
                    EventBus.getDefault().post(list);
                }

                //refreshRecyclerView(list);
//                if(myProgressDialog.isShowing()) {
//                    myProgressDialog.dismiss();
//                }
            }
        });
        requestQueue.add(favoriteListRequest);
    }



//    private void refreshRecyclerView(UserList list) {
//        if (page == 0) {
//            LogUtil.d("BillActivity", "page = 0 ，下拉。");
//            userList = list;
//            initRecyclerView();
//        } else {
//            LogUtil.d("BillActivity", "page != 0 ，上拉。");
//            if (list.users.size() == 0) {
//                ToastUtil.showToast("没有更多数据啦");
//            }else {
//                userList.users.addAll(list.users);
//                rvFriendsAdapter.notifyDataSetChanged();
//            }
//        }
//        srl_favorite.setRefreshing(false);
//    }


//    private void initRecyclerView() {
//        rvFriendsAdapter = new RvFriendsAdapter(baseContext, userList.users);
//        rvFriendsAdapter.setOnItemClickListener(new RvFriendsAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                Intent detailIntent = new Intent(FriendsListActivity.this, PersonalDetailsActivity.class);
//                detailIntent.putExtra("user_id", userList.users.get(position)._id);
//                startActivity(detailIntent);
//            }
//        });
//        rvFriendsAdapter.setOnLongClickListener(new RvFriendsAdapter.OnLongClickListener() {
//            @Override
//            public void onLongClick(final int position) {
//                MyAlertDialog myAlertDialog = new MyAlertDialog(baseContext, new MyAlertDialog.OnPositiveAlterListener() {
//                    @Override
//                    public void onAlter() {
//                        requestRemoveFavorite(position, userList.users.get(position)._id);
//                    }
//                });
//                myAlertDialog.setTitle(getString(R.string.notify));
//                myAlertDialog.setMessage(getString(R.string.ensure_to_remove));
//                myAlertDialog.show();
//            }
//        });
//        rv_favorite_list.setAdapter(rvFriendsAdapter);
//
//        linearLayoutManager = new LinearLayoutManager(baseContext,LinearLayoutManager.VERTICAL,false);
//        rv_favorite_list.setLayoutManager(linearLayoutManager);
//
//        myProgressDialog.dismiss();
//    }

//    private void requestRemoveFavorite(final int position, String id) {
//        final MyProgressDialog myProgressDialog = new MyProgressDialog(baseContext);
//        myProgressDialog.show();
//        Map<String, String> map = new HashMap<>();
//        map.put("user_id", id);
//        FavoriteRequest favoriteRequest = new FavoriteRequest(map, REMOVE, new VolleyListener() {
//            @Override
//            protected void onSuccess(JSONObject response) {
//                try {
//                    ToastUtil.showToast(response.getString("msg"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                userList.users.remove(position);
//                rvFriendsAdapter.notifyDataSetChanged();
//                myProgressDialog.dismiss();
//            }
//        });
//        requestQueue.add(favoriteRequest);
//
//    }

    private void initTitleBar() {
        tv_title.setText(getString(R.string.friend));
        iv_back.setVisibility(View.VISIBLE);
    }

    @Override
    protected void setListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.finishActivty();
            }
        });
//
//        //下拉刷新时，从第0页开始请求
//        srl_favorite.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                page = 0;
//                paramsMap.put("page", page + "");
//                requestFavoriteList();
//            }
//
//        });
//
//        //上拉加载时，从当前页数继续向下请求
//        rv_favorite_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                //SCROLL_STATE_DRAGGING  和   SCROLL_STATE_IDLE 两种效果自己看着来
//                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == rvFriendsAdapter.getItemCount()) {
//                    srl_favorite.setRefreshing(true);
//                    page++;
//                    paramsMap.put("page", page + "");
//                    requestFavoriteList();
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
//            }
//        });
    }

}
