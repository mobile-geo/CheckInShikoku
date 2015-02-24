package com.onemobileevent.checkinshikoku;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Temple Item information
 */
public class TempleItem implements ClusterItem {

    private TempleInfo info;

    public TempleItem(TempleInfo info) {
        this.info = info;
    }

    @Override
    public LatLng getPosition() {
        return info.getLatlng();
    }

    public TempleInfo getInfo() {
        return info;
    }
}
