package com.quootta.mdate.domain;

import java.util.List;

/**
 * Created by Ryon on 2016/9/6/0006.
 */
public class RankingListData {
    /**
     * last_week : {"riche_board":{"range":null,"score":null,"riches":[]},"charm_board":{"charms":[],"range":null,"score":null}}
     * this_week : {"riche_board":{"range":null,"score":null,"riches":[{"riche_score":"150","nick_name":"许许许","riche_range":0,"_id":"151","age":26,"gender":"male","avatar":"im4j6rpp22186851ffea4d2721d3ab706c95c800.png","cover_img":"404.jpg"}]},"charm_board":{"charms":[{"nick_name":"稳重成熟","charm_range":0,"_id":"230","charm_score":"105","age":9,"gender":"male","avatar":"isr86vu350bfed507b9a3e6ef7bd8040e10de248.png","cover_img":"isr8ltivd9e1b3f0c30f34d8799dfe728ecfd535.jpg"}],"range":null,"score":null}}
     * desc : 使用视频及语音功能每消耗1金币增加1土豪值！
     接受视频及语音功能每1秒钟增加1魅力值！
     排行榜截至时间为每周六24点！
     */

    private DataBean data;
    /**
     * data : {"last_week":{"riche_board":{"range":null,"score":null,"riches":[]},"charm_board":{"charms":[],"range":null,"score":null}},"this_week":{"riche_board":{"range":null,"score":null,"riches":[{"riche_score":"150","nick_name":"许许许","riche_range":0,"_id":"151","age":26,"gender":"male","avatar":"im4j6rpp22186851ffea4d2721d3ab706c95c800.png","cover_img":"404.jpg"}]},"charm_board":{"charms":[{"nick_name":"稳重成熟","charm_range":0,"_id":"230","charm_score":"105","age":9,"gender":"male","avatar":"isr86vu350bfed507b9a3e6ef7bd8040e10de248.png","cover_img":"isr8ltivd9e1b3f0c30f34d8799dfe728ecfd535.jpg"}],"range":null,"score":null}},"desc":"使用视频及语音功能每消耗1金币增加1土豪值！\n接受视频及语音功能每1秒钟增加1魅力值！\n排行榜截至时间为每周六24点！"}
     * msg : 获取排行榜成功
     * code : 0
     */

