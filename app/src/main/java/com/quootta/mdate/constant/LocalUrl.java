package com.quootta.mdate.constant;

import com.quootta.mdate.BuildConfig;

/**
 * Created by Ryon on 2016/3/2.
 * email:para.ryon@foxmail.com
 */
public class LocalUrl {


    private static final String VERSION_CODE = "Chunhua/0.0.1";


    private static final String Protocal_http = "http://";
    private static final String Protocal_https = "https://";
    private static final String API = "/api";
    //    private static final String URL_QUERY = "?_v=0.0.1";
    private static final String URL_QUERY = "?_v=01.00.09";

    /**
     * 下载地址
     */
    public static final String DOWNLOAD_URL = "http://web.aflive.xyz/public/app/chunhua-release.apk";

    /**
     * Status Code
     */
    public static final String SUCCESS_CODE = "0";//请求成功
    public static final String BUG_ERROR = "-1";//未知错误	出bug了
    public static final String PARAM_ERROR = "1";	//参数错误	参数不全，或者参数有误
    public static final String LOG_STATUS_ERROR = "2";	//账号未登录或登录已失效，请重新登录	账号未登录或登录已失效	应提示重新登录
    public static final String VERSION_ERROR = "3";	//客户端版本过低，请升级客户端后重试	版本太低，出现了不兼容的api	应提示升级应用
    public static final String SERVER_MAINTAINED = "4";	//服务器正在维护，请稍后重试	服务器正在维护	应提示退出应用
    public static final String SYSTEM_ERROR = "5";	//系统错误，请稍候重试	出bug了
    public static final String TOO_OFTEN_REQUEST_ERROR = "6";	//请求过频繁	超过了请求数量限制
    public static final String BLACK_LIST = "7";  //用户被拉黑名单

    public static final String ACCOUNT_ERROR = "1003";	//账号或者密码错误


    /**
     * Dev版本
     */
    private static final String CONSOLE_URL_DEV = "";
    private static final String SERVER_URL_DEV = "web.aflive.xyz";
    private static final String CDN_URL_DEV = "web.aflive.xyz";

    /**
     *Release版本
     * 域名自己配置
     */
    private static final String CONSOLE_URL = "";
    private static final String SERVER_URL = "web.aflive.xyz";
    private static final String CDN_URL = "web.aflive.xyz";
    /**
     * 服务器地址切换开关
     */
    private static final String CURRENT_SERVER_URL = BuildConfig.LOG_DEBUG ? SERVER_URL_DEV : SERVER_URL;
    private static final String CURRENT_CND_URL = BuildConfig.LOG_DEBUG ? CDN_URL_DEV : CDN_URL;

    /**
     * 芝麻信用APP ID
     */
    private static final String SESAME_DEV = "";
    private static final String SESAME_RELEASE = "";
    public static final String SESAME_ID = BuildConfig.LOG_DEBUG ? SESAME_DEV : SESAME_RELEASE;


    /**
     * 账号相关
     */
    public static final String POST_LOGIN = getURL("/login");
    public static final String GET_LOGOUT  = getURL("/logout");
    public static final String POST_SIGN_UP_SMS = getURL("/signup.sms");
    public static final String POST_SIGN_UP_MOBILE = getURL("/signup.mobile");
    public static final String POST_SIGN_UP_PASSWORD = getURL("/signup.password");
    //public static final String POST_PASSWORD_RESET = getURL("/password.reset");
    public static final String POST_SIGN_UP_INFO = getURL("/signup.info");
    public static final String POST_SIGN_UP_APPLY = getURL("/signup.apply");
    public static final String POST_FIND_PWD_SMS = getURL("/findpwd.sms");
    public static final String POST_FIND_PWD_MOBILE = getURL("/findpwd.mobile");
    public static final String POST_FIND_PWD_RESET = getURL("/findpwd.reset");

    /**
     * 第三方登录相关
     */
    public static final String POST_PLATFORM_SIGN_UP =getURL("/platform.signup");
    public static final String POST_PLATFORM_LOGIN =getURL("/platform.login");

