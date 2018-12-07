package com.quootta.mdate.domain;

import java.util.List;

public class FavoriteList {

    /**
     * code : 0
     * msg : 获取关注对象成功
     * data : {"users":{"to_me":[{"_id":"235","nick_name":"侯","cover_img":"iv7fvgep6e7462759606f6b22eb0be04eb1c82d0.jpg","avatar":"iszhstal7f23861e157c73025d66779d82f43e75.png","age":26,"last_login_time":1480648300810,"city":"北京市 北京市 朝阳区","personal_desc":"咯咯哦咯咯哦咯咯哦咯哦咯咯嘻嘻嘻无所谓王尼玛格格西门意义自信逆袭","distance":0.37362777652467266,"is_favorite":true},{"_id":"252","nick_name":"123456789011","cover_img":"iwq0zejie847ccbb0095b1953df98229020c2846.jpg","avatar":"iwblrig536c7294cf5934111aa30a62f4c40341e.jpg","age":19,"last_login_time":1482216522724,"city":"黑龙江省 哈尔滨市 市辖区","personal_desc":"你好你好你好你好你好你好你好你好","distance":109.88769970000054,"is_favorite":true},{"_id":"414","nick_name":"韩森","cover_img":"iwot77v106465a28b4df48de7b79f2a7bfb7fcb8.png0","avatar":"ivt3ueiqad7bc863acc50ad3b747c51c2f85b431.jpg","age":20,"last_login_time":1482217220564,"city":"北京市 北京市 朝阳区","personal_desc":"苟以国家生死以\n岂因祸福趋避之","distance":8.491271188647252E-4,"is_favorite":true}],"from_me":[{"_id":"252","nick_name":"123456789011","cover_img":"iwq0zejie847ccbb0095b1953df98229020c2846.jpg","avatar":"iwblrig536c7294cf5934111aa30a62f4c40341e.jpg","age":19,"last_login_time":1482216522724,"city":"黑龙江省 哈尔滨市 市辖区","personal_desc":"你好你好你好你好你好你好你好你好","distance":109.88769970000054,"is_favorite":true},{"_id":"187","nick_name":"蜜汁排骨","cover_img":"iqsva9s63adf88b38c029c9dff9cff3bdbf3ba24.png","avatar":"iqvtj3pl846fdddef307cb2bbc88d5a246bbc29e.jpg","age":26,"last_login_time":1482215050419,"city":"北京市 北京市 朝阳区","personal_desc":"","distance":0.37362777652467266,"is_favorite":true},{"_id":"235","nick_name":"侯","cover_img":"iv7fvgep6e7462759606f6b22eb0be04eb1c82d0.jpg","avatar":"iszhstal7f23861e157c73025d66779d82f43e75.png","age":26,"last_login_time":1480648300810,"city":"北京市 北京市 朝阳区","personal_desc":"咯咯哦咯咯哦咯咯哦咯哦咯咯嘻嘻嘻无所谓王尼玛格格西门意义自信逆袭","distance":0.37362777652467266,"is_favorite":true},{"_id":"334","nick_name":"就能看想开点","cover_img":"iunsa2npad7bc863acc50ad3b747c51c2f85b431.jpg","avatar":"iunsa2npad7bc863acc50ad3b747c51c2f85b431.jpg","age":20,"last_login_time":1480579408522,"city":"北京市 北京市 朝阳区","personal_desc":"见到你嘀嗒猫","distance":-1,"is_favorite":true},{"_id":"414","nick_name":"韩森","cover_img":"iwot77v106465a28b4df48de7b79f2a7bfb7fcb8.png0","avatar":"ivt3ueiqad7bc863acc50ad3b747c51c2f85b431.jpg","age":20,"last_login_time":1482217220564,"city":"北京市 北京市 朝阳区","personal_desc":"苟以国家生死以\n岂因祸福趋避之","distance":8.491271188647252E-4,"is_favorite":true},{"_id":"421","nick_name":"忘川不曾回眸","cover_img":"iwolbo6n5b1a45f540b966eb1d11ecfff9ca8eee.png0","avatar":"iw34xr0pad7bc863acc50ad3b747c51c2f85b431.jpg","age":20,"last_login_time":1481882575050,"city":"北京市 北京市 朝阳区","personal_desc":"","distance":0.009312857565076039,"is_favorite":true}]}}
     */

