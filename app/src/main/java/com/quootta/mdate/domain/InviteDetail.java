package com.quootta.mdate.domain;
public class InviteDetail {
    
    public Gift gift;

    public String _id;

    public String status;

    public User user;

    public String date_msg;

    public class Gift {
        public String num;

        public String cover;

        public String cost;

        public String description;

        public String name;
    }

    public class User {
        public String nick_name;

        public String weight;

        public String height;

        public String _id;

        public String age;

        public String personal_desc;

        public String job;

        public String avatar;

        public String cover_img;

        public String city;

    }
}