    /**
     * 全局开关
     */
    public static final String GET_SYS_CONFIG=getURL("/sys.config");

    /**
     * Home 约Ta
     */
    public static final String GET_LIST= getURL("/user.list");
    public static final String GET_HOT= getURL("/user.list.hot");
    public static final String GET_NEWEST= getURL("/user.list.newest");
    public static final String GET_BATCH= getURL("/user.batch");
    public static final String GET_DETAIL= getURL("/user.detail");
    public static final String POST_INVITE= getURL("/invite.create");
    //public static final String GET_GIFT_LIST= getURL("/gift.list");可以和礼物接口 共用
    //public static final String POST_GIFT_SEND_TO = getURL("/gift.send.to");可以和礼物接口 共用
    public static final String POST_UNLOCK_USER = getURL("/unlock.user");
    public static final String GET_CALL_LIST = getURL("/call.list");



    /**
     *
     * 礼物
     */
    public static final String GET_GIFT_LIST=getURL("/gift.list");
    public static final String POST_GIFT_SEND_TO=getURL("/gift.send.to");
    public static final String GET_LIST_RECEIVE= getURL("/gift.list.receive");
    public static final String GET_GIFT_LIST_SEND=getURL("//gift.list.send");


    /**
     *场景
     */

    public static final String GET_SCENE_LIST=getURL("/scene.list");
    public static final String GET_SCENE_CHECK_APPLY=getURL("/scene.check.apply");
    public static final String POST_SCENE_APPLY=getURL("/scene.apply");
    public static final String POST_SCENE_EXIT=getURL("/scene.exit");



    /**
     * Home 聊天
     * 聊天使用融云SDK
     */
    public static final String GET_INVITE_TO_ME= getURL("/invite.to.me");
    public static final String GET_INVITE_FROM_ME = getURL("/invite.from.me");
    public static final String GET_INVITE_DETAIL= getURL("/invite.detail");
    public static final String POST_INVITE_OPERATE = getURL("/invite.operate");

    /**
     * Home 个人中心
     */
    public static final String POST_STATUS= getURL("/my.status");
    public static final String POST_VISIBLE= getURL("/my.visible");
    public static final String POST_ANSWER_STATUS= getURL("/my.phone.ring");
    public static final String POST_VERIFY_VIDEO= getURL("/my.verify.video");
    public static final String GET_VERIFY_ZHIMA= getURL("/my.verify.zhima");
    public static final String POST_VERIFY_ZHIMA_CHECK= getURL("/my.verify.zhima.check");
    public static final String POST_ADD_BLACKLIST= getURL("/my.blacklist.add");
    public static final String POST_REMOVE_BLACKLIST= getURL("/my.blacklist.remove");
    public static final String GET_RYTOKEN= getURL("/my.rytoken");
    public static final String GET_INFO_DETAIL= getURL("/my.info.detail");
    public static final String POST_INFO_EDIT= getURL("/my.info.edit");
    public static final String GET_NOTICE= getURL("/my.notice");
    public static final String GET_FEED_LIST= getURL("/my.feed.list");
    public static final String GET_FEED_DETAIL= getURL("/feed.detail");
    public static final String POST_FEED_CREATE= getURL("/feed.create");
    public static final String POST_FEED_DETELE= getURL("/feed.delete");
    public static final String POST_AVATAR_UPDATE= getURL("/my.cover.update");

    public static final String GET_MY_LIKE_LIST= getURL("/my.favorite.list");
    public static final String POST_MY_LIKE_ADD= getURL("/my.favorite.add");
    public static final String POST_MY_LIKE_REMOVE= getURL("/my.favorite.remove");
    public static final String POST_BILL_WITHDRAW= getURL("/bill.withdraw");
    public static final String GET_BILL_LIST= getURL("/bill.list");
    public static final String GET_BILL_RECHARGE= getURL("/bill.recharge");
    public static final String GET_BILL_RECHARGE_CHECK= getURL("/bill.recharge.check");
    public static final String POST_BILL_RECHARGE_CHECK_IAP= getURL("/bill.recharge.check.iap");

