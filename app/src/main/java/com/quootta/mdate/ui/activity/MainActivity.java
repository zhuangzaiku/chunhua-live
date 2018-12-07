package com.quootta.mdate.ui.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.base.BaseFragment;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.domain.LocationDate;
import com.quootta.mdate.domain.UpdateNotify;
import com.quootta.mdate.domain.UserChatInfo;
import com.quootta.mdate.domain.UserChatInfoList;
import com.quootta.mdate.domain.UserDetail;
import com.quootta.mdate.engine.UpdateRequest;
import com.quootta.mdate.engine.invite.UserDetailRequest;
import com.quootta.mdate.engine.myCenter.OnlineRequest;
import com.quootta.mdate.helper.ConnectRongHelper;
import com.quootta.mdate.myListener.VolleyErrorListener;
import com.quootta.mdate.myListener.VolleyListener;
import com.quootta.mdate.ui.adapter.MyPagerAdapter;
import com.quootta.mdate.ui.dialog.MyAlertDialog;
import com.quootta.mdate.ui.fragment.ChatsListFragment;
import com.quootta.mdate.ui.fragment.DiscoveryFragment;
import com.quootta.mdate.ui.fragment.MeFragment;
import com.quootta.mdate.ui.fragment.RankingListFragment;
import com.quootta.mdate.ui.view.BadgeView;
import com.quootta.mdate.ui.view.MyViewPager;
import com.quootta.mdate.utils.ActivityUtil;
import com.quootta.mdate.utils.DBUtil;
import com.quootta.mdate.utils.GsonUtil;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import io.rong.eventbus.EventBus;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    public static final int REQUEST_FILTER = 1;
    private final int RESULT_SUCCESS = 4;
    @Bind(R.id.tv_title_bar) TextView tv_title_bar;
    @Bind(R.id.v_pink_indicator) View indicator;
    @Bind(R.id.iv_drop_down_title_bar) ImageView iv_drop_down;
    //    @Bind(R.id.iv_pink_indicator3) ImageView iv_indicator3;
 //   @Bind(R.id.tv_city_title_bar) TextView tv_city;
    @Bind(R.id.tv_popular_title_bar) TextView tv_popular;
    @Bind(R.id.tv_newest_title_bar) TextView tv_newest;
    @Bind(R.id.iv_system_message_title_bar)
    public ImageView iv_message;
    @Bind(R.id.rl_text_area_title_bar) RelativeLayout rl_text_area;
    @Bind(R.id.rb_discover) RadioButton rb_discover;
    @Bind(R.id.rb_chats) RadioButton rb_chats;
    @Bind(R.id.rb_me)  RadioButton rb_me;
//    @Bind(R.id.rb_list) RadioButton rb_list;
    @Bind(R.id.view_space) View viewSpace;
    @Bind(R.id.rg_main) RadioGroup rg_main;
    @Bind(R.id.vp_main) MyViewPager vp_main;

    public static boolean isForeground = false;
    //更新邀约列表和金额相关常量
    public static final int INVITE_ME = 0;
    public static final int MY_INVITE = 1;
    public static final int GOLD_AMOUNT = 2;

    public static MainActivity mainActivity;
    private RequestQueue requestQueue;
    private static DiscoveryFragment discoveryFragment;
