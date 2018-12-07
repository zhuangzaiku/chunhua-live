package com.quootta.mdate.engine.scene;

import com.quootta.mdate.base.StringForJsonRequest;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.utils.LogUtil;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Ryon on 2017/1/9/0009.
 */

public class SceneListRequest extends StringForJsonRequest {
    public SceneListRequest(Map<String,String> sceneMap,Response.Listener<JSONObject> listener) {
        super(Method.GET, LocalUrl.GET_SCENE_LIST, sceneMap, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.e("SceneListRequest","Error:" + error.toString());
            }
        });
    }
}
