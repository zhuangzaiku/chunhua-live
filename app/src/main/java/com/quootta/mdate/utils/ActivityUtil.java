package com.quootta.mdate.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.quootta.mdate.base.BaseActivity;

import java.util.List;
import java.util.Stack;

/**
 * Created by Ryon on 2016/2/14.
 * email:para.ryon@foxmail.com
 */
public class ActivityUtil {
    private static Stack<BaseActivity> activityStack = new Stack<>();

    /**
     * 添加activity到栈
     *
     * @param activity 要添加的activity
     */
    public static void addActivity(BaseActivity activity) {
        activityStack.add(activity);
    }

    /** vb
     * 从栈中删除activity
     *
     * @param activity 要删除的activity
     */
    public static void removeActivity(BaseActivity activity) {
        activityStack.remove(activity);
    }

    /**
     * 获取当前的activity
     *
     * @return
     */
    public static BaseActivity getCurrentActivity() {
        return activityStack.lastElement();
    }


    /**
     * 结束当前activity
     */
    public static void finishActivty() {
        finishActivity(getCurrentActivity());
    }

    /**
     * 结束指定activity
     *
     * @param activity
     */
    public static void finishActivity(BaseActivity activity) {
        if (activity != null && !activity.isFinishing()) {
            activity.finish();
        }
    }

    /**
     * 结束指定的activity
     *
     * @param index 位置
     */
    public static void finishActivity(int index) {
        if (index < activityStack.size() && index + activityStack.size() > 0) {
            finishActivity((activityStack.size() + index) % activityStack.size());
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public static void finishActivity(Class<?> cls) {
        for (BaseActivity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }
    /**
     * 结束所有Activity
     */
    public static void finishAllActivity() {
        while (activityStack.size()>0){
            finishActivity(activityStack.get(0));
        }
        activityStack.clear();
    }
    /**
     * 获取指定的Activity
     *
     */
    public static BaseActivity getActivity(Class<?> cls) {
        for (BaseActivity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                return activity;
            }
        }
        return null;
    }

    /**
     * 退出应用程序
     */
    public static void appExit(Context context) {
        try {
            finishAllActivity();
            // 杀死该应用进程
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception ignored) {
        }
    }

    /**
     * 判断应用是否已经启动
     * @param context 一个context
     * @param packageName 要判断应用的包名
     * @return boolean
     */
    public static boolean isAppAlive(Context context, String packageName){
        ActivityManager activityManager =
                (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos
                = activityManager.getRunningAppProcesses();
        for(int i = 0; i < processInfos.size(); i++){
            if(processInfos.get(i).processName.equals(packageName)){
                Log.i("NotificationLaunch",
                        String.format("the %s is running, isAppAlive return true", packageName));
                return true;
            }
        }
        Log.i("NotificationLaunch",
                String.format("the %s is not running, isAppAlive return false", packageName));
        return false;
    }

    public static String getChannelString(Context context) {
        ApplicationInfo info= null;
        try {
            info = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info.metaData.getString("UMENG_CHANNEL");
    }
}