    private String msg;
    private int code;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class DataBean {
        /**
         * riche_board : {"range":null,"score":null,"riches":[]}
         * charm_board : {"charms":[],"range":null,"score":null}
         */

        private LastWeekBean last_week;
        /**
         * riche_board : {"range":null,"score":null,"riches":[{"riche_score":"150","nick_name":"许许许","riche_range":0,"_id":"151","age":26,"gender":"male","avatar":"im4j6rpp22186851ffea4d2721d3ab706c95c800.png","cover_img":"404.jpg"}]}
         * charm_board : {"charms":[{"nick_name":"稳重成熟","charm_range":0,"_id":"230","charm_score":"105","age":9,"gender":"male","avatar":"isr86vu350bfed507b9a3e6ef7bd8040e10de248.png","cover_img":"isr8ltivd9e1b3f0c30f34d8799dfe728ecfd535.jpg"}],"range":null,"score":null}
         */

        private ThisWeekBean this_week;
        private String desc;

        public LastWeekBean getLast_week() {
            return last_week;
        }

        public void setLast_week(LastWeekBean last_week) {
            this.last_week = last_week;
        }

        public ThisWeekBean getThis_week() {
            return this_week;
        }

        public void setThis_week(ThisWeekBean this_week) {
            this.this_week = this_week;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public static class LastWeekBean {
            /**
             * range : null
             * score : null
             * riches : []
             */

            private RicheBoardBean riche_board;
            /**
             * charms : []
             * range : null
             * score : null
             */

            private CharmBoardBean charm_board;

            public RicheBoardBean getRiche_board() {
                return riche_board;
            }

            public void setRiche_board(RicheBoardBean riche_board) {
                this.riche_board = riche_board;
            }

            public CharmBoardBean getCharm_board() {
                return charm_board;
            }

            public void setCharm_board(CharmBoardBean charm_board) {
                this.charm_board = charm_board;
            }

            public static class RicheBoardBean {
                private Object range;
                private Object score;

                private List<RichesBean> riches;

                public Object getRange() {
                    return range;
                }

                public void setRange(Object range) {
                    this.range = range;
                }

                public Object getScore() {
                    return score;
                }

                public void setScore(Object score) {
                    this.score = score;
                }

                public List<RichesBean> getRiches() {
                    return riches;
                }

                public void setRiches(List<RichesBean> riches) {
                    this.riches = riches;
                }
                public static class RichesBean {
                    private String riche_score;
                    private String nick_name;
                    private int riche_range;
                    private String _id;
                    private int age;
                    private String gender;
                    private String avatar;
                    private String cover_img;

                    public String getRiche_score() {
                        return riche_score;
                    }

                    public void setRiche_score(String riche_score) {
                        this.riche_score = riche_score;
                    }

                    public String getNick_name() {
                        return nick_name;
                    }

                    public void setNick_name(String nick_name) {
                        this.nick_name = nick_name;
                    }

                    public int getRiche_range() {
                        return riche_range;
                    }

                    public void setRiche_range(int riche_range) {
                        this.riche_range = riche_range;
                    }

                    public String get_id() {
                        return _id;
                    }

                    public void set_id(String _id) {
                        this._id = _id;
                    }

                    public int getAge() {
                        return age;
                    }

                    public void setAge(int age) {
                        this.age = age;
                    }

                    public String getGender() {
                        return gender;
                    }

                    public void setGender(String gender) {
                        this.gender = gender;
                    }

                    public String getAvatar() {
                        return avatar;
                    }

                    public void setAvatar(String avatar) {
                        this.avatar = avatar;
                    }

                    public String getCover_img() {
                        return cover_img;
                    }

                    public void setCover_img(String cover_img) {
                        this.cover_img = cover_img;
                    }
                }
            }

            public static class CharmBoardBean {
                private Object range;
                private Object score;
                private List<CharmsBean> charms;

                public Object getRange() {
                    return range;
                }

                public void setRange(Object range) {
                    this.range = range;
                }

                public Object getScore() {
                    return score;
                }

                public void setScore(Object score) {
                    this.score = score;
                }

                public List<CharmsBean> getCharms() {
                    return charms;
                }

                public void setCharms(List<CharmsBean> charms) {
                    this.charms = charms;
                }
                public static class CharmsBean {
                    private String nick_name;
                    private int charm_range;
                    private String _id;
                    private String charm_score;
                    private int age;
                    private String gender;
                    private String avatar;
                    private String cover_img;

                    public String getNick_name() {
                        return nick_name;
                    }

                    public void setNick_name(String nick_name) {
                        this.nick_name = nick_name;
                    }

                    public int getCharm_range() {
                        return charm_range;
                    }

                    public void setCharm_range(int charm_range) {
                        this.charm_range = charm_range;
                    }

                    public String get_id() {
                        return _id;
                    }

                    public void set_id(String _id) {
                        this._id = _id;
                    }

                    public String getCharm_score() {
                        return charm_score;
                    }

                    public void setCharm_score(String charm_score) {
                        this.charm_score = charm_score;
                    }

                    public int getAge() {
                        return age;
                    }

                    public void setAge(int age) {
                        this.age = age;
                    }

                    public String getGender() {
                        return gender;
                    }

                    public void setGender(String gender) {
                        this.gender = gender;
                    }

                    public String getAvatar() {
                        return avatar;
                    }

                    public void setAvatar(String avatar) {
                        this.avatar = avatar;
                    }

                    public String getCover_img() {
                        return cover_img;
                    }

                    public void setCover_img(String cover_img) {
                        this.cover_img = cover_img;
                    }
                }
            }
        }

        public static class ThisWeekBean {
            /**
             * range : null
             * score : null
             * riches : [{"riche_score":"150","nick_name":"许许许","riche_range":0,"_id":"151","age":26,"gender":"male","avatar":"im4j6rpp22186851ffea4d2721d3ab706c95c800.png","cover_img":"404.jpg"}]
             */

            private RicheBoardBean riche_board;
            /**
             * charms : [{"nick_name":"稳重成熟","charm_range":0,"_id":"230","charm_score":"105","age":9,"gender":"male","avatar":"isr86vu350bfed507b9a3e6ef7bd8040e10de248.png","cover_img":"isr8ltivd9e1b3f0c30f34d8799dfe728ecfd535.jpg"}]
             * range : null
             * score : null
             */

            private CharmBoardBean charm_board;

            public RicheBoardBean getRiche_board() {
                return riche_board;
            }

            public void setRiche_board(RicheBoardBean riche_board) {
                this.riche_board = riche_board;
            }

            public CharmBoardBean getCharm_board() {
                return charm_board;
            }

            public void setCharm_board(CharmBoardBean charm_board) {
                this.charm_board = charm_board;
            }

            public static class RicheBoardBean {
                private Object range;
                private Object score;
                /**
                 * riche_score : 150
                 * nick_name : 许许许
                 * riche_range : 0
                 * _id : 151
                 * age : 26
                 * gender : male
                 * avatar : im4j6rpp22186851ffea4d2721d3ab706c95c800.png
                 * cover_img : 404.jpg
                 */

                private List<RichesBean> riches;

                public Object getRange() {
                    return range;
                }

                public void setRange(Object range) {
                    this.range = range;
                }

                public Object getScore() {
                    return score;
                }

                public void setScore(Object score) {
                    this.score = score;
                }

                public List<RichesBean> getRiches() {
                    return riches;
                }

                public void setRiches(List<RichesBean> riches) {
                    this.riches = riches;
                }

                public static class RichesBean {
                    private String riche_score;
                    private String nick_name;
                    private int riche_range;
                    private String _id;
                    private int age;
                    private String gender;
                    private String avatar;
                    private String cover_img;

                    public String getRiche_score() {
                        return riche_score;
                    }

                    public void setRiche_score(String riche_score) {
                        this.riche_score = riche_score;
                    }

                    public String getNick_name() {
                        return nick_name;
                    }

                    public void setNick_name(String nick_name) {
                        this.nick_name = nick_name;
                    }

                    public int getRiche_range() {
                        return riche_range;
                    }

                    public void setRiche_range(int riche_range) {
                        this.riche_range = riche_range;
                    }

                    public String get_id() {
                        return _id;
                    }

                    public void set_id(String _id) {
                        this._id = _id;
                    }

                    public int getAge() {
                        return age;
                    }

                    public void setAge(int age) {
                        this.age = age;
                    }

                    public String getGender() {
                        return gender;
                    }

                    public void setGender(String gender) {
                        this.gender = gender;
                    }

                    public String getAvatar() {
                        return avatar;
                    }

                    public void setAvatar(String avatar) {
                        this.avatar = avatar;
                    }

                    public String getCover_img() {
                        return cover_img;
                    }

                    public void setCover_img(String cover_img) {
                        this.cover_img = cover_img;
                    }
                }
            }

            public static class CharmBoardBean {
                private Object range;
                private Object score;
                /**
                 * nick_name : 稳重成熟
                 * charm_range : 0
                 * _id : 230
                 * charm_score : 105
                 * age : 9
                 * gender : male
                 * avatar : isr86vu350bfed507b9a3e6ef7bd8040e10de248.png
                 * cover_img : isr8ltivd9e1b3f0c30f34d8799dfe728ecfd535.jpg
                 */

                private List<CharmsBean> charms;

                public Object getRange() {
                    return range;
                }

                public void setRange(Object range) {
                    this.range = range;
                }

                public Object getScore() {
                    return score;
                }

                public void setScore(Object score) {
                    this.score = score;
                }

                public List<CharmsBean> getCharms() {
                    return charms;
                }

                public void setCharms(List<CharmsBean> charms) {
                    this.charms = charms;
                }

                public static class CharmsBean {
                    private String nick_name;
                    private int charm_range;
                    private String _id;
                    private String charm_score;
                    private int age;
                    private String gender;
                    private String avatar;
                    private String cover_img;

                    public String getNick_name() {
                        return nick_name;
                    }

                    public void setNick_name(String nick_name) {
                        this.nick_name = nick_name;
                    }

                    public int getCharm_range() {
                        return charm_range;
                    }

                    public void setCharm_range(int charm_range) {
                        this.charm_range = charm_range;
                    }

                    public String get_id() {
                        return _id;
                    }

                    public void set_id(String _id) {
                        this._id = _id;
                    }

                    public String getCharm_score() {
                        return charm_score;
                    }

                    public void setCharm_score(String charm_score) {
                        this.charm_score = charm_score;
                    }

                    public int getAge() {
                        return age;
                    }

                    public void setAge(int age) {
                        this.age = age;
                    }

                    public String getGender() {
                        return gender;
                    }

                    public void setGender(String gender) {
                        this.gender = gender;
                    }

                    public String getAvatar() {
                        return avatar;
                    }

                    public void setAvatar(String avatar) {
                        this.avatar = avatar;
                    }

                    public String getCover_img() {
                        return cover_img;
                    }

                    public void setCover_img(String cover_img) {
                        this.cover_img = cover_img;
                    }
                }
            }
        }
    }


/**
 * {
 msg: "获取排行榜成功",
 data : {
 desc:"",//描述
 this_week:{
 riche_board:{
 score:"",//土豪值
 range:"",//我的土豪榜排名
 riches:[
 {
 _id: "",
 nick_name: "",
 cover_img: "",
 avatar: "",
 age: 20,  // -1 表示未知年龄
 gender:male/female //性别
 riche_score:"",//用户土豪值
 riche_range:""//用户土豪榜排名
 }
 ];
 },
 charm_board:{
 score:"",//魅力值
 range:"",//我的魅力值排名
 charms:[
 _id: "",
 nick_name: "",
 cover_img: "",
 avatar: "",
 age: 20,  // -1 表示未知年龄
 charm_score:"",//用户魅力值
 charm_range:""//用户魅力榜排名
 ];
 };
 },
 last_week:{
 riche_board:{
 score:"",//土豪值
 range:"",//我的土豪榜排名
 riches:[
 {
 _id: "",
 nick_name: "",
 cover_img: "",
 avatar: "",
 age: 20,  // -1 表示未知年龄
 riche_score:"",//用户土豪值
 riche_range:""//用户土豪榜排名
 }
 ];
 },
 charm_board:{
 score:"",//魅力值
 range:"",//我的魅力值排名
 charms:[
 _id: "",
 nick_name: "",
 cover_img: "",
 avatar: "",
 age: 20,  // -1 表示未知年龄
 charm_score:"",//用户魅力值
 charm_range:""//用户魅力榜排名
 ];
 };
 }
 }
 }
 */
}
