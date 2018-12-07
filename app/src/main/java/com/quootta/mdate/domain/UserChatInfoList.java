package com.quootta.mdate.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Ryon on 2016/5/10/0010.
 */
public class UserChatInfoList implements Parcelable {

    public List<UserChatInfo> users;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(users);
    }

    public UserChatInfoList() {
    }

    protected UserChatInfoList(Parcel in) {
        this.users = in.createTypedArrayList(UserChatInfo.CREATOR);
    }

    public static final Parcelable.Creator<UserChatInfoList> CREATOR = new Parcelable.Creator<UserChatInfoList>() {
        @Override
        public UserChatInfoList createFromParcel(Parcel source) {
            return new UserChatInfoList(source);
        }

        @Override
        public UserChatInfoList[] newArray(int size) {
            return new UserChatInfoList[size];
        }
    };
}
