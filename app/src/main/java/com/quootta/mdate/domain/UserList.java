package com.quootta.mdate.domain;


import java.util.List;

public class UserList {

    public List<users> users;
    public static class users{
        public String _id;
        public String nick_name;
        public String cover_img;
        public String gender;
        public String avatar;
        public String personal_desc;
        public String is_favorite;
        public String age;
        public String last_login_time;
        public String is_verified_video;
        public String is_verified_zhima;
        public String city;
        public String distance;
        public String video_pay;
        public String audio_pay;
        public String connect_rate;
    }

}