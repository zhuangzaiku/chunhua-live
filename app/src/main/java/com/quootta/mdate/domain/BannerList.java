package com.quootta.mdate.domain;

import java.util.List;

/**
 * Created by Ryon on 2016/7/20.
 * email:para.ryon@foxmail.com
 */
public class BannerList {


    public List<Banner> banner;
    public class Banner{
        public String id;
        public String title;
        public String content;
        public String image_url;
        public String thumbnail;
        public String action_url;
        public String share_url;
        public String begin_time;
        public String end_time;
    }

//
//    public Banner banner;
//
//    public class Banner {
//        public String id;
//        public String title;
//        public String content;
//        public List<String> image_url ;
//        public List<String> action_url ;
//        public List<String> share_url;
//        public String begin_time;
//        public String end_time;
//
//    }
}
