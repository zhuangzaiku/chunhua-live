package com.quootta.mdate.domain;

/**
 * Created by Ryon on 2017/2/20/0020.
 */

public class WxRequestData {

    /**
     * code : 0
     * msg : 订单支付情况获取成功
     * data : {"charge":{"id":"7d23ad8270134e2285f10668e6b10ee6","paid":true,"body":{"return_code":"SUCCESS","return_msg":"OK","appid":"wx0646eab49bdb5c52","mch_id":"1313632901","nonce_str":"nnzA0TOFHsrJWQXj","sign":"36B98F0662D7ACEB426FA8702DBDBAF7","result_code":"SUCCESS","openid":"oLHEzwmTpqFs4_Ny2gHPgKsMWeSM","is_subscribe":"N","trade_type":"APP","bank_type":"CFT","total_fee":"1","fee_type":"CNY","transaction_id":"4007252001201702200635422981","out_trade_no":"7d23ad8270134e2285f10668e6b10ee6","attach":"","time_end":"20170220131145","trade_state":"SUCCESS","cash_fee":"1"},"is_alert_groupcall":true}}
     */

    private int code;
    private String msg;
    /**
     * charge : {"id":"7d23ad8270134e2285f10668e6b10ee6","paid":true,"body":{"return_code":"SUCCESS","return_msg":"OK","appid":"wx0646eab49bdb5c52","mch_id":"1313632901","nonce_str":"nnzA0TOFHsrJWQXj","sign":"36B98F0662D7ACEB426FA8702DBDBAF7","result_code":"SUCCESS","openid":"oLHEzwmTpqFs4_Ny2gHPgKsMWeSM","is_subscribe":"N","trade_type":"APP","bank_type":"CFT","total_fee":"1","fee_type":"CNY","transaction_id":"4007252001201702200635422981","out_trade_no":"7d23ad8270134e2285f10668e6b10ee6","attach":"","time_end":"20170220131145","trade_state":"SUCCESS","cash_fee":"1"},"is_alert_groupcall":true}
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
         * id : 7d23ad8270134e2285f10668e6b10ee6
         * paid : true
         * body : {"return_code":"SUCCESS","return_msg":"OK","appid":"wx0646eab49bdb5c52","mch_id":"1313632901","nonce_str":"nnzA0TOFHsrJWQXj","sign":"36B98F0662D7ACEB426FA8702DBDBAF7","result_code":"SUCCESS","openid":"oLHEzwmTpqFs4_Ny2gHPgKsMWeSM","is_subscribe":"N","trade_type":"APP","bank_type":"CFT","total_fee":"1","fee_type":"CNY","transaction_id":"4007252001201702200635422981","out_trade_no":"7d23ad8270134e2285f10668e6b10ee6","attach":"","time_end":"20170220131145","trade_state":"SUCCESS","cash_fee":"1"}
         * is_alert_groupcall : true
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
            private boolean paid;
            /**
             * return_code : SUCCESS
             * return_msg : OK
             * appid : wx0646eab49bdb5c52
             * mch_id : 1313632901
             * nonce_str : nnzA0TOFHsrJWQXj
             * sign : 36B98F0662D7ACEB426FA8702DBDBAF7
             * result_code : SUCCESS
             * openid : oLHEzwmTpqFs4_Ny2gHPgKsMWeSM
             * is_subscribe : N
             * trade_type : APP
             * bank_type : CFT
             * total_fee : 1
             * fee_type : CNY
             * transaction_id : 4007252001201702200635422981
             * out_trade_no : 7d23ad8270134e2285f10668e6b10ee6
             * attach :
             * time_end : 20170220131145
             * trade_state : SUCCESS
             * cash_fee : 1
             */

            private BodyBean body;
            private boolean is_alert_groupcall;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public boolean isPaid() {
                return paid;
            }

            public void setPaid(boolean paid) {
                this.paid = paid;
            }

            public BodyBean getBody() {
                return body;
            }

            public void setBody(BodyBean body) {
                this.body = body;
            }

            public boolean isIs_alert_groupcall() {
                return is_alert_groupcall;
            }

            public void setIs_alert_groupcall(boolean is_alert_groupcall) {
                this.is_alert_groupcall = is_alert_groupcall;
            }

            public static class BodyBean {
                private String return_code;
                private String return_msg;
                private String appid;
                private String mch_id;
                private String nonce_str;
                private String sign;
                private String result_code;
                private String openid;
                private String is_subscribe;
                private String trade_type;
                private String bank_type;
                private String total_fee;
                private String fee_type;
                private String transaction_id;
                private String out_trade_no;
                private String attach;
                private String time_end;
                private String trade_state;
                private String cash_fee;

                public String getReturn_code() {
                    return return_code;
                }

                public void setReturn_code(String return_code) {
                    this.return_code = return_code;
                }

                public String getReturn_msg() {
                    return return_msg;
                }

                public void setReturn_msg(String return_msg) {
                    this.return_msg = return_msg;
                }

                public String getAppid() {
                    return appid;
                }

                public void setAppid(String appid) {
                    this.appid = appid;
                }

                public String getMch_id() {
                    return mch_id;
                }

                public void setMch_id(String mch_id) {
                    this.mch_id = mch_id;
                }

                public String getNonce_str() {
                    return nonce_str;
                }

                public void setNonce_str(String nonce_str) {
                    this.nonce_str = nonce_str;
                }

                public String getSign() {
                    return sign;
                }

                public void setSign(String sign) {
                    this.sign = sign;
                }

                public String getResult_code() {
                    return result_code;
                }

                public void setResult_code(String result_code) {
                    this.result_code = result_code;
                }

                public String getOpenid() {
                    return openid;
                }

                public void setOpenid(String openid) {
                    this.openid = openid;
                }

                public String getIs_subscribe() {
                    return is_subscribe;
                }

                public void setIs_subscribe(String is_subscribe) {
                    this.is_subscribe = is_subscribe;
                }

                public String getTrade_type() {
                    return trade_type;
                }

                public void setTrade_type(String trade_type) {
                    this.trade_type = trade_type;
                }

                public String getBank_type() {
                    return bank_type;
                }

                public void setBank_type(String bank_type) {
                    this.bank_type = bank_type;
                }

                public String getTotal_fee() {
                    return total_fee;
                }

                public void setTotal_fee(String total_fee) {
                    this.total_fee = total_fee;
                }

                public String getFee_type() {
                    return fee_type;
                }

                public void setFee_type(String fee_type) {
                    this.fee_type = fee_type;
                }

                public String getTransaction_id() {
                    return transaction_id;
                }

                public void setTransaction_id(String transaction_id) {
                    this.transaction_id = transaction_id;
                }

                public String getOut_trade_no() {
                    return out_trade_no;
                }

                public void setOut_trade_no(String out_trade_no) {
                    this.out_trade_no = out_trade_no;
                }

                public String getAttach() {
                    return attach;
                }

                public void setAttach(String attach) {
                    this.attach = attach;
                }

                public String getTime_end() {
                    return time_end;
                }

                public void setTime_end(String time_end) {
                    this.time_end = time_end;
                }

                public String getTrade_state() {
                    return trade_state;
                }

                public void setTrade_state(String trade_state) {
                    this.trade_state = trade_state;
                }

                public String getCash_fee() {
                    return cash_fee;
                }

                public void setCash_fee(String cash_fee) {
                    this.cash_fee = cash_fee;
                }
            }
        }
    }
}
