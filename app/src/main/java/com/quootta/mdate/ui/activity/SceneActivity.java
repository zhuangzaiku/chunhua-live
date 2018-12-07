package com.quootta.mdate.ui.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.domain.SceneList;
import com.quootta.mdate.domain.Scene_chek_apply;
import com.quootta.mdate.domain.UserList;
import com.quootta.mdate.engine.myCenter.FavoriteRequest;
import com.quootta.mdate.engine.scene.SceneCheckApply;
import com.quootta.mdate.engine.scene.SceneExitRequest;
import com.quootta.mdate.engine.scene.SceneListRequest;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.ui.adapter.RvHotListAdapter;
import com.quootta.mdate.ui.dialog.MyAlertDialog;
import com.quootta.mdate.ui.dialog.MyProgressDialog;
import com.quootta.mdate.utils.ActivityUtil;
import com.quootta.mdate.utils.GsonUtil;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by Ryon on 2016/12/27/0027.
 */

public class SceneActivity extends BaseActivity {
    @Bind(R.id.Scene_recycler)
    RecyclerView Scene_re;
    @Bind(R.id.tv_title_bar)
    TextView title;
    @Bind(R.id.iv_back_title_bar)
    ImageView back;
    @Bind(R.id.tv_join_title_bar)
    TextView title_join;

    @Bind(R.id.scene_discover)
    SwipeRefreshLayout discover;
//    @Bind(R.id.tv_title_bar)
//    TextView title_bar;

    private final static int ADD = 0;
    private final static int REMOVE = 1;
    private String id;
    private int page;
    private RequestQueue requestQueue;
    private  SceneList sceneList;
    private String name;
    public final int VIDEO_VERIFY = 1;
    private final static String TAG="SceneActivity";
    private Scene_chek_apply chekApply;
    private final int RESULT_SUCCESS = 4;
    private GridLayoutManager gridLayoutManager;
    private int lastVisibleItem;
    private  RvHotListAdapter hotListAdapter;
    private MyProgressDialog myProgressDialog;
    public UserList userList;

    @Override
    protected void init() {
        Intent intent=getIntent();
        id= intent.getStringExtra("id");
        name=intent.getStringExtra("name");
        myProgressDialog=new MyProgressDialog(SceneActivity.this);
        page=0;
        requestQueue= BaseApp.getRequestQueue();

        Log.i(TAG,"类型--》"+id);
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_scene;
    }

    @Override
    protected void initData() {
        initTitle();
        //初始化场景列表
        if (id!=null){
            initScene();
        }
    }

