package com.quootta.mdate.domain;

/**
 * Created by Ryon on 2016/11/30/0030.
 */

public class GroupTextData {

    /**
     * text : {"female":"我说我是个帅哥，你信不信","male":"你是一个有故事的大哥哥么，我是一个想听故事的小妹妹"}
     */

    private DataBean data;
    /**
     * data : {"text":{"female":"我说我是个帅哥，你信不信","male":"你是一个有故事的大哥哥么，我是一个想听故事的小妹妹"}}
     * msg : 获取消息成功
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
         * female : 我说我是个帅哥，你信不信
         * male : 你是一个有故事的大哥哥么，我是一个想听故事的小妹妹
         */

        private TextBean text;

        public TextBean getText() {
            return text;
        }

        public void setText(TextBean text) {
            this.text = text;
        }

        public static class TextBean {
            private String female;
            private String male;

            public String getFemale() {
                return female;
            }

            public void setFemale(String female) {
                this.female = female;
            }

            public String getMale() {
                return male;
            }

            public void setMale(String male) {
                this.male = male;
            }
        }
    }
}
