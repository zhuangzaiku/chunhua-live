package com.quootta.mdate.domain;

/**
 * Created by Ryon on 2017/2/16/0016.
 */

public class WxPayList {

    /**
     * code : 0
     * msg : 订单签名成功
     * data : {"_id":"d97424cc72504689aeb0e7225c613566","return_code":"SUCCESS","return_msg":"OK","result_code":"SUCCESS","prepay_id":"wx2017022011552281c94d24270144125657","nonce_str":"KkMbGKo2ahWX3bzHhnTdAsbbkichn6gk","trade_type":"APP","sign":"8D70A72FD609230EDC34531A6DEAF7C7","timeStamp":1487562922}
     */

    private int code;
    private String msg;
    /**
     * _id : d97424cc72504689aeb0e7225c613566
     * return_code : SUCCESS
     * return_msg : OK
     * result_code : SUCCESS
     * prepay_id : wx2017022011552281c94d24270144125657
     * nonce_str : KkMbGKo2ahWX3bzHhnTdAsbbkichn6gk
     * trade_type : APP
     * sign : 8D70A72FD609230EDC34531A6DEAF7C7
     * timeStamp : 1487562922
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
        private String _id;
        private String return_code;
        private String return_msg;
        private String result_code;
        private String prepay_id;
        private String nonce_str;
        private String trade_type;
        private String sign;
        private int timeStamp;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

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

        public String getResult_code() {
            return result_code;
        }

        public void setResult_code(String result_code) {
            this.result_code = result_code;
        }

        public String getPrepay_id() {
            return prepay_id;
        }

        public void setPrepay_id(String prepay_id) {
            this.prepay_id = prepay_id;
        }

        public String getNonce_str() {
            return nonce_str;
        }

        public void setNonce_str(String nonce_str) {
            this.nonce_str = nonce_str;
        }

        public String getTrade_type() {
            return trade_type;
        }

        public void setTrade_type(String trade_type) {
            this.trade_type = trade_type;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public int getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(int timeStamp) {
            this.timeStamp = timeStamp;
        }
    }
}
