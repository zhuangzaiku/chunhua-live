package com.quootta.mdate.base;

import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.utils.LogUtil;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class StringForJsonRequest extends Request<JSONObject> {

    private int mMethod;
    private String mUrl;
    private Map<String,String> paramsMap;
    private Response.Listener<JSONObject> mListener;



    public StringForJsonRequest(int method, String url, Map<String,String> paramsMap, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mMethod = method;
        this.mUrl = url;
        this.paramsMap = paramsMap;
        this.mListener = listener;
        LogUtil.d("Url",url);
        LogUtil.d("UDID",BaseApp.getDeviceId());
        paramsMap.put("UDID", BaseApp.getDeviceId());

        setRetryPolicy(new DefaultRetryPolicy(5500,3,1f));

    }

    /**
     * 重写getUrl方法，为get请求添加参数
     * @author Ryon
     * @email para.ryon@foxmail.com
     * @return
     */
    @Override
    public String getUrl() {
        if(mMethod == Request.Method.GET) {
            StringBuilder stringBuilder = new StringBuilder(mUrl);
            Iterator<Map.Entry<String, String>> iterator = paramsMap.entrySet().iterator();
            int i = 1;
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                stringBuilder.append("&" + entry.getKey() + "=" + entry.getValue());
//                if(i == 1) {
//                    stringBuilder.append("?" + entry.getKey() + "=" + entry.getValue());
//                } else {
//                }
                iterator.remove(); // avoids a ConcurrentModificationException
                i++;
            }
            mUrl = stringBuilder.toString();
            LogUtil.d("StringForJsonRequest","mUrl = "+mUrl);

        }
        return mUrl;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        LogUtil.d("StringForJsonRequest",paramsMap.toString());
        return paramsMap;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();
        if (headers == null|| headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
        }
        BaseApp.getApplication().addSessionCookie(headers);
        headers.put("User-Agent", LocalUrl.getVersionCode() + " (" + android.os.Build.MODEL + "; Android" + android.os.Build.VERSION.RELEASE + ")");

        LogUtil.d("StringForJsonRequest", "UserAgent: " + LocalUrl.getVersionCode() + " (" + android.os.Build.MODEL + "; Android" + android.os.Build.VERSION.RELEASE + ")");

        return headers;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {



            BaseApp.getApplication().checkSessionCookie(response.headers);
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            return Response.success(new JSONObject(jsonString),HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        mListener.onResponse(response);
    }

}
