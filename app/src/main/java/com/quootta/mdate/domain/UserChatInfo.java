package com.quootta.mdate.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ryon on 2016/5/6/0006.
 */
public class UserChatInfo implements Parcelable{

    public String avatar;

    public String nick_name;

    public String _id;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(avatar);
        dest.writeString(nick_name);
        dest.writeString(_id);
    }

    public static final Parcelable.Creator<UserChatInfo> CREATOR = new Parcelable.Creator<UserChatInfo>() {
        @Override
        public UserChatInfo createFromParcel(Parcel source) {
            UserChatInfo userChatInfo = new UserChatInfo();
            userChatInfo.avatar = source.readString();
            userChatInfo.nick_name = source.readString();
            userChatInfo._id = source.readString();

            return userChatInfo;
        }

        @Override
        public UserChatInfo[] newArray(int size) {
            return new UserChatInfo[size];
        }
    };
}