//    private static DiscoverFragment discoverFragment;
    //    private static ChatsFragment chatsFragment;
    private static ChatsListFragment chatsListFragment;
    public static MeFragment meFragment;
    private MyPagerAdapter myPagerAdapter;
    private List<BaseFragment> main_fragments;
    private FragmentManager fm;
    private static RankingListFragment listFragment;

    private long mExitTime = 0;
    private int currentPos = 0;
    private String city="";
    private BadgeView unreadBadge;
    public BadgeView messageBadge;
    private ObjectAnimator animator;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private boolean isFirstLocate;
    private UserChatInfoList userChatInfoList;
    private RelativeLayout rel_title_bar;
    private PackageInfo info;
    private String version;

    /**
     * 加载UI前的预初始化
     */
    @Override
    protected void init() {
     //   isFirstLocate = true;
        requestQueue = BaseApp.getRequestQueue();
        messageBadge = new BadgeView(this);

        //获取包名
        try {
            info = getPackageManager().getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        pref = BaseApp.getApplication().getSharedPreferences("login", BaseActivity.MODE_PRIVATE);
        editor = pref.edit();
//        getLocationDetail();
//        connectRongCloud();

        //获取地址

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
//            getLocation();
        }
//        getLocation();
        //链接融云服务器
        connectRongCloud();

//        getCityInfo();
//        initRongIMUserInfo();
//        initRongIMUnReadListener();
        initFragments();


//        EventBus.getDefault().register(this);
    }



    private void connectRongCloud(){
        Log.i("tag","链接融云服务器");
        ConnectRongHelper connectRongHelper = new ConnectRongHelper(pref.getString("ry_token", null), new ConnectRongHelper.OnConnectListener() {
            @Override
            public void onConnectSuccess(UserChatInfoList userChatInfoList) {
                Log.i("userchat","链接成功");
               // LogUtil.i("tag", "ry_token--->"+pref);
                //链接融云成功之后加载用户列表信息和未读消息
                initRongIMUserInfo();
                initRongIMUnReadListener();
                //初始化消息列表的头像和id
                initRongIMUn(userChatInfoList);


            }

            @Override
            public void onConnectFail() {

            //    ToastUtil.showToast("登录过期 请重新登录");
                Log.i("tag","融云请求错误");
//                Intent intent=new Intent(baseContext, OtherLoginActivity.class);
//                baseContext.startActivity(intent);
            }
        },baseContext);
        connectRongHelper.connectRongIM();
    }


//
//    /**
//     * 更新地理信息，将经纬度信息保存在本地
//     * 在成功回调中链接融云服务器
//     * @param aMapLocation
//     */
//    private void uploadLocation(AMapLocation aMapLocation) {
//        Log.i("tag", "--经纬度");
//
//
//        city = aMapLocation.getCity();
//        LogUtil.d("LoginHelper", "city :" + aMapLocation.getCity());
//        LogUtil.d("time count", "getLocationDetail end time:" + System.currentTimeMillis());
//
//      //  getCityInfo(city);
//    }



//    private void getCityInfo(String curCity) {
//        city = curCity;
//        if (city == "" || city.isEmpty()) {
//            city = getString(R.string.mars);
//        } else {
//            String lastChar = city.substring(city.length() - 1, city.length());
//            if (lastChar.equals("市")) {
//                city = city.substring(0, city.length() - 1);
//                tv_city.setText(city);
//            }
//        }
//
//    }

    //未读消息刷新获取id
    private void initRongIMUn(UserChatInfoList userChatInfoList){
        if (userChatInfoList !=null){
            for (UserChatInfo userChatInfo : userChatInfoList.users) {
                LogUtil.e("userchat", "userChatInfoList--循环取出--->" + userChatInfo.avatar);
                RongIM.getInstance().refreshUserInfoCache(new UserInfo(userChatInfo._id,
                        userChatInfo.nick_name,
                        Uri.parse(LocalUrl.getPicUrl(userChatInfo.avatar))));
            }
        }

    }


    private void initRongIMUserInfo() {
    //    userChatInfoList = (UserChatInfoList) getIntent().getParcelableExtra("userChatInfoList");
//        Log.i("ConnectRongHelper","userchatinforList---->"+userChatInfoList.users.get(0)._id);
//        if (userChatInfoList != null) {

            LogUtil.d("userchat", "进入mainactivity中的initrongimuserinfo");

            RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                @Override
                public UserInfo getUserInfo(String s) {
                    LogUtil.d("userchat","getUserInfo:---------> " + s);

                        //根据id获取用户信息
                        getUser(s);

//                    for (UserChatInfo userChatInfo : userChatInfoList.users) {
//                        LogUtil.e("userchat","userchatInfo_id--->: " + userChatInfo._id);
//                        if (s.equals(userChatInfo._id)) {
//                            LogUtil.e("userchat", "userChatInfoList--循环取出--->" + userChatInfo.avatar);
//                            return new UserInfo(userChatInfo._id,
//                                    userChatInfo.nick_name,
//                                    Uri.parse(LocalUrl.getPicUrl(userChatInfo.avatar)));
//                        }
//                    }
                    return null;
                }
            }, true);
