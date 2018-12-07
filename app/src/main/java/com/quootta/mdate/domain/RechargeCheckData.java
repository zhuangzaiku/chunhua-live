package com.quootta.mdate.domain;

/**
 * Created by Ryon on 2016/11/30/0030.
 */

public class RechargeCheckData {

    /**
     * charge : {"id":"ch_C08uH8fLOGmHKSSKG8Lu5WvT","body":"春花聊天充值 600金币","paid":true,"is_first_recharge":true,"subject":"春花聊天充值"}
     */

    private DataBean data;
    /**
     * data : {"charge":{"id":"ch_C08uH8fLOGmHKSSKG8Lu5WvT","body":"春花聊天充值 600金币","paid":true,"is_first_recharge":true,"subject":"春花聊天充值"}}
     * msg : 订单支付情况获取成功
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
        /**
         * id : ch_C08uH8fLOGmHKSSKG8Lu5WvT
         * body : 春花聊天充值 600金币
         * paid : true
         * is_first_recharge : true
         * subject : 春花聊天充值
         */

        private ChargeBean charge;

        public ChargeBean getCharge() {
            return charge;
        }

        public void setCharge(ChargeBean charge) {
            this.charge = charge;
        }

        public static class ChargeBean {
            private String id;
            private String body;
            private boolean paid;
            private boolean is_alert_groupcall;
            private String subject;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getBody() {
                return body;
            }

            public void setBody(String body) {
                this.body = body;
            }

            public boolean isPaid() {
                return paid;
            }

            public void setPaid(boolean paid) {
                this.paid = paid;
            }

            public boolean is_alert_groupcall() {
                return is_alert_groupcall;
            }

            public void is_alert_groupcall(boolean is_first_recharge) {
                this.is_alert_groupcall = is_first_recharge;
            }

            public String getSubject() {
                return subject;
            }

            public void setSubject(String subject) {
                this.subject = subject;
            }
        }
    }
}
