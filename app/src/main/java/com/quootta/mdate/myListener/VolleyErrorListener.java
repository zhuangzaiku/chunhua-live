package com.quootta.mdate.myListener;

import com.android.volley.Response;
import com.android.volley.VolleyError;


public abstract class VolleyErrorListener implements Response.ErrorListener {
    @Override
    public void onErrorResponse(VolleyError error) {
        onFail(error);
    }


    protected abstract void onFail(VolleyError error);
}
