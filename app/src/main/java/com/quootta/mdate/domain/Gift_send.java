package com.quootta.mdate.domain;

import java.io.Serializable;

/**
 * Created by Ryon on 2016/11/15/0015.
 */
public class Gift_send implements Serializable {

    /**
     * count : 22
     * _id : 57f9f31972a4fbeb650e175b
     * cover : iu2be311df6929fe5a4229dbea220421a3c24b9f.png
     * name : 玫瑰
     */

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
