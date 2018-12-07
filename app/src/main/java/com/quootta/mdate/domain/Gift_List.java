package com.quootta.mdate.domain;

import java.util.List;

/**
 * Created by Ryon on 2016/11/16/0016.
 */
public class Gift_List {

    private DataBean data;
    /**
     * data : {"present":[{"count":1,"_id":"57f9f35972a4fbeb650e175d","cover":"iu2bfgi7237e99bedeb0498e0d43a0ab975fbb7d.png","name":"杜蕾斯"}]}
     * msg : 获取收到礼物列表成功
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
         * count : 1
         * _id : 57f9f35972a4fbeb650e175d
         * cover : iu2bfgi7237e99bedeb0498e0d43a0ab975fbb7d.png
         * name : 杜蕾斯
         */

        private List<PresentBean> present;

        public List<PresentBean> getPresent() {
            return present;
        }

        public void setPresent(List<PresentBean> present) {
            this.present = present;
        }

        public static class PresentBean {
            private int count;
            private String _id;
            private String cover;
            private String name;

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