//        }


    }

    private void initFragments() {
        discoveryFragment = new DiscoveryFragment();
//        discoverFragment = new DiscoverFragment();
//        chatsFragment = new ChatsFragment();
        chatsListFragment = new ChatsListFragment();
        listFragment=new RankingListFragment();
        meFragment = new MeFragment();

        main_fragments = new ArrayList<BaseFragment>();
//        main_fragments.add(0, discoverFragment);
        main_fragments.add(0, discoveryFragment);
        main_fragments.add(1, chatsListFragment);
//        main_fragments.add(2, listFragment);
        main_fragments.add(2,meFragment);
        fm = getSupportFragmentManager();
    }

    //从融云获取未读消息数量 然后显示在UI上
    private void initRongIMUnReadListener() {
        LogUtil.d("MainActivity", "initRongIMUnReadListener");
        unreadBadge = new BadgeView(MainActivity.this);

        if (RongIM.getInstance() != null) {
            LogUtil.d("MainActivity", "RongIM.getInstance() != null");
            RongIM.getInstance().setOnReceiveUnreadCountChangedListener(new RongIM.OnReceiveUnreadCountChangedListener() {
                @Override
                public void onMessageIncreased(int i) {
                    LogUtil.d("MainActivity", "onMessageIncreased" + i);
                    Log.i("tag","初始化融云会话未读消息");
                    unreadBadge.setBadgeCount(i);
                    unreadBadge.setTargetView(viewSpace);
                }
            }, Conversation.ConversationType.PRIVATE);
        }
    }


    /**
     * 加载布局
     *
     * @return 布局id
     */
    @Override
    protected int getRootView() {
        return R.layout.activity_main;
    }

    /**
     * 请求数据，设置UI
     */
    @Override
    protected void initData() {
        rel_title_bar= (RelativeLayout) findViewById(R.id.rel_title_bar);
        rel_title_bar.setVisibility(View.GONE);
        mainActivity = this;
        requestOnline("online");
        initTitle();
        //检查更新
        checkUpdate();
        initViewPager();
    }

    private void initTitle() {
        tv_title_bar.setVisibility(View.GONE);
        rl_text_area.setVisibility(View.VISIBLE);
//        tv_city.setText(city);
        iv_drop_down.setVisibility(View.GONE);
        iv_message.setVisibility(View.VISIBLE);
    }

    private void initViewPager() {
        myPagerAdapter = new MyPagerAdapter(fm,main_fragments);
        vp_main.setNoScroll(false);
        vp_main.setOffscreenPageLimit(3);
        vp_main.setAdapter(myPagerAdapter);
    }

    /**
     * 设置监听器
     */
    @Override
    protected void setListener() {
        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int position = -1;
                switch (checkedId) {
                    case R.id.rb_discover:
                        position = 0;
                        break;

                    case R.id.rb_chats:
                        position = 1;
                        break;

//                    case R.id.rb_list:
//                        position = 2;
//                        break;
                    case R.id.rb_me:
                        position = 2;
                        break;

                    default:
                        break;
                }
                alterTitle(position);
                vp_main.setCurrentItem(position, false);
            }
        });

        vp_main.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        position = 0;
                        rb_discover.setChecked(true);
                        break;
                    case 1:
                        position = 1;
                        rb_chats.setChecked(true);
                        break;
