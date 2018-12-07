package com.quootta.mdate.domain;

/**
 * Created by Ryon on 2016/9/22/0022.
 */
public class AccountSecurityData {

    /**
     * code : 0
     * msg : 获取账户安全成功
     * data : {"login_type":"qq","weibo_bind":false,"wechat_bind":false,"qq_bind":true,"mobile_bind":false,"mobile":"","qq_name":"春花聊天交友","weibo_name":"","wechat_name":""}
     */

    private int code;
    private String msg;
    /**
     * login_type : qq
     * weibo_bind : false
     * wechat_bind : false
     * qq_bind : true
     * mobile_bind : false
     * mobile :
     * qq_name : 春花聊天交友
     * weibo_name :
     * wechat_name :
     */

    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String login_type;
        private boolean weibo_bind;
        private boolean wechat_bind;
        private boolean qq_bind;
        private boolean mobile_bind;
        private String mobile;
        private String qq_name;
        private String weibo_name;
        private String wechat_name;

        public String getLogin_type() {
            return login_type;
        }

        public void setLogin_type(String login_type) {
            this.login_type = login_type;
        }

        public boolean isWeibo_bind() {
            return weibo_bind;
        }

        public void setWeibo_bind(boolean weibo_bind) {
            this.weibo_bind = weibo_bind;
        }

        public boolean isWechat_bind() {
            return wechat_bind;
        }

        public void setWechat_bind(boolean wechat_bind) {
            this.wechat_bind = wechat_bind;
        }

        public boolean isQq_bind() {
            return qq_bind;
        }

        public void setQq_bind(boolean qq_bind) {
            this.qq_bind = qq_bind;
        }

        public boolean isMobile_bind() {
            return mobile_bind;
        }

        public void setMobile_bind(boolean mobile_bind) {
            this.mobile_bind = mobile_bind;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getQq_name() {
            return qq_name;
        }

        public void setQq_name(String qq_name) {
            this.qq_name = qq_name;
        }

        public String getWeibo_name() {
            return weibo_name;
        }

        public void setWeibo_name(String weibo_name) {
            this.weibo_name = weibo_name;
        }

        public String getWechat_name() {
            return wechat_name;
        }

        public void setWechat_name(String wechat_name) {
            this.wechat_name = wechat_name;
        }
    }
}
