package com.onemobileevent.checkinshikoku;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.onemobileevent.checkinshikoku.backend.myApi.MyApi;
import com.onemobileevent.checkinshikoku.backend.myApi.model.CheckInInfo;
import com.onemobileevent.checkinshikoku.backend.myApi.model.CheckInInfoCollection;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Maps Activity Class
 */
public class MapsActivity extends FragmentActivity
        implements ClusterManager.OnClusterItemClickListener<TempleItem> {

    private static final String API_ROOT_URL = "https://YOUR_PROJECT_URL_HERE/_ah/api/";
    private static final LatLng DEFAULT_POS = new LatLng(33.743779, 133.508826);
    private static final double CHECK_IN_DISTANCE = 500; // m

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ClusterManager<TempleItem> mClusterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        TempleInfoManager.getTempleInfoManager().init(this);
        setUpMapIfNeeded();
    }


    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        Log.d("SETUP MAP", "set up");
        mClusterManager = new ClusterManager<TempleItem>(this, mMap);
        mClusterManager.setOnClusterItemClickListener(this);
        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mClusterManager.setRenderer(new TempleRender());
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_POS, 7));

        new GetListAsyncTask(getApiClient()) {
            @Override
            protected void onPostExecute(CheckInInfoCollection result) {
                if (result != null) {
                    Log.d("API RESULT", result.toString());
                    List<CheckInInfo> list = result.getItems();
                    if (list != null) {
                        for (int i = 0; i < list.size(); i++) {
                            CheckInInfo info = list.get(i);
                            TempleInfo temple = TempleInfoManager.getTempleInfoManager().getInfoList().get(info.getTempleID());
                            temple.setCheckInDate(info.getCheckInTime());
                        }
                    }
                    updateMap();
                } else {
                    showToastMessage("チェックイン情報取得に失敗しました");
                }
            }
        }.execute();
    }

    /**
     * Get Endpoint API Client Instance
     */
    private MyApi getApiClient() {
        return new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                .setRootUrl(API_ROOT_URL).build();
    }

    /**
     * updating map
     */
    private void updateMap() {
        Log.d("UPDATE MAP", "update!");
        mMap.clear();
        mClusterManager.clearItems();
        Vector<TempleInfo> infoList = TempleInfoManager.getTempleInfoManager().getInfoList();
        for (int i = 0; i < infoList.size(); i++) {
            TempleItem item = new TempleItem(infoList.get(i));
            mClusterManager.addItem(item);
        }
        mMap.animateCamera(CameraUpdateFactory.zoomIn(), 1000, null);
    }

    @Override
    public boolean onClusterItemClick(TempleItem item) {

        boolean distanceCheck = true;

        /*
         * Comment out for debugging
        Location myPos = mMap.getMyLocation();
        if (myPos != null) {
            Location templePos = new Location("temple");
            templePos.setLatitude(item.getInfo().getLatlng().latitude);
            templePos.setLongitude(item.getInfo().getLatlng().longitude);
            float distance = templePos.distanceTo(myPos);
            Log.d("CLICK ITEM", item.getInfo().getName() + " distance : " + distance);
            if (distance > CHECK_IN_DISTANCE) {
                distanceCheck = false;
            }
        }
        */

        if (distanceCheck && item.getInfo().getCheckInDate() < 0) {

            final int templeID = item.getInfo().getTempleID();
            TempleInfo info = item.getInfo();

            new AlertDialog.Builder(this)
                    .setTitle((info.getTempleID() + 1) + " : " + info.getName())
                    .setMessage("チェックインする")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new CheckInAsyncTask(getApiClient()) {
                                @Override
                                protected void onPostExecute(CheckInInfo result) {
                                    if (result != null) {
                                        Log.d("API RESULT", result.toString());
                                        TempleInfoManager.getTempleInfoManager().updateInfo(result.getTempleID().intValue(), result.getCheckInTime().longValue());
                                        updateMap();
                                        showToastMessage("チェックイン完了");
                                    } else {
                                        showToastMessage("チェックインに失敗しました");
                                    }
                                }
                            }.execute(templeID);
                        }
                    })
            .setNegativeButton("Cancel", null).show();
        }
        return false;
    }

    /**
     * Show complete message
     */
    private void showToastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Private class for rendering cluster item.
     */
    public class TempleRender extends DefaultClusterRenderer<TempleItem> {

        public TempleRender() {
            super(getApplicationContext(), mMap, mClusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(TempleItem temple, MarkerOptions markerOptions) {
            TempleInfo info = temple.getInfo();
            if (temple.getInfo().getCheckInDate() < 0) {
                markerOptions.title((info.getTempleID() + 1) + " : " + info.getName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.temple_pin_on)).snippet("未チェックイン");
            } else {
                Date date = new Date();
                date.setTime(temple.getInfo().getCheckInDate());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                markerOptions.title((info.getTempleID() + 1) + " : " + info.getName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.temple_pin_off))
                        .snippet(dateFormat.format(date) + " チェックイン済み");
            }
        }
    }
}