    /**
     * 充值会员
     */
    public static final String POST_TO_VIP=getURL("/to_vip");

    /**
     * 音频、视频通话
     */
    public static final String POST_CALL_COUNT= getURL("/call.count");
    public static final String POST_PAYMENT= getURL("/my.avpay");
    public static final String GET_BALANCE= getURL("/my.balance");
    public static final String POST_GROUP_CALL = getURL("/group.call");
    public static final String GET_GROUP_CALL_TEXT = getURL("/group.call.text");
    /**
     * 排行榜
     */
    public static final String GET_RANKING_LIST=getURL("/leader_board");
    /**
     * 检查更新
     */
    public static final String CHECK_UPDATE= getURL("/update");

    /**
     * Home 设置
     */
    public static final String POST_FEED_BACK= getURL("/feedback");
    public static final String POST_REPORT= getURL("/report");

    /**
     * Banner
     */
    public static final String GET_BANNER= getURL("/banner");
    public static final String GET_SHARE= getURL("/share");

    /**
     *账户安全
     *
     */

    public static final String GET_ACCOUNT_SECURITY=getURL("/account.security");
    public static final String POST_ACCOUNR_BINDPHONE=getURL("/bindmobile");
    public static final String POST_BIND_PHONE_SMS=getURL("/bindmobile.sms");
    public static final String POST_UNBIND=getURL("/unbind");
    public static final String POST_BIND_PLATFORM=getURL("/bindplatform");
    public static final String POST_PASSWORD_RESET_SMS=getURL("/password.reset.sms");
    public static final String POST_PASSWORD_RESET=getURL("/password.reset");


    //支付
    public static final String GET_WXPAY=getURL("/bill.wechat.preorder");
    public static final String GET_PAY=getBaseURL("/public/payment/index.html");
    public static final String GET_WXCHAT_CHACK=getURL("/bill.wechat.check");

    /**
     * 通话日志
     *
     */
    public static final String POST_RY_RECORD=getURL("/ry.record");


    public static String getVersionCode() {
        StringBuffer versionResult = new StringBuffer(VERSION_CODE);
        return versionResult.toString();
    }

    /**
     *
     * @param urlName 用于请求完整链接的router
     * @return 完整的请求链接
     */
    private static String getURL(String urlName){
        StringBuffer urlResult = new StringBuffer(Protocal_http);
        urlResult.append(CURRENT_SERVER_URL);
        urlResult.append("/api");
        urlResult.append(urlName);
        urlResult.append(URL_QUERY);
        return urlResult.toString();
    }

    private static String getBaseURL(String urlName){
        StringBuffer urlResult = new StringBuffer(Protocal_http);
        urlResult.append(CURRENT_SERVER_URL);
        urlResult.append(urlName);
        urlResult.append(URL_QUERY);
        return urlResult.toString();
    }

    /**
     * 图片接口
     * @param picUrl
     * @return
     */
    public static String getPicUrl(String picUrl) {
        StringBuffer urlResult = new StringBuffer(Protocal_http);
        urlResult.append(SERVER_URL_DEV);
        urlResult.append("/"+picUrl);

        return urlResult.toString();
    }

    /**
     * 视频缩略图接口
     * @param videoUrl
     * @return
     */
    public static String getVideoPicUrl(String videoUrl) {
        StringBuffer urlResult = new StringBuffer(Protocal_http);
        urlResult.append(SERVER_URL_DEV);
        urlResult.append("/"+videoUrl);
        return urlResult.toString();
    }

    /**
     * 视频接口
     * @param videoUrl
     * @return
     */
    public static String getVideoUrl(String videoUrl) {
        StringBuffer urlResult = new StringBuffer(Protocal_http);
        urlResult.append(SERVER_URL_DEV);
        urlResult.append("/"+videoUrl);
//        urlResult.append("?vframe/jpg/offset/1");
        return urlResult.toString();
    }


}