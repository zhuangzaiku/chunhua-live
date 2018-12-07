package com.quootta.mdate.domain;

/**
 * Created by Ryon on 2016/9/18/0018.
 */
public class PlatFormSignUpData {

    /**
     * msg :
     * data : {"_id":"","ry_token":"","visual_status":"","avatar":"","personal_desc":"","nick_name":"","gender":"male","birthday":"1991-01-01","city":"","height":175,"weight":65,"hobby":"","job":"","request":"","date_way":"","bill":10000,"is_verified_video":false,"is_verified_zhima":false,"verify_video":""}
     */

    private String msg;
    /**
     * _id :
     * ry_token :
     * visual_status :
     * avatar :
     * personal_desc :
     * nick_name :
     * gender : male
     * birthday : 1991-01-01
     * city :
     * height : 175
     * weight : 65
     * hobby :
     * job :
     * request :
     * date_way :
     * bill : 10000
     * is_verified_video : false
     * is_verified_zhima : false
     * verify_video :
     */

    private DataBean data;

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
        private String ry_token;
        private String visual_status;
        private String avatar;
        private String personal_desc;
        private String nick_name;
        private String gender;
        private String birthday;
        private String city;
        private int height;
        private int weight;
        private String hobby;
        private String job;
        private String request;
        private String date_way;
        private int bill;
        private boolean is_verified_video;
        private boolean is_verified_zhima;
        private String verify_video;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getRy_token() {
            return ry_token;
        }

        public void setRy_token(String ry_token) {
            this.ry_token = ry_token;
        }

        public String getVisual_status() {
            return visual_status;
        }

        public void setVisual_status(String visual_status) {
            this.visual_status = visual_status;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getPersonal_desc() {
            return personal_desc;
        }

        public void setPersonal_desc(String personal_desc) {
            this.personal_desc = personal_desc;
        }

        public String getNick_name() {
            return nick_name;
        }

        public void setNick_name(String nick_name) {
            this.nick_name = nick_name;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public String getHobby() {
            return hobby;
        }

        public void setHobby(String hobby) {
            this.hobby = hobby;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getRequest() {
            return request;
        }

        public void setRequest(String request) {
            this.request = request;
        }

        public String getDate_way() {
            return date_way;
        }

        public void setDate_way(String date_way) {
            this.date_way = date_way;
        }

        public int getBill() {
            return bill;
        }

        public void setBill(int bill) {
            this.bill = bill;
        }

        public boolean isIs_verified_video() {
            return is_verified_video;
        }

        public void setIs_verified_video(boolean is_verified_video) {
            this.is_verified_video = is_verified_video;
        }

        public boolean isIs_verified_zhima() {
            return is_verified_zhima;
        }

        public void setIs_verified_zhima(boolean is_verified_zhima) {
            this.is_verified_zhima = is_verified_zhima;
        }

        public String getVerify_video() {
            return verify_video;
        }

        public void setVerify_video(String verify_video) {
            this.verify_video = verify_video;
        }
    }
}
