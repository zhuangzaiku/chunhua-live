package com.quootta.mdate.domain;

public class InfoDetailNonStatic {

    public String _id;

    public String visual_status;

    public String avatar;

    public String personal_desc;

    public String nick_name;

    public String gender;

    public String birthday;

    public String city;

    public String height;

    public String weight;

    public String hobby;

    public String job;

    public String request;

    public String date_way;

    public String expertise;

    public String chat;

    public String bill;

    public String album_count;

    public String is_verified_video;

    public String zhima_score;

    public String is_verified_zhima;

    public String video_pay;

    public String audio_pay;

    public String connect_rate;

    public String video_enable;

    public String audio_enable;

    public String bRecharge;

    public String bWithdraw;
    public String invite_code;

    public String is_vip;

    public boolean if_special;
    public String vip_valid_time;

    private ShareBean share;

    @Override
    public String toString() {
        return "InfoDetailNonStatic{" +
                "_id='" + _id + '\'' +
                ", visual_status='" + visual_status + '\'' +
                ", avatar='" + avatar + '\'' +
                ", personal_desc='" + personal_desc + '\'' +
                ", nick_name='" + nick_name + '\'' +
                ", gender='" + gender + '\'' +
                ", invite_code='" + invite_code + '\'' +
                ", birthday='" + birthday + '\'' +
                ", city='" + city + '\'' +
                ", height='" + height + '\'' +
                ", weight='" + weight + '\'' +
                ", hobby='" + hobby + '\'' +
                ", job='" + job + '\'' +
                ", request='" + request + '\'' +
                ", date_way='" + date_way + '\'' +
                ", expertise='" + expertise + '\'' +
                ", chat='" + chat + '\'' +
                ", bill='" + bill + '\'' +
                ", album_count='" + album_count + '\'' +
                ", is_verified_video='" + is_verified_video + '\'' +
                ", zhima_score='" + zhima_score + '\'' +
                ", is_verified_zhima='" + is_verified_zhima + '\'' +
                ", video_pay='" + video_pay + '\'' +
                ", audio_pay='" + audio_pay + '\'' +
                ", connect_rate='" + connect_rate + '\'' +
                ", video_enable='" + video_enable + '\'' +
                ", audio_enable='" + audio_enable + '\'' +
                ", bRecharge='" + bRecharge + '\'' +
                ", bWithdraw='" + bWithdraw + '\'' +
                ", is_vip='" + is_vip + '\'' +
                ", vip_valid_time='" + vip_valid_time + '\'' +
                ", if_special='" + if_special + '\'' +
                '}';
    }

    public ShareBean getShare() {
        return share;
    }

    public void setShare(ShareBean share) {
        this.share = share;
    }

    public static class ShareBean {
        private String index_url;
        private String share_url;
        private String title;
        private String content;
        private String thumbnail;

        public String getIndex_url() {
            return index_url;
        }

        public void setIndex_url(String index_url) {
            this.index_url = index_url;
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }
    }
}