    private int code;
    private String msg;
    /**
     * users : {"to_me":[{"_id":"235","nick_name":"侯","cover_img":"iv7fvgep6e7462759606f6b22eb0be04eb1c82d0.jpg","avatar":"iszhstal7f23861e157c73025d66779d82f43e75.png","age":26,"last_login_time":1480648300810,"city":"北京市 北京市 朝阳区","personal_desc":"咯咯哦咯咯哦咯咯哦咯哦咯咯嘻嘻嘻无所谓王尼玛格格西门意义自信逆袭","distance":0.37362777652467266,"is_favorite":true},{"_id":"252","nick_name":"123456789011","cover_img":"iwq0zejie847ccbb0095b1953df98229020c2846.jpg","avatar":"iwblrig536c7294cf5934111aa30a62f4c40341e.jpg","age":19,"last_login_time":1482216522724,"city":"黑龙江省 哈尔滨市 市辖区","personal_desc":"你好你好你好你好你好你好你好你好","distance":109.88769970000054,"is_favorite":true},{"_id":"414","nick_name":"韩森","cover_img":"iwot77v106465a28b4df48de7b79f2a7bfb7fcb8.png0","avatar":"ivt3ueiqad7bc863acc50ad3b747c51c2f85b431.jpg","age":20,"last_login_time":1482217220564,"city":"北京市 北京市 朝阳区","personal_desc":"苟以国家生死以\n岂因祸福趋避之","distance":8.491271188647252E-4,"is_favorite":true}],"from_me":[{"_id":"252","nick_name":"123456789011","cover_img":"iwq0zejie847ccbb0095b1953df98229020c2846.jpg","avatar":"iwblrig536c7294cf5934111aa30a62f4c40341e.jpg","age":19,"last_login_time":1482216522724,"city":"黑龙江省 哈尔滨市 市辖区","personal_desc":"你好你好你好你好你好你好你好你好","distance":109.88769970000054,"is_favorite":true},{"_id":"187","nick_name":"蜜汁排骨","cover_img":"iqsva9s63adf88b38c029c9dff9cff3bdbf3ba24.png","avatar":"iqvtj3pl846fdddef307cb2bbc88d5a246bbc29e.jpg","age":26,"last_login_time":1482215050419,"city":"北京市 北京市 朝阳区","personal_desc":"","distance":0.37362777652467266,"is_favorite":true},{"_id":"235","nick_name":"侯","cover_img":"iv7fvgep6e7462759606f6b22eb0be04eb1c82d0.jpg","avatar":"iszhstal7f23861e157c73025d66779d82f43e75.png","age":26,"last_login_time":1480648300810,"city":"北京市 北京市 朝阳区","personal_desc":"咯咯哦咯咯哦咯咯哦咯哦咯咯嘻嘻嘻无所谓王尼玛格格西门意义自信逆袭","distance":0.37362777652467266,"is_favorite":true},{"_id":"334","nick_name":"就能看想开点","cover_img":"iunsa2npad7bc863acc50ad3b747c51c2f85b431.jpg","avatar":"iunsa2npad7bc863acc50ad3b747c51c2f85b431.jpg","age":20,"last_login_time":1480579408522,"city":"北京市 北京市 朝阳区","personal_desc":"见到你嘀嗒猫","distance":-1,"is_favorite":true},{"_id":"414","nick_name":"韩森","cover_img":"iwot77v106465a28b4df48de7b79f2a7bfb7fcb8.png0","avatar":"ivt3ueiqad7bc863acc50ad3b747c51c2f85b431.jpg","age":20,"last_login_time":1482217220564,"city":"北京市 北京市 朝阳区","personal_desc":"苟以国家生死以\n岂因祸福趋避之","distance":8.491271188647252E-4,"is_favorite":true},{"_id":"421","nick_name":"忘川不曾回眸","cover_img":"iwolbo6n5b1a45f540b966eb1d11ecfff9ca8eee.png0","avatar":"iw34xr0pad7bc863acc50ad3b747c51c2f85b431.jpg","age":20,"last_login_time":1481882575050,"city":"北京市 北京市 朝阳区","personal_desc":"","distance":0.009312857565076039,"is_favorite":true}]}
     */

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
        private UsersBean users;

        public UsersBean getUsers() {
            return users;
        }

        public void setUsers(UsersBean users) {
            this.users = users;
        }

        public static class UsersBean {
            /**
             * _id : 235
             * nick_name : 侯
             * cover_img : iv7fvgep6e7462759606f6b22eb0be04eb1c82d0.jpg
             * avatar : iszhstal7f23861e157c73025d66779d82f43e75.png
             * age : 26
             * last_login_time : 1480648300810
             * city : 北京市 北京市 朝阳区
             * personal_desc : 咯咯哦咯咯哦咯咯哦咯哦咯咯嘻嘻嘻无所谓王尼玛格格西门意义自信逆袭
             * distance : 0.37362777652467266
             * is_favorite : true
             */