//                    case 2:
//                        position = 2;
//                        rb_list.setChecked(true);
//                        break;
                    case 2:
                        position=2;
                        //刷新余额
                        meFragment.requestBalance();
                        rb_me.setChecked(true);
                        break;

                }
                alterTitle(position);
                vp_main.setCurrentItem(position, false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        iv_message.setOnClickListener(this);
        tv_popular.setOnClickListener(this);
      //  tv_city.setOnClickListener(this);
        tv_newest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_system_message_title_bar:
                messageBadge.setBadgeCount(0);
                messageBadge.setTargetView(iv_message);
                Intent messageIntent = new Intent(MainActivity.this, SystemMessageActivity.class);
                startActivity(messageIntent);
                break;
//            case R.id.tv_popular_title_bar:
//                LogUtil.d("MainActivity", "tv_popular click");
//                discoverFragment.setRefreshing();
//                setAnimation(0);
//                discoverFragment.setPage(0);
//                discoverFragment.requestPopularUserList();
//                break;
//            case R.id.tv_city_title_bar:
//                LogUtil.d("MainActivity", "tv_city click");
//                discoverFragment.setRefreshing();
//                setAnimation(1);
//                Intent filterIntent = new Intent(baseContext, FilterActivity.class);
//                startActivityForResult(filterIntent, REQUEST_FILTER);
//                break;
//            case R.id.tv_newest_title_bar:
//                LogUtil.d("MainActivity", "tv_newest click");
//                discoverFragment.setRefreshing();
//                setAnimation(2);
//                discoverFragment.setPage(0);
//                discoverFragment.requestNewestUserList();
//                break;
            default:

                break;
        }
    }

    private void setAnimation(final int targetPosition) {
        if (currentPos - targetPosition == 0){//不执行动画
            return;
        }

        float curTranslationX = indicator.getTranslationX();
        float delta = 0f;
        final int[] curLocation = new int[2];
        int[] tarLocation = new int[2];
        int tarX = 0;
        int curX = 0;

        //获取当前的像素位置
        indicator.getLocationOnScreen(curLocation);
        //获取目的地像素位置
        switch (targetPosition) {
            case 0:
                tv_popular.getLocationOnScreen(tarLocation);
                break;
            case 1:
            //    tv_city.getLocationOnScreen(tarLocation);
                break;
            case 2:
                tv_newest.getLocationOnScreen(tarLocation);
                break;
            default:
                break;
        }

        curX = curLocation[0];
        tarX = tarLocation[0];
        delta = (tarX - curX) + curTranslationX;
        animator = ObjectAnimator.ofFloat(indicator, "translationX", curTranslationX, delta);
        animator.setDuration(200);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (currentPos == 1) {
                    iv_drop_down.setVisibility(View.GONE);
                    indicator.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (targetPosition == 1) {
                    iv_drop_down.setVisibility(View.VISIBLE);
                    indicator.setVisibility(View.GONE);
                }
                currentPos = targetPosition;
            }
        });
        animator.start();
    }

    private void alterTitle(int position) {
        switch (position) {
            case 0:
                rel_title_bar.setVisibility(View.GONE);
//                rel_title_bar.setVisibility(View.VISIBLE);
//                tv_title_bar.setVisibility(View.GONE);
//                rl_text_area.setVisibility(View.VISIBLE);
//                iv_message.setVisibility(View.VISIBLE);
                break;
            case 1:
                rel_title_bar.setVisibility(View.VISIBLE);
                tv_title_bar.setVisibility(View.VISIBLE);
                rl_text_area.setVisibility(View.GONE);
                tv_title_bar.setText(getString(R.string.app_tips_text37));
                break;
//            case 2:
//                rel_title_bar.setVisibility(View.GONE);
//                break;
            case 2:
                rel_title_bar.setVisibility(View.VISIBLE);
                tv_title_bar.setVisibility(View.VISIBLE);
                rl_text_area.setVisibility(View.GONE);
                tv_title_bar.setText(getString(R.string.app_tips_text38));

                break;

        }
    }

    private void requestOnline(String status) {
        Map<String, String> onlineMap = new HashMap<>();
        onlineMap.put("status", status);
        OnlineRequest onlineRequest = new OnlineRequest(onlineMap, new VolleyListener() {
            @Override
            protected void onSuccess(JSONObject response) {
                LogUtil.d("MainActivity", "requestOnline Response:" + response);
            }
        });
        requestQueue.add(onlineRequest);
    }

    private void requestOnline(String status, VolleyListener volleyListener) {
        Map<String, String> onlineMap = new HashMap<>();
        onlineMap.put("status", status);
        OnlineRequest onlineRequest = new OnlineRequest(onlineMap, volleyListener);
        requestQueue.add(onlineRequest);
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        requestOnline("offline");
        EventBus.getDefault().unregister(this);
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if((System.currentTimeMillis() - mExitTime) > 2000) {
                ToastUtil.showToast(getString(R.string.app_tips_text39));
                mExitTime = System.currentTimeMillis();
            } else {
                requestOnline("offline", new VolleyListener() {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        ActivityUtil.appExit(baseContext);
                    }
                });
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.i("tag","--main---resultCode--->"+resultCode);
//        Log.i("tag","--main-->data"+data);
//        Log.i("tag","--main---requestCode--->"+requestCode);


        switch (requestCode) {

            case REQUEST_FILTER:
                if (resultCode == RESULT_OK){
                    LogUtil.d("MainActivity", "onActivityResult");
                    //接收参数
                    if (data !=null) {
                        Bundle bundle = data.getExtras();
                        ArrayList list = bundle.getParcelableArrayList("list");
                        //从List中将参数转回 List<Map<String, Object>>
                        List<Map<String, String>> lists= (List<Map<String, String>>)list.get(0);
                        Map<String,String> paramsMap = lists.get(0);
                        LogUtil.d("MainActivity","paramsMap:"+paramsMap.toString());
//                        discoverFragment.setParamsMap(paramsMap);
                    }
                }

                break;
            default:
                break;
        }
    }


    public static class RefreshRequestReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int type = intent.getIntExtra("type",-1);
            switch (type) {
                case INVITE_ME:
                    LogUtil.d("MainActivity", "RefreshRequestReceiver:INVITE_ME");
                    if (mainActivity.messageBadge != null ){
                        mainActivity.messageBadge.setText("new", TextView.BufferType.NORMAL);
                        mainActivity.messageBadge.setTextSize(8);
                        mainActivity.messageBadge.setBadgeMargin(0,8,4,0);
                        mainActivity.messageBadge.setTargetView(mainActivity.iv_message);
                    }
                    break;
                case GOLD_AMOUNT:
                    LogUtil.d("MainActivity", "RefreshRequestReceiver:GOLD_AMOUNT");
                    meFragment.requestBaseInfoDetail();
                    break;
                default:
                    LogUtil.d("MainActivity", "RefreshRequestReceiver:DEFAULT");
                    break;
            }
        }
    }


    private void getUser(String id){

        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("user_id",id);
        UserDetailRequest userDetailRequest = new UserDetailRequest(paramsMap,
                new VolleyListener() {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        LogUtil.d("userchat", response.toString());
                        try {
                           UserDetail userDetail = GsonUtil.parse(response.getString("data"), UserDetail.class);
                            if (userDetail!=null){
                                //刷新用户信息
                                RongIM.getInstance().refreshUserInfoCache(new UserInfo(userDetail._id,
                                        userDetail.nick_name,
                                        Uri.parse(LocalUrl.getPicUrl(userDetail.avatar))));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
        requestQueue.add(userDetailRequest);
    }


    //请求更新
    private void checkUpdate() {
        Log.i("tag","更新----->");
//        myProgressDialog.show();
        Map<String,String> updateParams = new HashMap<>();
        updateParams.put("platform","android");
        updateParams.put("version",info.versionName);
        UpdateRequest versionRequest = new UpdateRequest(updateParams,
                new VolleyListener() {
                    @Override
                    protected void onSuccess(JSONObject response) {
                       // Log.i("tag","splashActivity--->3");
                        LogUtil.d("SplashActivity","Response: " + response.toString());
                        try {
                            UpdateNotify updateNotify = GsonUtil.parse(response.getString("data"), UpdateNotify.class);
                            if (updateNotify.update.equals("true")) {
                                buildUpdateDialog(updateNotify);
                            }else {
                                //goToRouter();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new VolleyErrorListener() {
                    @Override
                    protected void onFail(VolleyError error) {
//                        myProgressDialog.dismiss();
                        Log.i("tag","更新错误---》"+error.getMessage());
                        // byte[] htmlBodyBytes=error.networkResponse.data;;
//                        Log.e("tag","更新获取错误信息---》"+error.networkResponse.data);
//                        error.printStackTrace();
                       // buildExitDialog();
                    }
                });
        requestQueue.add(versionRequest);
    }

    private void buildUpdateDialog(UpdateNotify updateNotify) {
        MyAlertDialog updateDialog = new MyAlertDialog(baseContext, new MyAlertDialog.OnPositiveAlterListener() {
            @Override
            public void onAlter() {
                Intent updateIntent = new Intent(Intent.ACTION_VIEW);
                updateIntent.setData(Uri.parse(LocalUrl.DOWNLOAD_URL));
                startActivity(updateIntent);
                ActivityUtil.appExit(baseContext);
            }
        });

        updateDialog.setOnNegativeAlterListener(new MyAlertDialog.OnNegativeAlterListener() {
            @Override
            public void onAlter() {
               // goToRouter();
            }
        });
        updateDialog.setTitle(getString(R.string.update_title) + "(" + updateNotify.version + ")");
        updateDialog.setMessage("\n" + updateNotify.info);
        updateDialog.setCancelable(false);
        updateDialog.show();
    }

    LocationManager locationManager;

    @SuppressLint("MissingPermission")
    private void getLocation() {
        // 获取位置管理服务
        if (locationManager == null) {
            String serviceName = Context.LOCATION_SERVICE;
            locationManager = (LocationManager) this.getSystemService(serviceName);
        }
        // 查找到服务信息
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗
//        String provider = locationManager.getBestProvider(criteria, true); // 获取GPS信息
        String provider = locationManager.NETWORK_PROVIDER;
        Location location = locationManager.getLastKnownLocation(provider); // 通过GPS获取位置
        if (location == null) {
            locationManager.requestLocationUpdates(provider, 5000, 1, locationListener);
        } else {
            LocationDate locationInfo = new LocationDate();
            locationInfo.setLatitude(location.getLatitude());
            locationInfo.setLongitude(location.getLongitude());
            DBUtil.saveBeanToPrefences(DBUtil.LOCATION_DATE,locationInfo);

//            Geocoder geocoder = new Geocoder(this, Locale.CHINESE);
//            List<Address> addresses = null;
//            try {
//                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            if (addresses.size()<=0){
//                Log.i(TAG, "获取地址失败!");
//            }
//            Address address = addresses.get(0);
//            String country = address.getCountryName();//得到国家
//            String locality = address.getLocality();//得到城市


        }
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                LocationDate locationInfo = new LocationDate();
                locationInfo.setLatitude(location.getLatitude());
                locationInfo.setLongitude(location.getLongitude());

                DBUtil.saveBeanToPrefences(DBUtil.LOCATION_DATE,locationInfo);
            }
            locationManager.removeUpdates(locationListener);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };


}
