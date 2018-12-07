package com.quootta.mdate.domain;

/**
 * Created by Ryon on 2017/1/9/0009.
 */

public class Scene_chek_apply {


    /**
     * msg : 可申请
     * code : 2000
     * if_apply : true
     */

    private DataBean data;
    /**
     * data : {"msg":"可申请","code":"2000","if_apply":true}
     * msg : 请求成功
     * code : 0
     */

    private String msg;
    private int code;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class DataBean {
        private String msg;
        private String code;
        private boolean if_apply;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public boolean isIf_apply() {
            return if_apply;
        }

        public void setIf_apply(boolean if_apply) {
            this.if_apply = if_apply;
        }
    }
}
