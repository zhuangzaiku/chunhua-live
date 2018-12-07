package com.quootta.mdate.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.base.BaseFragment;
import com.quootta.mdate.domain.FavoriteList;
import com.quootta.mdate.domain.UserList;
import com.quootta.mdate.engine.myCenter.FavoriteRequest;
import com.quootta.mdate.engine.myCenter.FriendsListRequest;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.ui.activity.PersonalDetailsActivity;
import com.quootta.mdate.ui.adapter.RvFriendsAdapter;
import com.quootta.mdate.ui.dialog.MyAlertDialog;
import com.quootta.mdate.ui.dialog.MyProgressDialog;
import com.quootta.mdate.utils.GsonUtil;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.android.volley.RequestQueue;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

import static com.quootta.mdate.R.id.rl_favorite_list;


/**
 * Created by Ryon on 2016/12/15/0015.
 */

public class FollowMeFragment extends BaseFragment {

    private RequestQueue requestQueue;

    private RvFriendsAdapter rvFriendsAdapter;
    private LinearLayoutManager linearLayoutManager;
    private Map<String, String> paramsMap;
    private final static int REMOVE = 1;
    private int page=0;
    private int lastVisibleItem;
    @Bind(rl_favorite_list)
    RecyclerView rv_favorite_list;

    private UserList userList;
    private MyProgressDialog myProgressDialog;
    @Override
    protected int getRootView() {
        return R.layout.fragment_followme;
    }

    @Override
    protected void init() {
        EventBus.getDefault().register(this);
        requestQueue= BaseApp.getRequestQueue();
        myProgressDialog=new MyProgressDialog(getContext());

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMes(FavoriteList favoriteList) {
        userList=new UserList();
        userList.users=favoriteList.getData().getUsers().getFrom_me();

        initRecyclerView(userList);

    }

        @Override
    protected void initData(View view) {



    }




    @Override
    protected void setListener() {
        //上拉加载时，从当前页数继续向下请求
        rv_favorite_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //SCROLL_STATE_DRAGGING  和   SCROLL_STATE_IDLE 两种效果自己看着来
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == rvFriendsAdapter.getItemCount()) {
                    Log.i("tag","执行加页--->"+page+"------>"+lastVisibleItem);
                    myProgressDialog.show();
                    page++;
                    requestFavoriteList();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    private void requestFavoriteList() {
        paramsMap = new HashMap<>();
        paramsMap.put("page",page+"");
        FriendsListRequest favoriteListRequest = new FriendsListRequest(paramsMap, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {

                myProgressDialog.dismiss();

                FavoriteList list = new FavoriteList();
                LogUtil.d("FriendsListActivity", "response:" + response.toString());

                list = GsonUtil.parse(response, FavoriteList.class);

               if (userList==null){
                   userList=new UserList();
               }
                if (list.getData().getUsers().getFrom_me().size()==0){
                    ToastUtil.showToast(getString(R.string.app_tips_text15));
                }else {
                    userList.users.addAll(list.getData().getUsers().getFrom_me());
                    rvFriendsAdapter.notifyDataSetChanged();
                }

            }
        });
        requestQueue.add(favoriteListRequest);
    }




    private void initRecyclerView(final UserList userList) {
        rvFriendsAdapter = new RvFriendsAdapter(getContext(), userList.users);
        rvFriendsAdapter.setOnItemClickListener(new RvFriendsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent detailIntent = new Intent(getContext(), PersonalDetailsActivity.class);
                detailIntent.putExtra("user_id", userList.users.get(position)._id);
                startActivity(detailIntent);
            }
        });
        rvFriendsAdapter.setOnLongClickListener(new RvFriendsAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(final int position) {
                MyAlertDialog myAlertDialog = new MyAlertDialog(getContext(), new MyAlertDialog.OnPositiveAlterListener() {
                    @Override
                    public void onAlter() {
                        requestRemoveFavorite(position, userList.users.get(position)._id,userList);
                    }
                });
                myAlertDialog.setTitle(getString(R.string.notify));
                myAlertDialog.setMessage(getString(R.string.ensure_to_remove));
                myAlertDialog.show();
            }
        });
        rv_favorite_list.setAdapter(rvFriendsAdapter);

        linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        rv_favorite_list.setLayoutManager(linearLayoutManager);

     //   myProgressDialog.dismiss();
   }

   private void requestRemoveFavorite(final int position, String id, final UserList userList) {
        final MyProgressDialog myProgressDialog = new MyProgressDialog(getContext());
        myProgressDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("user_id", id);
        FavoriteRequest favoriteRequest = new FavoriteRequest(map, REMOVE, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                try {
                    ToastUtil.showToast(response.getString("msg"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                userList.users.remove(position);
                rvFriendsAdapter.notifyDataSetChanged();
                myProgressDialog.dismiss();
            }
        });
        requestQueue.add(favoriteRequest);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