    private void initTitle(){

        if (name!=null){
            title.setText(name);
        }

        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.finishActivty();
            }
        });

        title_join.setVisibility(View.VISIBLE);
        title_join.setText(getString(R.string.app_tips_text72));

        initJoin();
    }


    //初始化界面
    private void initJoin(){
        Map<String,String> checkMap=new HashMap<>();
        checkMap.put("scene_id",id);
        final SceneCheckApply sceneCheckApply=new SceneCheckApply(checkMap, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG,"---scene-判断是否可以加入--"+response);
                 chekApply=GsonUtil.parse(response,Scene_chek_apply.class);


                //判断当不可以申请时
                if (!chekApply.getData().isIf_apply()){
                    //判断当前申请状态
                    switch (chekApply.getData().getCode()){
                        case "2000":
                            title_join.setText(getString(R.string.app_tips_text72));

                            break;
                        case "2001":
                            LogUtil.i(TAG,"申请正在处理");
                            title_join.setText(getString(R.string.app_tips_text73));
                            break;
                        case "2002":
                           // ToastUtil.showToast("申请的其他分类的请求正在处理");
                            title_join.setText(getString(R.string.app_tips_text72));
                            break;
                        case "2003":
                            LogUtil.i(TAG,"成功加入这个分类");
                            title_join.setText(getString(R.string.app_tips_text74));
                            break;
                        case "2004":
                            LogUtil.i(TAG,"成功加入其它分类");
                            title_join.setText(getString(R.string.app_tips_text72));
                            break;
                    }

                }

            }
        });
        requestQueue.add(sceneCheckApply);
    }


    //获取用户列表
    private void initScene(){
        Map<String,String> sceneMap=new HashMap<>();
        sceneMap.put("page",page+"");
        sceneMap.put("_id",id);
        SceneListRequest sceneListRequest=new SceneListRequest(sceneMap, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG,"---scene-情景--"+response);
                myProgressDialog.dismiss();
                discover.setRefreshing(false);
                sceneList= GsonUtil.parse(response,SceneList.class);
                freshRecyClerView(sceneList);
            }
        });

        requestQueue.add(sceneListRequest);
    }

    private void freshRecyClerView(SceneList sceneList){
        if (page==0){//当前是初始化页面或者下拉刷新状态
            userList=new UserList();
            userList.users=sceneList.getData().getUsers();
            if (sceneList.getData().getUsers().size()==0){
                Scene_re.setVisibility(View.GONE);
            }else {
                Scene_re.setVisibility(View.VISIBLE);
                initSceneAdapter(sceneList);
            }
        }else {//当前是上拉加载状态
            if (sceneList.getData().getUsers().size()==0){
                ToastUtil.showToast(getString(R.string.app_tips_text15));
            }else {
                userList.users.addAll(sceneList.getData().getUsers());
                hotListAdapter.notifyDataSetChanged();
            }
        }
    }

    private void initSceneAdapter(final SceneList sceneList){
        if (sceneList.getData().getUsers()!=null && sceneList.getData().getUsers().size()!=0){
            //直接使用热门适配器
             hotListAdapter=new RvHotListAdapter(SceneActivity.this,sceneList.getData().getUsers());

             gridLayoutManager = new GridLayoutManager(baseContext, 2);
            Scene_re.setLayoutManager(gridLayoutManager);
            Scene_re.setAdapter(hotListAdapter);

            //跳转到用户详情
            hotListAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (sceneList.getData().getUsers().size() != 0) {
                        Intent detailIntent = new Intent(SceneActivity.this, PersonalDetailsActivity.class);
                        detailIntent.putExtra("user_id", sceneList.getData().getUsers().get(position)._id);
                        startActivity(detailIntent);
                    }
                }
            });
            //加关注
            hotListAdapter.setOnLikeListener(new RvHotListAdapter.OnLikeListener() {
                @Override
                public void onLike(int position, boolean isLike) {
                    requestFavorite(sceneList.getData().getUsers().get(position)._id, isLike);
                }
            });
        }
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
    protected void setListener() {


       // 下拉刷新
        discover.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;

                initScene();
            }
        });


        //上拉加载
        Scene_re.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //SCROLL_STATE_DRAGGING  和   SCROLL_STATE_IDLE 两种效果自己看着来
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == hotListAdapter.getItemCount()) {

                    //  srlDiscover.setRefreshing(true);
                    myProgressDialog.show();
                    page++;
                    initScene();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
            }
        });





        title_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chekApply!=null){

                    if (chekApply.getData().isIf_apply()){
                        //直接申请
                        ToastUtil.showLongToast(getString(R.string.app_tips_text75));

                        Intent videoVerifyIntent=new Intent(SceneActivity.this,VideoVerifyActivity.class);
                        videoVerifyIntent.putExtra("type","scene");
                        videoVerifyIntent.putExtra("_id",id);
                        startActivityForResult(videoVerifyIntent,VIDEO_VERIFY);
                    }else {
                        if (chekApply.getData().getCode().equals("2003")){
                            //这里进行退出
                            MyAlertDialog myAlertDialog=new MyAlertDialog(baseContext, new MyAlertDialog.OnPositiveAlterListener() {
                                @Override
                                public void onAlter() {
                                    sceneExit();
                                }
                            });
                            myAlertDialog.setTitle(getString(R.string.app_tips_text76));
                            myAlertDialog.setMessage(getString(R.string.app_tips_text77));
                            myAlertDialog.show();

                        }else {
                            //弹出提示
                            ToastUtil.showLongToast(chekApply.getData().getMsg());
                        }

                    }

                }

            }
        });

    }

    //退出场景
    private void sceneExit(){
        Map<String,String> exitMap=new HashMap<>();
        exitMap.put("scene_id",id);
        SceneExitRequest sceneExitRequest=new SceneExitRequest(exitMap, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ToastUtil.showToast(getString(R.string.app_tips_text78));
                initJoin();
                title_join.setText(getString(R.string.app_tips_text72));
            }
        });
        requestQueue.add(sceneExitRequest);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case VIDEO_VERIFY:
                if (resultCode == RESULT_SUCCESS){
                    Log.i("tag","执行一次 edit");
                    initJoin();
                }
                break;
                default:
                    break;
        }
    }
}
