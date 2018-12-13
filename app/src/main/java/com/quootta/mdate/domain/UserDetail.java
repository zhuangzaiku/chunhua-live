package com.quootta.mdate.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 对方用户详情
 * Created by Ryon on 2016/3/7.
 * email:para.ryon@foxmail.com
 */
public class UserDetail implements Serializable {
    public String _id;
    public String sid;
    public String nick_name;
    public String avatar;
    public List<Album> album;
    public String cover_img;
    public String personal_desc;
    public String height;
    public String weight;
    public String age;
    public String constellation;
    public String hobby;
    public String job;
    public String city;
    public String request;
    public String date_way;
    public String gender;
    public String expertise;
    public String chat;
    public String distance;
    public String is_online;
    public String is_unlock;
    public String is_favorite;
    public String is_verified_video;
    public String is_verified_zhima;
    public String video_pay;
    public String audio_pay;
    public String connect_rate;
    public String video_enable;
    public String audio_enable;
    public List<Gift_receiver> gift_receive;
    public List<Gift_send> gift_send;
    public String is_blacklist;

}


//{
//        msg: "获取用户详情成功",
//        data : {
//        _id: "",
//        nick_name: "",
//        avatar: "",
//        album:[
//        {
//        _id: "",
//        res_type: "", // image, video
//        res_id: "", // resource id
//        content: "",
//        create_time: "1449848722819"
//        },
//        ],
//        cover_img: "",
//        personal_desc: "",
//        height: 169,  // -1 表示未知高度
//        weight: 50, // -1 表示未知重量
//        age: 25, // －1 表示未知年龄
//        constellation: "处女座",
//        hobby: "电影, 唱歌",
//        job: "程序员",
//        city: "浙江省杭州市",
//        request: "妹子 开朗",
//        date_way: "看电影 打球",
//        distance: 10,  //KM -1表示未知距离
//        is_verified_video: false,
//        is_verified_zhima: false,
//        is_online: true/false,
//        is_unlock: true/false,
//        is_like: true/false,
//        is_audio:true/false,//对方是否允许接听语音，false状态客户端需把音频聊天按钮置灰不可点击
//        is_video:true/false,//对方是否允许接听视频聊天，false状态客户端需把视频聊天按钮置灰不可点击
//        video_pay: "", //视频付费
//        audio_pay:"",//音频付费
//        connect_rate:"" //接通率
//        }
//        }