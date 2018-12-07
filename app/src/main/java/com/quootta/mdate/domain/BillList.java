package com.quootta.mdate.domain;

import java.util.List;

/**
 * Created by kky on 2016/3/21.
 */
public class BillList {

    public List<Bills> bills ;

    public class Bills {
        public String create_time;

        public String sum;

        public String type;

        public String _id;

        public String status;

    }
}
