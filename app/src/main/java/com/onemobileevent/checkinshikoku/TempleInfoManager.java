package com.onemobileevent.checkinshikoku;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Manager class for TempleInfo
 */
public class TempleInfoManager {

    private static TempleInfoManager myObj;
    private Vector<TempleInfo> infoList;

    /**
     * Singleton Class
     * @return object
     */
    public static TempleInfoManager getTempleInfoManager() {
        if (myObj == null) {
            myObj = new TempleInfoManager();
        }
        return myObj;
    }

    /**
     * Init data from CSV file.
     * @param context context
     * @return object
     */
    public TempleInfoManager init(Context context) {

        infoList = new Vector<TempleInfo>();
        AssetManager assetManager = context.getAssets();

        try {
            InputStream is = assetManager.open("address_list.csv");
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            BufferedReader bufferReader = new BufferedReader(inputStreamReader);
            String line = "";
            int index = 0;
            while ((line = bufferReader.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, ",");
                String name = st.nextToken();
                String address = st.nextToken();
                double lat = Double.valueOf(st.nextToken()).doubleValue();
                double lng = Double.valueOf(st.nextToken()).doubleValue();
                TempleInfo info = new TempleInfo(index, name, address, new LatLng(lat, lng));
                index++;
                infoList.add(info);
            }
            bufferReader.close();
        } catch (IOException e) {
            Log.d("ERROR", e.getMessage());
        }

        return this;
    }

    /**
     * Update temple info
     * @param templeID temple id
     * @param checkInTime check in time
     */
    public void updateInfo(int templeID, long checkInTime) {
        for (int i = 0; i < infoList.size(); i++) {
            TempleInfo info = infoList.get(i);
            if (info.getTempleID() == templeID) {
                info.setCheckInDate(checkInTime);
                break;
            }
        }
    }

    /**
     * Returning info list
     * @return info list
     */
    public Vector<TempleInfo> getInfoList() {
        return infoList;
    }
}
