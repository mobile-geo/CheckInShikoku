package com.onemobileevent.checkinshikoku.backend;

/**
 * Check in info Class
 */
public class CheckInInfo {

    private int templeID;
    private long checkInTime;

    public CheckInInfo(int templeID, long checkInTime) {
        this.templeID = templeID;
        this.checkInTime = checkInTime;
    }

    public int getTempleID() {
        return templeID;
    }

    public long getCheckInTime() {
        return checkInTime;
    }
}
