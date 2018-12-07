package com.quootta.mdate.domain;

/**
 * Created by Ryon on 2016/11/24/0024.
 */

public class OnOffData {

    /**
     * code : 0
     * msg : 获取系统设置成功
     * data : {"config":{"recharge":"on","withdraw":"on","av_android":"on","av_ios":"on","member":"on","gift":"on","red_envelope":"on"}}
     */

    private int code;
    private String msg;
    /**
     * config : {"recharge":"on","withdraw":"on","av_android":"on","av_ios":"on","member":"on","gift":"on","red_envelope":"on"}
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
        /**
         * recharge : on
         * withdraw : on
         * av_android : on
         * av_ios : on
         * member : on
         * gift : on
         * red_envelope : on
         */

        private ConfigBean config;

        public ConfigBean getConfig() {
            return config;
        }

        public void setConfig(ConfigBean config) {
            this.config = config;
        }

        public static class ConfigBean {
            private String recharge;
            private String withdraw;
            private String av_android;
            private String av_ios;
            private String member;
            private String gift;
            private String red_envelope;

            public String getChatlive() {
                return chatlive;
            }

            public void setChatlive(String chatlive) {
                this.chatlive = chatlive;
            }

            private String chatlive;

            public String getRecharge() {
                return recharge;
            }

            public void setRecharge(String recharge) {
                this.recharge = recharge;
            }

            public String getWithdraw() {
                return withdraw;
            }

            public void setWithdraw(String withdraw) {
                this.withdraw = withdraw;
            }

            public String getAv_android() {
                return av_android;
            }

            public void setAv_android(String av_android) {
                this.av_android = av_android;
            }

            public String getAv_ios() {
                return av_ios;
            }

            public void setAv_ios(String av_ios) {
                this.av_ios = av_ios;
            }

            public String getMember() {
                return member;
            }

            public void setMember(String member) {
                this.member = member;
            }

            public String getGift() {
                return gift;
            }

            public void setGift(String gift) {
                this.gift = gift;
            }

            public String getRed_envelope() {
                return red_envelope;
            }

            public void setRed_envelope(String red_envelope) {
                this.red_envelope = red_envelope;
            }
        }
    }
}
