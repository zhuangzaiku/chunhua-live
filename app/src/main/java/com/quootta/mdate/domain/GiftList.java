package com.quootta.mdate.domain;

import java.util.List;


public class GiftList {

    public List<gifts> gifts;
    public int currentGift = -1;

    public static class gifts{
        public String _id;
        public String cover;
        public String name;
        public String description;
        public String cost;

        public Boolean isSelected = false;
        public void select(){
            isSelected = !isSelected;
        }
    }

}

//{
//        msg: "获取礼物列表成功",
//        data : {
//        gifts:[
//        {
//        _id: "",
//        cover: "",
//        name: "",
//        description: "",
//        cost: "",
//        }
//        ]
//        }
//        }