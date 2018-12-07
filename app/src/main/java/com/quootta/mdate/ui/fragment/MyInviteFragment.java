package com.quootta.mdate.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.base.BaseFragment;
import com.quootta.mdate.domain.MyInviteList;
import com.quootta.mdate.engine.chat.MyInviteListRequest;
import com.quootta.mdate.myInterface.OnItemClickListener;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.ui.activity.InviteDetailActivity;
import com.quootta.mdate.ui.adapter.RvMyInviteListAdapter;
import com.quootta.mdate.utils.GsonUtil;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.android.volley.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by Ryon on 2016/2/18.
 */
public class MyInviteFragment extends BaseFragment {

    @Bind(R.id.srl_my_invite)SwipeRefreshLayout srl_my_invite;
    @Bind(R.id.rv_my_invite_fragment) RecyclerView rv_my_invite;

    private Context baseContext;
    private RvMyInviteListAdapter myInviteListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private int lastVisibleItem;
    private ItemTouchHelper itemTouchHelper;
    private RequestQueue requestQueue;
    private MyInviteList myInviteList;
    private Map<String,String> paramsMap;
    private int page;
    private View myView;

    @Override
    protected int getRootView() {
        return R.layout.fragment_my_invite;
    }

    @Override
    protected void setListener() {
        //下拉刷新时，从第0页开始请求
        srl_my_invite.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                paramsMap.put("page",page + "");
                requestMyInviteList();
            }

        });

        //上拉加载时，从当前页数继续向下请求
        rv_my_invite.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //SCROLL_STATE_DRAGGING  和   SCROLL_STATE_IDLE 两种效果自己看着来
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == myInviteListAdapter.getItemCount()) {
                    srl_my_invite.setRefreshing(true);
                    page++;
                    paramsMap.put("page", page + "");
                    requestMyInviteList();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    @Override
    protected void init() {
        requestQueue = BaseApp.getRequestQueue();
        paramsMap = new HashMap<>();
        paramsMap.put("page", "0");
    }

    @Override
    protected void initData(View view) {
        baseContext = view.getContext();
        myView = view;
        requestMyInviteList();
    }

    public void requestMyInviteList() {
        MyInviteListRequest myInviteListRequest = new MyInviteListRequest(paramsMap,
                new VolleyListener() {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        MyInviteList list = new MyInviteList();
                        try {
                            list = GsonUtil.parse(response.getString("data"),MyInviteList.class);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(page == 0) {
                            LogUtil.d("MyInviteFragment", "page = 0 ，下拉。");
                            myInviteList = list;
                            initRecyclerView(myInviteList);
                        } else {
                            LogUtil.d("MyInviteFragment", "page != 0 ，上拉。");
                            if (list.dates.size() == 0) {
                                ToastUtil.showToast("没有更多数据啦");
                            }else {
                                myInviteList.dates.addAll(list.dates);
                                myInviteListAdapter.notifyDataSetChanged();
                            }
                        }
                        srl_my_invite.setRefreshing(false);
                        LogUtil.d("MyInviteFragment", "response:"+response.toString());
                    }
                });
        requestQueue.add(myInviteListRequest);
    }

    private void initRecyclerView(final MyInviteList myInviteList) {

        //绑定Adapter
        myInviteListAdapter = new RvMyInviteListAdapter(baseContext, myInviteList.dates);
        rv_my_invite.setAdapter(myInviteListAdapter);

        //设置RecyclerView的布局管理
        linearLayoutManager = new LinearLayoutManager(baseContext,LinearLayoutManager.VERTICAL,false);
        rv_my_invite.setLayoutManager(linearLayoutManager);

        //设置点击事件
        myInviteListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent myInviteIntent = new Intent(baseContext, InviteDetailActivity.class);
                myInviteIntent.putExtra("invite_id", myInviteList.dates.get(position)._id);
                myInviteIntent.putExtra("invite_type", "1");
                baseContext.startActivity(myInviteIntent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    public void setPage(int page) {
        this.page = page;
    }
}
