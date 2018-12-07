package com.quootta.mdate.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.base.BaseFragment;
import com.quootta.mdate.domain.InviteMeList;
import com.quootta.mdate.engine.chat.InviteMeListRequest;
import com.quootta.mdate.engine.chat.InviteOperateRequest;
import com.quootta.mdate.myInterface.OnItemClickListener;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.ui.activity.InviteDetailActivity;
import com.quootta.mdate.ui.adapter.RvInviteMeListAdapter;
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
public class InviteMeFragment extends BaseFragment {

    @Bind(R.id.srl_invite_me)SwipeRefreshLayout srl_invite_me;
    @Bind(R.id.rv_invite_me_fragment) RecyclerView rv_invite_me;

    private Context baseContext;
    private RequestQueue requestQueue;
    private RvInviteMeListAdapter inviteMeListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private int lastVisibleItem;
    private ItemTouchHelper itemTouchHelper;
    private InviteMeList inviteMeList;
    private Map<String,String> paramsMap;
    private int page;
    private View myView;

    @Override
    protected int getRootView() {
        return R.layout.fragment_invite_me;
    }

    @Override
    protected void setListener() {
        //下拉刷新时，从第0页开始请求
        srl_invite_me.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                paramsMap.put("page",page + "");
                requestInviteMeList();
            }

        });

        //上拉加载时，从当前页数继续向下请求
        rv_invite_me.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //SCROLL_STATE_DRAGGING  和   SCROLL_STATE_IDLE 两种效果自己看着来
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == inviteMeListAdapter.getItemCount()) {
                    srl_invite_me.setRefreshing(true);
                    page++;
                    paramsMap.put("page", page + "");
                    requestInviteMeList();
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
        requestInviteMeList();

    }

    public void requestInviteMeList() {
        InviteMeListRequest inviteMeListRequest = new InviteMeListRequest(paramsMap,
                new VolleyListener() {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        InviteMeList list = new InviteMeList();
                        try {
                            list = GsonUtil.parse(response.getString("data"),InviteMeList.class);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(page == 0) {
                            LogUtil.d("InviteMeFragment", "page = 0 ，下拉。");
                            inviteMeList = list;
                            initRecyclerView(inviteMeList, myView);
                        } else {
                            LogUtil.d("InviteMeFragment", "page != 0 ，上拉。");
                            if (list.dates.size() == 0) {
                                ToastUtil.showToast(getString(R.string.app_tips_text15));
                            }else {
                                inviteMeList.dates.addAll(list.dates);
                                inviteMeListAdapter.notifyDataSetChanged();
                            }
                        }
                        srl_invite_me.setRefreshing(false);
                        LogUtil.d("InviteMeFragment","response:" + response.toString());
                    }
                });
        requestQueue.add(inviteMeListRequest);

    }

    private void initRecyclerView(final InviteMeList inviteMeList, View view) {

        //绑定Adapter
        inviteMeListAdapter = new RvInviteMeListAdapter(baseContext, inviteMeList.dates);
        rv_invite_me.setAdapter(inviteMeListAdapter);

        //设置RecyclerView的布局管理
        linearLayoutManager = new LinearLayoutManager(baseContext,LinearLayoutManager.VERTICAL,false);
        rv_invite_me.setLayoutManager(linearLayoutManager);

        //设置点击事件
        inviteMeListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent inviteMeIntent = new Intent(baseContext, InviteDetailActivity.class);
                inviteMeIntent.putExtra("invite_id",inviteMeList.dates.get(position)._id);
                inviteMeIntent.putExtra("invite_type","0");
                baseContext.startActivity(inviteMeIntent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        inviteMeListAdapter.setOnAcceptListener(new RvInviteMeListAdapter.OnAcceptListener() {
            @Override
            public void onAccept(int position) {
                Map<String, String> paramsMap = new HashMap<String, String>();
                paramsMap.put("invite_id", inviteMeList.dates.get(position)._id);
                paramsMap.put("type", "accept");
                requestInviteOperate(paramsMap);
            }
        });
    }

    private void requestInviteOperate(final Map<String, String> paramsMap) {
        InviteOperateRequest inviteOperateRequest = new InviteOperateRequest(paramsMap,
                new VolleyListener() {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        try {
                            Toast.makeText(baseContext,response.getString("msg"),Toast.LENGTH_SHORT).show();
                            if(response.getString("code").equals("0")){
                                setPage(0);
                                requestInviteMeList();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        requestQueue.add(inviteOperateRequest);
    }

    public void setPage(int page) {
        this.page = page;
    }
}
