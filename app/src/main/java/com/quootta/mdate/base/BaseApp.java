package com.quootta.mdate.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDex;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.quootta.mdate.BuildConfig;
import com.quootta.mdate.R;
import com.quootta.mdate.constant.LocalConfig;
import com.quootta.mdate.myListener.ConversationBehaviorListener;
import com.quootta.mdate.myListener.GiftListener;
import com.quootta.mdate.myListener.RongReceiveMessageListener;
import com.quootta.mdate.plugin.MyExtensionModule;
import com.quootta.mdate.provider.RongGiftMessageProvider;
import com.quootta.mdate.ui.message.RongGiftMessage;
import com.quootta.mdate.utils.BitmapLruCache;
import com.quootta.mdate.utils.FileUtil;
import com.quootta.mdate.utils.GlideImageLoader;
import com.quootta.mdate.utils.LogUtil;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.umeng.socialize.PlatformConfig;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.jpush.android.api.JPushInterface;
import io.rong.callkit.SingleCallActivity;
import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;



public class BaseApp extends Application{

    //Cookie相关
    private static final String SET_COOKIE_KEY = "set-cookie";
    private static final String COOKIE_KEY = "Cookie";
    private SharedPreferences cookiePreferences;
    private static final  int BALANCE=999999999;


    private static final String WXPARTNERID="1313632901";


    private static final String WXAPPID="wx0646eab49bdb5c52";







    private static String DEVICE_ID = null;
    // 获取到主线程的上下文
    private static BaseApp mContext = null;
    // 获取到主线程的handler
    private static Handler mMainThreadHandler = null;
    // 获取到主线程的looper
    private static Looper mMainThreadLooper = null;
    // 获取到主线程
    private static Thread mMainThead = null;
    // 获取到主线程的id
    private static int mMainTheadId;
    //全局请求队列
    public static RequestQueue requestQueue;
    //全局图片载入器
    private static int memClass;
    public static ImageLoader imageLoader;
    //图片默认保存目录
    public static final String picFile = "/sdcard/ChunhuaPic/";

    private static int goldCount;//账户金币余额
    private static int balnace;//每分钟扣除费用的余额

    private static String isVisual;//可见状态
    private static Boolean isAudioEnable = false;//是否接受语音请求
    private static Boolean isVideoEnable = false;//是否接受视频请求
    private static int audioPrice;//音频价格
    private static int videoPrice;//视频价格


    private static Boolean isSpecial;//是否特殊账号


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        MultiDex.install(this);
        initJPush();
        initRongCloud();
        initVolley();
        initOkHttp();
        initGalleryFinal();

//        initWeixinShare();
//        initQqSahrre();
//        initSianShare();

        mMainThreadHandler = new Handler();
        mMainThreadLooper = getMainLooper();
        mMainThead = Thread.currentThread();
        // android.os.Process.myUid() 获取到用户id
        // android.os.Process.myPid()获取到进程id
        // android.os.Process.myTid()获取到调用线程的id
        mMainTheadId = android.os.Process.myTid();

        cookiePreferences = getSharedPreferences("CookieStore",MODE_PRIVATE);

        BaseApp.setBalnace(BALANCE);
        Log.i("RongCallListener","--------baseapp.setbanlance---->"+BaseApp.getBalnace());



        //初始化礼物监听
        SingleCallActivity.setOnGiftClick(new GiftListener());

