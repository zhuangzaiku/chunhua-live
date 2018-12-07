package com.quootta.mdate.domain;

import java.io.Serializable;

/**
 * Created by maxiaopeng on 2018/8/6.
 */

public class LocationDate implements Serializable {
    private double latitude;
    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
