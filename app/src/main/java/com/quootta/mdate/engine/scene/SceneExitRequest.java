package com.quootta.mdate.engine.scene;

import com.quootta.mdate.base.StringForJsonRequest;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.utils.LogUtil;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Ryon on 2017/1/10/0010.
 */

public class SceneExitRequest extends StringForJsonRequest {
    public SceneExitRequest( Map<String, String> paramsMap, Response.Listener<JSONObject> listener) {
        super(Method.POST, LocalUrl.POST_SCENE_EXIT, paramsMap, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.e("SceneExitRequest","Error:" + error.toString());
            }
        });

    }
}
