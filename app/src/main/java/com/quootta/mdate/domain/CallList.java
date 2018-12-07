package com.quootta.mdate.domain;

import java.util.List;

/**
 * Created by ryon on 2016/11/21.
 */

public class CallList {

    public List<CallListBean> from ;

    public List<CallListBean> to ;

    public class CallListBean {
        public String _id;

        public String nick_name;

        public String avatar;
    }
}
