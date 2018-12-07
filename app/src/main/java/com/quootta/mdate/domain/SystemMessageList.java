package com.quootta.mdate.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ryon on 2016/5/17/0017.
 */
public class SystemMessageList implements Serializable {

    public List<notice> notice;

    public class notice implements Serializable {
        public String _id;
        public String type;
        public String send_gift_id;
        public String invite_id;
        public String url;
        public String title;
        public String content;
        public String create_time;
        public String isRead;
    }
}
