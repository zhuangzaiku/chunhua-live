package com.quootta.mdate.domain;

import java.io.Serializable;

/**
 * Created by kky on 2016/4/13.
 */
public class Album implements Serializable {
    public String _id;
    public String res_type;
    public String res_id;
    public String create_time;
    public String content;
    public boolean is_cover_img;
}
