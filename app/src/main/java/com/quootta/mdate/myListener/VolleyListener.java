package com.quootta.mdate.myListener;

import android.util.Log;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.helper.LoginHelper;
import com.quootta.mdate.utils.LogUtil;
import com.quootta.mdate.utils.ToastUtil;
import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kky on 2016/4/20.
 */
public abstract class VolleyListener implements Response.Listener<JSONObject> {

    @Override
    public void onResponse(JSONObject response) {
        LogUtil.d("VolleyListener", "Response : " + response);
        try {
            LoginHelper loginHelper = new LoginHelper(BaseApp.getApplication(), null);
            String code = response.getString("code");
            String msg = response.getString("msg");
            switch (code) {
                case LocalUrl.SUCCESS_CODE:
                    onSuccess(response);
                    break;
                case LocalUrl.BUG_ERROR://未知错误	出bug了
                    ToastUtil.showToast(msg);
                    onFail(response);
                    break;
                case LocalUrl.PARAM_ERROR://参数错误	参数不全，或者参数有误
                    ToastUtil.showToast(msg + BaseApp.getApplication().getString(R.string.app_tips_text2));
                    onFail(response);
                    break;
                case LocalUrl.LOG_STATUS_ERROR://账号未登录或登录已失效，请重新登录	账号未登录或登录已失效	应提示重新登录
                    ToastUtil.showToast(msg);
                    Log.i("tag","账号未登录或登录已失效，请重新登录\t账号未登录或登录已失效\t应提示重新登录");
                    loginHelper.logout();
                    onFail(response);
                    break;
                case LocalUrl.VERSION_ERROR://客户端版本过低，请升级客户端后重试	版本太低，出现了不兼容的api	应提示升级应用
                    ToastUtil.showToast(msg);
                    onFail(response);
                    break;
                case LocalUrl.SERVER_MAINTAINED://服务器正在维护，请稍后重试	服务器正在维护	应提示退出应用
                    Log.i("tag","服务器正在维护，请稍后重试\t服务器正在维护\t应提示退出应用");
                    ToastUtil.showToast(msg);
                    onFail(response);

                    loginHelper.logout();
                    break;
                case LocalUrl.SYSTEM_ERROR://系统错误，请稍候重试	出bug了
                    ToastUtil.showToast(msg);
                    onFail(response);
                    break;
                case LocalUrl.TOO_OFTEN_REQUEST_ERROR ://请求过频繁	超过了请求数量限制
                    ToastUtil.showToast(msg + BaseApp.getApplication().getString(R.string.app_tips_text3));
                    onFail(response);
                    break;
                case LocalUrl.BLACK_LIST:
                    Log.i("tag","黑名单");
                    ToastUtil.showToast(msg + "");
                    onFail(response);
                    loginHelper.logout();
                default://非全局的错误
                    onFail(response);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    protected abstract void onSuccess(JSONObject response);


    protected void onFail(JSONObject response) {
        try {
            ToastUtil.showToast(response.getString("msg"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