        //英文
        updateLocale(this,Locale.CHINESE);

    }





    //配置第三方平台的appkey
    private void initWeixinShare() {
        PlatformConfig.setWeixin("", "");
    }

    private void initQqSahrre(){
        PlatformConfig.setQQZone("","");
    }
    private void initSianShare(){
        PlatformConfig.setSinaWeibo("","");
    }



    /**
     * 初始化OKHttp全局配置
     */
    private void initOkHttp() {
        OkHttpUtils okHttpUtils = OkHttpUtils.getInstance();
        okHttpUtils.setConnectTimeout(600, TimeUnit.SECONDS);
        okHttpUtils.setReadTimeout(600, TimeUnit.SECONDS);
        okHttpUtils.setWriteTimeout(600, TimeUnit.SECONDS);
    }

    /**
     * 初始化GalleryFinal全局配置
     */
    private void initGalleryFinal() {
        FileUtil.createApplicationFolder();//创建视频缓存文件夹
        //配置主题
        //ThemeConfig
        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(getResources().getColor(R.color.color_ec76a2))
                .setTitleBarTextColor(Color.WHITE)
                .setTitleBarIconColor(Color.WHITE)
                .setFabNornalColor(getResources().getColor(R.color.color_ec76a2))
                .setFabPressedColor(getResources().getColor(R.color.color_ec76a2))
                .setCheckNornalColor(Color.GRAY)
                .setCheckSelectedColor(getResources().getColor(R.color.color_ec76a2))
                .setCropControlColor(getResources().getColor(R.color.myWhite))
                .build();
        //配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setCropSquare(true)
                .setForceCrop(true)
                .build();

        //配置imageloader
        cn.finalteam.galleryfinal.ImageLoader imageloader = new GlideImageLoader();
        //设置核心配置信息
        CoreConfig coreConfig = new CoreConfig.Builder(mContext, imageloader, theme)
                .setFunctionConfig(functionConfig)
                .setTakePhotoFolder(new File(Environment.getExternalStorageDirectory() + File.separator + LocalConfig.FILE_DIR))
                .build();
        GalleryFinal.init(coreConfig);
    }

    /**
     * 初始化Volley
     */
    private void initVolley() {
        //获取Volley全局请求队列
        requestQueue = Volley.newRequestQueue(this);
        //获取ImageLoader全局实例
        memClass = ((ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        int cacheSize = 1024 * 1024 * memClass / 8;
        imageLoader = new ImageLoader(requestQueue, new BitmapLruCache(cacheSize));
    }

    /**
     * IMKit SDK调用第一步 初始化
     */
    public void initRongCloud() {
        //这段保护代码，确保只有需要使用 RongIM 的进程和 Push 进程执行了 init。
        //io.rong.push 为融云 push 进程名称，不可修改。
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {
            RongIM.init(this);

            //注册礼物消息
            RongIM.registerMessageType(RongGiftMessage.class);
            //注册礼物消息模板，可以控制红包消息显示样式
            RongIM.registerMessageTemplate(new RongGiftMessageProvider());
            //扩展功能自定义
//            InputProvider.ExtendProvider[] provider = {
//                    new VideoCallInputProvider(RongContext.getInstance()),//视频
//                    new AudioCallInputProvider(RongContext.getInstance()),//音频
//                    new ImageInputProvider(RongContext.getInstance()),//图片
//                    new CameraInputProvider(RongContext.getInstance()),//相机
//                  //  new RedPacketProvider(RongContext.getInstance()),//红包表白
//                    new GiftProvider(RongContext.getInstance()),//礼物
//            };
  //          RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.PRIVATE, provider);

            //2.8.5扩展功能自定义
            List<IExtensionModule> moduleList = RongExtensionManager.getInstance().getExtensionModules();
            IExtensionModule defaultModule=null;
            if (moduleList!=null){
                for (IExtensionModule module:moduleList){

                    if (module instanceof DefaultExtensionModule){
                        defaultModule=module;
                        break;
                    }
                }
                if (defaultModule!=null){
                    RongExtensionManager.getInstance().unregisterExtensionModule(defaultModule);
                    RongExtensionManager.getInstance().registerExtensionModule(new MyExtensionModule());
                }
            }


            RongIM.setConversationBehaviorListener(new ConversationBehaviorListener());

            //收到的消息处理
            RongIM.getInstance().setOnReceiveMessageListener(new RongReceiveMessageListener());

        }
    }

    /**
     * 初始化极光推送
     */
    private void initJPush() {
        JPushInterface.setDebugMode(BuildConfig.LOG_DEBUG ? true : false);
        JPushInterface.init(this);

    }

    public static BaseApp getApplication() {
        return mContext;
    }

    public static String getDeviceId() {
        if (DEVICE_ID == null) {
            TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            DEVICE_ID = tm.getDeviceId();
        }
        return DEVICE_ID;
    }
    /**
     * 获取渠道名
     * @param context 此处习惯性的设置为activity，实际上context就可以
     * @return 如果没有获取成功，那么返回值为空
     */
    public static String getChannelName(Context context) {
        if (context == null) {
            return null;
        }
        String channelName = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                ApplicationInfo applicationInfo = packageManager.
                        getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelName = String.valueOf(applicationInfo.metaData.get("UMENG_CHANNEL"));
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channelName;
    }


    public static RequestQueue getRequestQueue(){
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mContext);
        }
        return requestQueue;
    }

    public static ImageLoader getImageLoader() {
        getRequestQueue();
        if (imageLoader == null) {
            int cacheSize = 1024 * 1024 * memClass / 8;
            imageLoader = new ImageLoader(requestQueue, new BitmapLruCache(cacheSize));
        }
        return imageLoader;
    }

    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    public static Looper getMainThreadLooper() {
        return mMainThreadLooper;
    }

    public static Thread getMainThread() {
        return mMainThead;
    }

    public static int getMainThreadId() {
        return mMainTheadId;
    }

    /**
     * Checks the response headers for session cookie and saves it
     * if it finds it.
     * @param headers Response Headers.
     */
    public final void checkSessionCookie(Map<String, String> headers) {
        if (headers.containsKey(SET_COOKIE_KEY)
//                && headers.get(SET_COOKIE_KEY).startsWith(SESSION_COOKIE)
                ) {
            String cookie = headers.get(SET_COOKIE_KEY);
            if (cookie.length() > 0) {
                String[] splitCookie = cookie.split(";");
                cookie = splitCookie[0];
                SharedPreferences.Editor prefEditor = cookiePreferences.edit();
                prefEditor.putString(COOKIE_KEY, cookie);
                LogUtil.d("BaseApp","获取cookie到本地 save cookie:" + cookie);
                prefEditor.commit();
            } else {
            }
        }
    }

    /**
     * Adds session cookie to headers if exists.
     * @param headers
     */
    public final void addSessionCookie(Map<String, String> headers) {
        String cookie = cookiePreferences.getString(COOKIE_KEY, "");
        if (cookie.length() > 0) {
            StringBuilder cookieBuilder = new StringBuilder();
            cookieBuilder.append(cookie);
            if (headers.containsKey(COOKIE_KEY)) {
                cookieBuilder.append("; ");
                cookieBuilder.append(headers.get(COOKIE_KEY));
            }
            headers.put(COOKIE_KEY, cookieBuilder.toString());
            LogUtil.d("BaseApp", "发送Cookie到服务器 addSessionCookie:" + cookie);
        } else {

        }
    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    public void updateLocale(Context pContext, Locale pNewUserLocale) {
        Configuration _Configuration = pContext.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            _Configuration.setLocale(pNewUserLocale);
        } else {
            _Configuration.locale = pNewUserLocale;
        }
        DisplayMetrics _DisplayMetrics = pContext.getResources().getDisplayMetrics();
        pContext.getResources().updateConfiguration(_Configuration, _DisplayMetrics);
    }

    public static String getWXPARTNERID() {
        return WXPARTNERID;
    }

    public static String getWXAPPID() {
        return WXAPPID;
    }


    public static int getBalnace() {
        return balnace;
    }

    public static void setBalnace(int balnace) {
        BaseApp.balnace = balnace;
    }

    public static int getGoldCount() {
        return goldCount;
    }

    public static void setGoldCount(String gold) {
        goldCount = Integer.parseInt(gold);
    }

    public static String getIsVisual() {
        return isVisual;
    }

    public static void setIsVisual(String isVisual) {
        BaseApp.isVisual = isVisual;
    }

    public static Boolean getIsAudioEnable() {
        return isAudioEnable;
    }

    public static void setIsAudioEnable(Boolean isAudioEnable) {
        LogUtil.d("BaseApp", "setIsVideoEnable: " + BaseApp.isVideoEnable);
        BaseApp.isAudioEnable = isAudioEnable;
    }

    public static Boolean getIsVideoEnable() {
        return isVideoEnable;
    }

    public static void setIsVideoEnable(Boolean isVideoEnable) {
        BaseApp.isVideoEnable = isVideoEnable;
        LogUtil.d("BaseApp", "setIsVideoEnable: " + BaseApp.isVideoEnable);
    }

    public static int getVideoPrice() {
        return videoPrice;
    }

    public static void setVideoPrice(int videoPrice) {
        BaseApp.videoPrice = videoPrice;
    }

    public static int getAudioPrice() {
        return audioPrice;
    }

    public static void setAudioPrice(int audioPrice) {
        BaseApp.audioPrice = audioPrice;
    }


    public static Boolean getIsSpecial() {
        return isSpecial;
    }

    public static void setIsSpecial(Boolean isSpecial) {
        BaseApp.isSpecial = isSpecial;
    }
}
