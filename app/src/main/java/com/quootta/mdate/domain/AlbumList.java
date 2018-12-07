package com.quootta.mdate.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ryon on 2016/3/11.
 * email:para.ryon@foxmail.com
 */
public class AlbumList implements Serializable {

    public List<Album> album ;
}

//{
//        msg: "获取相册成功",
//        data : {
//        album:[
//        {
//        _id: "",
//        image_id: "",
//        content: "",
//        create_time: "1449848722819",
//        },
//        {},
//        ],
//        }
//        }