            private List<UserList.users> to_me;
            /**
             * _id : 252
             * nick_name : 123456789011
             * cover_img : iwq0zejie847ccbb0095b1953df98229020c2846.jpg
             * avatar : iwblrig536c7294cf5934111aa30a62f4c40341e.jpg
             * age : 19
             * last_login_time : 1482216522724
             * city : 黑龙江省 哈尔滨市 市辖区
             * personal_desc : 你好你好你好你好你好你好你好你好
             * distance : 109.88769970000054
             * is_favorite : true
             */

            private List<UserList.users> from_me;

            public List<UserList.users> getTo_me() {
                return to_me;
            }

            public void setTo_me(List<UserList.users> to_me) {
                this.to_me = to_me;
            }

            public List<UserList.users> getFrom_me() {
                return from_me;
            }

            public void setFrom_me(List<UserList.users> from_me) {
                this.from_me = from_me;
            }

//            public static class ToMeBean {
//                private String _id;
//                private String nick_name;
//                private String cover_img;
//                private String avatar;
//                private int age;
//                private long last_login_time;
//                private String city;
//                private String personal_desc;
//                private double distance;
//                private boolean is_favorite;
//
//                public String get_id() {
//                    return _id;
//                }
//
//                public void set_id(String _id) {
//                    this._id = _id;
//                }
//
//                public String getNick_name() {
//                    return nick_name;
//                }
//
//                public void setNick_name(String nick_name) {
//                    this.nick_name = nick_name;
//                }
//
//                public String getCover_img() {
//                    return cover_img;
//                }
//
//                public void setCover_img(String cover_img) {
//                    this.cover_img = cover_img;
//                }
//
//                public String getAvatar() {
//                    return avatar;
//                }
//
//                public void setAvatar(String avatar) {
//                    this.avatar = avatar;
//                }
//
//                public int getAge() {
//                    return age;
//                }
//
//                public void setAge(int age) {
//                    this.age = age;
//                }
//
//                public long getLast_login_time() {
//                    return last_login_time;
//                }
//
//                public void setLast_login_time(long last_login_time) {
//                    this.last_login_time = last_login_time;
//                }
//
//                public String getCity() {
//                    return city;
//                }
//
//                public void setCity(String city) {
//                    this.city = city;
//                }
//
//                public String getPersonal_desc() {
//                    return personal_desc;
//                }
//
//                public void setPersonal_desc(String personal_desc) {
//                    this.personal_desc = personal_desc;
//                }
//
//                public double getDistance() {
//                    return distance;
//                }
//
//                public void setDistance(double distance) {
//                    this.distance = distance;
//                }
//
//                public boolean isIs_favorite() {
//                    return is_favorite;
//                }
//
//                public void setIs_favorite(boolean is_favorite) {
//                    this.is_favorite = is_favorite;
//                }
//            }

//            public static class FromMeBean {
//                private String _id;
//                private String nick_name;
//                private String cover_img;
//                private String avatar;
//                private int age;
//                private long last_login_time;
//                private String city;
//                private String personal_desc;
//                private double distance;
//                private boolean is_favorite;
//
//                public String get_id() {
//                    return _id;
//                }
//
//                public void set_id(String _id) {
//                    this._id = _id;
//                }
//
//                public String getNick_name() {
//                    return nick_name;
//                }
//
//                public void setNick_name(String nick_name) {
//                    this.nick_name = nick_name;
//                }
//
//                public String getCover_img() {
//                    return cover_img;
//                }
//
//                public void setCover_img(String cover_img) {
//                    this.cover_img = cover_img;
//                }
//
//                public String getAvatar() {
//                    return avatar;
//                }
//
//                public void setAvatar(String avatar) {
//                    this.avatar = avatar;
//                }
//
//                public int getAge() {
//                    return age;
//                }
//
//                public void setAge(int age) {
//                    this.age = age;
//                }
//
//                public long getLast_login_time() {
//                    return last_login_time;
//                }
//
//                public void setLast_login_time(long last_login_time) {
//                    this.last_login_time = last_login_time;
//                }
//
//                public String getCity() {
//                    return city;
//                }
//
//                public void setCity(String city) {
//                    this.city = city;
//                }
//
//                public String getPersonal_desc() {
//                    return personal_desc;
//                }
//
//                public void setPersonal_desc(String personal_desc) {
//                    this.personal_desc = personal_desc;
//                }
//
//                public double getDistance() {
//                    return distance;
//                }
//
//                public void setDistance(double distance) {
//                    this.distance = distance;
//                }
//
//                public boolean isIs_favorite() {
//                    return is_favorite;
//                }
//
//                public void setIs_favorite(boolean is_favorite) {
//                    this.is_favorite = is_favorite;
//                }
//            }
        }
    }
}
