package com.onemobileevent.checkinshikoku;

import com.google.android.gms.maps.model.LatLng;

/**
 * Temple Info Class
 */
public class TempleInfo {

    private int templeID;
    private String name;
    private String address;
    private LatLng latlng;
    private long checkInDate = -1;

    public TempleInfo(int templeID, String name, String address, LatLng latlng) {
        this.templeID = templeID;
        this.name = name;
        this.address = address;
        this.latlng = latlng;
    }

    public int getTempleID() {
        return templeID;
    }

    public void setTempleID(int templeID) {
        this.templeID = templeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }

    public long getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(long checkInDate) {
        this.checkInDate = checkInDate;
    }
}
