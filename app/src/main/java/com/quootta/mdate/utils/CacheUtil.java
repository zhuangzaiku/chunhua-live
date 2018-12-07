package com.quootta.mdate.utils;

import android.content.Context;

import java.net.CookieStore;
public class CacheUtil {
    private static ACache acache;
    private static CookieStore cookieStore;
    private static Context context;

    public CacheUtil(Context context) {
        this.context = context;
    }

    private ACache getInstance() {
        if (acache == null) {
            acache = ACache.get(context);
        }
        return acache;
    }

    public void setUsername(String username) {
        getInstance().put("username", username);
    }

    public String getUsername() {
        if (getInstance().getAsString("username") != null) {
            return getInstance().getAsString("username");
        }
        return null;
    }

    public void setPassword(String password) {
        getInstance().put("password", password);
    }

    public String getPassword() {
        if (getInstance().getAsString("password") != null) {
            return getInstance().getAsString("password");
        }
        return null;
    }

    public void setMainPageData(String mainPageData) {
        getInstance().put("main_page_data", mainPageData);
    }

    public String getMainPageData() {
        if (getInstance().getAsString("main_page_data") != null) {
            return getInstance().getAsString("main_page_data");
        }
        return null;
    }

    public void setConnoisseurData(String data) {
        getInstance().put("connoisseur_data", data);
    }

    public String getConnoisseurData() {
        if (getInstance().getAsString("connoisseur_data") != null) {
            return getInstance().getAsString("connoisseur_data");
        }
        return null;
    }

    public void setMeetingData(String data) {
        getInstance().put("meeting_data", data);
    }

    public String getMeetingData() {
        if (getInstance().getAsString("meeting_data") != null) {
            return getInstance().getAsString("meeting_data");
        }
        return null;
    }
    public void setActivityData(String data) {
        getInstance().put("activity_data", data);
    }

    public String getActivityData() {
        if (getInstance().getAsString("activity_data") != null) {
            return getInstance().getAsString("activity_data");
        }
        return null;
    }

    public void setUserInfo(String data) {
        getInstance().put("user_info", data);
    }

    public String getUserInfo() {
        if (getInstance().getAsString("user_info") != null) {
            return getInstance().getAsString("user_info");
        }
        return null;
    }

//    /**
//     * 设置cookiestore
//     * @param c
//     */
//    public static void setCookieStore(CookieStore c) {
//        cookieStore = c;
//    }
//
//    /**
//     * 获得cookiestore
//     * @return
//     */
//
//    public static CookieStore getCookieStore() {
//        if (cookieStore == null) {
//            return null;
//        }
//        return cookieStore;
//    }
}