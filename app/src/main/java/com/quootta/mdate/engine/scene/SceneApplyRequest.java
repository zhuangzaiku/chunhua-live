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

public class SceneApplyRequest extends StringForJsonRequest {
    public SceneApplyRequest(Map<String,String> ApplyApplyMap,Response.Listener<JSONObject> listener) {
        super(Method.POST, LocalUrl.POST_SCENE_APPLY, ApplyApplyMap, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.e("SceneApplyRequest","Error:" + error.toString());
            }
        });
    }
}
