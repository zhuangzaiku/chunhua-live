package com.quootta.mdate.ui.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.domain.BillList;
import com.quootta.mdate.engine.myCenter.BillRequest;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.ui.adapter.RvBillAdapter;
import com.quootta.mdate.utils.ActivityUtil;
import com.quootta.mdate.utils.GsonUtil;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.android.volley.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

public class BillActivity extends BaseActivity {

    @Bind(R.id.iv_back_title_bar)ImageView iv_back;
    @Bind(R.id.tv_title_bar)TextView tv_title;
    @Bind(R.id.srl_bill)SwipeRefreshLayout srl_bill;
    @Bind(R.id.rv_bill)RecyclerView rv_bill;

    private RequestQueue requestQueue;
    private RvBillAdapter billAdapter;
    private LinearLayoutManager linearLayoutManager;
    private int lastVisibleItem;
    private Map<String,String> paramsMap;
    private int page;
    private BillList billList;

    @Override
    protected void init() {
        requestQueue = BaseApp.getRequestQueue();
        paramsMap = new HashMap<>();
        paramsMap.put("page","0");
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_bill;
    }

    @Override
    protected void initData() {
        initTitle();
        requestBill();

    }

    private void initRecyclerView() {
        billAdapter = new RvBillAdapter(baseContext,billList.bills);
        rv_bill.setAdapter(billAdapter);

        linearLayoutManager = new LinearLayoutManager(baseContext,LinearLayoutManager.VERTICAL,false);
        rv_bill.setLayoutManager(linearLayoutManager);

    }

    private void requestBill() {
        final BillRequest billRequest = new BillRequest(paramsMap, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                BillList list = new BillList();
                LogUtil.d("BillActivity", "Response:" + response);
                try {
                    ToastUtil.showToast(response.getString("msg").toString());
                    list = GsonUtil.parse(response.getString("data"),BillList.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                refreshRecyclerView(list);

            }
        });
        requestQueue.add(billRequest);
    }

    private void refreshRecyclerView(BillList list) {
        if (page == 0) {
            LogUtil.d("BillActivity", "page = 0 ，下拉。");
            billList = list;
            initRecyclerView();
        } else {
            LogUtil.d("BillActivity", "page != 0 ，上拉。");
            if (list.bills.size() == 0) {
                ToastUtil.showToast(getString(R.string.app_tips_text15));
            }else {
                billList.bills.addAll(list.bills);
                billAdapter.notifyDataSetChanged();
            }
        }
        srl_bill.setRefreshing(false);
    }


    private void initTitle() {
        iv_back.setVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.bill));
    }

    @Override
    protected void setListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.finishActivty();
            }
        });


        //下拉刷新时，从第0页开始请求
        srl_bill.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                paramsMap.put("page",page + "");
                requestBill();
            }

        });

        //上拉加载时，从当前页数继续向下请求
        rv_bill.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //SCROLL_STATE_DRAGGING  和   SCROLL_STATE_IDLE 两种效果自己看着来
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == billAdapter.getItemCount()) {
                    srl_bill.setRefreshing(true);
                    page++;
                    paramsMap.put("page", page + "");
                    requestBill();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }
}
