package com.quootta.mdate.domain;

import java.util.List;

/**
 * Created by Ryon on 2017/1/9/0009.
 */

public class SceneList {

    /**
     * code : 0
     * msg : 成功
     * data : {"users":[]}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

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
        private List<UserList.users> users;

        public List<UserList.users> getUsers() {
            return users;
        }

        public void setUsers(List<UserList.users> users) {
            this.users = users;
        }


    }
}
