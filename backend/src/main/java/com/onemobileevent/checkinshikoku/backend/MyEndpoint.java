/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.onemobileevent.checkinshikoku.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

import java.util.Date;
import java.util.Vector;

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(name = "myApi", version = "v1", namespace = @ApiNamespace(ownerDomain = "backend.checkinshikoku.onemobileevent.com", ownerName = "backend.checkinshikoku.onemobileevent.com", packagePath = ""))
public class MyEndpoint {

    private static final String ENTITIY_KEY = "checkin";
    private static final String PROPERTY_KEY_TEMPLE_ID = "templeid";
    private static final String PROPERTY_KEY_TIME = "time";

    /**
     * Check in API
     * @param id temple id
     * @return updated info
     */
    @ApiMethod(name = "checkIn")
    public CheckInInfo checkIn(@Named("id") int id) {
        // チェックイン時間の取得
        Date checkInTime = new Date();
        // データストアの呼び出し
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        // 保存するデータの作成
        Entity entity = new Entity(ENTITIY_KEY);
        entity.setProperty(PROPERTY_KEY_TEMPLE_ID, id);
        entity.setProperty(PROPERTY_KEY_TIME, checkInTime.getTime());
        // データストアへ保存
        ds.put(entity);
        // チェックイン情報を返す
        return new CheckInInfo(id, checkInTime.getTime());
    }

    /**
     * Get check in info API
     * @return info list
     */
    @ApiMethod(name = "getList")
    public Vector<CheckInInfo> getList() {

        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query(ENTITIY_KEY);
        PreparedQuery pq = ds.prepare(query);
        Vector<CheckInInfo> list = new Vector<CheckInInfo>();

        for (Entity entity : pq.asIterable()) {
            int templeID = Integer.valueOf(entity.getProperty(PROPERTY_KEY_TEMPLE_ID).toString()).intValue();
            long date = Long.valueOf(entity.getProperty(PROPERTY_KEY_TIME).toString()).longValue();
            list.add(new CheckInInfo(templeID, date));
        }

        return list;
    }
}
